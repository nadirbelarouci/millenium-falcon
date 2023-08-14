package com.starwars.falcon.web;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Import(RouteController.class)
public class WebConfig {
  @Bean
  public Module guavaModule() {
    return new GuavaModule();
  }

  @Bean
  @Profile("local-dev")
  public WebMvcConfigurer corsConfigurer(@Value("${allowed-origins}") String... allowedOrigins) {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(allowedOrigins);
      }
    };
  }
}
