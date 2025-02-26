package org.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    String name;
    @ManyToOne
    @JoinColumn(name = "form_id")
    private FormNtu form;
    double radius;
    double length;
    double thickness;
    @ManyToOne
    @JoinColumn(name = "material_id")
    private MaterialInfoEntity material;

}
