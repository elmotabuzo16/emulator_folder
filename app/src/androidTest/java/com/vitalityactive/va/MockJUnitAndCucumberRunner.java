package com.vitalityactive.va;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

import cucumber.api.android.CucumberInstrumentationCore;
import cucumber.runtime.DuplicateStepDefinitionException;

public class MockJUnitAndCucumberRunner extends MockJUnitRunner {
    private static final String TAG = "MJUAndCRunner";
    private static final String VALIDATE_CUCUMBER_TEST_TAG = "VALIDATE_CUCUMBER";
    private final CucumberInstrumentationCore instrumentationCore = new CucumberInstrumentationCore(this);
    private boolean isLastTestRunner = false;
    private boolean runningSelectedGherkinTests;
    private boolean dryRun;

    @NonNull
    public static File getRootDirectoryBasedOnAppName() {
        return new File(Environment.getExternalStorageDirectory(), getInstance().getApplication().getString(R.string.app_name));
    }

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        initializeCucumberTests(bundle);
    }

    @Override
    public void onStart() {
        if (runningSelectedGherkinTests || BuildConfig.GHERKIN_TESTS_ONLY || dryRun) {
            Log.d(TAG, "running selected gherkin tests || explicitly running gherkin tests only || dry run, not running normal JUnit tests");
            isLastTestRunner = true;
            runCucumberTests();
            return;
        }

        isLastTestRunner = false;
        runCucumberTests();
        isLastTestRunner = true;
        runJUnitTests();
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        if (isLastTestRunner) {
            Log.d(TAG, "final test runner, really finishing");
            super.finish(resultCode, results);
        } else {
            Log.d(TAG, "more test runners pending");
        }
    }

    private void runCucumberTests() {
        waitForIdleSync();
        instrumentationCore.start();
    }

    private void runJUnitTests() {
        super.onStart();
    }

    private void overwriteCucumberOptionsTagsFromBuildSettings(Bundle bundle) {
        runningSelectedGherkinTests = bundle.getString("tags") != null;
        if (runningSelectedGherkinTests) {
            Log.d(TAG, "tags found in Bundle, running only these: " + bundle.getString("tags"));
            return;
        }

        final String tags = BuildConfig.GHERKIN_TAGS;
        runningSelectedGherkinTests = !tags.equals("null") && !tags.isEmpty();
        if (!runningSelectedGherkinTests) {
            Log.d(TAG, "no tags filter in BuildConfig, will run all tags / tags specified in CucumberTestCase.java");
            return;
        }

        // reformat tags as gherkin expects them (with --)
        Log.w(TAG, String.format("setup gherkin tags: %s", tags));
        String gherkinTags = tags.replaceAll(",", "--").replaceAll("\\s", "");
        bundle.putString("tags", gherkinTags);
    }

    private void setupDryRun(Bundle bundle) {
        bundle.putString("dryRun", "true");
        dryRun = true;
    }

    private void setupGherkinConfiguration(Bundle bundle) {
        if (bundle.getString("dry-run") != null) {
            Log.d(TAG, "found dry-run tag");
            setupDryRun(bundle);
        } else if (BuildConfig.GHERKIN_DRY_RUN) {
            setupDryRun(bundle);
        } else if (VALIDATE_CUCUMBER_TEST_TAG.equals(bundle.getString("tags"))) {
            Log.d(TAG, String.format("found %s in tags, doing a dry run only to validate cucumber feature scripts and step definitions",
                    VALIDATE_CUCUMBER_TEST_TAG));
            setupDryRun(bundle);
            bundle.remove("tags");
        } else {
            overwriteCucumberOptionsTagsFromBuildSettings(bundle);
        }
    }

    private void initializeCucumberTests(Bundle bundle) {
        setupGherkinConfiguration(bundle);

        try {
            instrumentationCore.create(bundle);
        } catch (DuplicateStepDefinitionException t) {
            Log.e(TAG, "Duplicate step definitions", t);
            throw new RuntimeException("Duplicate step definitions: " + t.getMessage(), t);
        } catch (Throwable t) {
            Log.e(TAG, "Failed to initialize cucumber tests", t);
            throw new RuntimeException("Failed to initialize cucumber tests: " + t.getMessage(), t);
        }
    }
}
