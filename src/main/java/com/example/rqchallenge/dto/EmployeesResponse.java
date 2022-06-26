package com.example.rqchallenge.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmployeesResponse extends APIBaseResponse {

    List<Employee> data = new ArrayList<>();
}
