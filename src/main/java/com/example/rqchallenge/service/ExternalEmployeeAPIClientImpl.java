package com.example.rqchallenge.service;

import com.example.rqchallenge.constants.APIUrlConstants;
import com.example.rqchallenge.constants.MessageConstants;
import com.example.rqchallenge.dto.APIBaseResponse;
import com.example.rqchallenge.dto.EmployeeResponse;
import com.example.rqchallenge.dto.EmployeesResponse;
import com.example.rqchallenge.exceptionhandler.OperationFailedException;
import com.example.rqchallenge.util.RestExecutorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * Logic to interact with RestClient for employee crud operations to external world
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ExternalEmployeeAPIClientImpl implements IExternalEmployeeAPIClient {

    private final RestExecutorService restExecutorService;

    @Override
    public ResponseEntity<EmployeesResponse> getAllEmployees() {
        return restExecutorService.execute(APIUrlConstants.BASE_URL + APIUrlConstants.GET_ALL_EMPLOYEES_ENDPOINT_URL,
                getHttpEntity(), HttpMethod.GET, EmployeesResponse.class);
    }

    @Override
    public ResponseEntity<EmployeeResponse> getEmployeeById(String id) {
        return restExecutorService.execute(APIUrlConstants.BASE_URL + String.format(APIUrlConstants.GET_EMPLOYEE_BY_ID_ENDPOINT_URL, id),
                getHttpEntity(), HttpMethod.GET, EmployeeResponse.class);
    }

    @Override
    public ResponseEntity<EmployeeResponse> createEmployee(Map<String, Object> employeeInput) {
        return restExecutorService.execute(APIUrlConstants.BASE_URL + APIUrlConstants.CREATE_EMPLOYEE_ENDPOINT_URL,
                getHttpEntity(employeeInput), HttpMethod.POST, EmployeeResponse.class);
    }

    @Override
    public ResponseEntity<APIBaseResponse> deleteEmployeeById(String id) {
        ResponseEntity<APIBaseResponse> responseEntity = restExecutorService.execute(APIUrlConstants.BASE_URL + String.format(APIUrlConstants.DELETE_EMPLOYEE_ENDPOINT_URL, id),
                getHttpEntity(), HttpMethod.DELETE, APIBaseResponse.class);

        if(responseEntity.getStatusCode().isError())
            throw new OperationFailedException(String.format(MessageConstants.DELETE_EMPLOYEE_FAILED, id));
        return responseEntity;
    }

    /**
     * @return HttpEntity with only headers
     */
    private HttpEntity getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity(headers);
    }

    /**
     * @param employeeInput
     * @return HttpEntity with header and input payload body
     */
    private HttpEntity getHttpEntity(Map<String, Object> employeeInput) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity(employeeInput, headers);
    }
}
