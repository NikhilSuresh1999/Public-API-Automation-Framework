package com.enterprise.api.config;

import org.aeonbits.owner.ConfigFactory;

public final class ConfigurationManager {

    private static volatile FrameworkConfig config;

    private ConfigurationManager() {
        // Prevent direct instantiation
    }

    public static FrameworkConfig get() {
        if (config == null) {
            synchronized (ConfigurationManager.class) {
                if (config == null) {
                    config = ConfigFactory.create(FrameworkConfig.class);
                }
            }
        }
        return config;
    }
}

