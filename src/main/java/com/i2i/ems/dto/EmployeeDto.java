package com.i2i.ems.dto;

import java.util.Date;
import java.util.List;

import com.i2i.ems.model.Type;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * Data transfer object that maps the employee details.
 * </p>
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
  private int id;

  @Pattern(regexp = "^[a-zA-Z]+( [a-zA-Z]+)*$", message = "Name should contain only alphabets")
  private String name;

  @Past(message = "Name should not be in future")
  private Date dob;

  @Email(message = "Email should be in proper format (eg. admin@gmail.com)")
  private String email;

  @Min(value = 1000000000L)
  @Max(value = 9999999999L)
  private long mobileNo;
  private int age;
  private Type userType;
  private String password;

  private AccountDto account;
  private RoleDto role;
  private List<SkillDto> skills;
}
