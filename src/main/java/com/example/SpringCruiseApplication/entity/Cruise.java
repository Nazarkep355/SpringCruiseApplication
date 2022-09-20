package com.example.SpringCruiseApplication.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.scheduling.annotation.Scheduled;

//import jakarta.persistence.*;
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
    @OrderColumn
    private List<Date> dates;
    @ManyToOne
    @JoinColumn(name = "route")
    private Route route;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ship",columnDefinition = "bigint")
    private Ship ship;
    @ManyToMany(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    private List<Staff> staff;
    @Column(name = "econom_tickets")
    private int economTickets;
    @Column(name = "middle_tickets")
    private int middleTickets;
    @Column(name = "premium_tickets")
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
            return ship.getEconomTotalPlaces() - economTickets;
        if (roomClass == RoomClass.MIDDLE)
            return ship.getMiddleTotalPlaces() - middleTickets;
        if (roomClass == RoomClass.PREMIUM)
            return ship.getPremiumTotalPlaces() - premiumTickets;
        else return ship.getEconomTotalPlaces() - economTickets;
    }

    public void addOneTicket(RoomClass roomClass) {
        if (roomClass == RoomClass.ECONOM) {
            economTickets++;
        }
        if (roomClass == RoomClass.MIDDLE) {
            middleTickets++;
        } else {
            premiumTickets++;
        }
    }


}
