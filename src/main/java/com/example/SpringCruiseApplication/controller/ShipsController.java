package com.example.SpringCruiseApplication.controller;

import com.example.SpringCruiseApplication.entity.Ship;
import com.example.SpringCruiseApplication.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ships")
public class ShipsController {
    @Autowired
    private ShipService shipService;

    @GetMapping("/all")
    public String page(Model model, Optional<Integer> page) {
        List<Ship> ships = shipService.findAllPageable(page.orElse(1));
        model.addAttribute("ships", ships);
        return "ships.html";
    }

    @GetMapping("/add")
    public String addPage(Model model, HttpSession session) {
        Optional<Object> error = Optional.ofNullable(session.getAttribute("error"));
        if (error.isPresent()) {
            model.addAttribute("error", error.get());
        }
        session.setAttribute("error", null);
        return "ship_add.html";
    }

    @PostMapping("/add")
    public String addShip(HttpSession session, String name,
                          int eCost, int eSeats,
                          int mCost, int mSeats,
                          int pCost, int pSeats) {
        Ship ship = new Ship();
        ship.setName(name);
        ship.setEnable(true);
        ship.setEconomCost(eCost);
        ship.setEconomTotalPlaces(eSeats);
        ship.setMiddleCost(mCost);
        ship.setMiddleTotalPlaces(mSeats);
        ship.setPremiumCost(pCost);
        ship.setPremiumTotalPlaces(pSeats);
        ship.setTotalSeats(eSeats + mSeats + pSeats);
        try {
            shipService.insert(ship);
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
            return "redirect:/ships/add";
        }
        return "redirect:/ships/all";
    }
}
