package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "aerodynamic_characteristics")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AerodynamicCharacteristics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "alfa")
    double alfa;

    @Column(name = "heightKm")
    int heightKm;

    @Column(name = "radius")
    double radius;

    @Column(name = "length")
    double length;

    @Column(name = "forceX")
    double forceX;

    @Column(name = "momentX")
    double momentX;

    @Column(name = "coefficientX")
    double coefficientX;

    @Column(name = "forceY")
    double forceY;

    @Column(name = "momentY")
    double momentY;

    @Column(name = "coefficientY")
    double coefficientY;

    @Column(name = "date_of_calculation")
    Timestamp dateOfCalculation;

}

