package com.starwars.falcon.web;

import com.starwars.falcon.api.EscapePlan;
import com.starwars.falcon.api.InterceptedData;
import com.starwars.falcon.api.MilleniumFalconService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/falcon/")
class MilleniumFalconController {
  MilleniumFalconService milleniumFalconService;

  public MilleniumFalconController(MilleniumFalconService milleniumFalconService) {
    this.milleniumFalconService = milleniumFalconService;
  }

  @PostMapping("escape-plan")
  EscapePlan calculateEscapePlan(@RequestBody InterceptedData interceptedData) {
    return milleniumFalconService.calculateEscapePlan(interceptedData);
  }

  @GetMapping("escape-plan")
  EscapePlan calculateEscapePlan() {
    return milleniumFalconService.calculateEscapePlan(null);
  }
}
