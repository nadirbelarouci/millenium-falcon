package com.starwars.falcon.api;

import com.google.common.collect.ImmutableSet;

public interface RouteService {
  TravelPlanResponse findTravelPlan(EmpireIntelRequest empireIntel);

  ImmutableSet<RouteResponse> findAllRoutes();
}
