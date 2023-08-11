package com.starwars.falcon.logic;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

import com.google.common.collect.ImmutableSet;
import org.jooq.DSLContext;

class RouteRepository {
  private final DSLContext context;

  RouteRepository(DSLContext context) {
    this.context = context;
  }

  ImmutableSet<Route> findAll() {
    return context.selectFrom("routes").fetchInto(Route.class).stream().collect(toImmutableSet());
  }
}
