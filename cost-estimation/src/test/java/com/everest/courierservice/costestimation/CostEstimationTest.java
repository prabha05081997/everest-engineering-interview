package com.everest.courierservice.costestimation;

import com.everest.courierservice.core.model.Coupon;
import com.everest.courierservice.core.model.PackageInfo;
import com.everest.courierservice.core.service.CouponService;
import com.everest.courierservice.costestimation.service.CostEstimationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@Slf4j
public class CostEstimationTest {

    Map<String, Coupon> couponMap = CouponService.getCouponMap();
    static List<PackageInfo> packageInfoList = new ArrayList<>();
    static List<PackageInfo> expectedCostEstimationPackageInfoList = new ArrayList<>();

    static {
        packageInfoList.add(new PackageInfo("PKG1", 5, 5, "OFR001"));
        packageInfoList.add(new PackageInfo("PKG2", 15, 5, "OFR002"));
        packageInfoList.add(new PackageInfo("PKG3", 10, 100, "OFR003"));

        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG1", 0, 175));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG2", 0, 275));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG3", 35, 665));
    }

    @Test
    public void testEligibilityForDiscount() {
        assertFalse(CostEstimationService.getInstance().checkIfPackageEligibleForDiscount(packageInfoList.get(0), couponMap));
        assertTrue(CostEstimationService.getInstance().checkIfPackageEligibleForDiscount(packageInfoList.get(2), couponMap));
    }

    @Test
    public void testCostEstimation(){
        int baseDeliveryCost = 100;
        List<PackageInfo> resultCostEstimationPackageInfoList = CostEstimationService.getInstance().findCostEstimationForCourierService(packageInfoList, baseDeliveryCost, packageInfoList.size(), couponMap);
        resultCostEstimationPackageInfoList = resultCostEstimationPackageInfoList.stream().map(expectedCostEstimationPackageInfo -> new PackageInfo(expectedCostEstimationPackageInfo.getPackageId(), expectedCostEstimationPackageInfo.getDiscount(),
                expectedCostEstimationPackageInfo.getDeliveryCost())).collect(Collectors.toList());
        assertEquals(expectedCostEstimationPackageInfoList, resultCostEstimationPackageInfoList);
    }
}
