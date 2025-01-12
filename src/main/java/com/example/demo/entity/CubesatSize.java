package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Table(name = "cubesat_size")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CubesatSize {

    @Id
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "length")
    float length;
    @Column(name = "width")
    float width;
    @Column(name = "height")
    float height;
}
