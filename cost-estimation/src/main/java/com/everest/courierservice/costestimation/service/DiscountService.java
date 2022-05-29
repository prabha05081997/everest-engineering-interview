package com.everest.courierservice.costestimation.service;

import com.everest.courierservice.core.model.Coupon;
import com.everest.courierservice.core.model.PackageInfo;

import java.util.Map;

public interface DiscountService {

    boolean isPackageEligibleForDiscount(PackageInfo packageInfo, Map<String, Coupon> couponMap);

}
