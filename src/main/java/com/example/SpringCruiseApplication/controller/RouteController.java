package com.example.SpringCruiseApplication.controller;

import com.example.SpringCruiseApplication.entity.Port;
import com.example.SpringCruiseApplication.entity.Route;
import com.example.SpringCruiseApplication.service.RouteService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/all")
    public String routesPage(Optional<Integer> page, Model model) {
        List<Route> routes = routeService.findAllPageable(page.orElse(1));
        Integer allSize = routeService.count();
        boolean max = !(allSize > (page.orElse(1) * 5));

        model.addAttribute("routes", routes);
        model.addAttribute("max", max);
        model.addAttribute("page", page.orElse(1));
        return "routes.html";
    }

    @GetMapping("/add")
    public String addPage(HttpSession session, Model model) {
        Optional<Object> error = Optional.ofNullable(session.getAttribute("error"));
        error.ifPresent(o -> model.addAttribute("error", o));
        session.setAttribute("error",null);
        return "routes_add.html";
    }

    @PostMapping("/add")
    public String addRoute(HttpServletRequest request, int number,HttpSession session){
        List<String> cities = new ArrayList<>();
        List<Integer> delays = new ArrayList<>();
        for(int i=1;i<=number;i++){
            cities.add(request.getParameter("city"+i));
            if(i==number)break;
            delays.add(Integer.parseInt(request.getParameter("delay"+i)));
        }
        try{
            routeService.insert(cities,delays);
        }catch (IllegalArgumentException e){
            session.setAttribute("error",e.getMessage());
            return "redirect:/routes/add";
        }
        return "redirect:/routes/all";
    }
}
