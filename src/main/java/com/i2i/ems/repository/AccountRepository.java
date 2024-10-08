package com.i2i.ems.repository;

import com.i2i.ems.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Provides methods to access account details from the database.
 * </p>
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
  Account findByIdAndIsDeletedFalse(int employeeId);
}