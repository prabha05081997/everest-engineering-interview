package com.everest.courierservice.costestimation.service;

import com.everest.courierservice.core.exception.ExceptionController;
import com.everest.courierservice.core.model.Coupon;
import com.everest.courierservice.core.model.InputRequestModel;
import com.everest.courierservice.core.model.PackageInfo;
import com.everest.courierservice.core.service.CouponService;
import com.everest.courierservice.core.service.ValidationService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class Main {

    /**
     *
     * Main method  - entry point for cost-estimation application, inputs will be provided from command line
     *
     * @param args
     */
    public static void main(String[] args) {
        // fetching input from user
        InputRequestModel inputRequestModel = UserInputServiceImpl.getInstance().fetchInputFromUser();
        try {
            // validation the request
            ValidationService.getInstance().validateInputs(inputRequestModel.getPackageInfoList(),
                    inputRequestModel.getBaseDeliveryCost(), inputRequestModel.getNoOfPackages());

            Map<String, Coupon> couponMap = CouponService.getCouponMap();
            log.info("couponMap {}", couponMap);

            // cost estimation service initiated
            List<PackageInfo> packageInfoResultList = CostEstimationServiceImpl.getInstance().findCostEstimation(inputRequestModel.getPackageInfoList(),
                    inputRequestModel.getBaseDeliveryCost(), inputRequestModel.getNoOfPackages(), couponMap);
            log.info("===============================================================================================");
            for (PackageInfo packageInfo : packageInfoResultList) {
                log.info(packageInfo.getPackageId() + " " + packageInfo.getDiscount() + " " + packageInfo.getDeliveryCost());
            }
        }catch (Exception e) {
            log.error("error occurred [{}]", ExceptionController.getErrorResponse(e));
        }
    }
}
