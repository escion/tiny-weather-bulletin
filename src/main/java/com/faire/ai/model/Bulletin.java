package com.faire.ai.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "Representing bulletin")
public class Bulletin {

    @ApiModelProperty(value = "City name")
    String city;

    @ApiModelProperty(value = "Country code")
    String country;

    @ApiModelProperty(value = "City local time")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime currentTime;

    @JsonIgnore
    List<Forecast> forecast;

    @ApiModelProperty(value = "Forecast during working hours (default 09:00 - 18:00)")
    @JsonProperty("during_working_hours")
    Forecast workingHours;

    @ApiModelProperty(value = "Forecast outside working hours")
    @JsonProperty("outside_working_hours")
    Forecast notWorkingHours;
}
