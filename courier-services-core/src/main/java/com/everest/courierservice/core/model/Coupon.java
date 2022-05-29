package com.everest.courierservice.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    private String couponId;
    private int discountInPercentage;
    private int minDistanceInKm;
    private int maxDistanceInKm;
    private int minWeightInKg;
    private int maxWeightInKg;
}
