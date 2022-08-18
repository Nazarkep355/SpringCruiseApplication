package com.example.SpringCruiseApplication.controller;

import com.example.SpringCruiseApplication.entity.User;
import com.example.SpringCruiseApplication.service.UserService;
import com.example.SpringCruiseApplication.util.ValidExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class RegisterController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authManager;
    @GetMapping("/register")
    public String registerPage(HttpSession session, Model model){
        Optional<Object> error = Optional.ofNullable(session.getAttribute("error"));
        if (error.isPresent()) {
             List<String> errors = (List)error.get();
            for(String s :errors){
            model.addAttribute(s.split("[.]")[1], true);}
        }
        session.setAttribute("error", null);
        return "register.html";
    }
    @PostMapping("/register")
    public String register(User user, HttpSession session) {
        try {
            user = userService.registerUser(user);
        }catch (ConstraintViolationException e){
                session.setAttribute("error", ValidExceptionHandler.parseConsValidException(e));
            return "redirect:/register";
        }catch (IllegalArgumentException e){
            session.setAttribute("error",List.of(e.getMessage()));
            return "redirect:/register";
        }
        session.setAttribute("user",user);
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        Authentication auth = authManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
        return "redirect:/home";
    }

}
