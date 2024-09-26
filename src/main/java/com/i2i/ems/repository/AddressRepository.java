package com.i2i.ems.repository;

import com.i2i.ems.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
  List<Address> findAllByEmployeeIdAndIsDeletedFalse(int employeeId);

  Address findByIdAndIsDeletedFalse(int employeeId);

  Address findByIdAndEmployeeIdAndIsDeletedFalse(int addressId, int employeeId);
}
