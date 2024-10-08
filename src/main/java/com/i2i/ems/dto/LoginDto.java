package com.i2i.ems.dto;

import com.i2i.ems.model.Type;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
  private String userName;
  private String password;
  private Type userType;
}
