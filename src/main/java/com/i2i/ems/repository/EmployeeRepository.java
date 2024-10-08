package com.i2i.ems.repository;

import java.util.List;

import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.i2i.ems.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

  List<Employee> findAllByIsDeletedFalse();

  Employee findByIdAndIsDeletedFalse(Integer id);

  boolean existsByMobileNoOrEmailAndIsDeletedFalse(long mobileNo, String email);

  Employee findByEmail(@Email(message = "Email should be in proper format (eg. admin@gmail.com)") String email);

  boolean existsByEmailAndIsDeletedFalse(@Email(message = "Email should be in proper format (eg. admin@gmail.com)") String email);
}