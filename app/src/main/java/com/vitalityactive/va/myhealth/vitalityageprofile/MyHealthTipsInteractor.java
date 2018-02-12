package com.vitalityactive.va.myhealth.vitalityageprofile;


import com.vitalityactive.va.myhealth.dto.HealthInformationSectionDTO;

import java.util.List;

public interface MyHealthTipsInteractor {

    List<HealthInformationSectionDTO> getHealthAttributeInformationByParentTypeKey(int parentTypekey);

    boolean healthinformationSectionHasSubsections(int typeKey);
}
