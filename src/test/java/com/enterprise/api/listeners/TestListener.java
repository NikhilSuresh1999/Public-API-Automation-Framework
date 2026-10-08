package com.enterprise.api.listeners;

import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info(">>>> Starting Test Suite: {} <<<<", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info(">>>> Test Suite Finished: {} (Passed: {}, Failed: {}, Skipped: {}) <<<<",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("[TEST STARTING] -> {}.{}",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        log.info("[TEST PASSED] -> {}.{} in {} ms",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName(),
                duration);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        log.error("[TEST FAILED] -> {}.{} in {} ms. Cause: {}",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName(),
                duration,
                result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown");

        if (result.getThrowable() != null) {
            Allure.addAttachment("Failure Stacktrace", result.getThrowable().toString());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("[TEST SKIPPED] -> {}.{}",
                result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
    }
}

