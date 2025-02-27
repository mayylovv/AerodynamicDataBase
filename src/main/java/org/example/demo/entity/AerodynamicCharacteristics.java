package org.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(schema = "aero_database", name = "aerodynamic_characteristics")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AerodynamicCharacteristics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    double alfa;
    int cubesatSizeId;
    int ntuId;
    double forceX;
    double momentX;
    double coefficientX;
    double forceY;
    double momentY;
    double coefficientY;
    double velocityHead;
    LocalDateTime dateOfCalculation;
    double density;
    double speed;
    double minSpeed;

}

