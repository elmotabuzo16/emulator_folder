package com.vitalityactive.va.vhc.content;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.ProductFeature;

public class VHCHealthAttributeContentFromStringResource extends BaseVHCHealthAttributeContentFromStringResource {

    public VHCHealthAttributeContentFromStringResource(Context vitalityActiveApplication,
                                                       InsurerConfigurationRepository insurerConfigurationRepository,
                                                       PartyInformationRepository partyInformationRepository){
        super(vitalityActiveApplication, insurerConfigurationRepository, partyInformationRepository);
    }

    @Override
    public String getCholesterolGroupTitle() {
        return getString(R.string.measurement_cholesterol_ratio_title_1173);
    }

    @NonNull
    @Override
    protected String getFieldNameFromProductFeatureTypeKey(int productFeatureTypeKey) {
        switch (productFeatureTypeKey) {
            case ProductFeature._LIPIDRATIO:
                return getCholesterolRatio();
            default:
                return super.getFieldNameFromProductFeatureTypeKey(productFeatureTypeKey);
        }
    }
}
