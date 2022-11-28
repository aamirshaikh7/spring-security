package com.demo.springsecurity.employee;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/employees")
public class EmployeeController {

    private static final List<Employee> EMPLOYEES = Arrays.asList(
            new Employee(1, "John doe"),
            new Employee(2, "Jane doe"),
            new Employee(3, "James doe")
    );

    @GetMapping("{id}")
    public Employee getEmployee(@PathVariable("id") Integer id) {
        return EMPLOYEES.stream()
                .filter(employee -> id.equals(employee.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Employee " + id + " does not exist"));
    }
}
