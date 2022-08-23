package com.example.SpringCruiseApplication.repository;

import com.example.SpringCruiseApplication.entity.Ticket;
import com.example.SpringCruiseApplication.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket, Long> {

    List<Ticket> findAllByOwner(User owner, Pageable pageable);

    @Query("select count(t) from Ticket t where t.owner = ?1")
    Integer countAllByOwner(User owner);
}
