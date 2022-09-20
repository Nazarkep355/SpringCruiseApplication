package com.example.SpringCruiseApplication.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@Entity
@Table(name = "ships")
@Getter
@Setter
@ToString
public class Ship implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column
    private String name;
    @Column
    private boolean enable;
    @Column
    private Integer economCost;
    @Column
    private Integer economTotalPlaces;
    @Column
    private Integer middleCost;
    @Column
    private Integer middleTotalPlaces;
    @Column
    private Integer premiumCost;
    @Column
    private Integer premiumTotalPlaces;
    @Column
    private Integer totalSeats;
    public Integer getId(){
        return this.id;
    }
}
