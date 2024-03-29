package com.everest.courierservice.deliverytimeestimation.service;

import com.everest.courierservice.core.exception.ServiceException;
import com.everest.courierservice.core.model.PackageInfo;
import com.everest.courierservice.core.model.Vehicle;
import com.everest.courierservice.core.model.VehicleAssignmentDetails;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
public class DeliveryTimeEstimationServiceImpl implements DeliveryTimeEstimationService {

    private static DeliveryTimeEstimationServiceImpl deliveryTimeEstimationServiceImpl;

    private DeliveryTimeEstimationServiceImpl() {

    }

    public static DeliveryTimeEstimationServiceImpl getInstance() {
        if (deliveryTimeEstimationServiceImpl == null) {
            synchronized (DeliveryTimeEstimationServiceImpl.class) {
                if (deliveryTimeEstimationServiceImpl == null) {
                    log.info("creating an instance of delivery time estimation service");
                    deliveryTimeEstimationServiceImpl = new DeliveryTimeEstimationServiceImpl();
                }
            }
        }
        return deliveryTimeEstimationServiceImpl;
    }

    /**
     *
     * This is simple method which will delegegate the delivery time estimation calculation to other methods
     *
     * @param packageInfoList - List of Object to track package info
     * @param noOfVehicles - total no of vehicles available
     * @param vehicle - Vehicle object to track speed and carriable weight info
     * @return - List of Package info object with delivery time and vehicle info updated
     *
     */
    @Override
    public List<PackageInfo> findDeliveryTimeEstimation(List<PackageInfo> packageInfoList, int noOfVehicles, Vehicle vehicle) throws Exception {
        log.info("in findDeliveryTimeEstimationForCourierService packageInfoList {} noOfVehicles {} vehicle {}",
                packageInfoList, noOfVehicles, vehicle);
        List<VehicleAssignmentDetails> vehicleAssignmentDetailsList = getVehicleAssignmentDetails(noOfVehicles);
        return assignVehiclesToPackages(vehicleAssignmentDetailsList, packageInfoList, vehicle);
    }

    /**
     *
     * This method will call the assignVehiclesToPackages method in a infinite loop until all the packages are delivered
     *
     * @param vehicleAssignmentDetailsList - List of Object to track vehicle and package assignment info
     * @param packageInfoList - List of Object to track package info
     * @param vehicle - Object to track vehicle info
     * @return - List of Package info object with delivery time and vehicle info updated
     *
     */
    public List<PackageInfo> assignVehiclesToPackages(List<VehicleAssignmentDetails> vehicleAssignmentDetailsList, List<PackageInfo> packageInfoList, Vehicle vehicle) throws Exception {
        // running infinite loop until all the packages been delivered
        while(true) {
            boolean isAllPackagesDelivered = checkIfAllPackagesAreDelivered(packageInfoList);
            if (isAllPackagesDelivered) {
                log.info("all the packages are delivered");
                break;
            }

            // sorting the list based on availablity time for vehicles
            vehicleAssignmentDetailsList = vehicleAssignmentDetailsList.stream()
                    .sorted(Comparator.comparing(VehicleAssignmentDetails::getTotalDeliveryTime))
                    .collect(Collectors.toList());
            log.info("vehicleAssignmentDetailsList sorted by deliver time {}", vehicleAssignmentDetailsList);
            for (VehicleAssignmentDetails vehicleAssignmentDetails : vehicleAssignmentDetailsList) {
                packageInfoList = assignVehicleToPackages(vehicleAssignmentDetails, packageInfoList, vehicle);
                log.info("vehicleAssignmentDetailsList at the end of each iteration {}", vehicleAssignmentDetailsList);
            }
            log.info("packageInfoList for final printing {}", packageInfoList);
        }
        return packageInfoList;
    }

    /**
     *
     * This method will delegate the calculation for packages to be delivered first
     * and the assignment of vehicles
     *
     * @param vehicleAssignmentDetails - Object to track vehicle and package assignment info
     * @param packageInfoList - List of Objects to track package info
     * @param vehicle - Object to track vehicle info
     * @return - List of Package info object with delivery time and vehicle info updated
     *
     */
    public List<PackageInfo> assignVehicleToPackages(VehicleAssignmentDetails vehicleAssignmentDetails, List<PackageInfo> packageInfoList, Vehicle vehicle) throws Exception {
        log.info("in assignVehiclesToPackages vehicleAssignmentDetails {} packageInfoList {} vehicle {}", vehicleAssignmentDetails, packageInfoList, vehicle);
        // check if all the packages are delivered before start assigning vehicles
        boolean isAllPackagesDelivered = checkIfAllPackagesAreDelivered(packageInfoList);
        if (isAllPackagesDelivered) {
            log.info("vehicles assigned for all the packages");
            return packageInfoList;
        }
        double maxSpeedInKmPerHr = vehicle.getMaxSpeedInKmPerHr();
        List<PackageInfo> vehicleAssignmentPackageInfoList = new ArrayList<>();
        List<Integer> maxUnassignedWeights = getMaxUnassignedWeights(packageInfoList, vehicle);

        // fail safe mechanism - check if the weights are empty, and throw exception to avoid infinite loop
        if(maxUnassignedWeights.isEmpty()) {
            log.error("max weight can't find from the given weights");
            throw new ServiceException("max weight can't find from the given weights");
        }

        assignVehicleToPackage(packageInfoList, vehicleAssignmentPackageInfoList, maxUnassignedWeights, vehicleAssignmentDetails);

        calculateAndUpdateVehicleTimings(vehicleAssignmentDetails, vehicleAssignmentPackageInfoList, maxUnassignedWeights, maxSpeedInKmPerHr);

        return vehicleAssignmentPackageInfoList;
    }

    /**
     *
     * This method will return the max weights that can be carried by vehicle for the packages that are not delivered yet
     *
     * @param packageInfoList - List of Objects to track package info
     * @param vehicle - Object to track vehicle info
     * @return - List of integers denotes the max unassigned weights for which packages are not delivered yet
     *
     */
    public List<Integer> getMaxUnassignedWeights(List<PackageInfo> packageInfoList, Vehicle vehicle) {
        // process the packages only if it's not delivered already
        List<Integer> unassignedWeights = packageInfoList.stream()
                .filter(packageInfo -> packageInfo.isVehicleAssigned() == Boolean.FALSE)
                .map(PackageInfo::getPackageWeightInKg)
                .collect(Collectors.toList());
        log.info("unassignedWeights {}", unassignedWeights);
        List<Integer> maxUnassignedWeights = MaxWeightEstimationServiceImpl.getInstance().findMaxWeights(unassignedWeights, vehicle.getMaxCarriableWeightInKg());
        log.info("maxUnassignedWeights {}", maxUnassignedWeights);
        return maxUnassignedWeights;
    }

    /**
     *
     * This method will assign vehicles to the packages based on certain conditions
     * 1) For a single delivery, max weight and max no of packages should be carried
     * 2) if weight is same package which can be delivered first should process first
     *
     * @param packageInfoList - List of Objects to track package info
     * @param vehicleAssignmentPackageInfoList - List of Objects to track package info
     * @param maxUnassignedWeights - weights of the packages that are not delivered yet
     * @param vehicleAssignmentDetails - Object to track vehicle and package assignment info
     *
     */
    public void assignVehicleToPackage(List<PackageInfo> packageInfoList, List<PackageInfo> vehicleAssignmentPackageInfoList, List<Integer> maxUnassignedWeights,
                                       VehicleAssignmentDetails vehicleAssignmentDetails) {
        // update vehicle info and weight details in package info object
        for(PackageInfo packageInfo : packageInfoList) {
            if(maxUnassignedWeights.contains(packageInfo.getPackageWeightInKg()) && packageInfo.isVehicleAssigned() == Boolean.FALSE) {
                log.info("packageInfo inside vehicle assignment {}", packageInfo);
                List<PackageInfo> tmpPackageInfoList = packageInfoList.stream().filter(packageInfo1 ->
                        (packageInfo1.getPackageWeightInKg() == packageInfo.getPackageWeightInKg()) && (packageInfo1.isVehicleAssigned() == Boolean.FALSE)).collect(Collectors.toList());
                if(tmpPackageInfoList.size() > 0) {
                    // if weights are same, assign vehicle to the package which can be delivered first
                    PackageInfo tmpPackageInfo = tmpPackageInfoList.stream().min(Comparator.comparing(PackageInfo::getPackageDistanceInKm))
                            .get();
                    if(tmpPackageInfo.getPackageDistanceInKm() != packageInfo.getPackageDistanceInKm()) {
                        vehicleAssignmentPackageInfoList.add(packageInfo);
                        continue;
                    }
                }
                log.info("package weight belongs to max unassigned weight {}", packageInfo.getPackageWeightInKg());
                packageInfo.setVehicleAssigned(Boolean.TRUE);
                packageInfo.setVehicleNoAssigned(vehicleAssignmentDetails.getVehicleNo());
                log.info("packageInfo after vehicle assignment {}", packageInfo);
            }
            vehicleAssignmentPackageInfoList.add(packageInfo);
        }
        log.info("vehicleAssignmentPackageInfoList after assigning vehicle to package {}", vehicleAssignmentPackageInfoList);
    }

    /**
     *
     * This method will calculate the estimated delivery time for the packages, max delivery time and total round trip time for the vehicle
     *
     * @param vehicleAssignmentDetails - Object to track vehicle and package assignment info
     * @param vehicleAssignmentPackageInfoList - List of Objects to track package info
     * @param maxUnassignedWeights - weights of the packages that are not delivered yet
     * @param maxSpeedInKmPerHr - max speed of a vehicle which we get from CLI input
     *
     */
    public void calculateAndUpdateVehicleTimings(VehicleAssignmentDetails vehicleAssignmentDetails, List<PackageInfo> vehicleAssignmentPackageInfoList,
                                                 List<Integer> maxUnassignedWeights, double maxSpeedInKmPerHr) {
        // atomic variables to track estimated delivery time and max delivery time for vehicle inside lambda reference
        AtomicReference<Double> estimatedDeliveryTime = new AtomicReference<>((double) 0);
        AtomicReference<Double> maxDeliveryTimeForVehicle = new AtomicReference<>((double) 0);
        double currentDeliveryTimeForVehicle = getTwoDigitPrecision(vehicleAssignmentDetails.getCurrentTime());
        double totalDeliveryTimeForVehicle = getTwoDigitPrecision(vehicleAssignmentDetails.getTotalDeliveryTime());
        log.info("vehicleAssignmentPackageInfoList {}", vehicleAssignmentPackageInfoList);

        // update the delivery time for package and for the vehicle
        vehicleAssignmentPackageInfoList = vehicleAssignmentPackageInfoList.stream().map(vehicleAssignmentPackageInfo -> {
            if(vehicleAssignmentPackageInfo.isVehicleAssigned() && maxUnassignedWeights.contains(vehicleAssignmentPackageInfo.getPackageWeightInKg())) {
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
            return vehicleAssignmentPackageInfo;
        }).collect(Collectors.toList());
        log.info("vehicleAssignmentPackageInfoList after updating estimated delivery time {}", vehicleAssignmentPackageInfoList);

        double totalRoundtripTimeForVehicle = getTwoDigitPrecision(maxDeliveryTimeForVehicle.get()) * 2;
        vehicleAssignmentDetails.setCurrentTime(totalRoundtripTimeForVehicle);
        log.info("adding current time {} to total delivery time {}", currentDeliveryTimeForVehicle, totalRoundtripTimeForVehicle);

        // add total roundtrip time of each delivery
        totalRoundtripTimeForVehicle += currentDeliveryTimeForVehicle;
        vehicleAssignmentDetails.setTotalDeliveryTime(totalRoundtripTimeForVehicle);
    }

    /**
     *
     * This method will assign unique number to each vehicles
     *
     * @param noOfVehicles - No of vehicles obtained from CLI input
     * @return - List of VehicleAssignmentDetails objects with updated vehicle number
     *
     */
    public List<VehicleAssignmentDetails> getVehicleAssignmentDetails(int noOfVehicles) {
        List<VehicleAssignmentDetails> vehicleAssignmentDetailsList = new ArrayList<>();
        for(int i = 1; i <= noOfVehicles; i++) {
            VehicleAssignmentDetails vehicleAssignmentDetails = new VehicleAssignmentDetails(i);
            vehicleAssignmentDetailsList.add(vehicleAssignmentDetails);
        }
        return vehicleAssignmentDetailsList;
    }

    /**
     *
     * This method will check if all the packages are delivered
     *
     * @param packageInfoList - List of Objects to track package info
     * @return - boolean value to denote all the packages are delivered or not
     *
     */
    public boolean checkIfAllPackagesAreDelivered(List<PackageInfo> packageInfoList) {
        return packageInfoList.stream().allMatch(PackageInfo::isVehicleAssigned) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     *
     * This method will give two digit precision for double data type
     * @param value - double value
     * @return - two digit precision double value
     *
     */
    public double getTwoDigitPrecision(double value) {
        value *= 100;
        value = Math.floor(value);
        value /= 100;
        return value;
    }
}
