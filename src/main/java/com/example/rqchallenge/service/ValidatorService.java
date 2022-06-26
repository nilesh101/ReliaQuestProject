package com.example.rqchallenge.service;

import com.example.rqchallenge.constants.MessageConstants;
import com.example.rqchallenge.exceptionhandler.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class ValidatorService {

    public void validateCreateEmployeeInputPayload(Map<String, Object> employeeInput) {
        if(Objects.isNull(employeeInput.get("name"))) {
            throw new InvalidRequestException(MessageConstants.NAME_FIELD_MANDATORY);
        }

        if(Objects.isNull(employeeInput.get("salary"))) {
            throw new InvalidRequestException(MessageConstants.SALARY_FIELD_MANDATORY);
        }

        if(Objects.isNull(employeeInput.get("age"))) {
            throw new InvalidRequestException(MessageConstants.AGE_FIELD_MANDATORY);
        }

    }
}
