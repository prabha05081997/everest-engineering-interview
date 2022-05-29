package com.everest.courierservice.deliverytimeestimation.service;

import com.everest.courierservice.core.model.PackageInfo;
import com.everest.courierservice.core.model.Vehicle;

import java.util.List;

public interface DeliveryTimeEstimationService {

    List<PackageInfo> findDeliveryTimeEstimation(List<PackageInfo> packageInfoList, int noOfVehicles, Vehicle vehicle) throws Exception;

}
