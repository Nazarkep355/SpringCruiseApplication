package com.example.SpringCruiseApplication.entity;

//import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cruise_requests")
@Getter
@Setter
public class CruiseRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "cruise")
    private Cruise cruise;
    @Column
    private String photo;
    @Column
    private Status status;
    @Column(name = "room_class")
    private RoomClass roomClass;

}
