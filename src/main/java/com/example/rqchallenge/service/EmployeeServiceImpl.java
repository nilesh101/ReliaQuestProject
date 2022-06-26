package com.example.rqchallenge.service;

import com.example.rqchallenge.constants.MessageConstants;
import com.example.rqchallenge.dto.APIBaseResponse;
import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.dto.EmployeeResponse;
import com.example.rqchallenge.dto.EmployeesResponse;
import com.example.rqchallenge.exceptionhandler.OperationFailedException;
import com.example.rqchallenge.exceptionhandler.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * Employee operation's business logic
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class EmployeeServiceImpl implements IEmployeeService {

    private final IExternalEmployeeAPIClient iExternalEmployeeAPIClient;

    private final ValidatorService validatorService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try {
            ResponseEntity<EmployeesResponse> empResponseEntity = iExternalEmployeeAPIClient.getAllEmployees();
            return new ResponseEntity<>(empResponseEntity.getBody().getData(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to get all employee list.", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        ResponseEntity<EmployeesResponse> empResponseEntity = iExternalEmployeeAPIClient.getAllEmployees();

        if(Objects.isNull(empResponseEntity.getBody()) || Objects.isNull(empResponseEntity.getBody().getData())) {
            log.error("Failed to get employee list to get employees by name search.");
            throw new OperationFailedException(String.format(MessageConstants.SEARCH_BY_NAME_OP_FAILED, searchString));
        }

        return new ResponseEntity<>(searchMatchingNameEmployeeList(empResponseEntity, searchString), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        try {
            ResponseEntity<EmployeeResponse> empResponseEntity = iExternalEmployeeAPIClient.getEmployeeById(id);

            if (Objects.nonNull(empResponseEntity.getBody()) && Objects.nonNull(empResponseEntity.getBody().getData()))
                return new ResponseEntity<>(empResponseEntity.getBody().getData(), HttpStatus.OK);
            else
                throw new ResourceNotFoundException(String.format(MessageConstants.EMP_NOT_FOUND, id));
        } catch (Exception e) {
            log.error("Failed to get employee with id {}", id, e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        ResponseEntity<EmployeesResponse> empResponseEntity = iExternalEmployeeAPIClient.getAllEmployees();

        if(Objects.isNull(empResponseEntity.getBody()) || Objects.isNull(empResponseEntity.getBody().getData())) {
            log.error("Failed to get employee list to calculate highest salary of employee.");
            throw new OperationFailedException(MessageConstants.CALC_HIGHEST_SALARY_OP_FAILED);
        }

        return new ResponseEntity<>(calculateMaxSalary(empResponseEntity), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        ResponseEntity<EmployeesResponse> empResponseEntity = iExternalEmployeeAPIClient.getAllEmployees();

        if(Objects.isNull(empResponseEntity.getBody()) || Objects.isNull(empResponseEntity.getBody().getData())) {
            log.error("Failed to get employee list to calculate top ten highest earning employees.");
            throw new OperationFailedException(MessageConstants.TOP_TEN_HIGHEST_EARNING_OP_FAILED);
        }

        return new ResponseEntity<>(calculateTopTenEarningEmployeeNames(empResponseEntity, MessageConstants.HIGHEST_EARNING_LIMIT), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        validatorService.validateCreateEmployeeInputPayload(employeeInput);
        ResponseEntity<EmployeeResponse> employeeResponseEntity = iExternalEmployeeAPIClient.createEmployee(employeeInput);

        if(Objects.isNull(employeeResponseEntity.getBody()) || Objects.isNull(employeeResponseEntity.getBody().getData())) {
            log.error("Failed to create employee record with given details {}", employeeInput);
            throw new OperationFailedException(String.format(MessageConstants.CREATE_EMPLOYEE_FAILED, employeeInput.get("name")));
        }
        return new ResponseEntity<>(employeeResponseEntity.getBody().getData(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        //Before deleting should we check does employee exist with that id?
        //Because for invalid id also original API returning success where we do not have control
        ResponseEntity<APIBaseResponse> deleteResponse = iExternalEmployeeAPIClient.deleteEmployeeById(id);
        return new ResponseEntity<>(deleteResponse.getBody().getMessage(), HttpStatus.OK);
    }

    /**
     * @param empResponseEntity
     * @return Max salary value
     */
    private Integer calculateMaxSalary(ResponseEntity<EmployeesResponse> empResponseEntity) {
        List<Employee> employeeList = empResponseEntity.getBody().getData();
        int result = 0;

        for (Employee employee : employeeList) {
            result = Math.max(result, employee.getEmployeeSalary());
        }
        return result;
    }

    /**
     * @param empResponseEntity
     * @param searchString
     * @return List of employees having searchString in its name
     */
    private List<Employee> searchMatchingNameEmployeeList(ResponseEntity<EmployeesResponse> empResponseEntity, String searchString) {
        List<Employee> employeeList = empResponseEntity.getBody().getData();
        List<Employee> employeeResultList = new ArrayList<>();

        for (Employee employee : employeeList) {

            if(employee.getEmployeeName().contains(searchString)) {
                employeeResultList.add(employee);
            }
        }
        return employeeResultList;
    }

    /**
     * @param empResponseEntity
     * @param limit
     * @return List of top 10 highest earning employee names
     */
    //Time Complexity- O(nlogn)
    private List<String> calculateTopTenEarningEmployeeNames(ResponseEntity<EmployeesResponse> empResponseEntity, Integer limit) {
        List<Employee> employeeList = empResponseEntity.getBody().getData();

        PriorityQueue<Employee> minHeap = new PriorityQueue<>(new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                return e1.getEmployeeSalary().compareTo(e2.getEmployeeSalary());
            }
        });//salary based comparator
        limit = Math.min(limit, employeeList.size()); //To avoid IndexOutOfBoundException if employee list is less than of size 10

        for (int i=0; i<limit; i++) {
            minHeap.add(employeeList.get(i));
        }

        for(int i=limit; i<employeeList.size(); i++) {
            if(minHeap.peek().getEmployeeSalary() < employeeList.get(i).getEmployeeSalary()) {
                minHeap.poll();
                minHeap.add(employeeList.get(i));
            }
        }
        List<String> resultNameList = new ArrayList<>();

        while (!minHeap.isEmpty()) {
            resultNameList.add(minHeap.poll().getEmployeeName());
        }
        Collections.reverse(resultNameList);
        log.trace("Top {} earning employee names are {}.", limit, resultNameList);
        return resultNameList;
    }

}
