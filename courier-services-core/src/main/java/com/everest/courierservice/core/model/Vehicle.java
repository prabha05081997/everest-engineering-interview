package com.everest.courierservice.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vehicle {
    private double maxSpeedInKmPerHr;
    private int maxCarriableWeightInKg;
}
