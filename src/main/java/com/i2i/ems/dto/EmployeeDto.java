package com.i2i.ems.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
  private int id;
  private String name;
  private Date dob;
  private String email;
  private long mobileNo;
  private int age;

  private AccountDto account;
  private RoleDto role;
  private List<SkillDto> skills;
}
