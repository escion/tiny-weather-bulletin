package com.faire.ai.utils;


import com.faire.ai.model.Bulletin;
import com.faire.ai.model.Forecast;
import com.faire.ai.model.openapi.OpenApiBulletin;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class BulletinUtilsTest {

    BulletinUtils utils = new BulletinUtils();

    ObjectMapper mapper = new ObjectMapper();

    OpenApiBulletin openApiBulletin;

    Bulletin bulletin;

    @BeforeEach
    void init() throws IOException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        openApiBulletin = mapper.readValue(new String(Files.readAllBytes(Paths.get("src/test/resources/openapi_mock.json"))), OpenApiBulletin.class);
        utils.setWorkingHoursInterval(Arrays.asList("09:00", "18:00"));
        bulletin = utils.mapOpenApiToBulletin(openApiBulletin);
    }

    @Test
    public void testMapBulletin() {
        Assertions.assertEquals(40, bulletin.getForecast().size());
    }

    @Test
    public void testFilterBulletin() {
        utils.filterBulletinByCollectingForecastsForNextThreeDays(bulletin);
        Assertions.assertEquals(24, bulletin.getForecast().size());
    }

    @Test
    public void getMinTemp(){
        utils.filterBulletinByCollectingForecastsForNextThreeDays(bulletin);
        Forecast forecast = utils.getMinimumTemperature(bulletin.getForecast(), true);
        Assertions.assertNotNull(forecast);
        Assertions.assertEquals(1.26, forecast.getMin());
        forecast = utils.getMinimumTemperature(bulletin.getForecast(), false);
        Assertions.assertNotNull(forecast);
        Assertions.assertEquals(0.15, forecast.getMin());
    }

    @Test
    public void getMaxTemp(){
        utils.filterBulletinByCollectingForecastsForNextThreeDays(bulletin);
        Forecast forecast = utils.getMaximumTemperature(bulletin.getForecast(), true);
        Assertions.assertNotNull(forecast);
        Assertions.assertEquals(5.63, forecast.getMin());
        forecast = utils.getMaximumTemperature(bulletin.getForecast(), false);
        Assertions.assertNotNull(forecast);
        Assertions.assertEquals(3.9, forecast.getMin());
    }

    @Test
    public void getAvgHumidity(){
        utils.filterBulletinByCollectingForecastsForNextThreeDays(bulletin);
        Double humidity = utils.getAvgHumidity(bulletin.getForecast(), true);
        Assertions.assertNotNull(humidity);
        Assertions.assertEquals(70.88, humidity);
        humidity = utils.getAvgHumidity(bulletin.getForecast(), false);
        Assertions.assertNotNull(humidity);
        Assertions.assertEquals(81.93, humidity);
    }
}
