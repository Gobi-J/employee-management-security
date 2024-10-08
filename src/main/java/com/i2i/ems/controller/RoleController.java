package com.i2i.ems.controller;

import com.i2i.ems.dto.RoleDto;
import com.i2i.ems.service.RoleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/employees/{employeeId}/roles")
public class RoleController{

  private static final Logger logger = LogManager.getLogger(RoleController.class);

  private final RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  /**
   * <p>
   * Retrieves the role of the employee
   * </p>
   *
   * @param employeeId
   *        employee id
   * @return {@link RoleDto}
   *         role details
   */
  @GetMapping
  public ResponseEntity<RoleDto> getAccount(@PathVariable int employeeId) {
    return new ResponseEntity<>(roleService.getEmployeeRole(employeeId), HttpStatus.OK);
  }

  /**
   * <p>
   * Creates the role of the employee
   * </p>
   *
   * @param employeeId
   *        employee id
   * @param roleDto
   *        role details
   * @return {@link RoleDto}
   *         created role details
   */
  @PostMapping
  public ResponseEntity<RoleDto> createAccount(@PathVariable int employeeId, @Validated @RequestBody RoleDto roleDto) {
    return new ResponseEntity<>(roleService.addRole(roleDto), HttpStatus.CREATED);
  }

  /**
   * <p>
   * Updates the role of the employee
   * </p>
   *
   * @param employeeId
   *        employee id
   * @param roleDto
   *        role details
   * @return {@link RoleDto}
   *         updated role details
   */
  @PutMapping
  public ResponseEntity<RoleDto> updateAccount(@PathVariable int employeeId, @Validated @RequestBody RoleDto roleDto) {
    return new ResponseEntity<>(roleService.updateRole(employeeId, roleDto), HttpStatus.OK);
  }

  /**
   * <p>
   * Deletes the role of the employee
   * </p>
   *
   * @param employeeId
   *        employee id
   * @return {@link HttpStatus}
   *         status of the operation
   */
  @DeleteMapping
  public ResponseEntity<HttpStatus> deleteAccount(@PathVariable int employeeId) {
    roleService.deleteRole(employeeId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
