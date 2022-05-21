package com.everest.courierservice.deliverytimeestimation.service;

import com.everest.courierservice.core.exception.ExceptionController;
import com.everest.courierservice.core.model.Coupon;
import com.everest.courierservice.core.model.PackageInfo;
import com.everest.courierservice.core.model.Vehicle;
import com.everest.courierservice.core.service.CouponService;
import com.everest.courierservice.core.service.ValidationService;
import com.everest.courierservice.costestimation.service.CostEstimationService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class Main {

    public static void main(String[] args) {
        Map<String, Coupon> couponMap = CouponService.getCouponMap();
        log.info("couponMap {}", couponMap);

        List<PackageInfo> packageInfoList = new ArrayList<>();
        int baseDeliveryCost = 0, noOfPackages = 0, noOfVehicles = 0, maxSpeed = 0, maxCarriableWeight = 0;
        try {
            Scanner scanner = new Scanner(System.in);
            baseDeliveryCost = Integer.parseInt(scanner.next());
            noOfPackages = Integer.parseInt(scanner.next());

            for(int i = 0; i < noOfPackages; i++) {
                String packageId = scanner.next();
                int pkgWeightInKg = Integer.parseInt(scanner.next());
                int pkgDistanceInKm = Integer.parseInt(scanner.next());
                String offerCode = scanner.next();
                packageInfoList.add(new PackageInfo(packageId, pkgWeightInKg, pkgDistanceInKm, offerCode));
            }
            noOfVehicles = Integer.parseInt(scanner.next());
            maxSpeed = Integer.parseInt(scanner.next());
            maxCarriableWeight = Integer.parseInt(scanner.next());
        } catch (Exception e) {
            log.error("invalid input format found {}", e);
            log.info("Please enter input as below format");
            log.info("---------------------------------------------");
            log.info("base_delivery_cost no_of_packges");
            log.info("pkg_id1 pkg_weight1_in_kg distance1_in_km offer_code1");
            log.info(".............");
            log.info("no_of_vehicles max_speed max_carriable_weight");
            System.exit(0);
        }

        Vehicle vehicle = new Vehicle(maxSpeed, maxCarriableWeight);
        log.info("input readed succesfully baseDeliveryCost {} noOfPackages {} noOfVehicles {} packageInfoList {} vehicle {}",
                baseDeliveryCost, noOfPackages, noOfVehicles, packageInfoList, vehicle);

        try {
            ValidationService.getInstance().validateInputs(packageInfoList, vehicle, noOfVehicles, baseDeliveryCost, noOfPackages);

            // calling cost estimation service to calculate the costs
            List<PackageInfo> packageInfoResultList = CostEstimationService.getInstance().findCostEstimationForCourierService(packageInfoList, baseDeliveryCost, noOfPackages, couponMap);
            for(PackageInfo packageInfo : packageInfoResultList) {
                log.info(packageInfo.getPackageId() + " " + packageInfo.getDiscount() + " " + packageInfo.getDeliveryCost() + " " + packageInfo.getEstimatedCostDeliveryTimeInHrs());
            }
            packageInfoResultList = DeliveryTimeEstimationService.getInstance().findDeliveryTimeEstimationForCourierService(packageInfoResultList, noOfVehicles, vehicle);
            for(PackageInfo packageInfo : packageInfoResultList) {
                log.info(packageInfo.getPackageId() + " " + packageInfo.getDiscount() + " " + packageInfo.getDeliveryCost() + " " + packageInfo.getEstimatedCostDeliveryTimeInHrs());
            }
        } catch (Exception e) {
            log.error("error occurred [{}]", ExceptionController.getErrorResponse(e));
        }
    }
}
