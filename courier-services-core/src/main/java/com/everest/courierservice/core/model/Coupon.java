package com.everest.courierservice.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coupon {
    private String couponId;
    private int discountInPercentage;
    private int minDistanceInKm;
    private int maxDistanceInKm;
    private int minWeightInKg;
    private int maxWeightInKg;
}
