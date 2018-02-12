package com.vitalityactive.va.dev.test;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "features",
        glue = {"com.vitalityactive.va.cucumber"},
        format = {"pretty",
                "html:/data/data/com.vitalityactive.va.dev/cucumber-reports/cucumber-html-report",
                "json:/data/data/com.vitalityactive.va.dev/cucumber-reports/cucumber.json",
                "junit:/data/data/com.vitalityactive.va.dev/cucumber-reports/cucumber.xml"
        }
)
public class CucumberTestCase {
}
