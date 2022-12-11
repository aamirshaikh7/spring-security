package com.demo.springsecurity.employee;

import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_TRAINEE')")
    public List<Employee> getAllEmployees() {
        return EMPLOYEES;
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_TRAINEE')")
    public Employee getEmployee(@PathVariable("id") Integer id) {
        return EMPLOYEES.stream()
                .filter(employee -> id.equals(employee.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Employee " + id + " does not exist"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('employee:write')")
    public void addEmployee(@RequestBody Employee employee) {
        System.out.println(employee);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('employee:write')")
    public void updateEmployee(@PathVariable("id") Integer id, @RequestBody Employee employee) {
        System.out.println(id + " " + employee);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('employee:write')")
    public void deleteEmployee(@PathVariable("id") Integer id) {
        System.out.println(id);
    }
}

