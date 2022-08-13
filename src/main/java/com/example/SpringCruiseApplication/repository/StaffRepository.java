package com.example.SpringCruiseApplication.repository;

import com.example.SpringCruiseApplication.entity.Staff;
import org.springframework.data.repository.CrudRepository;

public interface StaffRepository extends CrudRepository<Staff,Long> {
}
