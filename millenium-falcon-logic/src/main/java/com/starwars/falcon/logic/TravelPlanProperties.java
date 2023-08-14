package com.starwars.falcon.logic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties
record TravelPlanProperties(
    @Positive int autonomy, @NotBlank String departure, @NotBlank String arrival) {
  @ConstructorBinding
  TravelPlanProperties {}
}
