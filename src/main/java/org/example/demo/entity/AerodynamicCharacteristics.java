package org.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Table(schema = "aero_database", name = "aerodynamic_characteristics")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AerodynamicCharacteristics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int cubesatSizeId;
    int ntuId;
    int orbitId;
    @Column(name = "force_x")
    double forceX;
    @Column(name = "moment_x")
    double momentX;
    @Column(name = "coefficient_x")
    double coefficientX;
    @Column(name = "force_y")
    double forceY;
    @Column(name = "moment_y")
    double momentY;
    @Column(name = "coefficient_y")
    double coefficientY;
    double velocityHead;

}

