package org.example.demo.entity;

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
@Table(schema = "aero_database", name = "orbit_characteristics_time")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrbitCharacteristicsTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    double initialOrbit;
    double finalOrbit;
    double speed;
    double alfa;
    double gamma;

}
