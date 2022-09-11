package com.example.SpringCruiseApplication.controller;

import com.example.SpringCruiseApplication.entity.*;
import com.example.SpringCruiseApplication.service.CruiseRequestService;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.Session;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CRequestControllerTest {
    @Mock
    private User user;
    @Mock
    private CruiseRequestService service;
    private CRequestController controller;
    private MockMvc mockMvc;
    private List<CruiseRequest> requests;

    private List<Cruise> cruises;

    public CRequestControllerTest() {
        requests = new ArrayList<>();
        cruises = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CruiseRequest cruiseRequest = new CruiseRequest();
            cruiseRequest.setId(i + 1l);
            cruiseRequest.setRoomClass(RoomClass.PREMIUM);
            requests.add(cruiseRequest);
        }
        for (int i = 0; i < 3; i++) {
            Date tomorrow = Date.from(Instant.now());
            tomorrow.setDate(tomorrow.getDate() + 1);
            Ship ship = new Ship();
            ship.setPremiumTotalPlaces(10);
            Cruise cruise = new Cruise();
            if (i == 2) {
                cruise.setPremiumTickets(10);
            }
            cruise.setShip(ship);
            cruise.setDates(List.of(tomorrow));
            cruise.setId(i + 1l);
            cruises.add(cruise);
        }
        MockitoAnnotations.initMocks(this);
        controller = new CRequestController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        when(service.findById(1l)).thenReturn(requests.get(0));
        when(service.findCruiseById(1l)).thenReturn(cruises.get(0));
        when(service.findCruiseById(15l)).thenThrow(NoSuchElementException.class);
    }

    @Test
    void sendPage() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap();
        map.put("rClass", List.of("PREMIUM"));
        map.put("id", List.of("1"));
        mockMvc
                .perform(MockMvcRequestBuilders.get("/requests/send").params(map))
                .andExpect(MockMvcResultMatchers
                        .view().name("send_request.html"))
                .andExpect(MockMvcResultMatchers.model().attribute("roomClass", RoomClass.PREMIUM))
                .andExpect(MockMvcResultMatchers.model().attribute("cruise", cruises.get(0)));


        map.put("id", List.of("15"));


        mockMvc
                .perform(MockMvcRequestBuilders.get("/requests/send").params(map))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));

    }

    @Test
    void sendRequest() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file.png", "file.png".getBytes());
        MultiValueMap<String, String> map = new LinkedMultiValueMap();
        map.put("roomClass", List.of("PREMIUM"));
        map.put("id", List.of("1"));

        mockMvc
                .perform(MockMvcRequestBuilders.multipart("/requests/send")
                        .file(multipartFile)
                        .params(map))
                .andExpect(MockMvcResultMatchers
                        .redirectedUrl("/home"));
        when(service.saveFile(multipartFile)).thenReturn("file.png");
        when(service.findCruiseById(2l)).thenReturn(cruises.get(1));
        mockMvc
                .perform(MockMvcRequestBuilders.multipart("/requests/send")
                        .file(multipartFile)
                        .params(map)
                        .sessionAttr("user", user))
                .andExpect(MockMvcResultMatchers
                        .redirectedUrl("/cruises/1"));
        verify(service, times(0)).createRequest(user, "file.png", 1l, RoomClass.PREMIUM);
        map.put("id", List.of("2"));
        when(service.saveFile(multipartFile)).thenReturn("file.png");
        mockMvc
                .perform(MockMvcRequestBuilders.multipart("/requests/send")
                        .file("file", multipartFile.getBytes())
                        .params(map)
                        .sessionAttr("user", user))
                .andExpect(MockMvcResultMatchers
                        .redirectedUrl("/cruises/2"));
    }

    @Test
    void find() throws Exception {
        String error = "error.noFreePlaces";
        mockMvc
                .perform(MockMvcRequestBuilders.get("/requests/admin/1")
                        .sessionAttr("error", error))
                .andExpect(MockMvcResultMatchers
                        .view().name("request.html"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", error));
        verify(service, times(1)).findById(1);

    }

    @Test
    void requestsPage() throws Exception {
        String error = "ERROR";
        when(service.findBy(1, false, Optional.of(5l))).thenThrow(NoSuchElementException.class);
        when(service.findBy(1, false, Optional.empty())).thenReturn(requests);
        when(service.countBy(1, false, Optional.empty())).thenReturn(3);
        mockMvc
                .perform(MockMvcRequestBuilders.get("/requests/admin/all")
                        .sessionAttr("error", error))
                .andExpect(MockMvcResultMatchers
                        .view().name("requests.html"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", error))
                .andExpect(MockMvcResultMatchers.model().attribute("max", true))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc
                .perform(MockMvcRequestBuilders.get("/requests/admin/all?cruise=5"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/requests/admin/all"));

    }

    @Test
    void response() throws Exception {
        requests.get(2).setCruise(cruises.get(2));
        when(service.findById(3)).thenReturn(requests.get(2));
        requests.get(0).setCruise(cruises.get(0));
        when(service.findById(1)).thenReturn(requests.get(0));
        mockMvc
                .perform(MockMvcRequestBuilders.post("/requests/admin/response")
                        .param("response","true")
                        .param("id","3"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/requests/admin/3"));


        mockMvc
                .perform(MockMvcRequestBuilders.post("/requests/admin/response")
                        .param("response","true")
                        .param("id","1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/requests/admin/all"));
        verify(service,times(1)).respondRequest(requests.get(0),true);

    }
}