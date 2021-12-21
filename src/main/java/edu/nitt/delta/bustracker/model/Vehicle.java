package edu.nitt.delta.bustracker.model;

import lombok.Builder;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Builder
public class Vehicle {
    @Id
    private String id;
    
    private VehicleType type;

    private Boolean isOccupied;

    @Indexed(unique = true)
    private String vehicleNumber;
}
