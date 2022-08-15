package com.example.SpringCruiseApplication.service;

import com.example.SpringCruiseApplication.entity.Port;
import com.example.SpringCruiseApplication.repository.PortRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortService {
    @Autowired
    private PortRepository portRepository;

    public Port insert(Port port) {
        return portRepository.save(port);
    }
    public Port createNewPort(String city){
        if(portRepository.findPortByCity(city).isPresent()){
            throw new IllegalArgumentException("error.portInDB");
        }
        Port port = new Port();
        port.setCity(city);
        return insert(port);
    }

    public List<Port> findPortPageable(int page) {
        return portRepository.findAll(PageRequest.of(page - 1, 5));
    }

    public Integer count() {
        return portRepository.countAll();
    }
}
