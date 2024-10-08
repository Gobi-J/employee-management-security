package com.i2i.ems.repository;

import com.i2i.ems.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

  Role findByIdAndIsDeletedFalse(int id);

  Role findByDesignationAndDepartment(String designation, String department);
}
