package com.faire.ai.service;

import com.faire.ai.exception.ForecastNotAvailableException;
import com.faire.ai.model.Bulletin;

import java.util.Optional;

public interface WeatherService {
    Bulletin getForecast(String city, Optional<String> country) throws ForecastNotAvailableException;
}
