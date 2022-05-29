package com.everest.courierservice.costestimation.service;

import com.everest.courierservice.core.model.Coupon;
import com.everest.courierservice.core.model.PackageInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class DiscountServiceImpl implements DiscountService{

    private static DiscountServiceImpl discountServiceImpl;

    private DiscountServiceImpl() {

    }

    public static DiscountServiceImpl getInstance() {
        if (discountServiceImpl == null) {
            synchronized (DiscountServiceImpl.class) {
                if (discountServiceImpl == null) {
                    log.info("creating an instance of discount service");
                    discountServiceImpl = new DiscountServiceImpl();
                }
            }
        }
        return discountServiceImpl;
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
    @Override
    public boolean isPackageEligibleForDiscount(PackageInfo packageInfo, Map<String, Coupon> couponMap) {
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
