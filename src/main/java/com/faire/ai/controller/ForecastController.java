package com.faire.ai.controller;

import com.faire.ai.exception.ForecastNotAvailableException;
import com.faire.ai.model.Bulletin;
import com.faire.ai.model.Error;
import com.faire.ai.service.WeatherService;
import com.faire.ai.utils.BulletinUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/forecast")
@Api("Set of endpoints for retrieving next three days forecast and updating working hours")
public class ForecastController {

    @Autowired
    BulletinUtils utils;

    @Autowired
    @Qualifier("openApiWeather")
    WeatherService weatherService;

    @GetMapping(value = {"/next-three-days/{city}/{country}", "/next-three-days/{city}"})
    @ApiOperation("Returns next three days forecast (considering city local time): min temperature, max temperature and average humidity during and outside working hours")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = Bulletin.class, message = "OK"),
            @ApiResponse(code = 401, response = Error.class, message = "Wrong appid"),
            @ApiResponse(code = 404, response = Error.class, message = "City not found"),
            @ApiResponse(code = 429, response = Error.class, message = "Too many requests"),
            @ApiResponse(code = 500, response = Error.class, message = "Could not get forecast")
        }
    )
    public ResponseEntity getThreeDaysForecast(
            @ApiParam("City name or state, example Milan or Italy. Cannot be blank") @PathVariable(required = true) String city,
            @ApiParam("Country code, example IT for Italy") @PathVariable(required = false) Optional<String> country){
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

    @PostMapping(value = "/next-three-days")
    @ApiOperation("Updates working hours (default 09:00 - 18:00, considered at city local time)")
    public void modifyWorkingHours(
            @ApiParam("Array containing range interval of working hours with HH:mm pattern. Cannot be blank") @Size(max = 2) @RequestBody List<String> hours){
        utils.setWorkingHoursInterval(hours);
    }
}