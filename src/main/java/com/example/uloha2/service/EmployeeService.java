
package com.example.uloha2.service;

import com.example.uloha2.entity.Employee;
import com.example.uloha2.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Zamestnanec s ID " + id + " neexistuje"));
    }

    @Transactional
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException(
                    "Zamestnanec s ID " + id + " neexistuje");
        }

        employeeRepository.deleteById(id);
    }

    public boolean emailExistsForOther(String email, Integer currentId) {
        return employeeRepository.findByEmailIgnoreCase(email)
                .map(other -> !Objects.equals(other.getId(), currentId))
                .orElse(false);
    }
}
