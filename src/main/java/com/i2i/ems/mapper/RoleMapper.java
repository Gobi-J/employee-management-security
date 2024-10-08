package com.i2i.ems.mapper;

import com.i2i.ems.dto.RoleDto;
import com.i2i.ems.model.Role;

public class RoleMapper {
  public static RoleDto modelToDto(Role role) {
    return RoleDto.builder()
        .id(role.getId())
        .designation(role.getDesignation())
        .build();
  }

  public static Role dtoToModel(RoleDto roleDto) {
    return Role.builder()
        .id(roleDto.getId())
        .designation(roleDto.getDesignation())
        .level(roleDto.getLevel())
        .department(roleDto.getDepartment())
        .isDeleted(false)
        .build();
  }
}
