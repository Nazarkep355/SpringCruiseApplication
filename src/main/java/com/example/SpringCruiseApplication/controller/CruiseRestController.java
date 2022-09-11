package com.example.SpringCruiseApplication.controller;

import com.example.SpringCruiseApplication.entity.Cruise;
import com.example.SpringCruiseApplication.service.CruiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/cruises")
public class CruiseRestController {
    @Autowired
    private CruiseService cruiseService;

    @GetMapping
    public List<Cruise> findPageable(Pageable pageable) {
        return cruiseService.findAllPageable(pageable).getContent();
    }
    @GetMapping("/size")
    public Integer countAll(){
        return cruiseService.count();
    }
    @GetMapping("/city/{city}")
    public List<Cruise> findByCity(@PathVariable("city")String city,Pageable pageable){
        return  cruiseService.findByCityPageable(city,pageable);
    }

}
