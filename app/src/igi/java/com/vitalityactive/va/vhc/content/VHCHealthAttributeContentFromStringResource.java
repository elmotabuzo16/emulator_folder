package com.vitalityactive.va.vhc.content;

import android.content.Context;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.PartyInformationRepository;

public class VHCHealthAttributeContentFromStringResource extends BaseVHCHealthAttributeContentFromStringResource {

    public VHCHealthAttributeContentFromStringResource(Context vitalityActiveApplication,
                                                       InsurerConfigurationRepository insurerConfigurationRepository,
                                                       PartyInformationRepository partyInformationRepository){
        super(vitalityActiveApplication, insurerConfigurationRepository, partyInformationRepository);
    }
}
