package com.i2i.ems.controller;

import com.i2i.ems.dto.AccountDto;
import com.i2i.ems.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ems/api/v1/employees/{employeeId}/accounts")
public class AccountController {
  private static final Logger logger = LogManager.getLogger(AccountController.class);

  @Autowired
  private AccountService accountService;

  @GetMapping
  public ResponseEntity<AccountDto> getAccount(@PathVariable Integer employeeId) {
    return new ResponseEntity<>(accountService.getEmployeeAccount(employeeId), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<AccountDto> createAccount(@PathVariable Integer employeeId, @RequestBody AccountDto accountDto) {
    return new ResponseEntity<>(accountService.addAccount(employeeId, accountDto), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<AccountDto> updateAccount(@PathVariable Integer employeeId, @RequestBody AccountDto accountDto) {
    return new ResponseEntity<>(accountService.updateAccount(employeeId, accountDto), HttpStatus.OK);
  }

  @DeleteMapping
  public ResponseEntity<HttpStatus> deleteAccount(@PathVariable Integer employeeId) {
    accountService.removeAccount(employeeId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}