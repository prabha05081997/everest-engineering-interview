package com.everest.courierservice.deliverytimeestimation.service;

import com.everest.courierservice.core.model.PackageInfo;
import com.everest.courierservice.core.model.Vehicle;
import com.everest.courierservice.core.model.VehicleAssignmentDetails;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
public class DeliveryTimeEstimationService {

    private static DeliveryTimeEstimationService deliveryTimeEstimationService;

    private DeliveryTimeEstimationService() {

    }

    public static DeliveryTimeEstimationService getInstance() {
        if (deliveryTimeEstimationService == null) {
            synchronized (DeliveryTimeEstimationService.class) {
                if (deliveryTimeEstimationService == null) {
                    log.info("creating an instance of delivery time estimation service");
                    deliveryTimeEstimationService = new DeliveryTimeEstimationService();
                }
            }
        }
        return deliveryTimeEstimationService;
    }

    public List<PackageInfo> findDeliveryTimeEstimationForCourierService(List<PackageInfo> packageInfoList, int noOfVehicles, Vehicle vehicle) {
        log.info("in findDeliveryTimeEstimationForCourierService packageInfoList {} noOfVehicles {} vehicle {}",
                packageInfoList, noOfVehicles, vehicle);
        List<VehicleAssignmentDetails> vehicleAssignmentDetailsList = getVehicleAssignmentDetails(noOfVehicles);
        return assignVehiclesToPackages(vehicleAssignmentDetailsList, packageInfoList, vehicle);
    }

    public List<PackageInfo> assignVehiclesToPackages(List<VehicleAssignmentDetails> vehicleAssignmentDetailsList, List<PackageInfo> packageInfoList, Vehicle vehicle) {

        while(true) {
            List<PackageInfo> packageInfoListForVehicleAssigned = packageInfoList.stream().filter(packageInfo -> !packageInfo.isVehicleAssigned()).collect(Collectors.toList());
            if (packageInfoListForVehicleAssigned.size() == 0) {
                log.info("vehicles assigned for all the packages");
                break;
            }
            vehicleAssignmentDetailsList = vehicleAssignmentDetailsList.stream()
                    .sorted(Comparator.comparing(VehicleAssignmentDetails::getTotalDeliveryTime))
                    .collect(Collectors.toList());
            log.info("vehicleAssignmentDetailsList sorted by deliver time {}", vehicleAssignmentDetailsList);
            for (VehicleAssignmentDetails vehicleAssignmentDetails : vehicleAssignmentDetailsList) {
                packageInfoList = assignVehiclesToPackages(vehicleAssignmentDetails, packageInfoList, vehicle);
                log.info("vehicleAssignmentDetailsList at the end of each iteration {}", vehicleAssignmentDetailsList);
            }
            log.info("packageInfoList for final printing {}", packageInfoList);
        }
        return packageInfoList;
    }

    public List<PackageInfo> assignVehiclesToPackages(VehicleAssignmentDetails vehicleAssignmentDetails, List<PackageInfo> packageInfoList, Vehicle vehicle) {
        log.info("in assignVehiclesToPackages vehicleAssignmentDetails {} packageInfoList {} vehicle {}", vehicleAssignmentDetails, packageInfoList, vehicle);
        int maxWeightAssignableToVehicleInKg = 0;
        double maxSpeedInKmPerHr = vehicle.getMaxSpeedInKmPerHr();
        List<PackageInfo> vehicleAssignmentPackageInfoList = new ArrayList<>();
        List<Integer> unassignedWeights = packageInfoList.stream()
                .filter(packageInfo -> packageInfo.isVehicleAssigned() == Boolean.FALSE)
                .map(PackageInfo::getPackageWeightInKg)
                .collect(Collectors.toList());
        log.info("unassignedWeights {}", unassignedWeights);
        List<Integer> maxUnassignedWeights = MaxWeightService.getInstance().findMaxWeights(unassignedWeights, vehicle.getMaxCarriableWeightInKg());
        log.info("maxUnassignedWeights {}", maxUnassignedWeights);

//        packageInfoList = packageInfoList.stream()
//                .filter(packageInfo -> maxUnassignedWeights.contains(packageInfo.getPackageWeightInKg()))
//                .sorted(Comparator.comparing(PackageInfo::getPackageDistanceInKm))
//                .collect(Collectors.toList());

        for(PackageInfo packageInfo : packageInfoList) {
            if(maxUnassignedWeights.contains(packageInfo.getPackageWeightInKg())) {
                log.info("package weight belongs to max unassigned weight {}", packageInfo.getPackageWeightInKg());
                maxWeightAssignableToVehicleInKg += packageInfo.getPackageWeightInKg();
                packageInfo.setVehicleAssigned(Boolean.TRUE);
                packageInfo.setVehicleNoAssigned(vehicleAssignmentDetails.getVehicleNo());
            }
            vehicleAssignmentPackageInfoList.add(packageInfo);
        }
        AtomicReference<Double> estimatedDeliveryTime = new AtomicReference<>((double) 0);
        AtomicReference<Double> maxDeliveryTimeForVehicle = new AtomicReference<>((double) 0);
        double currentDeliveryTimeForVehicle = getTwoDigitPrecision(vehicleAssignmentDetails.getCurrentTime());
        double totalDeliveryTimeForVehicle = getTwoDigitPrecision(vehicleAssignmentDetails.getTotalDeliveryTime());
        log.info("vehicleAssignmentPackageInfoList {}", vehicleAssignmentPackageInfoList);
        vehicleAssignmentPackageInfoList = vehicleAssignmentPackageInfoList.stream().map(vehicleAssignmentPackageInfo -> {
            if(vehicleAssignmentPackageInfo.isVehicleAssigned()) {
                if(maxUnassignedWeights.contains(vehicleAssignmentPackageInfo.getPackageWeightInKg())) {
                    double packageDistanceInKm = vehicleAssignmentPackageInfo.getPackageDistanceInKm();
                    estimatedDeliveryTime.set(packageDistanceInKm / maxSpeedInKmPerHr);
                    if(estimatedDeliveryTime.get() > maxDeliveryTimeForVehicle.get()) {
                        maxDeliveryTimeForVehicle.set(estimatedDeliveryTime.get());
                    }

                    log.info("totalDeliveryTimeForVehicle {} estimatedDeliveryTime {}", totalDeliveryTimeForVehicle, estimatedDeliveryTime.get());
                    double estimatedCostDeliveryTimeForVehicle = getTwoDigitPrecision(totalDeliveryTimeForVehicle + estimatedDeliveryTime.get());
                    log.info("estimatedCostDeliveryTimeForVehicle {}", estimatedCostDeliveryTimeForVehicle);
                    vehicleAssignmentPackageInfo.setEstimatedCostDeliveryTimeInHrs(estimatedCostDeliveryTimeForVehicle);
                }
            }
            return vehicleAssignmentPackageInfo;
        }).collect(Collectors.toList());
        log.info("vehicleAssignmentPackageInfoList after updating estimated delivery time {}", vehicleAssignmentPackageInfoList);
        vehicleAssignmentDetails.setMaxWeightAssinableToVehicle(maxWeightAssignableToVehicleInKg);
        double totalRoundtripTimeForVehicle = getTwoDigitPrecision(maxDeliveryTimeForVehicle.get()) * 2;
        vehicleAssignmentDetails.setCurrentTime(totalRoundtripTimeForVehicle);

        if(currentDeliveryTimeForVehicle != 0) {
            log.info("adding current time {} to total delivery time {}", currentDeliveryTimeForVehicle, totalRoundtripTimeForVehicle);
            totalRoundtripTimeForVehicle += currentDeliveryTimeForVehicle;
        }
        vehicleAssignmentDetails.setTotalDeliveryTime(totalRoundtripTimeForVehicle);
        return vehicleAssignmentPackageInfoList;
    }

    public List<VehicleAssignmentDetails> getVehicleAssignmentDetails(int noOfVehicles) {
        List<VehicleAssignmentDetails> vehicleAssignmentDetailsList = new ArrayList<>();
        for(int i = 1; i <= noOfVehicles; i++) {
            VehicleAssignmentDetails vehicleAssignmentDetails = new VehicleAssignmentDetails(i);
            vehicleAssignmentDetailsList.add(vehicleAssignmentDetails);
        }
        return vehicleAssignmentDetailsList;
    }

    private double getTwoDigitPrecision(double value) {
        value *= 100;  // moves two digits from right to left of dec point
        value = Math.floor(value);  // removes all reminaing dec digits
        value /= 100;  // moves two digits from left to right of dec point
        return value;
    }
}
