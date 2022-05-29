package com.everest.courierservice.core.utils;

import com.everest.courierservice.core.model.Coupon;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CSVUtils {

    public static List<Coupon> getCouponList(String file) throws Exception {
        List<Coupon> couponList = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(file));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
             )) {
            for (CSVRecord csvRecord : csvParser) {
                try {
                    String couponId = csvRecord.get("couponId");
                    int discountInPercentage = Integer.parseInt(csvRecord.get("discountInPercentage"));
                    int minDistanceInKm = Integer.parseInt(csvRecord.get("minDistanceInKm"));
                    int maxDistanceInKm = Integer.parseInt(csvRecord.get("maxDistanceInKm"));
                    int minWeightInKg = Integer.parseInt(csvRecord.get("minWeightInKg"));
                    int maxWeightInKg = Integer.parseInt(csvRecord.get("maxWeightInKg"));

                    if (StringUtils.isBlank(couponId) || discountInPercentage == 0) continue;

                    Coupon coupon = new Coupon(couponId, discountInPercentage, minDistanceInKm, maxDistanceInKm, minWeightInKg, maxWeightInKg);
                    couponList.add(coupon);
                } catch (Exception e) {
                    log.error("error occured while parsing coupon csv file {}", e.getMessage());
                }
            }
        }
        log.info("couponList {}", couponList);
        return couponList;
    }
}
