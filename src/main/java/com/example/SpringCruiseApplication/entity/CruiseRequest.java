package com.example.SpringCruiseApplication.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cruiseRequests")
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
    @Column
    private RoomClass roomClass;

}
