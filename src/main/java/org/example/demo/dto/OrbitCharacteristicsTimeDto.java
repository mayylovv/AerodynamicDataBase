package org.example.demo.dto;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class OrbitCharacteristicsTimeDto {

    double startHeight;
    double endHeight;
    double speed;
    double theta0;
    double alfa;

}
