package com.example.SpringCruiseApplication.repository;

import com.example.SpringCruiseApplication.entity.Staff;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends CrudRepository<Staff,Long> {
    List<Staff> findAll(Pageable pageable);

    @Query("select count(s) from Staff s where s.id > 0")
    Integer countAll();

    Optional<Staff> findByName(String name);

    List<Staff> findAllByEnable(boolean enable,Pageable pageable);

}
