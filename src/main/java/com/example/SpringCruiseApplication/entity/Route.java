package com.example.SpringCruiseApplication.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "routes")
@Getter
@Setter
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ElementCollection
    private List<Integer> delays;
    @Column
    @ManyToMany
    @OrderColumn
    private List<Port> ports;
    public String portsString(){
        return ports.stream().map(Port::getCity)
                .collect(Collectors.joining("_"));
    }
    public Integer delaysSum(){
        return delays.stream().map(a->a+1).mapToInt(a->a).sum();
    }
    public String firstLast(){
        List<String> cities = ports.stream().map(Port::getCity).toList();
        return cities.get(0)+" - "+cities.get(cities.size()-1);
    }
}
