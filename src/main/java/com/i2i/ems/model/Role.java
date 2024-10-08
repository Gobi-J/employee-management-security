package com.i2i.ems.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * <p>
 * Model class that maps the role details.
 * </p>
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String designation;
  private String level;
  private String department;
  private Boolean isDeleted;
}
