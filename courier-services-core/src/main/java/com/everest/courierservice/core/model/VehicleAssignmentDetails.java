package com.everest.courierservice.core.model;

import lombok.Data;

import java.util.List;

@Data
public class VehicleAssignmentDetails {
    private int vehicleNo;
    private int maxWeightAssinableToVehicle;
    private List<PackageInfo> packageInfoList;
    private double totalDeliveryTime;
    private double currentTime;

    public VehicleAssignmentDetails(int vehicleNo) {
        this.vehicleNo = vehicleNo;
    }
}
