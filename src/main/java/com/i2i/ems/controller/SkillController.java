package com.i2i.ems.controller;

import com.i2i.ems.dto.SkillDto;
import com.i2i.ems.model.Skill;
import com.i2i.ems.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("ems/api/v1/skills")
public class SkillController {

  @Autowired
  private SkillService skillService;

  @GetMapping
  public ResponseEntity<List<SkillDto>> getAllSkills() {
    return new ResponseEntity<>(skillService.getAllSkills(), HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<Skill> getSkillById(@PathVariable int id) {
    return new ResponseEntity<>(skillService.getSkillById(id), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<SkillDto> addSkill(@RequestBody SkillDto skillDto) {
    return new ResponseEntity<>(skillService.addSkill(skillDto), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<SkillDto> updateSkill(@RequestBody SkillDto skillDto) {
    return new ResponseEntity<>(skillService.updateSkill(skillDto), HttpStatus.OK);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<HttpStatus> deleteSkill(@PathVariable int id) {
    skillService.deleteSkill(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
