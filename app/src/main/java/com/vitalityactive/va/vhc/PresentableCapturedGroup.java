package com.vitalityactive.va.vhc;

import java.util.ArrayList;
import java.util.List;

class PresentableCapturedGroup {
    public String title;
    List<PresentableCapturedField> capturedFields = new ArrayList<>();

    PresentableCapturedGroup(String title, List<PresentableCapturedField> capturedFields) {
        this.title = title;
        this.capturedFields = capturedFields;
    }
}
