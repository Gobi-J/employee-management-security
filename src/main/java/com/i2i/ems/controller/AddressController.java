package com.i2i.ems.controller;

import com.i2i.ems.dto.AddressDto;
import com.i2i.ems.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ems/api/v1/employees/{employeeId}/addresses")
public class AddressController {

  @Autowired
  private AddressService addressService;

  @GetMapping
  public ResponseEntity<List<AddressDto>> getEmployeeAddresses(@PathVariable int employeeId) {
    return new ResponseEntity<>(addressService.getEmployeeAddresses(employeeId), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<AddressDto> createEmployeeAddress(@PathVariable int employeeId, @RequestBody AddressDto addressDto) {
    return new ResponseEntity<>(addressService.addAddress(employeeId, addressDto), HttpStatus.CREATED);
  }

  @DeleteMapping("{addressId}")
  public ResponseEntity<HttpStatus> deleteEmployeeAddress(@PathVariable int employeeId, @PathVariable int addressId) {
    addressService.removeAddress(employeeId, addressId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
