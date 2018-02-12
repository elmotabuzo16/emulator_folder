package com.vitalityactive.va;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

// based on https://stackoverflow.com/a/8301639/1016377

public class RetryRule implements TestRule {
    private final int retryCount;

    public RetryRule() {
        this(10);
    }

    public RetryRule(int retryCount) {
        this.retryCount = Math.max(1, retryCount);
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Throwable lastError = null;

                for (int i = 0; i < retryCount; i++) {
                    try {
                        base.evaluate();
                        if (lastError != null) {
                            System.err.println(description.getDisplayName() + ": test is temperamental and should be fixed");
                        }
                        return;
                    } catch (Throwable t) {
                        lastError = t;
                        System.err.println(description.getDisplayName() + ": run " + (i + 1) + " failed: " + lastError.getMessage());
                    }
                }
                System.err.println(description.getDisplayName() + ": giving up after " + retryCount + " failures");
                throw lastError;
            }
        };
    }
}
