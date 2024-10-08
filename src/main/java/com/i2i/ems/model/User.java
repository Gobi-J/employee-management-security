package com.i2i.ems.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User{
  @Id
  private String userName;
  private String password;
  private Type userType;
}
