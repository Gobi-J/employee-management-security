package com.i2i.ems.mapper;

import com.i2i.ems.dto.AddressDto;
import com.i2i.ems.model.Address;

public class AddressMapper {
  public static AddressDto modelToDto(Address address) {
    return AddressDto.builder()
        .id(address.getId())
        .doorNumber(address.getDoorNumber())
        .street(address.getStreet())
        .city(address.getCity())
        .state(address.getState())
        .type(address.getType())
        .build();
  }

  public static Address dtoToModel(AddressDto addressDto) {
    return Address.builder()
        .id(addressDto.getId())
        .doorNumber(addressDto.getDoorNumber())
        .street(addressDto.getStreet())
        .city(addressDto.getCity())
        .state(addressDto.getState())
        .type(addressDto.getType())
        .build();
  }
}
