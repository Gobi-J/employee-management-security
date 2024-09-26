package com.i2i.ems.mapper;

import com.i2i.ems.dto.AccountDto;
import com.i2i.ems.dto.EmployeeDto;
import com.i2i.ems.dto.RoleDto;
import com.i2i.ems.model.Employee;
import com.i2i.ems.util.DateUtil;

import java.util.Date;
import java.util.stream.Collectors;

public class EmployeeMapper {
  public static EmployeeDto modelToDto(Employee employee) {
    return EmployeeDto.builder()
        .id(employee.getId())
        .name(employee.getName())
        .email(employee.getEmail())
        .mobileNo(employee.getMobileNo())
        .age(DateUtil.getYearsBetween(employee.getDob(), new Date()))
        .role(RoleDto.builder()
            .id(employee.getRole().getId())
            .designation(employee.getRole().getDesignation())
            .build())
        .account(AccountDto.builder()
            .id(employee.getAccount().getId())
            .accountNumber(employee.getAccount().getAccountNumber())
            .build())
        .skills(employee.getSkills().stream().
            map(SkillMapper::modelToDto)
            .collect(Collectors.toList()))
        .build();
  }

  public static Employee dtoToModel(EmployeeDto employeeDto) {
    return Employee.builder()
        .name(employeeDto.getName())
        .dob(employeeDto.getDob())
        .mobileNo(employeeDto.getMobileNo())
        .email(employeeDto.getEmail())
        .isDeleted(false)
        .build();
  }
}
