package com.i2i.ems.controller;

import java.security.Principal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i2i.ems.dto.EmployeeDto;
import com.i2i.ems.service.EmployeeService;

/**
 * <p>
 * Controller that handles HTTP requests related to employees
 * </p>
 *
 * @author Gobi
 * @version 1.0
 */
@RestController
@RequestMapping("v1/employees")
public class EmployeeController {

  private static final Logger logger = LogManager.getLogger(EmployeeController.class);

  private final EmployeeService employeeService;

  private EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  /**
   * <p>
   * Adding new employee
   * </p>
   *
   * @param employee employee details given by user to add
   * @return {@link EmployeeDto}
   *         added employee details with http status code 201
   */
  @PostMapping
  public ResponseEntity<EmployeeDto> addEmployee(@Validated @RequestBody EmployeeDto employee, Principal principal) {
    logger.debug("Adding employee {}", employee.getName());
    return new ResponseEntity<>(employeeService.addEmployee(employee, principal.getName()), HttpStatus.CREATED);
  }

  /**
   * <p>
   * Getting employee details by id
   * </p>
   *
   * @param id employee id to get details
   * @return {@link EmployeeDto}
   *         employee details with http status code 200
   */
  @GetMapping("{id}")
  public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Integer id, Principal principal) {
    logger.debug("Getting employee {}", id);
    return new ResponseEntity<>(employeeService.getEmployee(id, principal.getName()), HttpStatus.OK);
  }

  /**
   * <p>
   * Getting all employees
   * </p>
   *
   * @return {@link List<EmployeeDto>}
   *         list of employees
   */
  @GetMapping
  public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
    logger.debug("Getting all employees");
    return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
  }

  /**
   * <p>
   * Updating employee details
   * </p>
   *
   * @param employeeDto employee details given by user to update
   * @return {@link EmployeeDto}
   *         updated employee details with http status code 200
   */
  @PutMapping
  public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto) {
    logger.debug("Updating employee {}", employeeDto.getId());
    return new ResponseEntity<>(employeeService.updateEmployee(employeeDto), HttpStatus.OK);
  }

  /**
   * <p>
   * Deleting employee details by id
   * </p>
   *
   * @param id employee id to delete details
   * @return {@link HttpStatus}
   *         http status code 204 if deleted successfully
   */
  @DeleteMapping("{id}")
  public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Integer id) {
    logger.debug("Deleting employee {}", id);
    employeeService.deleteEmployee(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
