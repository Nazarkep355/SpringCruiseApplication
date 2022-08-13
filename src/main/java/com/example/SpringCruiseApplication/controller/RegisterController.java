package com.example.SpringCruiseApplication.controller;

import com.example.SpringCruiseApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class RegisterController {
    @Autowired
    private UserService userService;
    @GetMapping("/register")
    public String registerPage(HttpSession session, Model model){
        Optional<Object> error = Optional.ofNullable(session.getAttribute("error"));
        if (error.isPresent()) {
            model.addAttribute("error", error.get());
        }
        session.setAttribute("error", null);

    }

}
