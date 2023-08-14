package com.starwars.falcon.web;

import com.google.common.collect.ImmutableSet;
import com.starwars.falcon.api.EmpireIntelRequest;
import com.starwars.falcon.api.RouteResponse;
import com.starwars.falcon.api.RouteService;
import com.starwars.falcon.api.TravelPlanResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/routes")
class RouteController {
  RouteService routeService;

  public RouteController(RouteService routeService) {
    this.routeService = routeService;
  }

  @PostMapping(value = "travel-plan")
  TravelPlanResponse calculateEscapePlan(@RequestBody EmpireIntelRequest empireIntelRequest) {
    return routeService.findTravelPlan(empireIntelRequest);
  }

  @GetMapping
  ImmutableSet<RouteResponse> findAllRoutes() {
    return routeService.findAllRoutes();
  }
}
