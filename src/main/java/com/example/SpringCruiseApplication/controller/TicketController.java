package com.example.SpringCruiseApplication.controller;

import com.example.SpringCruiseApplication.entity.Ticket;
import com.example.SpringCruiseApplication.entity.User;
import com.example.SpringCruiseApplication.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.Optional;

@Controller
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/tickets")
    public String ticketsPage(Model model, @SessionAttribute Optional<User> user, Optional<Integer> page) {
        if (user.isEmpty()) {
            return "/home";
        }

        List<Ticket> tickets = ticketService.findByUser(user.get(), page.orElse(1));
        Integer count = ticketService.countByUser(user.get());
        boolean max = !(count > (page.orElse(1) * 5));

        model.addAttribute("max",max);
        model.addAttribute("tickets",tickets);
        model.addAttribute("page",page.orElse(1));
        return "tickets.html";
    }
}
