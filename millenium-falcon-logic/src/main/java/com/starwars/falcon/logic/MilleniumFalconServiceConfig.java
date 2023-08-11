package com.starwars.falcon.logic;

import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({MilleniumFalconTravelPlannerImpl.class, RouteRepository.class})
@EnableConfigurationProperties(TravelPlanProperties.class)
public class MilleniumFalconServiceConfig {
  @Bean
  public DSLContext dsl(DataSource dataSource) {
    return DSL.using(new DefaultConfiguration().set(dataSource).derive(SQLDialect.SQLITE));
  }
}
