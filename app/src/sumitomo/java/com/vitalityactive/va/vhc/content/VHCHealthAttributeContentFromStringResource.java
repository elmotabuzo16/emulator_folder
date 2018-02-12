package com.vitalityactive.va.vhc.content;

import android.content.Context;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.R;

public class VHCHealthAttributeContentFromStringResource extends BaseVHCHealthAttributeContentFromStringResource {
    public VHCHealthAttributeContentFromStringResource(Context vitalityActiveApplication,
                                                       InsurerConfigurationRepository insurerConfigurationRepository,
                                                       PartyInformationRepository partyInformationRepository) {
        super(vitalityActiveApplication, insurerConfigurationRepository, partyInformationRepository);
    }

    @Override
    public String getBmiSection3Content() {
        if (partyInformationRepository.getPersonalDetails().getAge() >= 65) {
            //Based from ticket https://jira.vitalityservicing.com/browse/VA-17328
            //Points should be 500 and 2000 for 65 and above
            return String.format(getString(R.string.learn_more_bmi_section_3_dynamic_message_227),
                    "500",
                    "2,000");
        } else {
            return getString(R.string.learn_more_bmi_section_3_message_227);
        }
    }
}
