package org.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AerodynamicCharacteristicsDto {

    int id;
    String cubesatName;
    String ntuName;
    double height;
    double speed;
    double alfa;
    double forceX;
    double coefficientX;
    double momentX;
    double forceY;
    double coefficientY;
    double momentY;
    double velocityHead;

}
