package com.i2i.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
  private int id;
  private String bankName;
  private String accountNumber;
  private String ifscCode;
}