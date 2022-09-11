package com.example.SpringCruiseApplication.config;

import com.example.SpringCruiseApplication.filter.UpdateUserFilter;
import com.example.SpringCruiseApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    @Autowired
    private DataSource dataSource;

//    @Bean
//    public UserDetailsService userDetailsService(HttpSession session) {
//        com.example.SpringCruiseApplication.entity.User sessionUser = (com.example.SpringCruiseApplication.entity.User) session.getAttribute("user");
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username(sessionUser.getEmail())
//                        .password(sessionUser.getPassword())
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }


    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .authorizeRequests()
                .antMatchers("/home", "/user/**",
                        "tickets/**","/ships/all","/ports/all","/requests/send","/tickets",
                        "/user/changeBalance"
                )
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/ships/add", "/ports/add",
                        "/staff/**","routes/**","/admin/response",
                        "/cruises/admin/add","/requests/admin/response","/requests/admin/*","/cruises/admin/plan")
                .hasRole("ADMIN")
                .antMatchers("/register","/cruises/*")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/home")
                .permitAll()
                .successForwardUrl("/loginSuccess")
                .permitAll()
//                .failureForwardUrl("/loginFail")
//                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .permitAll()
                .and()
                .addFilterAfter(new UpdateUserFilter(userService), BasicAuthenticationFilter.class);

    }
    @Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select email,password,enabled "
                        + "from users "
                        + "where email = ?")
                .authoritiesByUsernameQuery("select email, role "
                        + "from users where email = ?");
    }

    @Bean
    public PasswordEncoder encoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}