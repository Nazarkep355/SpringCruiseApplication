package com.example.SpringCruiseApplication.service;

import com.example.SpringCruiseApplication.controller.UploadController;
import com.example.SpringCruiseApplication.entity.*;
import com.example.SpringCruiseApplication.mail.Sender;
import com.example.SpringCruiseApplication.repository.CruiseRepository;
import com.example.SpringCruiseApplication.repository.CruiseRequestRepository;
import com.example.SpringCruiseApplication.repository.TicketRepository;
import com.example.SpringCruiseApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CruiseRequestService {
    @Value("${path.upload}")
    private String path;
    @Autowired
    private CruiseRepository cruiseRepository;
    @Autowired
    private CruiseRequestRepository crRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private Sender sender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UploadController uploadController;
    private Long fileIndex = 0l;


    public List<CruiseRequest> findBy(int page, boolean actual, Optional<Long> cruiseId) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        if (cruiseId.isPresent()&&cruiseId.get()>0) {
            Cruise cruise = cruiseRepository.findById(cruiseId.get()).orElseThrow();
            return crRepository.findAllByCruise(cruise, pageable);
        }
        if (actual) {
            List<Long> ids = crRepository.findAllActual(pageable);
            List<CruiseRequest> requests = ids.stream()
                    .map(id -> crRepository.findById(id).get())
                    .toList();
            return requests;
//            List<Cruise> actualCruises = cruiseRepository.findActual(
//                            PageRequest.of(page - 1, 5)).stream()
//                    .map(a -> cruiseRepository.findById(a).get())
//                    .collect(Collectors.toList());
//
//            List<CruiseRequest> requests = actualCruises.stream()
//                    .map(a -> crRepository.findAllByCruise(a, pageable))
//                    .flatMap(a -> a.stream())
//                    .collect(Collectors.toList());
//            return requests;
        }
        return crRepository.findAll(pageable);
    }

    public Integer countBy(int page, boolean actual, Optional<Long> cruiseId) {

        if (cruiseId.isPresent()&&cruiseId.get()>0) {
            Cruise cruise = cruiseRepository.findById(cruiseId.get()).get();
            return crRepository.countAllByCruise(cruise);
        }
        if (actual) {
            return crRepository.countAllActual();

//            List<Cruise> actualCruises = cruiseRepository.findActual(
//                            PageRequest.of(page - 1, 5)).stream()
//                    .map(a -> cruiseRepository.findById(a).get())
//                    .collect(Collectors.toList());
//
//            List<CruiseRequest> requests = actualCruises.stream()
//                    .map(a -> crRepository.findAllByCruise(a, pageable))
//                    .flatMap(a -> a.stream())
//                    .collect(Collectors.toList());
//            return requests;
        }
        return crRepository.countAllBy();
    }

    public Cruise findCruiseById(Long id) {
        return cruiseRepository.findById(id).orElseThrow();
    }

    public String saveFile(MultipartFile file) throws IOException {
        return uploadController.saveFile(file);
    }

    public CruiseRequest findById(long id) {
        return crRepository.findById(id).get();
    }

    @Transactional
    public void responseRequest(CruiseRequest cruiseRequest, Boolean response) throws MessagingException {
        if (response) {
            RoomClass roomClass = cruiseRequest.getRoomClass();
            Cruise cruise = cruiseRequest.getCruise();
            cruise.addOneTicket(roomClass);
            User user = cruiseRequest.getSender();
            user.setMoney(user.getMoney() - cruise.getCostByClass(roomClass));
            Ticket ticket = new Ticket();
            ticket.setCruise(cruise);
            ticket.setOwner(user);
            ticket.setRoomClass(roomClass);
            ticket.setPurchaseDate(Date.from(Instant.now()));
            cruiseRequest.setStatus(Status.ACCEPTED);
            ticketRepository.save(ticket);
            userRepository.save(user);
            cruiseRepository.save(cruise);
            crRepository.save(cruiseRequest);
            sender.sendMessageAboutAccepting(cruiseRequest);
        }else {
            cruiseRequest.setStatus(Status.REFUSED);
            crRepository.save(cruiseRequest);
            sender.sendMessageAboutRefusing(cruiseRequest);
        }
    }

    public CruiseRequest createRequest(User user, String file, Long cruiseId, RoomClass roomClass) {
        CruiseRequest cruiseRequest = new CruiseRequest();
        cruiseRequest.setCruise(cruiseRepository.findById(cruiseId).get());
        cruiseRequest.setPhoto(file);
        cruiseRequest.setRoomClass(roomClass);
        cruiseRequest.setSender(user);
        cruiseRequest.setStatus(Status.WAITING);
        return crRepository.save(cruiseRequest);

    }
}
