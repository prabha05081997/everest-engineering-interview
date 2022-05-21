package com.everest.courierservice.costestimation.service;

import com.everest.courierservice.core.model.Coupon;
import lombok.extern.slf4j.Slf4j;

import com.everest.courierservice.core.model.PackageInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CostEstimationService {

    private static CostEstimationService costEstimationService;

    private CostEstimationService() {

    }

    public static CostEstimationService getInstance() {
        if (costEstimationService == null) {
            synchronized (CostEstimationService.class) {
                if (costEstimationService == null) {
                    log.info("creating an instance of cost estimation service");
                    costEstimationService = new CostEstimationService();
                }
            }
        }
        return costEstimationService;
    }

    /**
     *
     * This method is used for estimating the cost for each package
     *
     * @param packageInfoList - List of Object to track package info
     * @param baseDeliveryCost - base delivery cost
     * @param noOfPackages - total no of packages
     * @param couponMap - coupon list
     * @return - List of Package info object with estimation cost updated
     *
     */
    public List<PackageInfo> findCostEstimationForCourierService(List<PackageInfo> packageInfoList, int baseDeliveryCost, int noOfPackages, Map<String, Coupon> couponMap) {
        log.info("in findCostEstimationForCourierService packageInfoList {} baseDeliveryCost {} noOfPackages {} couponMap {}",
                packageInfoList, baseDeliveryCost, noOfPackages, couponMap);
        packageInfoList = packageInfoList.stream().map(packageInfo -> {
            int pkgDistanceInKm = packageInfo.getPackageDistanceInKm();
            int pkgWeightInKg = packageInfo.getPackageWeightInKg();

            int deliveryCost = baseDeliveryCost + (pkgWeightInKg * 10) + (pkgDistanceInKm * 5);
            boolean isEligibleForDiscount = checkIfPackageEligibleForDiscount(packageInfo, couponMap);

            int discount = 0;
            if (isEligibleForDiscount) {
                Coupon coupon = couponMap.get(packageInfo.getOfferCode());
                int discountInPercentage = coupon.getDiscountInPercentage();
                discount = (deliveryCost * discountInPercentage) / 100;
                deliveryCost -= discount;
            }
            packageInfo.setDiscount(discount);
            packageInfo.setDeliveryCost(deliveryCost);
            return packageInfo;
        }).collect(Collectors.toList());

        return packageInfoList;
    }

    /**
     *
     * This method will check for the eligibility for discount for the given coupon code
     *
     * @param packageInfo - package info
     * @param couponMap - coupon list
     * @return - boolean value to indicate the elgibility for discount
     *
     */
    private boolean checkIfPackageEligibleForDiscount(PackageInfo packageInfo, Map<String, Coupon> couponMap) {
        String offerCode = packageInfo.getOfferCode();
        Coupon coupon = couponMap.getOrDefault(offerCode, null);
        if(coupon == null) return Boolean.FALSE;

        int minWeightInKg = coupon.getMinWeightInKg();
        int maxWeightInKg = coupon.getMaxWeightInKg();
        int minDistanceInKm = coupon.getMinDistanceInKm();
        int maxDistanceInKm = coupon.getMaxDistanceInKm();

        int pkgDistanceInKm = packageInfo.getPackageDistanceInKm();
        int pkgWeightInKg = packageInfo.getPackageWeightInKg();

        if(pkgWeightInKg <= maxWeightInKg && pkgWeightInKg >= minWeightInKg && pkgDistanceInKm <= maxDistanceInKm
            && pkgDistanceInKm >= minDistanceInKm) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
