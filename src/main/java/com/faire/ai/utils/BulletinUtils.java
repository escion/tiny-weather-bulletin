package com.faire.ai.utils;

import com.faire.ai.model.Bulletin;
import com.faire.ai.model.Forecast;
import com.faire.ai.model.openapi.OpenApiBulletin;
import com.faire.ai.model.openapi.OpenApiForecast;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BulletinUtils {

    @Value("${working-hours.from}")
    String workingHourFrom;

    @Value("${working-hours.to}")
    String workingHourTo;

    Pair<LocalTime, LocalTime> workingHoursInterval;

    String DEFAULT_TIMEZONE = "UTC";

    @PostConstruct
    private void init(){
        workingHoursInterval = Pair.of(LocalTime.parse(workingHourFrom), LocalTime.parse(workingHourTo));
    }

    public synchronized void setWorkingHoursInterval(List<String> hours){
        workingHoursInterval = Pair.of(LocalTime.parse(hours.get(0)), LocalTime.parse(hours.get(1)));
    }

    public Bulletin mapOpenApiToBulletin(OpenApiBulletin openApiBulletin){
        Bulletin bulletin = new Bulletin();
        bulletin.setCity(openApiBulletin.getCity().getName());
        bulletin.setCountry(openApiBulletin.getCity().getCountry());
        bulletin.setCurrentTime(getNow(openApiBulletin.getCity().getTimezone()));
        bulletin.setForecast(openApiBulletin.getForecast()
                .stream()
                .map(forecast -> getForecastFromOpenApi(forecast, openApiBulletin.getCity().getTimezone()))
                .collect(Collectors.toList()));
        return bulletin;
    }

    public void filterBulletinByCollectingForecastsForNextThreeDays(Bulletin bulletin){
        final Pair<LocalDateTime, LocalDateTime> interval = getNextThreeDaysInterval(bulletin.getCurrentTime());
        bulletin.setForecast(bulletin.getForecast()
                .stream()
                .filter(forecast -> (forecast.getTime().compareTo(interval.getLeft()) >= 0 && forecast.getTime().compareTo(interval.getRight()) <= 0))
                .collect(Collectors.toList()));
        bulletin.setWorkingHours(collectForecastDuringWorkingHours(bulletin.getForecast()));
        bulletin.setNotWorkingHours(collectForecastOutsideWorkingHours(bulletin.getForecast()));
    }

    public Forecast collectForecastDuringWorkingHours(List<Forecast> forecasts){
        return collectForecast(forecasts, true);
    }

    public Forecast collectForecastOutsideWorkingHours(List<Forecast> forecasts){
        return collectForecast(forecasts, false);
    }

    private Forecast collectForecast(List<Forecast> forecasts, boolean workingHours){
        Forecast forecast = new Forecast();
        forecast.setMin(getMinimumTemperature(forecasts, workingHours).getMin());
        forecast.setMax(getMaximumTemperature(forecasts, workingHours).getMax());
        forecast.setHumidity(getAvgHumidity(forecasts, workingHours));
        return forecast;
    }

    public Forecast getMinimumTemperature(List<Forecast> forecasts, boolean workingHours){
        return forecasts.stream()
                .filter(forecast -> workingHours ? isInWorkingHours(forecast) : isOutsideWorkingHours(forecast))
                .min(Comparator.comparing(Forecast::getMin))
                .get();
    }

    public Forecast getMaximumTemperature(List<Forecast> forecasts, boolean workingHours){
        return forecasts.stream()
                .filter(forecast -> workingHours ? isInWorkingHours(forecast) : isOutsideWorkingHours(forecast))
                .max(Comparator.comparing(Forecast::getMax))
                .get();
    }

    public Double getAvgHumidity(List<Forecast> forecasts, boolean workingHours){
        Double humidity = forecasts.stream()
                .filter(forecast -> workingHours ? isInWorkingHours(forecast) : isOutsideWorkingHours(forecast))
                .mapToDouble(Forecast::getHumidity)
                .average()
                .getAsDouble();
        return Math.floor(humidity * 100)/100;
    }

    private boolean isInWorkingHours(Forecast forecast) {
        return forecast.getTime().toLocalTime().compareTo(workingHoursInterval.getLeft()) >= 0 && forecast.getTime().toLocalTime().compareTo(workingHoursInterval.getRight()) <=0;
    }

    private boolean isOutsideWorkingHours(Forecast forecast) {
        return forecast.getTime().toLocalTime().compareTo(workingHoursInterval.getLeft()) < 0 || forecast.getTime().toLocalTime().compareTo(workingHoursInterval.getRight()) > 0;
    }

    private Pair<LocalDateTime, LocalDateTime> getNextThreeDaysInterval(LocalDateTime current){
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDateTime todayMidnight = LocalDateTime.of(current.toLocalDate(), midnight);
        return Pair.of(todayMidnight.plusDays(1), todayMidnight.plusDays(3).plusHours(23).plusMinutes(59).plusSeconds(59));
    }

    private LocalDateTime getLocalDateFromTimestamp(int timestamp){
        return Instant.ofEpochSecond(new Long(timestamp)).atZone(ZoneId.of(DEFAULT_TIMEZONE)).toLocalDateTime();
    }

    private LocalDateTime getNow(int timezoneOffset){
        return getLocalDateFromTimestamp((int) (Instant.now().getEpochSecond() + timezoneOffset));
    }

    private Forecast getForecastFromOpenApi(OpenApiForecast forecast, Integer timezone){
        Forecast f = new Forecast();
        f.setTime(getLocalDateFromTimestamp(forecast.getTimestamp() + timezone));
        f.setMin(forecast.getForecast().getMinTemperature());
        f.setMax(forecast.getForecast().getMaxTemperature());
        f.setHumidity(forecast.getForecast().getHumidity().doubleValue());
        return f;
    }
}
