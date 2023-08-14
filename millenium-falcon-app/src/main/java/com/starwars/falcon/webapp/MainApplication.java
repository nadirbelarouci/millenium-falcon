package com.starwars.falcon.webapp;

import com.starwars.falcon.logic.ServiceConfig;
import com.starwars.falcon.web.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Import({ServiceConfig.class, WebConfig.class})
@SpringBootApplication
@Profile("local-dev")
public class MainApplication {
  public static void main(String[] args) {
    SpringApplication.run(MainApplication.class, args);
  }
}
