package com.faire.ai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Forecast {

    @JsonIgnore
    LocalDateTime time;

    @JsonProperty("min_temperature")
    Double min;

    @JsonProperty("max_temperature")
    Double max;

    @JsonProperty("humidity")
    Double humidity;
}
