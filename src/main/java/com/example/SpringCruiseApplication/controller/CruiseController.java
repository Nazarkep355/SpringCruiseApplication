package com.example.SpringCruiseApplication.controller;

import com.example.SpringCruiseApplication.entity.*;
import com.example.SpringCruiseApplication.service.CruiseService;
import com.example.SpringCruiseApplication.util.ParseDateUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cruises")
public class CruiseController {
    @Autowired
    private CruiseService cruiseService;

    @GetMapping("/all")
    public String cruisesPage(Model model, Optional<Integer> page,
                              Optional<Boolean> actual, Optional<String> city,
                              Optional<Boolean> freeOnly) {
        List<Cruise> cruises = cruiseService.findBy(page.orElse(1), city,
                actual.orElse(false), freeOnly.orElse(false));
        Integer count = cruiseService.findCount(page.orElse(1), city,
                actual.orElse(false), freeOnly.orElse(false));
        boolean max = !(count>(page.orElse(1)*5));
        model.addAttribute("max",max);
        model.addAttribute("cruises", cruises);
        model.addAttribute("page", page.orElse(1));
        model.addAttribute("freeOnly",freeOnly.orElse(false));
        model.addAttribute("actual",actual.orElse(false));
        return "cruises.html";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        List<Ship> ships = cruiseService.findShips();
        List<Route> routes = cruiseService.findRoutes();
        List<Staff> staff = cruiseService.findEnabledStaff();


        model.addAttribute("ships", ships);
        model.addAttribute("routes", routes);
        model.addAttribute("staff", staff);
        return "cruises_add.html";
    }

    @PostMapping("/add")
    public String addCruise(String date, long route, HttpServletRequest request, int number, Long ship) {
        Date departureDate = ParseDateUtility.getDateFromForm(date);
        List<Long> staff = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            staff.add(Long.parseLong(request.getParameter("staff" + i)));
        }
        cruiseService.planCruise(route, ship, departureDate, staff);
        return "redirect:/cruises/all";
    }


}
