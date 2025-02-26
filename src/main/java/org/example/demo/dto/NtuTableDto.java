package org.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
public class NtuTableDto {
    int id;
    String name;
    String form;
    double radius;
    double length;
    double thickness;
    String material;
    double density;
}
