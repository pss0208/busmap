package com.example.busmap.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
//@AllArgsConstructor
public class Info {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column
//    private Long id;
    @Id
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
    @JoinColumn(name = "BusSeq")
    private Bus bus;

    public Info(double gpsX, double gpsY){
        this.gpsX=gpsX;
        this.gpsY=gpsY;
    }

    public Info(Long busId, double gpsX, double gpsY, int isFull, int isrun, String numberPlate, Bus bus){
        this.busId=busId;
        this.gpsX=gpsX;
        this.gpsY=gpsY;
        this.isFull=isFull;
        this.isrun=isrun;
        this.numberPlate=numberPlate;
        this.bus=bus;
    }
}
