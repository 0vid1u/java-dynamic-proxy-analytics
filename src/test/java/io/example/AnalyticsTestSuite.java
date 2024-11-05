package io.example;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Analytics System Test Suite")
@SelectPackages("io.example.tests")
public class AnalyticsTestSuite {

}
