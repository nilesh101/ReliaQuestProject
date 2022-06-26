package com.example.rqchallenge.controller;

import com.example.rqchallenge.aspects.Timed;
import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.employees.IEmployeeController;
import com.example.rqchallenge.service.IEmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Employee crud and other operation implementation controller
 */
@Controller
@ResponseBody
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeControllerImpl implements IEmployeeController {

    private final IEmployeeService iEmployeeService;

    /**
     * @return all employees
     */
    @Override
    @Timed
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return iEmployeeService.getAllEmployees();
    }

    /**
     * @param searchString
     * @return all employees whose name contains or matches the string input provided
     */
    @Override
    @Timed
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        return iEmployeeService.getEmployeesByNameSearch(searchString);
    }

    /**
     * @param id
     * @return a single employee
     */
    @Override
    @Timed
    public ResponseEntity<Employee> getEmployeeById(String id) {
        return iEmployeeService.getEmployeeById(id);
    }

    /**
     * @return a single integer indicating the highest salary of all employees
     */
    @Override
    @Timed
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return iEmployeeService.getHighestSalaryOfEmployees();
    }

    /**
     * @return a list of the top 10 employees based off of their salaries
     */
    @Override
    @Timed
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return iEmployeeService.getTopTenHighestEarningEmployeeNames();
    }

    /**
     * @param employeeInput
     * @return the object of created employee
     */
    @Override
    @Timed
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        return iEmployeeService.createEmployee(employeeInput);
    }

    /**
     * @param id
     * @return Meesage of operation executed successfully
     */
    @Override
    @Timed
    public ResponseEntity<String> deleteEmployeeById(String id) {
        return iEmployeeService.deleteEmployeeById(id);
    }
}
