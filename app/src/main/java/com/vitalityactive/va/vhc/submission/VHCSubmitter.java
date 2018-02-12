package com.vitalityactive.va.vhc.submission;

import com.vitalityactive.va.networking.RequestResult;

public interface VHCSubmitter {
    void submit();

    boolean isSubmitting();

    RequestResult getSubmissionRequestResult();
}
