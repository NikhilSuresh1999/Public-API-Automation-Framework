package com.enterprise.api.listeners;

import com.enterprise.api.config.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(RetryAnalyzer.class);
    private int count = 0;
    private final int maxRetry = ConfigurationManager.get().maxRetryAttempts();

    @Override
    public boolean retry(ITestResult result) {
        if (count < maxRetry) {
            count++;
            log.warn("Retrying test {}.{} (Attempt {} of {}) due to failure/timeout",
                    result.getTestClass().getRealClass().getSimpleName(),
                    result.getMethod().getMethodName(),
                    count,
                    maxRetry);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            return true;
        }
        return false;
    }
}
