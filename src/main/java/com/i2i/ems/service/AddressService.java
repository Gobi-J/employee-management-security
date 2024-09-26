package com.i2i.ems.service;

import com.i2i.ems.dto.AddressDto;
import com.i2i.ems.helper.EmployeeException;
import com.i2i.ems.mapper.AddressMapper;
import com.i2i.ems.model.Address;
import com.i2i.ems.repository.AddressRepository;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AddressService {

  private static final Logger logger = LogManager.getLogger(AddressService.class);

  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private EmployeeService employeeService;

  public Address saveAddress(@NonNull Address address) {
    logger.debug("Saving address {}", address);
    try {
      address = addressRepository.save(address);
    } catch (Exception e) {
      throw new EmployeeException("Error saving account", e);
    }
    return address;
  }

  public AddressDto addAddress(Integer employeeId, @NonNull AddressDto addressDto) {
    logger.debug("Adding address {}", addressDto);
    Address address;
    try {
      address = AddressMapper.dtoToModel(addressDto);
      address.setEmployee(employeeService.getEmployee(employeeId));
      address = saveAddress(address);
    } catch (Exception e) {
      throw new EmployeeException("Error adding account", e);
    }
    return AddressMapper.modelToDto(address);
  }

  public List<AddressDto> getEmployeeAddresses(int employeeId) {
    logger.debug("Getting account {}", employeeId);
    List<Address> addresses;
    try {
      addresses = addressRepository.findAllByEmployeeIdAndIsDeletedFalse(employeeId);
      if (null == addresses) {
        throw new NoSuchElementException("Address for employee " + employeeId + " not found");
      }
    } catch (Exception e) {
      throw new EmployeeException("Error getting address", e);
    }
    return addresses.stream()
        .map(AddressMapper::modelToDto)
        .collect(Collectors.toList());
  }

  public List<AddressDto> getAllAddresses() {
    logger.debug("Getting all addresses");
    return addressRepository.findAll().stream().map(AddressMapper::modelToDto).toList();
  }

//  public List<AddressDto> updateAddress(int employeeId, AccountDto accountDto) {
//    logger.debug("Updating account {}", employeeId);
//    Account account;
//    account = accountRepository.findByEmployeeIdAndIsDeletedFalse(employeeId);
//    if (null == account) {
//      throw new NoSuchElementException("Account for employee " + employeeId + " not found");
//    }
//    removeAccount(employeeId);
//    return addAccount(employeeId, accountDto);
//  }

  /**
   * <p>
   * Remove account
   * </p>
   *
   * @param employeeId account to remove
   */
  public void removeAddress(int employeeId, int addressId) {
    logger.debug("Removing address");
    Address address = addressRepository.findByIdAndEmployeeIdAndIsDeletedFalse(addressId, employeeId);
    if (null == address) {
      throw new NoSuchElementException("Address for employee " + employeeId + " not found");
    }
    address.setIsDeleted(true);
    saveAddress(address);
  }
}
