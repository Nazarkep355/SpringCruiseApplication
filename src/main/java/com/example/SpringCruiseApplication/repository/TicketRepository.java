package com.example.SpringCruiseApplication.repository;

import com.example.SpringCruiseApplication.entity.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket,Long> {
}
