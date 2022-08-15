package com.example.SpringCruiseApplication.service;

import com.example.SpringCruiseApplication.entity.Ship;
import com.example.SpringCruiseApplication.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipService {
    @Autowired
    private ShipRepository shipRepository;

    public List<Ship> findAllPageable(int page) {

        return shipRepository.findAll(PageRequest.of(page - 1, 5));
    }
    public boolean insert(Ship ship){
        if(shipRepository.existsShipByName(ship.getName()))
            throw new IllegalArgumentException("error.shipNameExist");
        shipRepository.save(ship);
        return true;
    }
    public Integer count(){
        return shipRepository.countAll();
    }

    public Ship update(Ship ship){
        return shipRepository.save(ship);
    }

}
