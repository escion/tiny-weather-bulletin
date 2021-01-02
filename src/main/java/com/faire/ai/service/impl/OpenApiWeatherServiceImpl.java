package com.faire.ai.service.impl;

import com.faire.ai.exception.ForecastNotAvailableException;
import com.faire.ai.model.Bulletin;
import com.faire.ai.model.openapi.OpenApiBulletin;
import com.faire.ai.service.WeatherService;
import com.faire.ai.utils.WeatherMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service("openApiWeather")
public class OpenApiWeatherServiceImpl implements WeatherService {

    @Value("${api.openweather.baseUrl}")
    String baseUrl;

    RestTemplate restTemplate;

    public OpenApiWeatherServiceImpl(){
        restTemplate = new RestTemplate();
    }

    @Override
    public Bulletin getForecast(String city, Optional<String> country) throws ForecastNotAvailableException{
        try{
            if(country.isPresent()){
                city = StringUtils.joinWith(",", city, country.get());
            }
            ResponseEntity<OpenApiBulletin> response = restTemplate.getForEntity(baseUrl, OpenApiBulletin.class, city);
            if(response.getStatusCode().is2xxSuccessful()){
                return WeatherMapper.map(response.getBody());
            }
            else{
                throw new ForecastNotAvailableException(response.getStatusCodeValue(), response.getBody().getMessage());
            }
        }
        catch(Throwable e){
            if(e instanceof RestClientResponseException){
                RestClientResponseException exception = (RestClientResponseException)e;
                throw new ForecastNotAvailableException(exception.getRawStatusCode(), exception.getMessage());
            }
            else{
                throw new ForecastNotAvailableException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            }
        }
    }
}
