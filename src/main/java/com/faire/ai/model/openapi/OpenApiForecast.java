package com.faire.ai.model.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenApiForecast {

    @JsonProperty("dt")
    Integer timestamp;

    @JsonProperty("main")
    Main forecast;

    @Data
    public class Main{

        @JsonProperty("temp_min")
        Double minTemperature;

        @JsonProperty("temp_max")
        Double maxTemperature;

        @JsonProperty("humidity")
        Integer humidity;
    }
}
