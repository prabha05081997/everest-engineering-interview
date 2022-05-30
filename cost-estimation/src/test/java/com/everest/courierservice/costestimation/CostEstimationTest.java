package com.everest.courierservice.costestimation;

import com.everest.courierservice.core.model.Coupon;
import com.everest.courierservice.core.model.PackageInfo;
import com.everest.courierservice.core.service.CouponService;
import com.everest.courierservice.costestimation.service.CostEstimationServiceImpl;
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

    @Test
    public void testCostEstimation(){
        int baseDeliveryCost = 100;
        List<PackageInfo> packageInfoList = new ArrayList<>();
        packageInfoList.add(new PackageInfo("PKG1", 5, 5, "OFR001"));
        packageInfoList.add(new PackageInfo("PKG2", 15, 5, "OFR002"));
        packageInfoList.add(new PackageInfo("PKG3", 10, 100, "OFR003"));

        List<PackageInfo> expectedCostEstimationPackageInfoList = new ArrayList<>();
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG1", 0, 175));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG2", 0, 275));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG3", 35, 665));

        List<PackageInfo> resultCostEstimationPackageInfoList = CostEstimationServiceImpl.getInstance().findCostEstimation(packageInfoList, baseDeliveryCost, packageInfoList.size(), couponMap);
        resultCostEstimationPackageInfoList = resultCostEstimationPackageInfoList.stream().map(expectedCostEstimationPackageInfo -> new PackageInfo(expectedCostEstimationPackageInfo.getPackageId(), expectedCostEstimationPackageInfo.getDiscount(),
                expectedCostEstimationPackageInfo.getDeliveryCost())).collect(Collectors.toList());
        assertEquals(expectedCostEstimationPackageInfoList, resultCostEstimationPackageInfoList);

        packageInfoList = new ArrayList<>();
        packageInfoList.add(new PackageInfo("PKG1", 50, 30, "OFR001"));
        packageInfoList.add(new PackageInfo("PKG2", 75, 125, "OFR008"));
        packageInfoList.add(new PackageInfo("PKG3", 175, 100, "OFR003"));
        packageInfoList.add(new PackageInfo("PKG4", 110, 60, "OFR002"));
        packageInfoList.add(new PackageInfo("PKG5", 155, 95, "NA"));

        expectedCostEstimationPackageInfoList = new ArrayList<>();
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG1", 0, 750));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG2", 0, 1475));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG3", 0, 2350));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG4", 105, 1395));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG5", 0, 2125));

        resultCostEstimationPackageInfoList = CostEstimationServiceImpl.getInstance().findCostEstimation(packageInfoList, baseDeliveryCost, packageInfoList.size(), couponMap);
        resultCostEstimationPackageInfoList = resultCostEstimationPackageInfoList.stream().map(expectedCostEstimationPackageInfo -> new PackageInfo(expectedCostEstimationPackageInfo.getPackageId(), expectedCostEstimationPackageInfo.getDiscount(),
                expectedCostEstimationPackageInfo.getDeliveryCost())).collect(Collectors.toList());
        assertEquals(expectedCostEstimationPackageInfoList, resultCostEstimationPackageInfoList);
    }
}
