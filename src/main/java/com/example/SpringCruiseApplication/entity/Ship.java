package com.example.SpringCruiseApplication.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "ships")
@Getter
@Setter
@ToString
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column
    private String name;
    @Column
    private boolean enable;
    @Column
    private int economCost;
    @Column
    private int economTotalPlaces;
    @Column
    private int middleCost;
    @Column
    private int middleTotalPlaces;
    @Column
    private int premiumCost;
    @Column
    private int premiumTotalPlaces;
    @Column
    private int totalSeats;
}
