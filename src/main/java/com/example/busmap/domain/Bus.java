package com.example.busmap.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @Column
    private String busNum;

    @Column
    private Long routeId;

    @OneToMany(mappedBy = "bus")
    private List<Info> info=new ArrayList<>();

    public Bus(String busNum, Long routeId){
        this.busNum=busNum;
        this.routeId=routeId;
    }
}
