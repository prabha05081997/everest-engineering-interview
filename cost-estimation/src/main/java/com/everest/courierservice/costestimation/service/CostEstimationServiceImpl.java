package com.everest.courierservice.costestimation.service;

import com.everest.courierservice.core.model.Coupon;
import com.everest.courierservice.core.model.PackageInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CostEstimationServiceImpl implements CostEstimationService {

    private static CostEstimationServiceImpl costEstimationServiceImpl;

    private CostEstimationServiceImpl() {

    }

    public static CostEstimationServiceImpl getInstance() {
        if (costEstimationServiceImpl == null) {
            synchronized (CostEstimationServiceImpl.class) {
                if (costEstimationServiceImpl == null) {
                    log.info("creating an instance of cost estimation service");
                    costEstimationServiceImpl = new CostEstimationServiceImpl();
                }
            }
        }
        return costEstimationServiceImpl;
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
    @Override
    public List<PackageInfo> findCostEstimation(List<PackageInfo> packageInfoList, int baseDeliveryCost, int noOfPackages, Map<String, Coupon> couponMap) {
        log.info("in findCostEstimationForCourierService packageInfoList {} baseDeliveryCost {} noOfPackages {} couponMap {}",
                packageInfoList, baseDeliveryCost, noOfPackages, couponMap);
        packageInfoList = packageInfoList.stream().map(packageInfo -> {
            int pkgDistanceInKm = packageInfo.getPackageDistanceInKm();
            int pkgWeightInKg = packageInfo.getPackageWeightInKg();

            int deliveryCost = baseDeliveryCost + (pkgWeightInKg * 10) + (pkgDistanceInKm * 5);
            boolean isEligibleForDiscount = DiscountServiceImpl.getInstance().isPackageEligibleForDiscount(packageInfo, couponMap);

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
}
