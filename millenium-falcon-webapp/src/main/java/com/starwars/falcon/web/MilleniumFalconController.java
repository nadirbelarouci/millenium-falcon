package com.starwars.falcon.web;


import com.google.common.collect.ImmutableSet;
import com.starwars.falcon.api.EmpireIntelRequest;
import com.starwars.falcon.api.MilleniumFalconTravelPlanner;
import com.starwars.falcon.api.RouteResponse;
import com.starwars.falcon.api.TravelPlanResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/falcon/")
class MilleniumFalconController {
  MilleniumFalconTravelPlanner milleniumFalconTravelPlanner;

  public MilleniumFalconController(MilleniumFalconTravelPlanner milleniumFalconTravelPlanner) {
    this.milleniumFalconTravelPlanner = milleniumFalconTravelPlanner;
  }

  @PostMapping(value = "travel-plan")
  @ResponseBody
  TravelPlanResponse calculateEscapePlan(@RequestBody EmpireIntelRequest empireIntelRequest) {
    return milleniumFalconTravelPlanner.findTravelPlan(empireIntelRequest);
  }

  @GetMapping("routes")
  ImmutableSet<RouteResponse> calculateEscapePlan() {
    return milleniumFalconTravelPlanner.findAllRoutes();
  }
}
