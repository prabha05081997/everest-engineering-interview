package com.everest.courierservice.core.service;

import com.everest.courierservice.core.model.Coupon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CouponService {

    static Map<String, Coupon> couponMap;

    static {
        List<Coupon> couponList = new ArrayList<>();
        couponList.add(new Coupon("OFR001", 10, 0, 200, 70, 200));
        couponList.add(new Coupon("OFR002", 7, 50, 150, 100, 250));
        couponList.add(new Coupon("OFR003", 5, 50, 250, 10, 100));

        couponMap = couponList.stream().collect(Collectors.toMap(Coupon::getCouponId, Function.identity()));
    }

    public static Map<String, Coupon> getCouponMap() {
        return couponMap;
    }
}
