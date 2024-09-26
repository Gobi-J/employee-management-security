package com.i2i.ems.dto;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
  private int id;
  private String designation;
  private String level;
  private String department;
}
