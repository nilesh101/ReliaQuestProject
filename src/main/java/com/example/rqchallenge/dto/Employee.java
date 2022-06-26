package com.example.rqchallenge.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    private Long id;

    @JsonAlias({"employee_name", "name"})
    private String employeeName;

    @JsonAlias({"employee_salary", "salary"})
    private Integer employeeSalary;

    @JsonAlias({"employee_age", "age"})
    private Integer employeeAge;

    @JsonAlias({"profile_image"})
    private String profileImage;

}
