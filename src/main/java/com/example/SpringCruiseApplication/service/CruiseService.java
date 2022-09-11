package com.example.SpringCruiseApplication.service;

import com.example.SpringCruiseApplication.entity.*;
import com.example.SpringCruiseApplication.mail.Sender;
import com.example.SpringCruiseApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class CruiseService {
    @Autowired
    private CruiseRepository cruiseRepository;
    @Autowired
    private PortRepository portRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private Sender sender;

    public void planAllEndings(){
        System.out.println(cruiseRepository.timestamp());
        List<Cruise> cruises = cruiseRepository.findUnfinished(Pageable.unpaged())
                .stream().map(a -> cruiseRepository.findById(a).get())
                .collect(Collectors.toList());
        for (Cruise cruise : cruises) {
            System.out.println(cruise.getId());
            setFinishTimer(cruise, cruise.getDates().get(cruise.getDates().size() - 1));
        }
    }
    public Page<Cruise> findAllPageable(Pageable pageable) {
        return cruiseRepository.findPage(pageable);
    }

    public List<Cruise> findAllPageable(int page) {
        return cruiseRepository.findAll(PageRequest.of(page - 1, 5));
    }

    public Integer count() {
        return cruiseRepository.countAll();
    }

    public Cruise findById(Long id) {
        return cruiseRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Cruise planCruise(Long route, Long ship, Date date, List<Long> staffList) {
        Optional<Route> routeOptional = routeRepository.findById(route);
        Cruise cruise = new Cruise();
        cruise.setShip(shipRepository.findById(ship).get());
        cruise.setRoute(routeOptional.get());
        cruise.setStaff(staffList.stream()
                .map(a -> staffRepository.findById(a).get())
                .collect(Collectors.toList()));
        List<Date> dates = new ArrayList<>();
        dates.add(date);
        for (Integer i : routeOptional.get().getDelays()) {
            date = new Date(date.getTime());
            date.setDate(date.getDate() + 1 + i);
            dates.add(date);
        }
        cruise.getShip().setEnable(false);
        for (Staff staf : cruise.getStaff()) {
            staf.setEnable(false);
        }
        cruise.setDates(dates);
        setFinishTimer(cruise, date);
        return cruiseRepository.save(cruise);
    }

    public void setFinishTimer(Cruise cruise, Date date) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                endCruise(cruise);
                System.out.println("ENDED " + Date.from(Instant.now()));
            }
        };
        timer.schedule(task, date);
    }


    public List<Staff> findEnabledStaff() {
        return staffRepository.findAllByEnable(true, PageRequest.of(0, 50));
    }

    public List<Route> findRoutes() {
        return routeRepository.findAll(PageRequest.of(0, 30));
    }

    public List<Ship> findShips() {
        return shipRepository.findAllByEnable(true, PageRequest.of(0, 30));
    }

    @Transactional
    public void endCruise(Cruise cruise) {
        cruise.getShip().setEnable(true);
        cruise.getStaff().forEach(
                a -> {
                    a.setEnable(true);
                    staffRepository.save(a);
                });
        shipRepository.save(cruise.getShip());
        cruiseRepository.save(cruise);
    }

    public List<Cruise> findByCityPageable(String city, Pageable pageable) {
        Port port = portRepository.findPortByCity(city).orElseThrow();
        List<Long> ids = cruiseRepository.findByCity(port.getCity(),
                pageable);
        return ids.stream().map(a -> cruiseRepository.findById(a).get())
                .collect(Collectors.toList());
    }

    public List<Cruise> findBy(int page, Optional<String> city, Boolean actual, Boolean freeOnly) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        if (city.isPresent() && !StringUtils.isEmpty(city.get())) {
            return findByCityPageable(city.get(), pageable);
        } else if (freeOnly) {
            List<Long> ids = cruiseRepository.findFree(PageRequest.of(page - 1, 5));
            return ids.stream().map(id -> cruiseRepository.findById(id).get())
                    .collect(Collectors.toList());
        } else if (actual) {
            List<Long> ids = cruiseRepository.findActual(PageRequest.of(page - 1, 5));
            return ids.stream().map(a -> cruiseRepository.findById(a).get())
                    .collect(Collectors.toList());
        }
        return cruiseRepository.findAll(PageRequest.of(page - 1, 5));
    }

    public Integer findCount(int page, Optional<String> city, Boolean actual, Boolean freeOnly) {
        if (city.isPresent() && !StringUtils.isEmpty(city.get())) {
            Port port = portRepository.findPortByCity(city.get()).orElseThrow();
            return cruiseRepository.countByCity(port.getCity());
        } else if (freeOnly) {
            List<Long> ids = cruiseRepository.findFree(PageRequest.of(page - 1, 5));
            return cruiseRepository.countFree();
        } else if (actual) {
            List<Long> ids = cruiseRepository.findActual(PageRequest.of(page - 1, 5));
            return cruiseRepository.countActual();
        }
        return cruiseRepository.countAll();

    }

}
