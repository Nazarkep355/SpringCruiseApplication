package com.example.SpringCruiseApplication.controller;


import com.example.SpringCruiseApplication.entity.Cruise;
import com.example.SpringCruiseApplication.entity.CruiseRequest;
import com.example.SpringCruiseApplication.entity.RoomClass;
import com.example.SpringCruiseApplication.entity.User;
import com.example.SpringCruiseApplication.service.CruiseRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/requests")
public class CRequestController {

    @Autowired
    private CruiseRequestService crService;

    @GetMapping("/send")
    public String sendPage(String rClass, Long id, HttpSession session, Model model) {
        Cruise cruise = null;
        try {
            cruise = crService.findCruiseById(id);
        } catch (NoSuchElementException e) {
            session.setAttribute("error", "error.noCruise");
            return "redirect:/home";
        }
        if (cruise.getDates().get(0).before(Date.from(Instant.now()))) {
            session.setAttribute("error", "error.notActualCruise");
            return "redirect:/cruises/" + id;
        }
        RoomClass roomClass = RoomClass.valueOf(rClass);
        if (cruise.getFreePlacesByClass(roomClass) < 1) {
            session.setAttribute("error", "error.noFreePlaces");
            return "redirect:/cruises/" + id;
        }


        model.addAttribute("roomClass", roomClass);
        model.addAttribute("cruise", cruise);
        return "send_request.html";
    }

    @PostMapping("/send")
    public String sendRequest(@SessionAttribute Optional<User> user,
                              MultipartFile file, Long id,
                              String roomClass, HttpSession session) throws IOException {
        if (user.isEmpty()) {
            return "redirect:/home";
        }
        String name = crService.saveFile(file);
        Cruise cruise = crService.findCruiseById(id);
        if (cruise.getFreePlacesByClass(RoomClass.valueOf(roomClass)) < 1) {
            session.setAttribute("error", "error.noFreePlaces");
            return "redirect:/cruises/" + id;
        }
        crService.createRequest(user.get(), name, id, RoomClass.valueOf(roomClass));
        return "redirect:/cruises/" + id;
    }

    @GetMapping("/admin/{id}")
    public String find(@PathVariable("id") long id, Model model, HttpSession session) {
        Object error = session.getAttribute("error");
        if (error != null) {
            model.addAttribute("error", error);
            session.setAttribute("error", null);
        }
        CruiseRequest cruiseRequest = crService.findById(id);
        model.addAttribute("req", cruiseRequest);
        return "request.html";
    }

    @GetMapping("/admin/all")
    public String requestsPage(Optional<Integer> page, Optional<Boolean> actual,
                               Optional<Long> cruise, Model model, HttpSession session) {
        List<CruiseRequest> requests = null;
        Object error = session.getAttribute("error");
        if(error!=null){
            model.addAttribute("error",error);
            session.setAttribute("error",null);
        }
        try {
            requests = crService.findBy(page.orElse(1)
                    , actual.orElse(false), cruise);
        } catch (NoSuchElementException e) {
            session.setAttribute("error", "error.noCruise");
            return "redirect:/requests/admin/all";
        }
        int allSize = crService.countBy(page.orElse(1), actual.orElse(false), cruise);
        boolean max = !(allSize > (page.orElse(1) * 5));
        model.addAttribute("max", max);
        model.addAttribute("page", page.orElse(1));
        model.addAttribute("actual", actual.orElse(false));
        model.addAttribute("cruiseId", cruise.orElse(0l));
        model.addAttribute("requests", requests);
        return "requests.html";
    }

    @PostMapping("/admin/response")
    public String response(boolean response, Long id, HttpSession session) throws MessagingException {
        CruiseRequest request = crService.findById(id);
        RoomClass roomClass = request.getRoomClass();
        if (request.getCruise().getFreePlacesByClass(roomClass) < 1) {
            session.setAttribute("error", "error.noFreePlaces");
            return "redirect:/requests/admin/" + id;
        }
        crService.responseRequest(request, response);
        return "redirect:/requests/admin/all";
    }

}
