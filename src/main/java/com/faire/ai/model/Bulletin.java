package com.faire.ai.model;

import lombok.Data;

import java.util.List;

@Data
public class Bulletin {
    String city;
    String country;
    List<Forecast> forecast;
}
