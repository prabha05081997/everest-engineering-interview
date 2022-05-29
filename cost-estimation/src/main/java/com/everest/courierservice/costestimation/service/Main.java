package com.everest.courierservice.costestimation.service;

import com.everest.courierservice.core.exception.ExceptionController;
import com.everest.courierservice.core.model.Coupon;
import com.everest.courierservice.core.model.PackageInfo;
import com.everest.courierservice.core.service.CouponService;
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
        int baseDeliveryCost = 0, noOfPackages = 0;

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
        } catch (Exception e) {
            log.error("invalid input format found {}", e);
            log.info("Please enter input as below format");
            log.info("---------------------------------------------");
            log.info("base_delivery_cost no_of_packges");
            log.info("pkg_id1 pkg_weight1_in_kg distance1_in_km offer_code1");
            System.exit(0);
        }

        log.info("input readed succesfully baseDeliveryCost {} noOfPackages {} packageInfoList {}",
                baseDeliveryCost, noOfPackages, packageInfoList);

        try {
            List<PackageInfo> packageInfoResultList = CostEstimationServiceImpl.getInstance().findCostEstimation(packageInfoList, baseDeliveryCost, noOfPackages, couponMap);
            log.info("===============================================================================================");
            for (PackageInfo packageInfo : packageInfoResultList) {
                log.info(packageInfo.getPackageId() + " " + packageInfo.getDiscount() + " " + packageInfo.getDeliveryCost());
            }
        }catch (Exception e) {
            log.error("error occurred [{}]", ExceptionController.getErrorResponse(e));
        }
    }
}
