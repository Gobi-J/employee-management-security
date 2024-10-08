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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * <p>
 *   Service class that handles business logic related to accounts
 * </p>
 */
@Service
public class AccountService {

  private static final Logger logger = LogManager.getLogger(AccountService.class);

  private final AccountRepository accountRepository;
  private final EmployeeService employeeService;

  public AccountService(AccountRepository accountRepository, EmployeeService employeeService) {
    this.accountRepository = accountRepository;
    this.employeeService = employeeService;
  }

  /**
   * <p>
   *   Save account
   * </p>
   *
   * @param account account to save
   * @return {@link Account} saved account
   */
  public Account saveAccount(@NonNull Account account) {
    logger.debug("Saving account {}", account.getBankName());
    try {
      account = accountRepository.save(account);
      logger.info("Account saved");
    } catch (Exception e) {
      logger.error(e);
      throw new EmployeeException("Cannot save account", e);
    }
    return account;
  }

  /**
   * <p>
   *   Add account to an employee
   * </p>
   *
   * @param employeeId
   *        employee to add account
   * @param accountDto
   *        account details
   * @return {@link AccountDto}
   *         added account details
   */
  public AccountDto addAccount(int employeeId, @NonNull AccountDto accountDto) {
    logger.debug("Adding account {}", accountDto.getBankName());
    Account account;
    try {
      if (null != employeeService.getEmployeeById(employeeId).getAccount()) {
        throw new DuplicateKeyException("Account already exists for employee " + employeeId);
      }
      account = saveAccount(AccountMapper.dtoToModel(accountDto));
      Employee employee = employeeService.getEmployeeById(employeeId);
      employee.setAccount(account);
      employeeService.saveEmployee(employee);
      logger.info("Account added for employee {}", employeeId);
    } catch (DuplicateKeyException e) {
      logger.warn(e);
      throw new DuplicateKeyException(e.getMessage());
    } catch (Exception e) {
      logger.error(e);
      throw new EmployeeException("Cannot add account", e);
    }
    return AccountMapper.modelToDto(account);
  }

  /**
   * <p>
   *   Get account of an employee
   * </p>
   *
   * @param employeeId
   *        employee whose account needs to be fetched
   * @return {@link AccountDto}
   *         account details
   */
  public AccountDto getEmployeeAccount(int employeeId) {
    logger.debug("Getting account of employee {}", employeeId);
    Account account;
    try {
      account = employeeService.getEmployeeById(employeeId).getAccount();
      if (null == account) {
        throw new NoSuchElementException("Account for employee " + employeeId + " not found");
      }
      logger.info("Account found for employee {}", employeeId);
    } catch (NoSuchElementException e) {
      logger.warn(e);
      throw new NoSuchElementException(e.getMessage());
    } catch (Exception e) {
      logger.error(e);
      throw new EmployeeException("Cannot get account for employee " + employeeId, e);
    }
    return AccountMapper.modelToDto(account);
  }

  /**
   * <p>
   *   Updating account of an employee by replacing with new account
   * </p>
   *
   * @param employeeId
   *        employee whose account needs to be updated
   * @param accountDto
   *        new account details
   * @return {@link AccountDto}
   *         updated account details
   */
  public AccountDto updateAccount(int employeeId, AccountDto accountDto) {
    logger.debug("Updating account {}", employeeId);
    Account account = employeeService.getEmployeeById(employeeId).getAccount();
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
    Account account;
    try {
      Employee employee = employeeService.getEmployeeById(employeeId);
      if(null == employee) {
        throw new NoSuchElementException("Employee with id " + employeeId + " not found");
      }
      account = employee.getAccount();
      if (null == account) {
        throw new NoSuchElementException("Account for employee " + employeeId + " not found");
      }
      account.setIsDeleted(true);
      saveAccount(account);
      employee.setAccount(null);
      employeeService.saveEmployee(employee);
      logger.info("Account removed for employee {}", employeeId);
    } catch (NoSuchElementException e) {
      logger.warn(e);
      throw new NoSuchElementException(e.getMessage());
    } catch (Exception e) {
      logger.error(e);
      throw new EmployeeException("Cannot delete account for employee " + employeeId, e);
    }
  }
}