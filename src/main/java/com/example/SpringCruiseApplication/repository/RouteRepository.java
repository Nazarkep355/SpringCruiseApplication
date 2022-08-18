package com.example.SpringCruiseApplication.repository;

import com.example.SpringCruiseApplication.entity.Route;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RouteRepository extends CrudRepository<Route,Long> {
    List<Route> findAll(Pageable pageable);

    @Query("select count(r) from Route r where r.id > 0")
    Integer countAll();
}
