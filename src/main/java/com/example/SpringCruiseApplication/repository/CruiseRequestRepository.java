package com.example.SpringCruiseApplication.repository;

import com.example.SpringCruiseApplication.entity.Cruise;
import com.example.SpringCruiseApplication.entity.CruiseRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CruiseRequestRepository extends CrudRepository<CruiseRequest, Long> {

    List<CruiseRequest> findAllByCruise(Cruise cruise, Pageable pageable);

    List<CruiseRequest> findAll(Pageable pageable);

    @Query(value = "select r.id from cruise_requests r, cruise_dates d" +
            " where d.cruise_id = r.cruise and d.dates_order = 0 " +
            "and d.dates > current_timestamp"
            , nativeQuery = true)
    List<Long> findAllActual(Pageable pageable);

    @Query("select count(c) from CruiseRequest c where c.cruise = ?1")
    Integer countAllByCruise(Cruise cruise);

    @Query(value = "select count(r.id) from cruise_requests r, cruise_dates d" +
            " where d.cruise_id = r.cruise and d.dates_order = 0 " +
            "and d.dates > current_timestamp"
            , nativeQuery = true)
    Integer countAllActual();

    @Query("select count(c) from CruiseRequest c")
    Integer countAllBy();


}
