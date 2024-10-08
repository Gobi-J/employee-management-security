package com.i2i.ems.repository;

import com.i2i.ems.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>
 * Provides methods to access skill details from the database.
 * </p>
 */
public interface SkillRepository extends JpaRepository<Skill, Integer> {
  Skill findByIdAndIsDeletedFalse(int id);

  Skill findByName(String name);

  boolean existsByName(String name);
}
