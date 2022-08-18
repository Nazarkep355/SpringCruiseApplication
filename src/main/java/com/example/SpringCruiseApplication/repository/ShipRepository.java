package com.example.SpringCruiseApplication.repository;

import com.example.SpringCruiseApplication.entity.Ship;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShipRepository extends CrudRepository<Ship,Long> {
    Boolean existsShipByName(String name);
    List<Ship> findAll(Pageable pageable);
    @Query("select count(s) from Ship s where s.id >0")
    Integer countAll();

    List<Ship> findAllByEnable(boolean enable, Pageable pageable);
}
