package com.starwars.falcon.logic;

import java.util.List;
import org.jooq.DSLContext;

class RouteRepository {
  private final DSLContext context;

  RouteRepository(DSLContext context) {
    this.context = context;
  }

  List<Route> findAll() {
    return context.selectFrom("routes").fetchInto(Route.class).stream().toList();
  }
}
