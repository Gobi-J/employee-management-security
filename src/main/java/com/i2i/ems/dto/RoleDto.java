package com.i2i.ems.dto;

import lombok.*;

/**
 * <p>
 * Data transfer object that maps the role details.
 * </p>
 */
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
