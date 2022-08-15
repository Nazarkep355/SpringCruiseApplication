package com.example.SpringCruiseApplication.controller;

import com.example.SpringCruiseApplication.entity.Port;
import com.example.SpringCruiseApplication.service.PortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ports")
public class PortController {
    @Autowired
    private PortService portService;

    @GetMapping("/all")
    public String portsPage(Model model, Optional<Integer> page){
        List<Port> ports = portService.findPortPageable(page.orElse(1));
        Integer sizePort = portService.count();
        boolean max = !(sizePort>(page.orElse(1)*5));

        model.addAttribute("max",max);
        model.addAttribute("ports",ports);
        model.addAttribute("page",page.orElse(1));
        return "ports.html";
    }
    @PostMapping("/add")
    public String addPort(String city, HttpSession session){
        try{
            portService.createNewPort(city);
        }catch (IllegalArgumentException e){
            session.setAttribute("error",e.getMessage());
            return "redirect:/ports/add";
        }
        return "redirect:/ports/all";
    }
    @GetMapping("/add")
    public String addPage(Model model, @SessionAttribute Optional<String> error){
        error.ifPresent(s -> model.addAttribute("error", s));
    return "port_add.html";
    }
}
