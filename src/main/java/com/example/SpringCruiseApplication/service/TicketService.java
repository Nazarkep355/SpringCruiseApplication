package com.example.SpringCruiseApplication.service;

import com.example.SpringCruiseApplication.entity.Ticket;
import com.example.SpringCruiseApplication.entity.User;
import com.example.SpringCruiseApplication.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public List<Ticket> findByUser(User user, int page) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        return ticketRepository.findAllByOwner(user, pageable);
    }

    public Integer countByUser(User user) {
        return ticketRepository.countAllByOwner(user);
    }
}
