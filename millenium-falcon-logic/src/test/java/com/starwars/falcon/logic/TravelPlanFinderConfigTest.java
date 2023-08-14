package com.starwars.falcon.logic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableTable;
import com.starwars.falcon.api.BountyHunterLocationRequest;
import com.starwars.falcon.api.EmpireIntelRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TravelPlanFinderConfigTest {

  @DisplayName(
      "Given a set of routes, TravelPlanFinderConfig graph should have bidirectional edges")
  @Test
  void getGraph() {
    var config =
        new TravelPlanFinderConfig(
            ImmutableSet.of(new Route("1", "2", 3), new Route("1", "3", 3)),
            new EmpireIntelRequest(0, ImmutableSet.of()),
            0);
    assertThat(config.getGraph())
        .isEqualTo(
            ImmutableTable.builder()
                .put("1", "2", 3)
                .put("2", "1", 3)
                .put("1", "3", 3)
                .put("3", "1", 3)
                .build());
  }

  @DisplayName(
      "Given EmpireIntel bounty hunter locations, TravelPlanFinderConfig should map them to riskNodeByDay")
  @Test
  void getRiskNodeByDay() {
    var config =
        new TravelPlanFinderConfig(
            ImmutableSet.of(),
            new EmpireIntelRequest(
                0,
                ImmutableSet.of(
                    new BountyHunterLocationRequest("1", 1),
                    new BountyHunterLocationRequest("1", 2),
                    new BountyHunterLocationRequest("2", 2))),
            0);
    assertThat(config.getRiskNodeByDay())
        .isEqualTo(ImmutableSetMultimap.of(1, "1", 2, "1", 2, "2"));
  }

  @Test
  void getMaxTime() {
    var config =
        new TravelPlanFinderConfig(
            ImmutableSet.of(), new EmpireIntelRequest(10, ImmutableSet.of()), 0);
    assertThat(config.getMaxTime()).isEqualTo(10);
  }

  @Test
  void getInitialAutonomy() {
    var config =
        new TravelPlanFinderConfig(
            ImmutableSet.of(), new EmpireIntelRequest(0, ImmutableSet.of()), 10);
    assertThat(config.getInitialAutonomy()).isEqualTo(10);
  }
}
