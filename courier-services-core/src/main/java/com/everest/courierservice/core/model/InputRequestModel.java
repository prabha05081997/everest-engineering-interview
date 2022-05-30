package com.everest.courierservice.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InputRequestModel {
    private List<PackageInfo> packageInfoList;
    private int baseDeliveryCost;
    private int noOfPackages;
    private Vehicle vehicle;
    private int noOfVehicles;

    public InputRequestModel(List<PackageInfo> packageInfoList, int baseDeliveryCost, int noOfPackages) {
        this.packageInfoList = packageInfoList;
        this.baseDeliveryCost = baseDeliveryCost;
        this.noOfPackages = noOfPackages;
    }
}
