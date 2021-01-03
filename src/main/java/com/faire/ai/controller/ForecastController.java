package com.faire.ai.controller;

import com.faire.ai.exception.ForecastNotAvailableException;
import com.faire.ai.model.Bulletin;
import com.faire.ai.model.Error;
import com.faire.ai.service.WeatherService;
import com.faire.ai.utils.BulletinUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/forecast")
public class ForecastController {

    @Autowired
    BulletinUtils utils;

    @Autowired
    @Qualifier("openApiWeather")
    WeatherService weatherService;

    @GetMapping(value = {"/next-three-days/{city}/{country}", "/next-three-days/{city}"})
    public ResponseEntity getThreeDaysForecast(
            @PathVariable String city,
            @PathVariable(required = false) Optional<String> country){
        try{
            Bulletin bulletin = weatherService.getForecast(city, country);
            utils.filterBulletinOnlyForNextThreeDays(bulletin);
            return ResponseEntity.ok(bulletin);
        }
        catch (ForecastNotAvailableException e) {
            Error error = new Error(e.getDescription());
            return ResponseEntity.status(e.getCode()).body(error);
        }
    }

    @PostMapping(value = "/next-three-days")
    public void modifyWorkingHours(@RequestBody List<String> hours){
        utils.setWorkingHoursInterval(hours);
    }
}