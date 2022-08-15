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
    @Email(message = "error.emailMust")
    private String email;
    @Column
    @Pattern(regexp = "[(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)]{8,20}",message = "error.mustContain")
    private String password;
    @Column
    @NotBlank(message = "error.enterWrongFormat")
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
