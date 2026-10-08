package com.example.uloha2.dao;

import com.example.uloha2.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDAOJpaImpl implements EmployeeDAO {
    private final EntityManager em;

    @Autowired
    public EmployeeDAOJpaImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Employee> findAll() {
        TypedQuery<Employee> query = em.createQuery("from Employee", Employee.class);
        List<Employee> employees = query.getResultList();
        return employees;
    }
}
