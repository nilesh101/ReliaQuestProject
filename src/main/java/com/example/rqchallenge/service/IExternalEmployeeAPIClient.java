package com.example.rqchallenge.service;

import com.example.rqchallenge.dto.APIBaseResponse;
import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.dto.EmployeeResponse;
import com.example.rqchallenge.dto.EmployeesResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

public interface IExternalEmployeeAPIClient {

    ResponseEntity<EmployeesResponse> getAllEmployees();

    ResponseEntity<EmployeeResponse> getEmployeeById(String id);

    ResponseEntity<EmployeeResponse> createEmployee(Map<String, Object> employeeInput);

    ResponseEntity<APIBaseResponse> deleteEmployeeById(String id);

}

