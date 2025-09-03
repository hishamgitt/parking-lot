package com.parkinglot.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI parkingLotOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Parking Lot Reservation API")
                        .description("API for managing parking lot reservations")
                        .version("1.0"));
    }
}