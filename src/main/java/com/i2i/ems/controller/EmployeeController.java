package com.i2i.ems.controller;

import java.util.List;
import java.util.NoSuchElementException;

import com.i2i.ems.dto.EmployeeDto;
import com.i2i.ems.helper.EmployeeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i2i.ems.model.Employee;
import com.i2i.ems.service.EmployeeService;

/**
 * <p>
 * Controller that handles HTTP requests related to employees
 * </p>
 * 
 * @version 1.0
 * @author Gobi 
 */
@RestController
@RequestMapping("/ems/api/v1/employees")
public class EmployeeController {

  private static final Logger logger = LogManager.getLogger(EmployeeController.class);

  @Autowired
  private EmployeeService employeeService;

  /**
   * <p>
   * Adding new employee
   * </p>
   * 
   * @param employeeDto 
   *        employee details given by user to add
   * @return ResponseEntity<EmployeeDto>
   *         returns the added employee details with http status code 201
   */
  @PostMapping
  public ResponseEntity<EmployeeDto> addEmployee(@RequestBody EmployeeDto employeeDto) {
    logger.debug("Adding employee {}", employeeDto.getName());
    EmployeeDto savedEmployeeDto;
    try {
      savedEmployeeDto = employeeService.addEmployee(employeeDto);
      logger.info("Employee {} added successfully ", savedEmployeeDto.getId());
    } catch (EmployeeException e) {
      logger.error("Error adding employee with name {}", employeeDto.getName(), e);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(savedEmployeeDto, HttpStatus.CREATED);
  }

  /**
   * <p>
   * Getting employee details by id
   * </p>
   * 
   * @param id 
   *        employee id to get details
   * @return ResponseEntity<Employee>
   *         returns the employee details with http status code 200
   */
  @GetMapping("{id}")
  public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Integer id) {
    logger.debug("Getting employee {}", id);
    EmployeeDto employee;
    try {
      employee = employeeService.getEmployee(id);
    } catch (NoSuchElementException e) {
      logger.error("Employee {} not found", id);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (EmployeeException e) {
      logger.error("Error retrieving employee with id {}", id, e);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(employee, HttpStatus.OK);
  }

  /**
   * <p>
   * Getting all employees
   * </p>
   * 
   * @return ResponseEntity<List<EmployeeDto>>
   *         returns the list of employees with http status code 200
   */
  @GetMapping
  public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
    logger.debug("Getting all employees");
    List<EmployeeDto> employees;
    try {
      employees = employeeService.getAllEmployees();
      logger.info("Retrieved all employees");
    } catch (EmployeeException e) {
      logger.error("Error retrieving all employees", e);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(employees, HttpStatus.OK);
  }
  
  /**
   * <p>
   * Updating employee details
   * </p>
   * 
   * @param employeeDto 
   *        employee details given by user to update
   * @return ResponseEntity<EmployeeDto>
   *         returns the updated employee details with http status code 200
   */
  @PutMapping
  public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto) {
    return new ResponseEntity<>(employeeService.updateEmployee(employeeDto), HttpStatus.OK);
  }

  /**
   * <p>
   * Updating employee details partially
   * </p>
   * 
   * @param employeeDto 
   *        employee details given by user to update
   * @return ResponseEntity<EmployeeDto>
   *         returns the updated employee details with http status code 200
   */
  @PatchMapping
  public ResponseEntity<EmployeeDto> patchEmployee(@RequestBody EmployeeDto employeeDto) {
    return new ResponseEntity<>(employeeService.updateEmployee(employeeDto), HttpStatus.OK);
  }

  /**
   * <p>
   * Deleting employee details by id
   * </p>
   * 
   * @param id 
   *        employee id to delete details
   * @return ResponseEntity<HttpStatus.Series>
   *         returns the http status code 204 if deleted successfully
   */
  @DeleteMapping("{id}")
  public ResponseEntity<HttpStatus.Series> deleteEmployee(@PathVariable Integer id) {
    try {
      employeeService.deleteEmployee(id);
      logger.info("Employee {} deleted successfully ", id);
    } catch (NoSuchElementException e) {
      logger.error("Employee {} not found", id);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (EmployeeException e) {
      logger.error("Error deleting employee with id {}", id, e);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
