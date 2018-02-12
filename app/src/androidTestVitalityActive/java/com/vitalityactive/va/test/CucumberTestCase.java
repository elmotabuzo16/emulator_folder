package com.vitalityactive.va.test;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "features",
        glue = {"com.vitalityactive.va.cucumber"},
        format = {"pretty",
                "html:/data/data/com.vitalityactive.va/cucumber-reports/cucumber-html-report",
                "json:/data/data/com.vitalityactive.va/cucumber-reports/cucumber.json",
                "junit:/data/data/com.vitalityactive.va/cucumber-reports/cucumber.xml"
        }
)
public class CucumberTestCase {
}
