package com.demo.springsecurity.employee;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("admin/api/v1/employees")
public class EmployeeAdministrationController {

    private static final List<Employee> EMPLOYEES = Arrays.asList(
            new Employee(1, "John doe"),
            new Employee(2, "Jane doe"),
            new Employee(3, "James doe")
    );

    @GetMapping
    public List<Employee> getAllEmployees() {
        return EMPLOYEES;
    }

    @GetMapping("{id}")
    public Employee getEmployee(@PathVariable("id") Integer id) {
        return EMPLOYEES.stream()
                .filter(employee -> id.equals(employee.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Employee " + id + " does not exist"));
    }

    @PostMapping
    public void addEmployee(@RequestBody Employee employee) {
        System.out.println(employee);
    }

    @PutMapping("{id}")
    public void updateEmployee(@PathVariable("id") Integer id, @RequestBody Employee employee) {
        System.out.println(id + " " + employee);
    }

    @DeleteMapping("{id}")
    public void deleteEmployee(@PathVariable("id") Integer id) {
        System.out.println(id);
    }
}

