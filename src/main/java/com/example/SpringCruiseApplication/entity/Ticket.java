package com.example.SpringCruiseApplication.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;
    @ManyToOne
    @JoinColumn(name = "cruise")
    private Cruise cruise;
    @Column(name = "room_class")
    private RoomClass roomClass;
    @Column(name="purchase_date")
    private Date purchaseDate;
}
