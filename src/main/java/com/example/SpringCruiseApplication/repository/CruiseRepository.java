package com.example.SpringCruiseApplication.repository;

import com.example.SpringCruiseApplication.entity.Cruise;
import com.example.SpringCruiseApplication.entity.Port;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CruiseRepository extends CrudRepository<Cruise, Long> {

    List<Cruise> findAll(Pageable pageable);

    @Query("select count(c) from Cruise c where c.id > 0")
    Integer countAll();

    @Query(value = "select c.id from cruises c" +
            ",routes_ports r,ports p, cruise_dates d where " +
            " c.route= r.route_id and p.city = :city and d.cruise_id = c.id " +
            "and d.dates_order = 0 and r.ports_id = p.id and d.dates > current_timestamp" +
            "", nativeQuery = true)
    List<Long> findByCity(String city, Pageable pageable);

    @Query(value = "select count(c.id) from cruises c" +
            ",routes_ports r,ports p, cruise_dates d where " +
            " c.route= r.route_id and p.city = :city and d.cruise_id = c.id " +
            "and d.dates_order = 0 and r.ports_id = p.id and d.dates > current_timestamp" +
            "", nativeQuery = true)
    Integer countByCity(String city);

    @Query(value = "select c.id from cruises c,cruise_dates d where c.id = d.cruise_id and " +
            " d.dates_order = 0 and d.dates > current_timestamp ",nativeQuery = true)
    List<Long> findActual(Pageable pageable);

    @Query(value = "select distinct c.id   from cruises c,cruise_dates d,cruise_dates cd" +
            "             group by c.id, d.cruise_id, d.dates_order" +
            "             having c.id = d.cruise_id" +
            "             and d.dates_order = (max(d.dates_order)) and d.dates > current_timestamp",nativeQuery = true)
    List<Long> findUnfinished(Pageable pageable);
    @Query(value = "select current_timestamp ",nativeQuery = true)
    String timestamp();

    @Query(value = "select count(c.id) from cruises c,cruise_dates d where c.id = d.cruise_id and " +
            " d.dates_order = 0 and d.dates > current_timestamp ",nativeQuery = true)
    Integer countActual();

    @Query(value = "select c.id from cruises c,cruise_dates d, ships s where c.id = d.cruise_id and " +
            " d.dates_order = 0 and d.dates > current_timestamp and c.ship = s.id " +
            "and c.econom_tickets + c.middle_tickets + c.premium_tickets < s.total_seats",nativeQuery = true)
    List<Long> findFree(Pageable pageable);

    @Query(value = "select count(c.id) from cruises c,cruise_dates d, ships s where c.id = d.cruise_id and " +
            " d.dates_order = 0 and d.dates > current_timestamp and c.ship = s.id " +
            "and c.econom_tickets + c.middle_tickets + c.premium_tickets > s.total_seats",nativeQuery = true)
    Integer countFree();

    @Query("select c from Cruise c")
    Page<Cruise> findPage(Pageable pageable);


//        @Query("select c from Cruise c where c.dates > current_date")
//        List<Cruise> findCruiseByDatesAfter(String city, Pageable pageable);
}
