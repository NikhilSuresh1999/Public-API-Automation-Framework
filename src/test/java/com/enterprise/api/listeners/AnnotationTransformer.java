package com.enterprise.api.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class AnnotationTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        Class<?> retryClass = annotation.getRetryAnalyzerClass();
        if (retryClass == null || retryClass.getName().contains("Disabled")) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        }
    }
}
