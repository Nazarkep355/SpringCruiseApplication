package com.example.SpringCruiseApplication.filter;

import com.example.SpringCruiseApplication.entity.User;
import com.example.SpringCruiseApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
@Service
public class UpdateUserFilter extends GenericFilterBean {
    private UserService userService;

    public UpdateUserFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user= (User) request.getSession().getAttribute("user");
        if(user!=null){
            user = userService.getUserByEmail(user.getEmail());
            request.getSession().setAttribute("user",user);
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
