package org.example.demo.dto;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrbitCharacteristicsDto {

    double heightKm;
    double alfa;
    double speed;
}
