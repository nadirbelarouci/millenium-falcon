package com.starwars.falcon.web;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(MilleniumFalconController.class)
public class WebConfig {
    @Bean
    public Module guavaModule() {
        return new GuavaModule();
    }
}
