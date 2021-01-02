package com.faire.ai.utils;

import com.faire.ai.model.Bulletin;
import com.faire.ai.model.Forecast;
import com.faire.ai.model.openapi.OpenApiBulletin;
import com.faire.ai.model.openapi.OpenApiForecast;

import java.util.stream.Collectors;

public class WeatherMapper {

    public static Bulletin map(OpenApiBulletin openApiBulletin){
        Bulletin bulletin = new Bulletin();
        bulletin.setCity(openApiBulletin.getCity().getName());
        bulletin.setCountry(openApiBulletin.getCity().getCountry());
        bulletin.setForecast(openApiBulletin.getForecast().stream().map(forecast -> getForecastFromOpenApi(forecast, openApiBulletin.getCity().getTimezone())).collect(Collectors.toList()));
        return bulletin;
    }

    private static Forecast getForecastFromOpenApi(OpenApiForecast forecast, Integer timezone){
        Forecast f = new Forecast();
        f.setDate(BulletinUtils.getDateFromTimestamp(forecast.getTimestamp() + timezone));
        f.setMin(forecast.getForecast().getMinTemperature());
        f.setMax(forecast.getForecast().getMaxTemperature());
        f.setHumidity(forecast.getForecast().getHumidity());
        return f;
    }
}
