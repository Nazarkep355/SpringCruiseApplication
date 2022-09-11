package com.example.SpringCruiseApplication.controller;

import com.example.SpringCruiseApplication.entity.User;
import com.example.SpringCruiseApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/user")
public class UpdateUserController {
    @Autowired
    private UserService userService;
    @GetMapping("/changeBalance")
    public String changeBalancePage() {
        return "change_balance.html";
    }

    @PostMapping("/changeBalance")
    public String changeBalance(@SessionAttribute User user,int money){
        userService.changeBalance(user, money);
        return "redirect:/home";
    }

}
