package com.everest.courierservice.core.service;

import com.everest.courierservice.core.exception.InputValidationException;
import com.everest.courierservice.core.model.PackageInfo;
import com.everest.courierservice.core.model.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Slf4j
public class ValidationService {
    private static ValidationService validationService;
    private boolean[][] dp;
    private static List<List<Integer>> maxWeights;

    private ValidationService() {

    }

    public static ValidationService getInstance() {
        if (validationService == null) {
            synchronized (ValidationService.class) {
                if (validationService == null) {
                    log.info("creating an instance of validation service");
                    validationService = new ValidationService();
                }
            }
        }
        return validationService;
    }

    public void validateInputs(List<PackageInfo> packageInfoList, Vehicle vehicle, int noOfVehicles,
                               int baseDeliveryCost, int noOfPackages) throws Exception {
        if(noOfVehicles <= 0) {
            log.error("no of vehicles should be positive integer");
            throw new InputValidationException("no of vehicles should be positive integer");
        }
        int maxCarriableWeightInKg = vehicle.getMaxCarriableWeightInKg();
        double maxSpeedInKmPerHr = vehicle.getMaxSpeedInKmPerHr();
        if(maxCarriableWeightInKg == 0) {
            log.error("vehicle max carriable weight should be positive integer");
            throw new InputValidationException("vehicle max carriable weight should be positive integer");
        }
        if(maxSpeedInKmPerHr == 0) {
            log.error("vehicle max speed should be positive integer");
            throw new InputValidationException("vehicle max speed should be positive integer");
        }

        for(PackageInfo packageInfo : packageInfoList) {
            if(packageInfo.getPackageWeightInKg() > maxCarriableWeightInKg) {
                log.error("package weight exceeds max carriable capacity by vehicle");
                throw new InputValidationException("package weight exceeds max carriable capacity by vehicle");
            }
        }
        validateInputs(packageInfoList, baseDeliveryCost, noOfPackages);
    }

    public void validateInputs(List<PackageInfo> packageInfoList, int baseDeliveryCost, int noOfPackages) throws Exception {
        if(baseDeliveryCost == 0) {
            log.error("base delivery cost should be positive integer");
            throw new InputValidationException("base delivery should be positive integer");
        }
        if(noOfPackages == 0) {
            log.error("no of packages should be positive integer");
            throw new InputValidationException("no of packages should be positive integer");
        }
        for(PackageInfo packageInfo : packageInfoList) {
            if(StringUtils.isBlank(packageInfo.getPackageId())) {
                log.error("package id cannot be empty");
                throw new InputValidationException("package id cannot be empty");
            }
            if(StringUtils.isBlank(packageInfo.getOfferCode())) {
                log.error("offer code cannot be empty");
                throw new InputValidationException("offer code cannot be empty");
            }
            if(packageInfo.getPackageWeightInKg() <= 0) {
                log.error("package weight should be positive integer");
                throw new InputValidationException("package weight speed should be positive integer");
            }
            if(packageInfo.getPackageDistanceInKm() <= 0) {
                log.error("package weight should be positive integer");
                throw new InputValidationException("package weight should be positive integer");
            }
        }
    }
}
