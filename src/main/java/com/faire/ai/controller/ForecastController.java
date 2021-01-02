package com.faire.ai.controller;

import com.faire.ai.exception.ForecastNotAvailableException;
import com.faire.ai.model.Bulletin;
import com.faire.ai.model.Error;
import com.faire.ai.service.WeatherService;
import com.faire.ai.utils.BulletinUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/forecast")
public class ForecastController {

    @Autowired
    @Qualifier("openApiWeather")
    WeatherService weatherService;

    @GetMapping("/next-three-days/{city}")
    public ResponseEntity getThreeDaysForecast(@PathVariable String city, @RequestParam(required = false) Optional<String> country, @RequestParam(required = false, defaultValue = "working-hours") String period){
        try{
            Bulletin bulletin = weatherService.getForecast(city, country);
            return ResponseEntity.ok(bulletin);
        }
        catch (ForecastNotAvailableException e) {
            Error error = new Error(e.getDescription());
            return ResponseEntity.status(e.getCode()).body(error);
        }
    }
}
