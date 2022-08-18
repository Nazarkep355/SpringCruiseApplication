package com.example.SpringCruiseApplication.service;

import com.example.SpringCruiseApplication.entity.Port;
import com.example.SpringCruiseApplication.entity.Route;
import com.example.SpringCruiseApplication.repository.PortRepository;
import com.example.SpringCruiseApplication.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private PortRepository portRepository;

    public List<Route> findAllPageable(int page) {
        return routeRepository.findAll(PageRequest.of(page - 1, 5));
    }

    public Integer count() {
        return routeRepository.countAll();
    }

    public Route insert(List<String> cities, List<Integer> delays) {
        List<Port> ports = new ArrayList<>();
        for (String city : cities) {
            Optional<Port> portOptional = portRepository.findPortByCity(city);
            if (portOptional.isEmpty()) {
                throw new IllegalArgumentException("error.portNotFound");
            }
            ports.add(portOptional.get());
        }
        Route route = new Route();
        route.setDelays(delays);
        route.setPorts(ports);
        return routeRepository.save(route);
    }


}
