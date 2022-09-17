package com.example.domain.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Transactional
    @CachePut(cacheNames = "employee", key = "#employee.id")
    public Employee update(Employee employee) {
        return repository.save(employee);
    }

    @Transactional
    @Cacheable("employee")
    public Employee findById(String id) {
        // 2秒待つ
        try {
            long time = 2000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        Optional<Employee> employee = repository.findById(id);
        return employee.get();
    }

    @Transactional
    public List<Employee> findAll() {
        return repository.findAll();
    }

    @Transactional
    @CacheEvict("employee")
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
