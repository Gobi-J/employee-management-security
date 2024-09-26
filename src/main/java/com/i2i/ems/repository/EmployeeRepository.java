package com.i2i.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.i2i.ems.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
  boolean existsByIdAndIsDeletedFalse(Integer id);

  List<Employee> findAllByIsDeletedFalse();

  Employee findByIdAndIsDeletedFalse(Integer id);
}
