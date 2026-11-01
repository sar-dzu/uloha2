package com.example.uloha2.repository;

import com.example.uloha2.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmailIgnoreCase(String email);
}
