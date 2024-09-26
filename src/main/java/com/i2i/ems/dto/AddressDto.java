package com.i2i.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
  private int id;
  private String doorNumber;
  private String street;
  private String city;
  private String state;
  private String type;
}
