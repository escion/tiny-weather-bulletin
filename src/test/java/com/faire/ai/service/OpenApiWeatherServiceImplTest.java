package com.faire.ai.service;

import com.faire.ai.exception.ForecastNotAvailableException;
import com.faire.ai.model.Bulletin;
import com.faire.ai.model.openapi.OpenApiBulletin;
import com.faire.ai.service.impl.OpenApiWeatherServiceImpl;
import com.faire.ai.utils.BulletinUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OpenApiWeatherServiceImplTest {

    @Mock
    RestTemplate restTemplate;

    @Mock
    BulletinUtils utils;

    @InjectMocks
    OpenApiWeatherServiceImpl service;

    @Test
    public void testCallServiceOk() throws ForecastNotAvailableException {
        ReflectionTestUtils.setField(service, "baseUrl", "fake/path");
        when(restTemplate.getForEntity(anyString(), eq(OpenApiBulletin.class), any(Object.class))).thenReturn(ResponseEntity.ok().body(new OpenApiBulletin()));
        when(utils.mapOpenApiToBulletin(any(OpenApiBulletin.class))).thenReturn(new Bulletin());
        Bulletin bulletin = service.getForecast("Milan", Optional.empty());
        Assertions.assertNotNull(bulletin);
    }

    @Test
    public void testCallService401(){
        ReflectionTestUtils.setField(service, "baseUrl", "fake/path");
        when(restTemplate.getForEntity(anyString(), eq(OpenApiBulletin.class), any(Object.class))).thenThrow(new RestClientResponseException("Appid not valid",401,"Appid not valid",null,null,null));
        Assertions.assertThrows(ForecastNotAvailableException.class, () -> service.getForecast("Milan", Optional.empty()));
    }

    @Test
    public void testCallService500(){
        ReflectionTestUtils.setField(service, "baseUrl", "fake/path");
        when(restTemplate.getForEntity(anyString(), eq(OpenApiBulletin.class), any(Object.class))).thenThrow(RuntimeException.class);
        Assertions.assertThrows(ForecastNotAvailableException.class, () -> service.getForecast("Milan", Optional.empty()));
    }

    @Test
    public void testCallService404(){
        ReflectionTestUtils.setField(service, "baseUrl", "fake/path");
        when(restTemplate.getForEntity(anyString(), eq(OpenApiBulletin.class), any(Object.class))).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OpenApiBulletin()));
        Assertions.assertThrows(ForecastNotAvailableException.class, () -> service.getForecast("Milan", Optional.empty()));
    }
}
