package com.i2i.ems.mapper;

import com.i2i.ems.dto.SkillDto;
import com.i2i.ems.model.Skill;

public class SkillMapper {
  public static Skill dtoToModel(SkillDto skillDto) {
    return Skill.builder()
        .id(skillDto.getId())
        .name(skillDto.getName())
        .category(skillDto.getCategory())
        .institute(skillDto.getInstitute())
        .isDeleted(false)
        .build();
  }

  public static SkillDto modelToDto(Skill skill) {
    return SkillDto.builder()
        .id(skill.getId())
        .name(skill.getName())
        .build();
  }
}
