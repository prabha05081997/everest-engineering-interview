package com.everest.courierservice.costestimation.service;

import com.everest.courierservice.core.model.InputRequestModel;
import com.everest.courierservice.core.model.PackageInfo;
import com.everest.courierservice.core.service.UserInputService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class UserInputServiceImpl implements UserInputService {

    private static UserInputServiceImpl userInputServiceImpl;

    private UserInputServiceImpl() {

    }

    public static UserInputServiceImpl getInstance() {
        if (userInputServiceImpl == null) {
            synchronized (UserInputServiceImpl.class) {
                if (userInputServiceImpl == null) {
                    log.info("creating an instance of user input service");
                    userInputServiceImpl = new UserInputServiceImpl();
                }
            }
        }
        return userInputServiceImpl;
    }

    @Override
    public InputRequestModel fetchInputFromUser() {
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

        return new InputRequestModel(packageInfoList, baseDeliveryCost, noOfPackages);
    }
}
