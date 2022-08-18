package com.example.SpringCruiseApplication.controller;

import com.example.SpringCruiseApplication.entity.Ship;
import com.example.SpringCruiseApplication.entity.Staff;
import com.example.SpringCruiseApplication.repository.StaffRepository;
import com.example.SpringCruiseApplication.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;

    @GetMapping("/all")
    public String staffPage(Model model, Optional<Integer> page) {
        List<Staff> staff = staffService.findStaffPageable(page.orElse(1));
        Integer allSize = staffService.count();
        boolean max = !(allSize > (page.orElse(1) * 5));


        model.addAttribute("max", max);
        model.addAttribute("page", page.orElse(1));
        model.addAttribute("staff", staff);
        return "staff.html";
    }

    @GetMapping("/add")
    public String addPage(Model model, HttpSession session) {
        Optional<Object> error = Optional.ofNullable(session.getAttribute("error"));
        error.ifPresent(o -> model.addAttribute("error", o));
        session.setAttribute("error",null);
        return "staff_add.html";
    }

    @PostMapping("/add")
    public String addStaff(Staff staff,HttpSession session){
        try{
            staffService.createNewStaff(staff.getName(),staff.getPosition());
        }catch (IllegalArgumentException e){
            session.setAttribute("error",e.getMessage());
            return "redirect:/staff/add";
        }
        return "redirect:/staff/all";
    }

}
