package com.everest.courierservice.costestimation;

import com.everest.courierservice.core.model.Coupon;
import com.everest.courierservice.core.model.PackageInfo;
import com.everest.courierservice.core.service.CouponService;
import com.everest.courierservice.costestimation.service.DiscountServiceImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DiscountEligibilityTest {

    Map<String, Coupon> couponMap = CouponService.getCouponMap();
    static List<PackageInfo> packageInfoList = new ArrayList<>();
    static {
        packageInfoList.add(new PackageInfo("PKG1", 5, 5, "OFR001"));
        packageInfoList.add(new PackageInfo("PKG2", 15, 5, "OFR002"));
        packageInfoList.add(new PackageInfo("PKG3", 10, 100, "OFR003"));
    }

    @Test
    public void testEligibilityForDiscount() {
        assertFalse(DiscountServiceImpl.getInstance().isPackageEligibleForDiscount(packageInfoList.get(0), couponMap));
        assertTrue(DiscountServiceImpl.getInstance().isPackageEligibleForDiscount(packageInfoList.get(2), couponMap));
    }

}
