package com.faire.ai.model.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OpenApiBulletin {

    String message;

    @JsonProperty("list")
    List<OpenApiForecast> forecast;

    City city;

    @Data
    public class City{
        String name;
        String country;
        Integer timezone;
    }
}
