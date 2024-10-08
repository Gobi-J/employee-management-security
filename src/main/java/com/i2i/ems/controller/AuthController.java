package com.i2i.ems.controller;

import com.i2i.ems.dto.EmployeeDto;
import com.i2i.ems.service.EmployeeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *   Controller that handles the authentication related operations.
 * </p>
 */
@RestController
@RequestMapping("v1/auth")
public class AuthController {

  private final EmployeeService employeeService;

  public AuthController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  /**
   * <p>
   *   Register a new employee with their email and password.
   * </p>
   *
   * @param employeeDto
   *        EmployeeDto object with email and password.
   * @return {@link EmployeeDto}
   *       EmployeeDto object with email and id of an employee.
   */
  @PostMapping("/register")
  public ResponseEntity<EmployeeDto> registerUser(@RequestBody EmployeeDto employeeDto) {
    return new ResponseEntity<>(employeeService.registerEmployee(employeeDto), HttpStatus.CREATED);
  }

  /**
   * <p>
   *   Login an employee with their email and password.
   * </p>
   *
   * @param employeeDto
   *        EmployeeDto object with email and password.
   * @return {@link EmployeeDto}
   *       EmployeeDto object with their all details and a token in header.
   */
  @PostMapping("/login")
  public ResponseEntity<HttpStatus> loginUser(@RequestBody EmployeeDto employeeDto) {
    String token = employeeService.loginEmployee(employeeDto);
    return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
  }
}