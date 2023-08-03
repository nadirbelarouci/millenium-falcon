package com.starwars.falcon.logic;

import com.starwars.falcon.api.EscapePlan;
import com.starwars.falcon.api.InterceptedData;
import com.starwars.falcon.api.MilleniumFalconService;
import java.util.List;

class MilleniumFalconServiceImpl implements MilleniumFalconService {
  private final RouteRepository routeRepository;

  MilleniumFalconServiceImpl(RouteRepository routeRepository) {
    this.routeRepository = routeRepository;
  }

  @Override
  public EscapePlan calculateEscapePlan(InterceptedData interceptedData) {
    List<Route> all = routeRepository.findAll();
    return new EscapePlan(100);
  }
}
