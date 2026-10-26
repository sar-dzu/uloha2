
package com.example.uloha2.controller;

import com.example.uloha2.entity.Employee;
import com.example.uloha2.response.ResponseFactory;
import com.example.uloha2.response.ResponseWrapper;
import com.example.uloha2.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {
    private final EmployeeService employeeService;
    private final ResponseFactory response;

    @Autowired
    public EmployeeRestController(EmployeeService employeeService, ResponseFactory response) {
        this.employeeService = employeeService;
        this.response = response;
    }

    @GetMapping("/employees")
    public ResponseEntity<ResponseWrapper<List<Employee>>> findAll() {
        List<Employee> employees = employeeService.findAll();
        return response.ok("Úspešne načítané", employees);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<ResponseWrapper<Employee>> getEmployeeById(@PathVariable int id) {
        Employee employee = employeeService.findById(id);

//        if (employee == null) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND, "Employee not found: " + id);
//        }

        return response.ok("Zamestnanec uspesne nacitany", employee);
    }

    @PostMapping("/employees")
    public ResponseEntity<ResponseWrapper<Employee>> createEmployee(@Valid @RequestBody Employee employee) {
        employee.setId(null);
        Employee savedEmployee = employeeService.save(employee);
        return response.created("Zamestnanec uspesne pridany", savedEmployee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<ResponseWrapper<Employee>> updateEmployee(@PathVariable int id,@Valid @RequestBody Employee employee) {
//        if (existingEmployee == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found: " + id);
//        }
        employeeService.findById(id);
        employee.setId(id);
        Employee updatedEmployee = employeeService.save(employee);
        return response.ok("Zamestnanec uspesne aktualizovaný", updatedEmployee);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteEmployee(@PathVariable int id) {
        employeeService.deleteById(id);
        return response.ok("Zamestnanec uspesne vymazany");
    }
}
