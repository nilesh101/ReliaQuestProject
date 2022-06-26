package com.example.rqchallenge.service;

import com.example.rqchallenge.dto.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IEmployeeService {

    ResponseEntity<List<Employee>> getAllEmployees();

    ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString);

    ResponseEntity<Employee> getEmployeeById(String id);

    ResponseEntity<Integer> getHighestSalaryOfEmployees();

    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

    ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput);

    ResponseEntity<String> deleteEmployeeById(String id);
}
