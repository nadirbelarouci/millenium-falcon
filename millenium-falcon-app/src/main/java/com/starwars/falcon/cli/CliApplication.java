package com.starwars.falcon.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.starwars.falcon.api.EmpireIntelRequest;
import com.starwars.falcon.api.RouteService;
import com.starwars.falcon.api.StepResponse;
import com.starwars.falcon.api.TravelPlanResponse;
import com.starwars.falcon.logic.ServiceConfig;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@Import({ServiceConfig.class})
@SpringBootApplication
public class CliApplication implements CommandLineRunner {
  private static final Logger LOG = LoggerFactory.getLogger(CliApplication.class);

  @Autowired private RouteService routeService;
  @Autowired private ObjectMapper objectMapper;

  public static void main(String[] args) {
    SpringApplication.run(CliApplication.class, args);
  }

  @Override
  public void run(String... args) {
    if (args.length == 0) {
      LOG.info("Expecting at least Empire Intel");
      System.exit(0);
    }
    objectMapper.registerModule(new GuavaModule());

    File src = new File(args[0]);
    if (!src.exists()) {
      LOG.error("Empire Intel doesn't exist: {}", src.getAbsolutePath());
      System.exit(0);
    }
    EmpireIntelRequest empireIntelRequest;
    try {
      empireIntelRequest = objectMapper.readValue(src, EmpireIntelRequest.class);
      findPlan(empireIntelRequest);
    } catch (IOException e) {
      LOG.error("Empire Intel is corrupted: {}", e.getMessage());
    }
  }

  private void findPlan(EmpireIntelRequest empireIntelRequest) {
    TravelPlanResponse travelPlan = routeService.findTravelPlan(empireIntelRequest);
    LOG.info("Travel plan success probability: {}%", travelPlan.successProbability());
    LOG.info(
        "Travel plan path: {}",
        travelPlan.path().stream().map(StepResponse::node).collect(Collectors.joining(" -> ")));
  }
}
