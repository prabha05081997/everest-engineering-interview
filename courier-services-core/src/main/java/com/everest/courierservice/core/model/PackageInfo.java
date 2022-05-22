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

    // vehicle parameters
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

    public PackageInfo(String packageId, int discount, int deliveryCost) {
        this.packageId = packageId;
        this.discount = discount;
        this.deliveryCost = deliveryCost;
    }

    public PackageInfo(String packageId, int discount, int deliveryCost, double estimatedCostDeliveryTimeInHrs) {
        this.packageId = packageId;
        this.discount = discount;
        this.deliveryCost = deliveryCost;
        this.estimatedCostDeliveryTimeInHrs = estimatedCostDeliveryTimeInHrs;
    }

    public PackageInfo(String packageId, int packageWeightInKg, int packageDistanceInKm, String offerCode, boolean isVehicleAssigned) {
        this.packageId = packageId;
        this.packageWeightInKg = packageWeightInKg;
        this.packageDistanceInKm = packageDistanceInKm;
        this.offerCode = offerCode;
        this.isVehicleAssigned = isVehicleAssigned;
    }
}
