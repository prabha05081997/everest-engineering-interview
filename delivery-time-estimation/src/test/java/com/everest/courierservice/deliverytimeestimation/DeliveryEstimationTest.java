package com.everest.courierservice.deliverytimeestimation;

import com.everest.courierservice.core.model.Coupon;
import com.everest.courierservice.core.model.PackageInfo;
import com.everest.courierservice.core.model.Vehicle;
import com.everest.courierservice.core.model.VehicleAssignmentDetails;
import com.everest.courierservice.core.service.CouponService;
import com.everest.courierservice.costestimation.service.CostEstimationServiceImpl;
import com.everest.courierservice.deliverytimeestimation.service.DeliveryTimeEstimationServiceImpl;
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

        List<VehicleAssignmentDetails> resultVehicleAssignmentDetailsList = DeliveryTimeEstimationServiceImpl.getInstance().getVehicleAssignmentDetails(noOfVehicles);
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

        List<PackageInfo> packageInfoResultList = CostEstimationServiceImpl.getInstance().findCostEstimation(packageInfoList, baseDeliveryCost, packageInfoList.size(), couponMap);
        List<PackageInfo> resultCostEstimationPackageInfoList = DeliveryTimeEstimationServiceImpl.getInstance().findDeliveryTimeEstimation(packageInfoResultList, noOfVehicles, vehicle);
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

        List<PackageInfo> packageInfoResultList = CostEstimationServiceImpl.getInstance().findCostEstimation(packageInfoList, baseDeliveryCost, packageInfoList.size(), couponMap);
        List<PackageInfo> resultCostEstimationPackageInfoList = DeliveryTimeEstimationServiceImpl.getInstance().assignVehiclesToPackages(expectedVehicleAssignmentDetailsList, packageInfoResultList, vehicle);
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

        List<PackageInfo> packageInfoResultList = CostEstimationServiceImpl.getInstance().findCostEstimation(packageInfoList, baseDeliveryCost, packageInfoList.size(), couponMap);
        List<PackageInfo> resultCostEstimationPackageInfoList = DeliveryTimeEstimationServiceImpl.getInstance().assignVehicleToPackages(expectedVehicleAssignmentDetailsList.get(0), packageInfoResultList, vehicle);
        resultCostEstimationPackageInfoList = resultCostEstimationPackageInfoList.stream().map(expectedCostEstimationPackageInfo -> new PackageInfo(expectedCostEstimationPackageInfo.getPackageId(), expectedCostEstimationPackageInfo.getDiscount(),
                expectedCostEstimationPackageInfo.getDeliveryCost(), expectedCostEstimationPackageInfo.getEstimatedCostDeliveryTimeInHrs())).collect(Collectors.toList());
        assertEquals(expectedVehicleCostEstimationPackageInfoList, resultCostEstimationPackageInfoList);

        // failure case assertion
    }

    @Test
    public void testIfAllPackagesDelivered() {
        List<PackageInfo> packageInfoList = new ArrayList<>();
        packageInfoList.add(new PackageInfo("PKG1", 50, 30, "OFR001", Boolean.TRUE));
        packageInfoList.add(new PackageInfo("PKG2", 75, 125, "OFR008", Boolean.TRUE));
        packageInfoList.add(new PackageInfo("PKG3", 175, 100, "OFR003", Boolean.TRUE));

        assertEquals(Boolean.TRUE, DeliveryTimeEstimationServiceImpl.getInstance().checkIfAllPackagesAreDelivered(packageInfoList));

        packageInfoList.add(new PackageInfo("PKG4", 110, 60, "OFR002", Boolean.FALSE));
        packageInfoList.add(new PackageInfo("PKG5", 155, 95, "NA", Boolean.TRUE));

        assertEquals(Boolean.FALSE, DeliveryTimeEstimationServiceImpl.getInstance().checkIfAllPackagesAreDelivered(packageInfoList));
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

        assertEquals(List.of(110, 75), DeliveryTimeEstimationServiceImpl.getInstance().getMaxUnassignedWeights(packageInfoList, vehicle));
    }

    @Test
    public void testAssignVehicleToPackage() {
        Vehicle vehicle = new Vehicle(70, 200);
        List<PackageInfo> packageInfoList = new ArrayList<>();
        packageInfoList.add(new PackageInfo("PKG1", 50, 30, "OFR001"));
        packageInfoList.add(new PackageInfo("PKG2", 75, 125, "OFR008"));
        packageInfoList.add(new PackageInfo("PKG3", 175, 100, "OFR003"));
        packageInfoList.add(new PackageInfo("PKG4", 110, 60, "OFR002"));
        packageInfoList.add(new PackageInfo("PKG5", 155, 95, "NA"));

        List<PackageInfo> expectedVehicleAssignmentPackageInfoList = packageInfoList.stream().map(packageInfo -> {
            if (packageInfo.getPackageId().equals("PKG2")) {
                packageInfo.setVehicleNoAssigned(1);
                packageInfo.setVehicleAssigned(Boolean.TRUE);
                packageInfo.setDeliveryCost(1475);
            } else if (packageInfo.getPackageId().equals("PKG4")) {
                packageInfo.setVehicleNoAssigned(1);
                packageInfo.setVehicleAssigned(Boolean.TRUE);
                packageInfo.setDiscount(105);
                packageInfo.setDeliveryCost(1395);
            }
            return packageInfo;
        }).collect(Collectors.toList());

        VehicleAssignmentDetails vehicleAssignmentDetails = new VehicleAssignmentDetails(1);
        List<Integer> maxUnassignedWeights = DeliveryTimeEstimationServiceImpl.getInstance().getMaxUnassignedWeights(packageInfoList, vehicle);

        List<PackageInfo> vehicleAssignmentPackageInfoList = new ArrayList<>();
        DeliveryTimeEstimationServiceImpl.getInstance().assignVehicleToPackage(packageInfoList, vehicleAssignmentPackageInfoList, maxUnassignedWeights, vehicleAssignmentDetails);
        assertEquals(expectedVehicleAssignmentPackageInfoList, packageInfoList);

        expectedVehicleAssignmentPackageInfoList = expectedVehicleAssignmentPackageInfoList.stream().map(packageInfo -> {
            if(packageInfo.getPackageId().equals("PKG3")){
                packageInfo.setVehicleNoAssigned(2);
                packageInfo.setVehicleAssigned(Boolean.TRUE);
                packageInfo.setDeliveryCost(2350);
            }
            return packageInfo;
        }).collect(Collectors.toList());

        DeliveryTimeEstimationServiceImpl.getInstance().assignVehicleToPackage(packageInfoList, vehicleAssignmentPackageInfoList, maxUnassignedWeights, vehicleAssignmentDetails);
        assertEquals(expectedVehicleAssignmentPackageInfoList, packageInfoList);
    }

    /**
     * This will test the calculation of estimated time for delivery for the package
     */
    @Test
    public void testCalculateAndUpdateVehicleTimings() {
        Vehicle vehicle = new Vehicle(70, 200);
        List<PackageInfo> packageInfoList = new ArrayList<>();
        packageInfoList.add(new PackageInfo("PKG1", 50, 30, "OFR001"));
        packageInfoList.add(new PackageInfo("PKG2", 75, 125, "OFR008", Boolean.TRUE, 1));
        packageInfoList.add(new PackageInfo("PKG3", 175, 100, "OFR003"));
        packageInfoList.add(new PackageInfo("PKG4", 110, 60, "OFR002", Boolean.TRUE, 1));
        packageInfoList.add(new PackageInfo("PKG5", 155, 95, "NA"));

        List<PackageInfo> expectedVehicleAssignmentPackageInfoList = packageInfoList.stream().map(packageInfo -> {
            if (packageInfo.getPackageId().equals("PKG2")) {
                packageInfo.setEstimatedCostDeliveryTimeInHrs(1.78);
            } else if (packageInfo.getPackageId().equals("PKG4")) {
                packageInfo.setEstimatedCostDeliveryTimeInHrs(0.85);
            }
            return packageInfo;
        }).collect(Collectors.toList());

        VehicleAssignmentDetails vehicleAssignmentDetails = new VehicleAssignmentDetails(1);
        List<Integer> maxUnassignedWeights = DeliveryTimeEstimationServiceImpl.getInstance().getMaxUnassignedWeights(packageInfoList, vehicle);

        DeliveryTimeEstimationServiceImpl.getInstance().calculateAndUpdateVehicleTimings(vehicleAssignmentDetails, packageInfoList, maxUnassignedWeights, vehicle.getMaxSpeedInKmPerHr());
        assertEquals(expectedVehicleAssignmentPackageInfoList, packageInfoList);
    }

    @Test
    public void testGetTwoDigitPrecision() {
        assertEquals(0.85, DeliveryTimeEstimationServiceImpl.getInstance().getTwoDigitPrecision(0.85934342), 0);
        assertEquals(1.42, DeliveryTimeEstimationServiceImpl.getInstance().getTwoDigitPrecision(1.42454644), 0);
    }
}
