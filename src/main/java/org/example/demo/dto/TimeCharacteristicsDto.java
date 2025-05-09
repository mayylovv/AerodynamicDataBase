package org.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TimeCharacteristicsDto {

    int id;
    String cubesatName;
    String ntuName;
    double initialOrbit;
    double finalOrbit;
    double speed;
    double alfa;
    double gamma;
    double landTime;

}
