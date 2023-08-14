package com.starwars.falcon.logic;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

import com.google.common.collect.ImmutableSet;
import com.starwars.falcon.api.EmpireIntelRequest;
import com.starwars.falcon.api.RouteResponse;
import com.starwars.falcon.api.RouteService;
import com.starwars.falcon.api.StepResponse;
import com.starwars.falcon.api.TravelPlanResponse;
import com.starwars.falcon.logic.TravelPlanFinder.ComputedTravelPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RouteServiceImpl implements RouteService {
  private static final Logger LOG = LoggerFactory.getLogger(RouteServiceImpl.class);

  private final RouteRepository routeRepository;
  private final TravelPlanProperties travelPlanProperties;

  RouteServiceImpl(RouteRepository routeRepository, TravelPlanProperties travelPlanProperties) {
    this.routeRepository = routeRepository;
    this.travelPlanProperties = travelPlanProperties;
  }

  @Override
  public TravelPlanResponse findTravelPlan(EmpireIntelRequest empireIntelRequest) {
    var routes = routeRepository.findAll();
    var travelPlanFinderConfig =
        new TravelPlanFinderConfig(routes, empireIntelRequest, travelPlanProperties.autonomy());

    LOG.info(
        "Finding a travel plane from {} to {} in under {} days",
        travelPlanProperties.departure(),
        travelPlanProperties.arrival(),
        travelPlanFinderConfig.getMaxTime());

    var computedTravelPlan =
        new TravelPlanFinder(travelPlanFinderConfig)
            .findTravelPlan(travelPlanProperties.departure(), travelPlanProperties.arrival());
    return toTravelPlanResponse(computedTravelPlan);
  }

  private static TravelPlanResponse toTravelPlanResponse(ComputedTravelPlan computedTravelPlan) {
    return new TravelPlanResponse(
        (1 - computedTravelPlan.risk()) * 100,
        computedTravelPlan.path().stream()
            .map(step -> new StepResponse(step.node(), step.day(), step.fuel(), step.risk()))
            .collect(toImmutableSet()));
  }

  @Override
  public ImmutableSet<RouteResponse> findAllRoutes() {
    return routeRepository.findAll().stream()
        .map(route -> new RouteResponse(route.origin(), route.destination(), route.travelTime()))
        .collect(toImmutableSet());
  }
}
