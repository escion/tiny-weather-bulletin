package com.faire.ai.model;

import lombok.Data;

@Data
public class Forecast {
    String date;
    Double min;
    Double max;
    Integer humidity;
}
