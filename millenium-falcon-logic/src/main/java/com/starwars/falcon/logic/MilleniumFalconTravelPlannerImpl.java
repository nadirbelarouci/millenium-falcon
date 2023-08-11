package com.starwars.falcon.logic;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

import com.google.common.collect.ImmutableSet;
import com.starwars.falcon.api.EmpireIntelRequest;
import com.starwars.falcon.api.MilleniumFalconTravelPlanner;
import com.starwars.falcon.api.Step;
import com.starwars.falcon.api.TravelPlanResponse;
import com.starwars.falcon.logic.TravelPlanFinder.ComputedTravelPlan;
import com.starwars.falcon.api.RouteResponse;

class MilleniumFalconTravelPlannerImpl implements MilleniumFalconTravelPlanner {
  private final RouteRepository routeRepository;
  private final TravelPlanProperties travelPlanProperties;

  MilleniumFalconTravelPlannerImpl(RouteRepository routeRepository, TravelPlanProperties travelPlanProperties) {
    this.routeRepository = routeRepository;
    this.travelPlanProperties = travelPlanProperties;
  }

  @Override
  public TravelPlanResponse findTravelPlan(EmpireIntelRequest empireIntelRequest) {
    var routes = routeRepository.findAll();
    var travelPlanFinderConfig =
        new TravelPlanFinderConfig(routes, empireIntelRequest, travelPlanProperties.autonomy());
    var computedTravelPlan =
        new TravelPlanFinder(travelPlanFinderConfig)
            .findTravelPlan(travelPlanProperties.departure(), travelPlanProperties.arrival());
    return toTravelPlanResponse(computedTravelPlan);
  }

  private static TravelPlanResponse toTravelPlanResponse(
      ComputedTravelPlan computedTravelPlan) {
    return new TravelPlanResponse(
        1 - computedTravelPlan.risk(),
        computedTravelPlan.path().stream()
            .map(step -> new Step(step.node(), step.day(), step.fuel(), step.risk()))
            .collect(toImmutableSet()));
  }

  @Override
  public ImmutableSet<RouteResponse> findAllRoutes() {
    return routeRepository.findAll().stream()
        .map(route -> new RouteResponse(route.origin(), route.destination(), route.travelTime()))
        .collect(toImmutableSet());
  }
}
