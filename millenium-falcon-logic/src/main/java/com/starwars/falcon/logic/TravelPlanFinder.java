package com.starwars.falcon.logic;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

class TravelPlanFinder {
  private static final Comparator<NodeState> BY_RISK_THEN_DAY =
      Comparator.comparingDouble(NodeState::risk).thenComparing(NodeState::day);
  private final TravelPlanFinderConfig config;

  TravelPlanFinder(TravelPlanFinderConfig config) {
    this.config = config;
  }

   ComputedTravelPlan findTravelPlan(String departure, String arrival) {
    var memo = new HashMap<NodeStateId, Double>();
    var queue = new PriorityQueue<>(BY_RISK_THEN_DAY);
    queue.add(
        new NodeState(
            departure,
            0,
            config.getInitialAutonomy(),
            0,
            0,
            List.of(new Step(departure, 0, config.getInitialAutonomy(), 0))));
    while (!queue.isEmpty()) {
      var currentNodeState = queue.poll();

      if (currentNodeState.day() > config.getMaxTime()) {
        continue;
      }

      if (currentNodeState.node().equals(arrival)) {
        return new ComputedTravelPlan(currentNodeState.risk(), currentNodeState.path());
      }

      NodeStateId id = currentNodeState.id();
      if (memo.containsKey(id) && currentNodeState.risk() >= memo.get(id)) {
        continue;
      }
      memo.put(id, currentNodeState.risk());

      // Try to travel to all adjacent nodes.
      for (var entry : config.getGraph().row(currentNodeState.node()).entrySet()) {
        var neighbor = entry.getKey();
        var travelTime = entry.getValue();
        if (currentNodeState.autonomy() >= travelTime) {
          addNewState(
              currentNodeState,
              neighbor,
              travelTime,
              currentNodeState.autonomy() - travelTime,
              queue);
        }
      }

      // Stay in the current node for 1 day and refuel.
      addNewState(currentNodeState, currentNodeState.node(), 1, config.getInitialAutonomy(), queue);
    }
    return new ComputedTravelPlan(1, List.of());
  }

  private void addNewState(
      NodeState currentNodeState,
      String newNode,
      int travelTime,
      int newAutonomy,
      PriorityQueue<NodeState> queue) {
    var newRisk = currentNodeState.risk();
    var k = currentNodeState.riskCount();
    int newDay = currentNodeState.day() + travelTime;
    if (config.getRiskNodeByDay().get(newDay).contains(newNode)) {
      k = currentNodeState.riskCount() + 1;
      newRisk = currentNodeState.risk() + (Math.pow(9, k - 1) / Math.pow(10, k));
    }
    var newPath = new ArrayList<>(currentNodeState.path());
    newPath.add(new Step(newNode, newDay, newAutonomy,newRisk));
    queue.add(new NodeState(newNode, newDay, newAutonomy, k, newRisk, newPath));
  }

  /**
   * Represents the state of a node during the search process in the graph. This captures all the
   * essential information about the current position, the risk, the fuel left, and other parameters
   * required for the search.
   *
   * @param node The current node.
   * @param autonomy The remaining autonomy.
   * @param day The current day.
   * @param riskCount The number of times we'll be at risk.
   * @param risk The calculated risk for the current traversal.
   * @param path The path taken to reach this state.
   */
  record NodeState(
      String node, int day, int autonomy, int riskCount, double risk, List<Step> path) {
    NodeStateId id() {
      return new NodeStateId(node, day, autonomy, riskCount);
    }
  }

  /**
   * Represents a unique identifier for a NodeState. This captures the essential parameters to
   * uniquely identify a state during the search.
   */
  record NodeStateId(String node, int day, int autonomy, int bountyHunterCount) {}

  /**
   * Represents a step or segment of the journey. This captures the information of a particular leg
   * of the journey, including the plant, the day of arrival to that node, the fuel remaining and
   * the accumulated risk at that node.
   */
  record Step(String node, int day, int fuel, double risk) {}

  /** The computed travel plan. */
  record ComputedTravelPlan(double risk, List<Step> path) {}
}
