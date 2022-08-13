package com.example.SpringCruiseApplication.repository;

import com.example.SpringCruiseApplication.entity.Cruise;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CruiseRepository extends CrudRepository<Cruise,Long> {

//    Iterable<Cruise> findAllPageable(Pageable pageable);
}
