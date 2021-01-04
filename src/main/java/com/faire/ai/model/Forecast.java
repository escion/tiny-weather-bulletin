package com.faire.ai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "Representing forecast with specific information")
public class Forecast {

    @JsonIgnore
    LocalDateTime time;

    @ApiModelProperty("Minimum average temperature in Celsius")
    @JsonProperty("min_temperature")
    Double min;

    @ApiModelProperty("Maximum average temperature in Celsius")
    @JsonProperty("max_temperature")
    Double max;

    @ApiModelProperty("Average humidity")
    @JsonProperty("humidity")
    Double humidity;
}
