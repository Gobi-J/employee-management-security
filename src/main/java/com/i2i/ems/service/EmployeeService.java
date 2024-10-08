package com.i2i.ems.service;

import java.util.*;
import java.util.stream.Collectors;

import com.i2i.ems.dto.EmployeeDto;
import com.i2i.ems.helper.EmployeeException;
import com.i2i.ems.helper.UnAuthorizedException;
import com.i2i.ems.mapper.EmployeeMapper;
import com.i2i.ems.model.Account;
import com.i2i.ems.util.JwtTokenUtil;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.i2i.ems.model.Employee;
import com.i2i.ems.repository.EmployeeRepository;

import io.jsonwebtoken.Jwts;

/**
 * <p>
 * Service class that handles business logic related to employees
 * </p>
 *
 * @author Gobi
 * @version 1.0
 */
@Service
public class EmployeeService {

  private static final Logger logger = LogManager.getLogger(EmployeeService.class);

  private final EmployeeRepository employeeRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final JwtTokenUtil jwtTokenUtil;
  private final AuthenticationManager authenticationManager;

  public EmployeeService(EmployeeRepository employeeRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager) {
    this.employeeRepository = employeeRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenUtil = jwtTokenUtil;
    this.authenticationManager = authenticationManager;
  }

  /**
   * <p>
   * Saves the employee to the database
   * </p>
   *
   * @param employee employee details to be saved
   * @return {@link Employee}
   *         saved employee details
   */
  public Employee saveEmployee(@NonNull Employee employee) {
    logger.debug("Saving employee {}", employee.getName());
    try {
      employee = employeeRepository.save(employee);
      logger.info("Employee {} saved successfully", employee.getId());
    } catch (Exception e) {
      logger.error("Cannot save employee {}", employee.getName(), e);
      throw new EmployeeException("Cannot save employee with name " + employee.getName(), e);
    }
    return employee;
  }

  /**
   * <p>
   * Adds new employee
   * </p>
   *
   * @param employeeDto employee details given by user to add
   * @param email       email of the employee who is adding the details
   * @return {@link EmployeeDto}
   *         added employee details
   */
  public EmployeeDto addEmployee(@NonNull EmployeeDto employeeDto, String email) {
    logger.debug("Adding employee {}", employeeDto.getName());
    Employee employee;
    try {
      employee = EmployeeMapper.dtoToModel(employeeDto);
      Employee existingEmployee = employeeRepository.findByEmail(employeeDto.getEmail());
      if (null == existingEmployee) {
        throw new NoSuchElementException("User not found with given email. Try registering");
      }
      if (!email.equals(existingEmployee.getEmail())) {
        throw new IllegalArgumentException("You are not authorized to add details of other employee");
      }
      employee.setPassword(existingEmployee.getPassword());
      employee.setId(existingEmployee.getId());
      saveEmployee(employee);
      logger.info("Employee {} added successfully", employee.getId());
    } catch (DuplicateKeyException e) {
      logger.error("Employee {} already exists", employeeDto.getName());
      throw new DuplicateKeyException(e.getMessage());
    } catch (Exception e) {
      logger.error("Cannot add employee {}", employeeDto.getName(), e);
      throw new EmployeeException("Cannot save employee with name " + employeeDto.getName(), e);
    }
    return EmployeeMapper.modelToDto(employee);
  }

  /**
   * <p>
   * Gets employee details by id
   * </p>
   *
   * @param id employee id to get details
   * @return {@link Employee}
   *         employee details
   */
  public EmployeeDto getEmployee(int id, String email) {
    logger.debug("Getting employee {}", id);
    Employee employee = getEmployeeById(id);
    if (!email.equals(employee.getEmail())) {
      throw new IllegalArgumentException("You are not authorized to view this employee");
    }
    logger.info("Returning employee {}", employee.getId());
    return EmployeeMapper.modelToDto(employee);
  }

  /**
   * <p>
   * Gets all employees
   * </p>
   *
   * @return {@link List<EmployeeDto>}
   *         details of all employees
   */
  public List<EmployeeDto> getAllEmployees() {
    logger.debug("Getting all employees");
    List<Employee> employees;
    try {
      employees = employeeRepository.findAllByIsDeletedFalse();
      logger.info("Returning employees list");
    } catch (Exception e) {
      logger.error("Cannot get all employees", e);
      throw new EmployeeException("Cannot getting all employees", e);
    }
    return employees.stream()
        .map(EmployeeMapper::modelsToDtos)
        .collect(Collectors.toList());
  }

  /**
   * <p>
   * Updates employee details
   * </p>
   *
   * @param employeeDto employee details to be updated
   * @return {@link EmployeeDto}
   *         updated employee details
   */
  public EmployeeDto updateEmployee(@NonNull EmployeeDto employeeDto) {
    logger.debug("Updating employee {}", employeeDto.getName());
    if (employeeDto.getId() == 0) {
      throw new IllegalArgumentException("Employee id is required to update");
    }
    try {
      employeeDto = EmployeeMapper.modelToDto(saveEmployee(EmployeeMapper.dtoToModel(employeeDto)));
      logger.info("Employee {} updated successfully", employeeDto.getId());
    } catch (Exception e) {
      logger.error("Cannot update employee {}", employeeDto.getName(), e);
      throw new EmployeeException("Cannot updating employee " + employeeDto.getId(), e);
    }
    return employeeDto;
  }

  /**
   * <p>
   * Deletes employee by id
   * </p>
   *
   * @param id employee id to delete
   */
  public void deleteEmployee(int id) {
    logger.debug("Deleting employee {}", id);
    try {
      Employee employee = getEmployeeById(id);
      employee.setIsDeleted(true);
      Account account = employee.getAccount();
      if (null != account) {
        account.setIsDeleted(true);
      }
      saveEmployee(employee);
      logger.info("Employee {} deleted successfully", id);
    } catch (NoSuchElementException e) {
      logger.error("Employee {} not found", id);
      throw new NoSuchElementException(e.getMessage());
    } catch (Exception e) {
      logger.error("Cannot delete employee {}", id, e);
      throw new EmployeeException("Cannot delete employee " + id, e);
    }
  }

  /**
   * <p>
   * Gets employee by id
   * </p>
   *
   * @param id employee id to get details
   * @return {@link Employee}
   *         returns the employee details
   */
  protected Employee getEmployeeById(int id) {
    logger.debug("Getting employee {}", id);
    Employee employee;
    try {
      employee = employeeRepository.findByIdAndIsDeletedFalse(id);
      if (null == employee) {
        throw new NoSuchElementException("Employee with id " + id + " not found");
      }
      logger.info("Returning employee {}", employee.getId());
    } catch (NoSuchElementException e) {
      logger.error("Employee {} not found", id);
      throw new NoSuchElementException(e.getMessage());
    } catch (Exception e) {
      logger.error("Cannot get employee {}", id, e);
      throw new EmployeeException("Cannot get employee " + id, e);
    }
    return employee;
  }

  /**
   * <p>
   * Registers a new employee
   * </p>
   *
   * @param employeeDto employee details to be registered
   * @return {@link EmployeeDto}
   *         registered employee details
   */
  public EmployeeDto registerEmployee(EmployeeDto employeeDto) {
    logger.debug("Registering employee {}", employeeDto.getEmail());
    Employee employee;
    try {
      employee = EmployeeMapper.dtoToModel(employeeDto);
      if (employeeRepository.existsByEmailAndIsDeletedFalse(employeeDto.getEmail())) {
        throw new DuplicateKeyException("Employee with the same email already exists");
      }
      employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
      saveEmployee(employee);
      logger.info("Employee {} registered successfully", employee.getId());
    } catch (DuplicateKeyException e) {
      logger.error("Employee {} already exists", employeeDto.getEmail());
      throw new DuplicateKeyException(e.getMessage());
    } catch (Exception e) {
      logger.error("Cannot register employee {}", employeeDto.getEmail(), e);
      throw new EmployeeException("Cannot register employee with email " + employeeDto.getEmail(), e);
    }
    return EmployeeMapper.modelToDto(employee);
  }

  /**
   * <p>
   * Logs in an employee
   * </p>
   *
   * @param employeeDto employee details to login
   * @return {@link String}
   *         jwt token
   */
  public String loginEmployee(EmployeeDto employeeDto) {
    try {
      authenticationManager
          .authenticate(
              new UsernamePasswordAuthenticationToken(
                  employeeDto.getEmail(), employeeDto.getPassword()
              )
          );
      return jwtTokenUtil.generateAccessToken(employeeDto.getEmail());
    } catch (BadCredentialsException e) {
      throw new UnAuthorizedException("Invalid email or password");
    }
  }
}
