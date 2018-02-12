package com.vitalityactive.va.vhc.captureresults;

import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;

public class CaptureResultsPresenterImpl extends BaseCaptureResultsPresenterImpl {

    public CaptureResultsPresenterImpl(HealthAttributeRepository repository,
                                       VHCHealthAttributeContent content,
                                       MeasurementPersister measurementPersister) {
        super(repository, content, measurementPersister);
    }
}
