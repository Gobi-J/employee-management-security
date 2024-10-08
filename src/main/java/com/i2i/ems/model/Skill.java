package com.i2i.ems.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * <p>
 * Model class that maps the skill details.
 * </p>
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "skill")
public class Skill {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  private String category;
  private String institute;
  private Boolean isDeleted;
}