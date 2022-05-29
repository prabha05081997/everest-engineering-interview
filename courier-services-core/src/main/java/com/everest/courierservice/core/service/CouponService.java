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

    private static void initializeCoupon() {
        try {
            ConfigurationManager.getInstance().getCouponFilePath();
            String couponFilePath = ConfigurationManager.getInstance().getCouponFilePath();
            if(StringUtils.isBlank(couponFilePath)) throw new ServiceException("coupon file path is empty");
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
