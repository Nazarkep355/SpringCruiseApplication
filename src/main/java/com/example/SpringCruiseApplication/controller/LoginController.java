package com.example.SpringCruiseApplication.controller;

import com.example.SpringCruiseApplication.entity.User;
import com.example.SpringCruiseApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(@SessionAttribute Optional<User> user) {
        if (user.isEmpty()) {
            return "login.html";
        } else {
            return "redirect:/home";
        }

    }

    @GetMapping("/home")
    public String homePage(@SessionAttribute Optional<User> user) {
        if (user.isPresent()) {
            System.out.println(user.get().getName());
            return "home.html";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/loginSuccess")
    public String loginSuccess(String username, String password, HttpSession session) {
        User user = userService.getUserByEmail(username);
        session.setAttribute("user", user);
        return "redirect:/home";
    }

    @PostMapping("/logout")
    public String logOut(HttpSession session) {
        session.setAttribute("user", null);
        return "redirect:/home";
    }
//    @PostMapping("/loginFail")
//    public String fail(){
//
//    }

}