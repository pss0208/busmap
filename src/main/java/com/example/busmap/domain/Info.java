package com.example.busmap.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_seq")
    private Long id;

    @Column
    private Long busId;

    @Column
    private double gpsX;

    @Column
    private double gpsY;

    @Column
    private int isFull;

    @Column
    private int isrun;

    @Column
    private String numberPlate;

    @ManyToOne
    @JoinColumn(name = "routeId")
    private Bus bus;

    public Info(double gpsX, double gpsY){
        this.gpsX=gpsX;
        this.gpsY=gpsY;
    }

    public Info(Long busId, double gpsX, double gpsY, int isFull, int isrun, String numberPlate){
        this.busId=busId;
        this.gpsX=gpsX;
        this.gpsY=gpsY;
        this.isFull=isFull;
        this.isrun=isrun;
        this.numberPlate=numberPlate;
    }
}
