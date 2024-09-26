package com.i2i.ems.service;

import com.i2i.ems.dto.SkillDto;
import com.i2i.ems.mapper.SkillMapper;
import com.i2i.ems.model.Skill;
import com.i2i.ems.repository.SkillRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class SkillService {

  @Autowired
  private SkillRepository skillRepository;

  public Skill saveSkill(Skill skill) {
    return skillRepository.save(skill);
  }

  public SkillDto addSkill(SkillDto skillDto) {
    Skill skill = SkillMapper.dtoToModel(skillDto);
    return SkillMapper.modelToDto(saveSkill(skill));
  }

  public List<SkillDto> getAllSkills() {
    return skillRepository.findAll().stream()
        .map(SkillMapper::modelToDto)
        .collect(Collectors.toList());
  }

  public Skill getSkillById(int id) {
    return skillRepository.findByIdAndIsDeletedFalse(id);
  }

  public SkillDto updateSkill(@NonNull SkillDto skillDto) {
    Skill skill = getSkillById(skillDto.getId());
    if (skill == null) {
      throw new NoSuchElementException("Skill not found");
    }
    skill = SkillMapper.dtoToModel(skillDto);
    return SkillMapper.modelToDto(saveSkill(skill));
  }

  public void deleteSkill(int id) {
    Skill skill = getSkillById(id);
    if (skill == null) {
      throw new NoSuchElementException("Skill not found");
    }
    skill.setIsDeleted(true);
    skillRepository.save(skill);
  }
}
