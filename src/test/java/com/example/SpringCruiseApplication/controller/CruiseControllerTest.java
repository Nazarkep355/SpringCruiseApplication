package com.example.SpringCruiseApplication.controller;

import com.example.SpringCruiseApplication.entity.*;
import com.example.SpringCruiseApplication.service.CruiseService;
import com.example.SpringCruiseApplication.util.ParseDateUtility;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CruiseControllerTest {
    @Mock
    private CruiseService cruiseService;
    private MockMvc mockMvc;
    private CruiseController controller;
    private List<Cruise> cruiseList;

    public CruiseControllerTest() {
        MockitoAnnotations.initMocks(this);
        controller = new CruiseController(cruiseService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        cruiseList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Date tomorrow = Date.from(Instant.now());
            tomorrow.setDate(tomorrow.getDate() + 1);
            Ship ship = new Ship();
            ship.setPremiumTotalPlaces(10);
            Cruise cruise = new Cruise();
            cruise.setRoute(new Route());
            cruise.getRoute().setPorts(new ArrayList<>());
            if (i == 1) {
                Route route = new Route();
                Port port = new Port();
                port.setCity("Kyiv");
                route.setPorts(List.of(port));
                cruise.setRoute(route);
            }
            if (i == 2) {
                cruise.setPremiumTickets(10);
            }
            cruise.setShip(ship);
            cruise.setDates(List.of(tomorrow));
            cruise.setId(i + 1l);
            cruiseList.add(cruise);
        }
    }

    @Test
    void findOne() throws Exception {
        when(cruiseService.findById(5l)).thenThrow(NoSuchElementException.class);
        when(cruiseService.findById(1l)).thenReturn(cruiseList.get(0));
        mockMvc
                .perform(MockMvcRequestBuilders.get("/cruises/5"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cruises/all"));
        mockMvc
                .perform(MockMvcRequestBuilders.get("/cruises/1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.view().name("cruise.html"))
                .andExpect(MockMvcResultMatchers.model().attribute("cruise", cruiseList.get(0)));
    }

    @Test
    void cruisesPage() throws Exception {
        String city = "Kyiv";
        when(cruiseService.findBy(1, Optional.empty(), false, false))
                .thenReturn(cruiseList.subList(0, 2));
        when(cruiseService.findBy(1, Optional.of(city), false, false))
                .thenReturn(cruiseList.stream().filter(a->{
                    List<String> cities =a.getRoute().getPorts().stream().map(p->p.getCity()).toList();
                    return cities.contains(city);
                }).toList());
        mockMvc
                .perform(MockMvcRequestBuilders.get("/cruises/all"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.view().name("cruises.html"))
                .andExpect(MockMvcResultMatchers.model().attribute("cruises", cruiseList.subList(0, 2)));
        mockMvc
                .perform(MockMvcRequestBuilders.get("/cruises/all?city=Kyiv"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.view().name("cruises.html"))
                .andExpect(MockMvcResultMatchers.model().attribute("cruises", List.of(cruiseList.get(1))));

    }

    @Test
    void addPage() throws Exception {
        List<Ship> ships = List.of(new Ship(),new Ship());
        List<Staff> staff = List.of(new Staff(), new Staff());
        when(cruiseService.findShips()).thenReturn(ships);
        when(cruiseService.findEnabledStaff()).thenReturn(staff);
        when(cruiseService.findRoutes()).thenReturn(List.of(cruiseList.get(1).getRoute()));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/cruises/admin/add"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.view().name("cruises_add.html"))
                .andExpect(MockMvcResultMatchers.model().attribute("staff",staff))
                .andExpect(MockMvcResultMatchers.model().attribute("ships",ships))
                .andExpect(MockMvcResultMatchers.model().attribute("routes",List.of(cruiseList.get(1).getRoute())));


    }

    @Test
    void addCruise() throws Exception {
        String formDate = "2022-08-24T18:30";
        Date date = ParseDateUtility.getDateFromForm(formDate);
        mockMvc
                .perform(MockMvcRequestBuilders.post("/cruises/admin/add")
                        .param("date",formDate)
                        .param("ship","1")
                        .param("number","2")
                        .param("staff1","1")
                        .param("staff2","2")
                        .param("route","1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cruises/all"));
        verify(cruiseService,times(1)).planCruise(1l,1l,date,List.of(1l,2l));

    }
}