package com.example.rqchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenericErrorResponse {
    private String message;
    private Integer status;
    private String error;
}
