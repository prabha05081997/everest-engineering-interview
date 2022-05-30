package com.everest.courierservice.core.service;

import com.everest.courierservice.core.exception.ServiceException;
import com.everest.courierservice.core.manager.ConfigurationManager;
import com.everest.courierservice.core.model.Coupon;
import com.everest.courierservice.core.utils.CSVUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class CouponService {

    static Map<String, Coupon> couponMap;

    /**
     *
     * initializing the coupons in static block
     * It can be easily extended by adding new coupon without touching the existing coupons
     *
     */
    static {
        ConfigurationManager.getInstance().loadConfiguration();
        initializeCoupon();
    }

    /**
     *
     * This method will get the coupon csv file path from properties file and will read the csv file
     * and construct the coupon object for all coupons
     *
     * NOTE: No code change is required for adding new coupons. only need to add new row in csv file then
     * restart the application
     *
     */
    private static void initializeCoupon() {
        try {
            String couponFilePath = ConfigurationManager.getInstance().getCouponFilePath();
            if(StringUtils.isBlank(couponFilePath)) throw new ServiceException("coupon file path is empty");
            log.info("couponFilePath {}", couponFilePath);
            List<Coupon> couponList = CSVUtils.getCouponList(couponFilePath);
            couponMap = couponList.stream().collect(Collectors.toMap(Coupon::getCouponId, Function.identity()));
        } catch (Exception e) {
            log.error("error while loading coupons {}", e);
        }
    }

    public static Map<String, Coupon> getCouponMap() {
        return couponMap;
    }
}
