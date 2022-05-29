package com.everest.courierservice.costestimation.service;

import com.everest.courierservice.core.model.Coupon;
import com.everest.courierservice.core.model.PackageInfo;

import java.util.List;
import java.util.Map;

public interface CostEstimationService {

    List<PackageInfo> findCostEstimation(List<PackageInfo> packageInfoList, int baseDeliveryCost, int noOfPackages, Map<String, Coupon> couponMap);

}
