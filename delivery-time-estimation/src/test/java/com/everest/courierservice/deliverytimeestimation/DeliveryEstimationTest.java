package com.everest.courierservice.deliverytimeestimation;

import com.everest.courierservice.core.model.Coupon;
import com.everest.courierservice.core.model.PackageInfo;
import com.everest.courierservice.core.model.Vehicle;
import com.everest.courierservice.core.model.VehicleAssignmentDetails;
import com.everest.courierservice.core.service.CouponService;
import com.everest.courierservice.costestimation.service.CostEstimationService;
import com.everest.courierservice.deliverytimeestimation.service.DeliveryTimeEstimationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@Slf4j
public class DeliveryEstimationTest {

    Map<String, Coupon> couponMap = CouponService.getCouponMap();

    @Test
    public void testVehicleAssignmentDetails() {
        int noOfVehicles = 2;
        List<VehicleAssignmentDetails> expectedVehicleAssignmentDetailsList = new ArrayList<>();
        expectedVehicleAssignmentDetailsList.add(new VehicleAssignmentDetails(1));
        expectedVehicleAssignmentDetailsList.add(new VehicleAssignmentDetails(2));

        List<VehicleAssignmentDetails> resultVehicleAssignmentDetailsList = DeliveryTimeEstimationService.getInstance().getVehicleAssignmentDetails(noOfVehicles);
        assertEquals(expectedVehicleAssignmentDetailsList, resultVehicleAssignmentDetailsList);
    }

    @Test
    public void testDeliveryEstimation() throws Exception {
        int noOfVehicles = 2, baseDeliveryCost = 100;
        Vehicle vehicle = new Vehicle(70, 200);
        List<PackageInfo> expectedCostEstimationPackageInfoList = new ArrayList<>();
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG1", 0, 750, 3.98));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG2", 0, 1475, 1.78));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG3", 0, 2350, 1.42));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG4", 105, 1395, 0.85));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG5", 0, 2125, 4.19));

        List<PackageInfo> packageInfoList = new ArrayList<>();
        packageInfoList.add(new PackageInfo("PKG1", 50, 30, "OFR001"));
        packageInfoList.add(new PackageInfo("PKG2", 75, 125, "OFR008"));
        packageInfoList.add(new PackageInfo("PKG3", 175, 100, "OFR003"));
        packageInfoList.add(new PackageInfo("PKG4", 110, 60, "OFR002"));
        packageInfoList.add(new PackageInfo("PKG5", 155, 95, "NA"));

        List<PackageInfo> packageInfoResultList = CostEstimationService.getInstance().findCostEstimationForCourierService(packageInfoList, baseDeliveryCost, packageInfoList.size(), couponMap);
        List<PackageInfo> resultCostEstimationPackageInfoList = DeliveryTimeEstimationService.getInstance().findDeliveryTimeEstimationForCourierService(packageInfoResultList, noOfVehicles, vehicle);
        resultCostEstimationPackageInfoList = resultCostEstimationPackageInfoList.stream().map(expectedCostEstimationPackageInfo -> new PackageInfo(expectedCostEstimationPackageInfo.getPackageId(), expectedCostEstimationPackageInfo.getDiscount(),
                expectedCostEstimationPackageInfo.getDeliveryCost(), expectedCostEstimationPackageInfo.getEstimatedCostDeliveryTimeInHrs())).collect(Collectors.toList());
        assertEquals(expectedCostEstimationPackageInfoList, resultCostEstimationPackageInfoList);
    }

    @Test
    public void testAssignVehicles() throws Exception {
        int baseDeliveryCost = 100;
        Vehicle vehicle = new Vehicle(70, 200);
        List<PackageInfo> expectedCostEstimationPackageInfoList = new ArrayList<>();
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG1", 0, 750, 3.98));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG2", 0, 1475, 1.78));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG3", 0, 2350, 1.42));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG4", 105, 1395, 0.85));
        expectedCostEstimationPackageInfoList.add(new PackageInfo("PKG5", 0, 2125, 4.19));

        List<PackageInfo> packageInfoList = new ArrayList<>();
        packageInfoList.add(new PackageInfo("PKG1", 50, 30, "OFR001"));
        packageInfoList.add(new PackageInfo("PKG2", 75, 125, "OFR008"));
        packageInfoList.add(new PackageInfo("PKG3", 175, 100, "OFR003"));
        packageInfoList.add(new PackageInfo("PKG4", 110, 60, "OFR002"));
        packageInfoList.add(new PackageInfo("PKG5", 155, 95, "NA"));

        List<VehicleAssignmentDetails> expectedVehicleAssignmentDetailsList = new ArrayList<>();
        expectedVehicleAssignmentDetailsList.add(new VehicleAssignmentDetails(1));
        expectedVehicleAssignmentDetailsList.add(new VehicleAssignmentDetails(2));

        List<PackageInfo> packageInfoResultList = CostEstimationService.getInstance().findCostEstimationForCourierService(packageInfoList, baseDeliveryCost, packageInfoList.size(), couponMap);
        List<PackageInfo> resultCostEstimationPackageInfoList = DeliveryTimeEstimationService.getInstance().assignVehiclesToPackages(expectedVehicleAssignmentDetailsList, packageInfoResultList, vehicle);
        resultCostEstimationPackageInfoList = resultCostEstimationPackageInfoList.stream().map(expectedCostEstimationPackageInfo -> new PackageInfo(expectedCostEstimationPackageInfo.getPackageId(), expectedCostEstimationPackageInfo.getDiscount(),
                expectedCostEstimationPackageInfo.getDeliveryCost(), expectedCostEstimationPackageInfo.getEstimatedCostDeliveryTimeInHrs())).collect(Collectors.toList());
        assertEquals(expectedCostEstimationPackageInfoList, resultCostEstimationPackageInfoList);
    }

    @Test
    public void testAssignVehicle() throws Exception {
        int baseDeliveryCost = 100;
        Vehicle vehicle = new Vehicle(70, 200);
        List<PackageInfo> expectedVehicleCostEstimationPackageInfoList = new ArrayList<>();
        expectedVehicleCostEstimationPackageInfoList.add(new PackageInfo("PKG1", 0, 750, 0));
        expectedVehicleCostEstimationPackageInfoList.add(new PackageInfo("PKG2", 0, 1475, 1.78));
        expectedVehicleCostEstimationPackageInfoList.add(new PackageInfo("PKG3", 0, 2350, 0));
        expectedVehicleCostEstimationPackageInfoList.add(new PackageInfo("PKG4", 105, 1395, 0.85));
        expectedVehicleCostEstimationPackageInfoList.add(new PackageInfo("PKG5", 0, 2125, 0));

        List<PackageInfo> packageInfoList = new ArrayList<>();
        packageInfoList.add(new PackageInfo("PKG1", 50, 30, "OFR001"));
        packageInfoList.add(new PackageInfo("PKG2", 75, 125, "OFR008"));
        packageInfoList.add(new PackageInfo("PKG3", 175, 100, "OFR003"));
        packageInfoList.add(new PackageInfo("PKG4", 110, 60, "OFR002"));
        packageInfoList.add(new PackageInfo("PKG5", 155, 95, "NA"));

        List<VehicleAssignmentDetails> expectedVehicleAssignmentDetailsList = new ArrayList<>();
        expectedVehicleAssignmentDetailsList.add(new VehicleAssignmentDetails(1));
        expectedVehicleAssignmentDetailsList.add(new VehicleAssignmentDetails(2));

        List<PackageInfo> packageInfoResultList = CostEstimationService.getInstance().findCostEstimationForCourierService(packageInfoList, baseDeliveryCost, packageInfoList.size(), couponMap);
        List<PackageInfo> resultCostEstimationPackageInfoList = DeliveryTimeEstimationService.getInstance().assignVehicleToPackages(expectedVehicleAssignmentDetailsList.get(0), packageInfoResultList, vehicle);
        resultCostEstimationPackageInfoList = resultCostEstimationPackageInfoList.stream().map(expectedCostEstimationPackageInfo -> new PackageInfo(expectedCostEstimationPackageInfo.getPackageId(), expectedCostEstimationPackageInfo.getDiscount(),
                expectedCostEstimationPackageInfo.getDeliveryCost(), expectedCostEstimationPackageInfo.getEstimatedCostDeliveryTimeInHrs())).collect(Collectors.toList());
        assertEquals(expectedVehicleCostEstimationPackageInfoList, resultCostEstimationPackageInfoList);
    }

    @Test
    public void testIfAllPackagesDelivered() {
        List<PackageInfo> packageInfoList = new ArrayList<>();
        packageInfoList.add(new PackageInfo("PKG1", 50, 30, "OFR001", Boolean.TRUE));
        packageInfoList.add(new PackageInfo("PKG2", 75, 125, "OFR008", Boolean.TRUE));
        packageInfoList.add(new PackageInfo("PKG3", 175, 100, "OFR003", Boolean.TRUE));

        assertEquals(Boolean.TRUE, DeliveryTimeEstimationService.getInstance().checkIfAllPackagesAreDelivered(packageInfoList));

        packageInfoList.add(new PackageInfo("PKG4", 110, 60, "OFR002", Boolean.FALSE));
        packageInfoList.add(new PackageInfo("PKG5", 155, 95, "NA", Boolean.TRUE));

        assertEquals(Boolean.FALSE, DeliveryTimeEstimationService.getInstance().checkIfAllPackagesAreDelivered(packageInfoList));
    }

    @Test
    public void testGetMaxUnassignedWeights() {
        Vehicle vehicle = new Vehicle(70, 200);
        List<PackageInfo> packageInfoList = new ArrayList<>();
        packageInfoList.add(new PackageInfo("PKG1", 50, 30, "OFR001"));
        packageInfoList.add(new PackageInfo("PKG2", 75, 125, "OFR008"));
        packageInfoList.add(new PackageInfo("PKG3", 175, 100, "OFR003"));
        packageInfoList.add(new PackageInfo("PKG4", 110, 60, "OFR002"));
        packageInfoList.add(new PackageInfo("PKG5", 155, 95, "NA"));

        assertEquals(List.of(110, 75), DeliveryTimeEstimationService.getInstance().getMaxUnassignedWeights(packageInfoList, vehicle));
    }
}
