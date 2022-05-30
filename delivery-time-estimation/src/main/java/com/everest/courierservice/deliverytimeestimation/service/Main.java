package com.everest.courierservice.deliverytimeestimation.service;

import com.everest.courierservice.core.exception.ExceptionController;
import com.everest.courierservice.core.model.Coupon;
import com.everest.courierservice.core.model.InputRequestModel;
import com.everest.courierservice.core.model.PackageInfo;
import com.everest.courierservice.core.service.CouponService;
import com.everest.courierservice.core.service.ValidationService;
import com.everest.courierservice.costestimation.service.CostEstimationServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class Main {

    /**
     *
     * Main method  - entry point for delivery-time-estimation application, inputs will be provided from command line
     *
     * @param args
     */
    public static void main(String[] args) {
        InputRequestModel inputRequestModel = UserInputServiceImpl.getInstance().fetchInputFromUser();
        try {
            ValidationService.getInstance().validateInputs(inputRequestModel.getPackageInfoList(), inputRequestModel.getVehicle(), inputRequestModel.getNoOfVehicles(),
                    inputRequestModel.getBaseDeliveryCost(), inputRequestModel.getNoOfPackages());

            Map<String, Coupon> couponMap = CouponService.getCouponMap();
            log.info("couponMap {}", couponMap);

            // calling cost estimation service to calculate the costs
            List<PackageInfo> packageInfoResultList = CostEstimationServiceImpl.getInstance().findCostEstimation(inputRequestModel.getPackageInfoList(), inputRequestModel.getBaseDeliveryCost(),
                    inputRequestModel.getNoOfPackages(), couponMap);
            for(PackageInfo packageInfo : packageInfoResultList) {
                log.info(packageInfo.getPackageId() + " " + packageInfo.getDiscount() + " " + packageInfo.getDeliveryCost() + " " + packageInfo.getEstimatedCostDeliveryTimeInHrs());
            }

            // calling delivery estimation service to calculation delivery time for packages
            packageInfoResultList = DeliveryTimeEstimationServiceImpl.getInstance().findDeliveryTimeEstimation(packageInfoResultList, inputRequestModel.getNoOfVehicles(),
                    inputRequestModel.getVehicle());
            log.info("===============================================================================================");
            for(PackageInfo packageInfo : packageInfoResultList) {
                log.info(packageInfo.getPackageId() + " " + packageInfo.getDiscount() + " " + packageInfo.getDeliveryCost() + " " + packageInfo.getEstimatedCostDeliveryTimeInHrs());
            }
        } catch (Exception e) {
            log.error("error occurred [{}]", ExceptionController.getErrorResponse(e));
        }
    }
}
