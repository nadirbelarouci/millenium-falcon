package com.starwars.falcon.webapp;

import com.starwars.falcon.logic.MilleniumFalconServiceConfig;
import com.starwars.falcon.web.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({MilleniumFalconServiceConfig.class, WebConfig.class})
@SpringBootApplication
public class MainApplication {
  public static void main(String[] args) {
    SpringApplication.run(MainApplication.class, args);
  }
}
