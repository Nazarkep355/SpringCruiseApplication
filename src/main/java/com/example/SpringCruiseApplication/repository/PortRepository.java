package com.example.SpringCruiseApplication.repository;

import com.example.SpringCruiseApplication.entity.Port;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PortRepository extends CrudRepository<Port, Long> {
    @Query("select p from Port p")
    List<Port> findAll(Pageable pageable);


    @Query("select count(p) from Port p where p.id > 0")
    Integer countAll();

    Optional<Port> findPortByCity(String city);

}
