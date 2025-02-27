package org.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FlightCharacteristics {

    double heightKm;
    double alfa;
    double speed;
}
