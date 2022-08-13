package com.example.SpringCruiseApplication.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    @Email
    private String email;
    @Column
    @Pattern(regexp = "[(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)]{8,20}")
    private String password;
    @Column
    @NotBlank
    private String name;
    @Column
    private String role;
    @Column
    private int money;
    @Column
    private boolean enabled;
    public boolean isAdmin(){
        return (role.equals("ROLE_ADMIN"));
    }


}
