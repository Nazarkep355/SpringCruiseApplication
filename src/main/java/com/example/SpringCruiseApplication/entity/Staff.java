package com.example.SpringCruiseApplication.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "staff")
@Getter
@Setter
@ToString
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column
    private String Position;
    @Column
    private boolean enable;
}
