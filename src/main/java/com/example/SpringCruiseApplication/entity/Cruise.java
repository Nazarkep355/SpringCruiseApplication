package com.example.SpringCruiseApplication.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cruises")
@Getter
@Setter
public class Cruise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    @ElementCollection
    private List<Date> dates;
    @OneToMany
    private List<Port> ports;
    @ManyToOne
    @JoinColumn(name = "route")
    private Route route;
    @ManyToOne
    @JoinColumn(name = "ship")
    private Ship ship;
    @OneToMany
    private List<Staff> staff;
    @Column
    private int economTickets;
    @Column
    private int middleTickets;
    @Column
    private int premiumTickets;

    public int getCostByClass(RoomClass roomClass) {
        if (roomClass == RoomClass.ECONOM)
            return ship.getEconomCost();
        if (roomClass == RoomClass.MIDDLE)
            return ship.getMiddleCost();
        if (roomClass == RoomClass.PREMIUM)
            return ship.getPremiumCost();
        else return ship.getEconomCost();
    }

    public int getFreePlacesByClass(RoomClass roomClass) {
        if (roomClass == RoomClass.ECONOM)
            return ship.getEconomTotalPlaces()-economTickets;
        if (roomClass == RoomClass.MIDDLE)
            return ship.getMiddleTotalPlaces()-middleTickets;
        if (roomClass == RoomClass.PREMIUM)
            return ship.getPremiumTotalPlaces()-premiumTickets;
        else return ship.getEconomTotalPlaces()-economTickets;
    }


}