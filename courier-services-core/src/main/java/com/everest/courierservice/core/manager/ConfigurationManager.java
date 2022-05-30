package com.everest.courierservice.core.manager;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;

import static com.everest.courierservice.core.constants.PropertyConstants.*;

@Slf4j
@Data
public class ConfigurationManager {
    private static ConfigurationManager configurationManager;
    private static Properties properties;

    private String couponFilePath;

    private ConfigurationManager() {

    }

    public static ConfigurationManager getInstance() {
        if (configurationManager == null) {
            synchronized (ConfigurationManager.class) {
                if (configurationManager == null) {
                    log.info("creatinng an instance of configuration manager service");
                    configurationManager = new ConfigurationManager();
                }
            }
        }
        return configurationManager;
    }

    public void loadConfiguration() {
        log.info("In loadConfiguration");
        try {
//            File file = new File(ConfigurationManager.class.getClassLoader().getResource(APP_PROPERTIES).getFile());
            InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream(APP_PROPERTIES);
            properties = new Properties();
//            properties.load(new FileReader(file));
            properties.load(file);

            couponFilePath = properties.getProperty(COUPON_FILE_PATH);
        } catch (Exception e) {
            log.error("Error while loading configuration - " + e);
            System.exit(0);
        }

        log.info("Return from loadConfiguration");
    }
}
