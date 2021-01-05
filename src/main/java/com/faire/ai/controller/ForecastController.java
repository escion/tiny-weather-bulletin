package com.faire.ai.controller;

import com.faire.ai.exception.ForecastNotAvailableException;
import com.faire.ai.model.Bulletin;
import com.faire.ai.model.Error;
import com.faire.ai.service.WeatherService;
import com.faire.ai.utils.BulletinUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forecast")
@Api("Set of endpoints for retrieving next three days forecast and updating working hours")
public class ForecastController {

    @Autowired
    BulletinUtils utils;

    @Autowired
    @Qualifier("openApiWeather")
    WeatherService weatherService;

    @GetMapping(value = {"/next-three-days/{city}"})
    @ApiOperation("Returns next three days forecast by city name or state(considering city local time): min temperature, max temperature and average humidity during and outside working hours")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = Bulletin.class, message = "OK"),
            @ApiResponse(code = 401, response = Error.class, message = "Wrong appid"),
            @ApiResponse(code = 404, response = Error.class, message = "City not found"),
            @ApiResponse(code = 429, response = Error.class, message = "Too many requests"),
            @ApiResponse(code = 500, response = Error.class, message = "Could not get forecast")
    }
    )
    public ResponseEntity getThreeDaysForecast(
            @ApiParam(value = "City name or state, example Milan or Italy. Cannot be blank") @PathVariable String city){
        return getForecast(city, null);
    }

    @GetMapping(value = {"/next-three-days/{city}/{country}"})
    @ApiOperation("Returns next three days forecast by city name or state and by country code (considering city local time): min temperature, max temperature and average humidity during and outside working hours")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = Bulletin.class, message = "OK"),
            @ApiResponse(code = 401, response = Error.class, message = "Wrong appid"),
            @ApiResponse(code = 404, response = Error.class, message = "City not found"),
            @ApiResponse(code = 429, response = Error.class, message = "Too many requests"),
            @ApiResponse(code = 500, response = Error.class, message = "Could not get forecast")
        }
    )
    public ResponseEntity getThreeDaysForecastWithCountry(
            @ApiParam(value = "City name or state, example Milan or Italy. Cannot be blank") @PathVariable String city,
            @ApiParam(value = "Country code, example IT for Italy") @PathVariable String country){
        return getForecast(city, country);
    }

    @PostMapping(value = "/next-three-days")
    @ApiOperation("Updates working hours (default 09:00 - 18:00, considered at city local time)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = Bulletin.class, message = "OK"),
            @ApiResponse(code = 500, response = Error.class, message = "Error setting working hours")
    }
    )
    public ResponseEntity<Object> modifyWorkingHours(
            @ApiParam("Array containing range interval of working hours with HH:mm pattern. Cannot be blank") @RequestBody List<String> hours){
        try{
            utils.setWorkingHoursInterval(hours);
            return ResponseEntity.ok().body(null);
        }
        catch(Throwable e){
            Error error = new Error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    private ResponseEntity getForecast(String city, String country){
        try{
            Bulletin bulletin = weatherService.getForecast(city, country);
            utils.filterBulletinByCollectingForecastsForNextThreeDays(bulletin);
            return ResponseEntity.ok(bulletin);
        }
        catch (ForecastNotAvailableException e) {
            Error error = new Error(e.getDescription());
            return ResponseEntity.status(e.getCode()).body(error);
        }
    }
}