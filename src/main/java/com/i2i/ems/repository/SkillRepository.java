package com.i2i.ems.repository;

import com.i2i.ems.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
  Skill findByIdAndIsDeletedFalse(int id);

  Skill findByName(String name);

  boolean existsByName(String name);
}
