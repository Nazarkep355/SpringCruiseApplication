package com.example.SpringCruiseApplication.repository;

import com.example.SpringCruiseApplication.entity.Ship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShipRepository extends CrudRepository<Ship,Long> {
    Boolean existsShipByName(String name);
    List<Ship> findAll(Pageable pageable);
}
