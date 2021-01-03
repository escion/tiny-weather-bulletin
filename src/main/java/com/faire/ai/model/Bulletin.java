package com.faire.ai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Bulletin {

    String city;

    String country;

    LocalDateTime currentTime;

    @JsonIgnore
    List<Forecast> forecast;

    @JsonProperty("during_working_hours")
    Forecast workingHours;

    @JsonProperty("outside_working_hours")
    Forecast notWorkingHours;
}
