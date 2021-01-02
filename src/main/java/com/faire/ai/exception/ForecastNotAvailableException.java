package com.faire.ai.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForecastNotAvailableException extends Exception{
    int code;
    String description;
}
