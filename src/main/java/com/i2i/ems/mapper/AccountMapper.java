package com.i2i.ems.mapper;

import com.i2i.ems.dto.AccountDto;
import com.i2i.ems.model.Account;

public class AccountMapper {

  public static AccountDto modelToDto(Account account) {
    return AccountDto.builder()
        .id(account.getId())
        .accountNumber(account.getAccountNumber())
        .build();

  }
  public static Account dtoToModel(AccountDto accountDto) {
    return Account.builder()
        .accountNumber(accountDto.getAccountNumber())
        .bankName(accountDto.getBankName())
        .ifscCode(accountDto.getIfscCode())
        .isDeleted(false)
        .build();
  }
}
