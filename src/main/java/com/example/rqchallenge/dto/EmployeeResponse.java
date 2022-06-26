package com.example.rqchallenge.dto;

import lombok.Data;

@Data
public class EmployeeResponse extends APIBaseResponse {

    private Employee data;
}
