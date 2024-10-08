package com.i2i.ems.controller;

import com.i2i.ems.dto.AccountDto;
import com.i2i.ems.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * Controller that handles the account related operations.
 * </p>
 */
@RestController
@RequestMapping("v1/employees/{employeeId}/accounts")
public class AccountController {

  private static final Logger logger = LogManager.getLogger(AccountController.class);

  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  /**
   * <p>
   * Retrieves the account details of the employee
   * </p>
   *
   * @param employeeId
   *        employee id whose account details are to be retrieved
   * @return {@link AccountDto}
   *         account details of the employee
   */
  @GetMapping
  public ResponseEntity<AccountDto> getAccount(@PathVariable int employeeId) {
    return new ResponseEntity<>(accountService.getEmployeeAccount(employeeId), HttpStatus.OK);
  }

  /**
   * <p>
   * Adds new account to the employee
   * </p>
   *
   * @param employeeId
   *        employee id to whom account is to be added
   * @param accountDto
   *        account details to be added
   * @return {@link AccountDto}
   *         added account details with http status code 201
   */
  @PostMapping
  public ResponseEntity<AccountDto> createAccount(@PathVariable int employeeId, @Validated @RequestBody AccountDto accountDto) {
    return new ResponseEntity<>(accountService.addAccount(employeeId, accountDto), HttpStatus.CREATED);
  }

  /**
   * <p>
   * Updates the account details of the employee
   * </p>
   *
   * @param employeeId
   *        employee id whose account details are to be updated
   * @param accountDto
   *        account details to be updated
   * @return {@link AccountDto}
   *         updated account details with http status code 200
   */
  @PutMapping
  public ResponseEntity<AccountDto> updateAccount(@PathVariable int employeeId, @Validated @RequestBody AccountDto accountDto) {
    return new ResponseEntity<>(accountService.updateAccount(employeeId, accountDto), HttpStatus.OK);
  }

  /**
   * <p>
   * Deletes the account details of the employee
   * </p>
   *
   * @param employeeId
   *        employee id whose account details are to be deleted
   * @return {@link HttpStatus}
   *         http status code 204
   */
  @DeleteMapping
  public ResponseEntity<HttpStatus> deleteAccount(@PathVariable int employeeId) {
    accountService.removeAccount(employeeId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}