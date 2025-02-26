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
@Table(schema = "aero_database", name = "cubesat_size")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CubesatSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    double length;
    double width;
    double height;
    double xMass;
    double yMass;
    double zMass;
    double mass;
}
