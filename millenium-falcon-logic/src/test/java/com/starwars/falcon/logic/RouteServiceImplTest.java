package com.starwars.falcon.logic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableSet;
import com.starwars.falcon.api.EmpireIntelRequest;
import com.starwars.falcon.api.RouteResponse;
import com.starwars.falcon.api.RouteService;
import com.starwars.falcon.api.StepResponse;
import com.starwars.falcon.api.TravelPlanResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@MockBean(classes = RouteRepository.class)
@ContextConfiguration(classes = RouteServiceImpl.class)
@TestPropertySource(properties = {"autonomy=6", "departure=A", "arrival=C"})
@EnableConfigurationProperties(TravelPlanProperties.class)
@ExtendWith(SpringExtension.class)
class RouteServiceImplTest {
  private static final ImmutableSet<Route> ROUTES =
      ImmutableSet.of(new Route("A", "B", 1), new Route("B", "C", 4));
  @Autowired private RouteRepository routeRepository;
  @Autowired private RouteService routeService;

  @Test
  void findTravelPlan() {
    when(routeRepository.findAll()).thenReturn(ROUTES);
    assertThat(routeService.findTravelPlan(new EmpireIntelRequest(5, ImmutableSet.of())))
        .isEqualTo(
            new TravelPlanResponse(
                100,
                ImmutableSet.of(
                    new StepResponse("A", 0, 6, 0),
                    new StepResponse("B", 1, 5, 0),
                    new StepResponse("C", 5, 1, 0))));
  }

  @Test
  void findAllRoutes() {
    when(routeRepository.findAll()).thenReturn(ROUTES);
    assertThat(routeService.findAllRoutes())
        .isEqualTo(ImmutableSet.of(new RouteResponse("A", "B", 1), new RouteResponse("B", "C", 4)));
  }
}
