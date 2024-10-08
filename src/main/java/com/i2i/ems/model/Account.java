package com.i2i.ems.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * <p>
 * Model class that maps the account details.
 * </p>
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String bankName;
  private String accountNumber;
  private String ifscCode;
  private Boolean isDeleted;
}