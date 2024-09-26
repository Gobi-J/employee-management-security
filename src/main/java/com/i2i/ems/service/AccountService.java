package com.i2i.ems.service;

import com.i2i.ems.dto.AccountDto;
import com.i2i.ems.helper.EmployeeException;
import com.i2i.ems.mapper.AccountMapper;
import com.i2i.ems.model.Account;
import com.i2i.ems.model.Employee;
import com.i2i.ems.repository.AccountRepository;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AccountService {
  private static final Logger logger = LogManager.getLogger(AccountService.class);

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  EmployeeService employeeService;

  public Account saveAccount(@NonNull Account account) {
    logger.debug("Saving account {}", account.getBankName());
    try {
      account = accountRepository.save(account);
    } catch (Exception e) {
      throw new EmployeeException("Error saving account", e);
    }
    return account;
  }

  public AccountDto addAccount(Integer employeeId, @NonNull AccountDto accountDto) {
    logger.debug("Adding account {}", accountDto.getBankName());
    Account account;
    try {
      if(null != getEmployeeAccount(employeeId)) {
        throw new DuplicateKeyException("Account already exists for employee " + employeeId);
      }
      account = AccountMapper.dtoToModel(accountDto);
      account.setEmployee(employeeService.getEmployee(employeeId));
      account = saveAccount(account);
    } catch (Exception e) {
      throw new EmployeeException("Error adding account", e);
    }
    return AccountMapper.modelToDto(account);
  }

  public AccountDto getEmployeeAccount(int employeeId) {
    logger.debug("Getting account of employee {}", employeeId);
    Account account;
    try {
      account = accountRepository.findByEmployeeIdAndIsDeletedFalse(employeeId);
      if (null == account) {
        throw new NoSuchElementException("Account for employee " + employeeId + " not found");
      }
    } catch (Exception e) {
      throw new EmployeeException("Error getting account", e);
    }
    return AccountMapper.modelToDto(account);
  }

  public List<AccountDto> getAllAccounts() {
    logger.debug("Getting all accounts");
    List<Account> accounts;
    return accountRepository.findAll().stream().map(AccountMapper::modelToDto).toList();
  }

  /**
   * <p>
   * Binds an account and an employee.
   * </p>
   *
   * @param account  account to bind
   * @param employee employee to bind the account to
   */
  public void bindEmployeeAccount(@NonNull Account account, Employee employee) {
    logger.debug("Binding account to an employee");
    account.setEmployee(employee);
  }

  public AccountDto updateAccount(int employeeId, AccountDto accountDto) {
    logger.debug("Updating account {}", employeeId);
    Account account;
    account = accountRepository.findByEmployeeIdAndIsDeletedFalse(employeeId);
    if (null == account) {
      throw new NoSuchElementException("Account for employee " + employeeId + " not found");
    }
    removeAccount(employeeId);
    return addAccount(employeeId, accountDto);
  }

  /**
   * <p>
   * Remove account
   * </p>
   *
   * @param employeeId account to remove
   */
  public void removeAccount(int employeeId) {
    logger.debug("Removing account");
    Account account = accountRepository.findByEmployeeIdAndIsDeletedFalse(employeeId);
    if (null == account) {
      throw new NoSuchElementException("Account for employee " + employeeId + " not found");
    }
    account.setIsDeleted(true);
    saveAccount(account);
  }
}