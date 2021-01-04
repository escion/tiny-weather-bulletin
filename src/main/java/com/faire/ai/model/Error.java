package com.faire.ai.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(description = "Representing error while getting forecast")
public class Error {
    String description;
}
