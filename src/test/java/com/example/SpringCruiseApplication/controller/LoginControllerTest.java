package com.example.SpringCruiseApplication.controller;

import com.example.SpringCruiseApplication.entity.User;
import com.example.SpringCruiseApplication.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.ModelResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;

class LoginControllerTest {
    @Mock
    private UserService userService;

    private LoginController controller;

    private MockMvc mockMvc;

    private List<User> users;

    public LoginControllerTest() {
        MockitoAnnotations.initMocks(this);
        controller = new LoginController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        users =new ArrayList<>();
        for(int i=0;i<3;i++){
            User user= new User();
            user.setId(i+1l);
            user.setEmail(i+"email@gmail.com");
            users.add(user);
        }
    }

    @Test
    void loginPage() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get("/login")
                        .sessionAttr("user",users.get(0)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.view().name("login.html"));
    }

    @Test
    void homePage() {
    }

    @Test
    void loginSuccess() throws Exception {
        String email = "1email@gmail.com";
        when(userService.getUserByEmail(email)).thenReturn(users.stream()
                .filter(a->a.getEmail().equals(email)).findFirst().orElseThrow());
        mockMvc
                .perform(MockMvcRequestBuilders.post("/loginSuccess")
                        .param("username",email))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.request().sessionAttribute("user",users.get(1)))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));
    }

    @Test
    void logOut() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.post("/logout")
                        .sessionAttr("user",users.get(0)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));
    }
}