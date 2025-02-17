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
@Table(schema = "aero_database", name = "characteristics_ntu")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CharacteristicsNtu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String typeForm;
    double radius;
    double length;
    double thickness;
    int materialId;

}
