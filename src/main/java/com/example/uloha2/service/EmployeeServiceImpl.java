package com.example.uloha2.service;

import com.example.uloha2.dao.EmployeeRepository;
import com.example.uloha2.entity.Employee;
import com.example.uloha2.exception.EmailAlreadyExistsException;
import com.example.uloha2.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(int id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Zamestnanec", id));
    }

    @Transactional
    @Override
    public Employee save(Employee employee) {
        // Kontrola duplicitného emailu pri vytváraní nového študenta
        if (employee.getId() == null) {
            if (employeeRepository.existsByEmail(employee.getEmail())) {
                throw new EmailAlreadyExistsException(employee.getEmail());
            }
        } else {
            // Pri aktualizácii skontroluje, či email nepatrí inému študentovi
            Employee existingWithEmail = employeeRepository.findByEmail(employee.getEmail()).orElse(null);
            if (existingWithEmail != null && !Objects.equals(existingWithEmail.getId(), employee.getId())) {
                throw new EmailAlreadyExistsException(employee.getEmail());
            }
        }

        return employeeRepository.save(employee);
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        // Skontroluje, či študent existuje
        if (!employeeRepository.existsById(id)) {
            throw new ObjectNotFoundException("Zamestnanec", id);
        }
        employeeRepository.deleteById(id);
    }
}
