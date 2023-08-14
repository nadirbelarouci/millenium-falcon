package com.starwars.falcon.logic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.google.common.collect.ImmutableSet;
import com.starwars.falcon.api.BountyHunterLocationRequest;
import com.starwars.falcon.api.EmpireIntelRequest;
import com.starwars.falcon.logic.TravelPlanFinder.ComputedTravelPlan;
import com.starwars.falcon.logic.TravelPlanFinder.Step;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TravelPlanFinderTest {
  private static final ImmutableSet<Route> ROUTES =
      ImmutableSet.of(
          new Route("Tatooine", "Dagobah", 6),
          new Route("Dagobah", "Endor", 4),
          new Route("Dagobah", "Hoth", 1),
          new Route("Hoth", "Endor", 1),
          new Route("Tatooine", "Hoth", 6));

  private static final ImmutableSet<BountyHunterLocationRequest> BOUNTY_HUNTER_LOCATIONS =
      ImmutableSet.of(
          new BountyHunterLocationRequest("Hoth", 6),
          new BountyHunterLocationRequest("Hoth", 7),
          new BountyHunterLocationRequest("Hoth", 8));

  public static Stream<Arguments> configurations() {
    return Stream.of(
        arguments(
            "findTravelPlan returns an infeasible plan because we cannot go from Tatooine to Endor in 7 days or less",
            new TravelPlanFinderConfig(
                ROUTES, new EmpireIntelRequest(7, BOUNTY_HUNTER_LOCATIONS), 6),
            "Tatooine",
            "Endor",
            new ComputedTravelPlan(1.0, List.of())),
        arguments(
            "findTravelPlan should accumulate risk",
            new TravelPlanFinderConfig(
                ROUTES, new EmpireIntelRequest(8, BOUNTY_HUNTER_LOCATIONS), 6),
            "Tatooine",
            "Endor",
            new ComputedTravelPlan(
                0.19,
                List.of(
                    new Step("Tatooine", 0, 6, 0.0),
                    new Step("Hoth", 6, 0, 0.1),
                    new Step("Hoth", 7, 6, 0.19),
                    new Step("Endor", 8, 5, 0.19)))),
        arguments(
            "findTravelPlan should be able to refuel",
            new TravelPlanFinderConfig(
                ROUTES, new EmpireIntelRequest(9, BOUNTY_HUNTER_LOCATIONS), 6),
            "Tatooine",
            "Endor",
            new ComputedTravelPlan(
                0.10,
                List.of(
                    new Step("Tatooine", 0, 6, 0),
                    new Step("Dagobah", 6, 0, 0),
                    new Step("Dagobah", 7, 6, 0),
                    new Step("Hoth", 8, 5, 0.10),
                    new Step("Endor", 9, 4, 0.10)))),
        arguments(
            "findTravelPlan should be able to refuel, wait",
            new TravelPlanFinderConfig(
                ROUTES, new EmpireIntelRequest(10, BOUNTY_HUNTER_LOCATIONS), 6),
            "Tatooine",
            "Endor",
            new ComputedTravelPlan(
                0,
                List.of(
                    new Step("Tatooine", 0, 6, 0),
                    new Step("Dagobah", 6, 0, 0),
                    new Step("Dagobah", 7, 6, 0),
                    new Step("Dagobah", 8, 6, 0),
                    new Step("Hoth", 9, 5, 0),
                    new Step("Endor", 10, 4, 0)))),
        arguments(
            "findTravelPlan should be able to cycle",
            new TravelPlanFinderConfig(
                ImmutableSet.of(
                    new Route("A", "B", 1), new Route("A", "C", 1), new Route("B", "D", 1)),
                new EmpireIntelRequest(
                    10,
                    ImmutableSet.of(
                        new BountyHunterLocationRequest("B", 1),
                        new BountyHunterLocationRequest("C", 1),
                        new BountyHunterLocationRequest("B", 3),
                        new BountyHunterLocationRequest("C", 3),
                        new BountyHunterLocationRequest("B", 5),
                        new BountyHunterLocationRequest("C", 5),
                        new BountyHunterLocationRequest("B", 2),
                        new BountyHunterLocationRequest("A", 2),
                        new BountyHunterLocationRequest("B", 4),
                        new BountyHunterLocationRequest("A", 4))),
                10),
            "A",
            "D",
            new ComputedTravelPlan(
                0,
                List.of(
                    new Step("A", 0, 10, 0),
                    new Step("A", 1, 10, 0),
                    new Step("C", 2, 9, 0),
                    new Step("A", 3, 8, 0),
                    new Step("C", 4, 7, 0),
                    new Step("A", 5, 6, 0),
                    new Step("B", 6, 5, 0),
                    new Step("D", 7, 4, 0)))));
  }

  @ParameterizedTest
  @MethodSource("configurations")
  void findTravelPlan(
      @SuppressWarnings("unused") String description,
      TravelPlanFinderConfig config,
      String departure,
      String arrival,
      ComputedTravelPlan expected) {
    assertThat(new TravelPlanFinder(config).findTravelPlan(departure, arrival)).isEqualTo(expected);
  }
}
