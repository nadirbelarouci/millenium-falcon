package com.starwars.falcon.logic;

import static com.google.common.collect.ImmutableSetMultimap.toImmutableSetMultimap;
import static com.google.common.collect.ImmutableTable.toImmutableTable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Table;
import com.starwars.falcon.api.BountyHunterLocation;
import com.starwars.falcon.api.EmpireIntelRequest;
import java.util.stream.Stream;

final class TravelPlanFinderConfig {
  private final Table<String, String, Integer> graph;
  private final ImmutableSetMultimap<Integer, String> riskNodeByDay;
  private final int maxTime;
  private final int initialAutonomy;

  TravelPlanFinderConfig(ImmutableSet<Route> routes, EmpireIntelRequest empireIntelRequest, int autonomy) {
    graph =
        routes.stream()
            .flatMap(route -> Stream.of(route, route.reverse()))
            .collect(toImmutableTable(Route::origin, Route::destination, Route::travelTime));
    riskNodeByDay =
        empireIntelRequest.bountyHunters().stream()
            .collect(
                toImmutableSetMultimap(BountyHunterLocation::day, BountyHunterLocation::planet));
    maxTime = empireIntelRequest.countdown();
    initialAutonomy = autonomy;
  }

  public Table<String, String, Integer> getGraph() {
    return graph;
  }

  public ImmutableSetMultimap<Integer, String> getRiskNodeByDay() {
    return riskNodeByDay;
  }

  public int getMaxTime() {
    return maxTime;
  }

  public int getInitialAutonomy() {
    return initialAutonomy;
  }
}
