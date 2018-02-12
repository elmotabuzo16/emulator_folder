package com.vitalityactive.va.snv.confirmandsubmit.service;

import com.vitalityactive.va.networking.RequestResult;

public interface SNVSubmitter {
    void submit();

    boolean isSubmitting();

    RequestResult getSubmissionRequestResult();
}
