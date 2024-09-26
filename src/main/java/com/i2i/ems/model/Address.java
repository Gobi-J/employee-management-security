package com.i2i.ems.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Builder
@Setter
@Getter
@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "door_no")
  private String doorNumber;
  private String street;
  private String city;
  private String state;
  private String type;

  @Column(name = "is_deleted")
  private Boolean isDeleted;
}
