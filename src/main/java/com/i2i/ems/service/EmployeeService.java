package com.i2i.ems.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.i2i.ems.dto.EmployeeDto;
import com.i2i.ems.helper.EmployeeException;
import com.i2i.ems.mapper.EmployeeMapper;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.i2i.ems.model.Employee;
import com.i2i.ems.repository.EmployeeRepository;

/**
 * <p>
 * Service class that handles business logic related to employees
 * </p>
 * 
 * @version 1.0
 * @author Gobi
 */
@Service
public class EmployeeService {

  private static final Logger logger = LogManager.getLogger(EmployeeService.class);

  @Autowired
  private EmployeeRepository employeeRepository;

  /**
   * <p>
   * Saves the employee to the database
   * </p>
   * 
   * @param employee
   *        employee details to be saved
   * @return Employee
   *         returns the saved employee details
   */
  public Employee saveEmployee(@NonNull Employee employee) {
    logger.debug("Saving employee {}", employee.getName());
    try {
      employee = employeeRepository.save(employee);
    } catch (Exception e) {
      throw new EmployeeException("Error saving employee", e);
    }
    return employee;
  }

  /**
   * <p>
   * Adds new employee
   * </p>
   * 
   * @param employeeDto
   *        employee details given by user to add
   * @return EmployeeDto
   *         returns the added employee details
   */
  public EmployeeDto addEmployee(@NonNull EmployeeDto employeeDto) {
    logger.debug("Adding employee {}", employeeDto.getName());
    Employee employee;
    try {
      employee = saveEmployee(EmployeeMapper.dtoToModel(employeeDto));
    } catch (Exception e) {
      throw new EmployeeException("Error adding employee", e);
    }
    return EmployeeMapper.modelToDto(employee);
  }

  /**
   * <p>
   * Gets employee details by id
   * </p>
   * 
   * @param id
   *        employee id to get details
   * @return Employee
   *         returns the employee details
   */
  public EmployeeDto getEmployee(int id) {
    logger.debug("Getting employee {}", id);
    Employee employee;
    try {
      employee = employeeRepository.findByIdAndIsDeletedFalse(id);
      if (null == employee) {
        throw new NoSuchElementException("Employee with id " + id + " not found");
      }
    } catch (Exception e) {
      throw new EmployeeException("Error getting employee", e);
    }
    return EmployeeMapper.modelToDto(employee);
  }

  /**
   * <p>
   * Gets all employees
   * </p>
   * 
   * @return List<EmployeeDto>
   *         returns the list of employees
   */
  public List<EmployeeDto> getAllEmployees() {
    logger.debug("Getting all employees");
    List<Employee> employees;
    try {
      employees = employeeRepository.findAllByIsDeletedFalse();
    } catch (Exception e) {
      throw new EmployeeException("Error getting all employees", e);
    }
    return employees.stream()
        .map(EmployeeMapper::modelToDto)
        .collect(Collectors.toList());
  }

  /**
   * <p>
   * Updates employee details
   * </p>
   * 
   * @param employeeDto
   *        employee details to be updated
   * @return EmployeeDto
   *         returns the updated employee details
   */
  public EmployeeDto updateEmployee(@NonNull EmployeeDto employeeDto) {
    logger.debug("Updating employee {}", employeeDto.getName());
    if(employeeDto.getId() == 0) {
      throw new IllegalArgumentException("Employee id is required to update");
    }
    Employee employee = EmployeeMapper.dtoToModel(getEmployee(employeeDto.getId()));
    if(employee == null) {
      throw new NoSuchElementException("Employee " + employeeDto.getId() + " not found");
    }
    employee = EmployeeMapper.dtoToModel(employeeDto);
    try {
      employeeDto = EmployeeMapper.modelToDto(saveEmployee(employee));
    } catch (Exception e) {
      throw new EmployeeException("Error updating employee " + employeeDto.getId(), e);
    }
    return employeeDto;
  }

  /**
   * <p>
   * Deletes employee by id
   * </p>
   * 
   * @param id
   *        employee id to delete
   */
  public void deleteEmployee(int id) {
    logger.debug("Deleting employee {}", id);
    try {
      Employee employee = EmployeeMapper.dtoToModel(getEmployee(id));
      employee.setIsDeleted(true);
      saveEmployee(employee);
    } catch (NoSuchElementException e) {
      throw new NoSuchElementException("Employee with id " + id + " not found");
    } catch (Exception e) {
      throw new EmployeeException("Error deleting employee " + id, e);
    }
  }
}
