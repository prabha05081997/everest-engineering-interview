package com.everest.courierservice.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PackageInfo {
    private String packageId;
    private int packageWeightInKg;
    private int packageDistanceInKm;
    private String offerCode;

    private boolean isVehicleAssigned;
    private int vehicleNoAssigned;

    // output parameters
    private int discount;
    private int deliveryCost;
    private double estimatedCostDeliveryTimeInHrs;

    public PackageInfo(String packageId, int packageWeightInKg, int packageDistanceInKm, String offerCode) {
        this.packageId = packageId;
        this.packageWeightInKg = packageWeightInKg;
        this.packageDistanceInKm = packageDistanceInKm;
        this.offerCode = offerCode;
    }
}
