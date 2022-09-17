package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.employee.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

}
