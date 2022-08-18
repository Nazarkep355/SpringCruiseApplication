package com.example.SpringCruiseApplication.service;

import com.example.SpringCruiseApplication.entity.*;
import com.example.SpringCruiseApplication.mail.Sender;
import com.example.SpringCruiseApplication.repository.*;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
//                System.out.println("ENDED "+Date.from(Instant.now()));
            }
        };
        timer.schedule(task, date);
    }

    //    public void add() {
//        Cruise cruise = new Cruise();
//        List<Staff> staff = new ArrayList<>();
//        staff.add(staffRepository.findById(1l).get());
//        staff.add(staffRepository.findById(31l).get());
//        cruise.setStaff(staff);
//        Route route = routeRepository.findById(42l).get();
//        Ship ship = shipRepository.findById(1l).get();
//        cruise.setRoute(route);
//        List<Date> dates = new ArrayList<>();
//        Date date = Date.from(Instant.now());
//        dates.add(date);
//        for(int i =0;i<route.getDelays().size();i++){
//            date = new Date(date.getTime());
//            date.setDate(date.getDate()+1+route.getDelays().get(i));
//            dates.add(date);
//        }
//        cruise.setDates(dates);
//        cruise.setShip(ship);
//        cruiseRepository.save(cruise);
//    }
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

    public List<Cruise> findBy(int page, Optional<String> city, Boolean actual, Boolean freeOnly) {
        if (city.isPresent() && !StringUtils.isEmpty(city.get())) {
            Port port = portRepository.findPortByCity(city.get()).orElseThrow();
            List<Long> ids = cruiseRepository.findByCity(port.getCity(),
                    PageRequest.of(page - 1, 5));
            return ids.stream().map(a -> cruiseRepository.findById(a).get())
                    .collect(Collectors.toList());
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

    public void delete(Long id) {
        cruiseRepository.deleteById(id);
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
