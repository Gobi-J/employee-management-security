package com.i2i.ems.controller;

import com.i2i.ems.dto.SkillDto;
import com.i2i.ems.model.Skill;
import com.i2i.ems.service.SkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/employees/{employeeId}/skills")
public class SkillController {

  private final SkillService skillService;

  public SkillController(SkillService skillService) {
    this.skillService = skillService;
  }

  /**
   * <p>
   * Retrieves all the skills.
   * </p>
   *
   * @return {@link List<SkillDto>}
   *           returns list of skills.
   */
  @GetMapping("/all")
  public ResponseEntity<List<SkillDto>> getAllSkills() {
    return new ResponseEntity<>(skillService.getAllSkills(), HttpStatus.OK);
  }

  /**
   * <p>
   * Retrieves the skill by id.
   * </p>
   *
   * @param id
   *        skill id.
   * @return {@link Skill}
   *         returns skill.
   */
  @GetMapping("{id}")
  public ResponseEntity<Skill> getSkillById(@PathVariable int id) {
    return new ResponseEntity<>(skillService.getSkillById(id), HttpStatus.OK);
  }

  /**
   * <p>
   * Retrieves the skills by employee id.
   * </p>
   *
   * @param employeeId
   *        employee id.
   * @return {@link List<SkillDto>}
   *         returns list of skills.
   */
  @GetMapping
  public ResponseEntity<List<SkillDto>> getSkillsByEmployeeId(@PathVariable int employeeId) {
    return new ResponseEntity<>(skillService.getEmployeeSkills(employeeId), HttpStatus.OK);
  }

  /**
   * <p>
   * Adds the skill.
   * </p>
   *
   * @param skillDto
   *        skill details.
   * @param employeeId
   *        employee id.
   * @return {@link SkillDto}
   *         returns skill details.
   */
  @PostMapping
  public ResponseEntity<SkillDto> addSkill(@RequestBody SkillDto skillDto, @PathVariable int employeeId) {
    return new ResponseEntity<>(skillService.addSkill(skillDto, employeeId), HttpStatus.CREATED);
  }

  /**
   * <p>
   * Updates the skill.
   * </p>
   *
   * @param skillDto
   *        skill details.
   * @param employeeId
   *        employee id.
   * @return {@link SkillDto}
   *         returns skill details.
   */
  @PutMapping
  public ResponseEntity<SkillDto> updateSkill(@RequestBody SkillDto skillDto, @PathVariable int employeeId) {
    return new ResponseEntity<>(skillService.updateSkill(skillDto), HttpStatus.OK);
  }

  /**
   * <p>
   * Deletes the skill.
   * </p>
   *
   * @param employeeId
   *        employee id.
   * @return {@link HttpStatus}
   *         returns status.
   */
  @DeleteMapping
  public ResponseEntity<HttpStatus> deleteSkill(@PathVariable int employeeId) {
    skillService.deleteSkill(employeeId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * <p>
   * Deletes the skill.
   * </p>
   *
   * @param id
   *        skill id.
   * @param employeeId
   *        employee id.
   * @return {@link HttpStatus}
   *         returns status.
   */
  @DeleteMapping("{id}")
  public ResponseEntity<HttpStatus> deleteSkill(@PathVariable int id, @PathVariable int employeeId) {
    skillService.deleteSkill(id, employeeId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}