package com.vitalityactive.va.snv.content;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.ProductFeature;
import com.vitalityactive.va.shared.UnitOfMeasureStringLoader;
import com.vitalityactive.va.shared.content.OnboardingContent;

public class SNVHealthAttributeContentFromStringResource extends UnitOfMeasureStringLoader implements SNVHealthAttributeContent {
    private InsurerConfigurationRepository insurerConfigurationRepository;

    public SNVHealthAttributeContentFromStringResource(Context vitalityActiveApplication, InsurerConfigurationRepository insurerConfigurationRepository){
        super(vitalityActiveApplication);
        this.insurerConfigurationRepository = insurerConfigurationRepository;
    }


    @NonNull
    @Override
    public String getValueString() {
        return getString(R.string.vhc_summary_value);
    }

    @NonNull
    @Override
    public String getHeightString() {
        return getString(R.string.measurement_height_title_145);
    }

    @NonNull
    @Override
    public String getWeightString() {
        return getString(R.string.measurement_weight_title_146);
    }

    @NonNull
    @Override
    public String getTestedOnString() {
        return getString(R.string.summary_screen_date_tested_title_185);
    }

    @NonNull
    @Override
    public String getProofSectionTitle() {
        return getString(R.string.summary_screen_uploaded_proof_title_186);
    }

    @NonNull
    @Override
    public String getFastingGlucoseString() {
        return getString(R.string.measurement_fasting_glucose_title_148);
    }

    @NonNull
    @Override
    public String getRandomGlucoseString() {
        return getString(R.string.measurement_random_glucose_title_147);
    }

    @NonNull
    @Override
    public String getSystolicBloodPressureString() {
        return getString(R.string.measurement_systolic_title_157);
    }

    @NonNull
    @Override
    public String getDiastolicBloodPressureString() {
        return getString(R.string.measurement_diastolic_title_156);
    }

    @NonNull
    @Override
    public String getTotalCholesterolString() {
        return getString(R.string.measurement_total_cholesterol_title_149);
    }

    @NonNull
    @Override
    public String getHDLCholesterolString() {
        return getString(R.string.measurement_hdl_title_150);
    }

    @NonNull
    @Override
    public String getLDLCholesterolString() {
        return getString(R.string.measurement_ldl_title_151);
    }

    @NonNull
    @Override
    public String getTriglyceridesCholesterolString() {
        return getString(R.string.measurement_triglycerides_title_152);
    }

    @NonNull
    @Override
    public String getHbA1cString() {
        return getString(R.string.measurement_hba1c_title_139);
    }

    @NonNull
    @Override
    public String getUrinaryProteinString() {
        return getString(R.string.measurement_urine_protein_title_283);
    }

    @NonNull
    @Override
    public String getWaistCircumferenceString() {
        return getString(R.string.measurement_waist_circumference_title_135);
    }

    @NonNull
    @Override
    public String getBMIString() {
        return getString(R.string.measurement_body_mass_index_title_134);
    }

    @NonNull
    @Override
    public String getUrinaryProteinNegativeString() {
        return getString(R.string.result_negative_title_286);
    }

    @NonNull
    @Override
    public String getUrinaryProteinNeutralString() {
        return getString(R.string.result_neutral_title_285);
    }

    @NonNull
    @Override
    public String getUrinaryProteinPositiveString() {
        return getString(R.string.result_positive_title_284);
    }

    @NonNull
    @Override
    public String getBloodPressureString() {
        return getString(R.string.measurement_blood_pressure_title_137);
    }

    @NonNull
    @Override
    public String getValidOptionDescription(String validOptionValue) {
        switch (validOptionValue) {
            case "-":
                return getUrinaryProteinNegativeString();
            case "+-":
                return getUrinaryProteinNeutralString();
            case "+":
            case "++":
            case "+++":
            case "++++":
                return getUrinaryProteinPositiveString();
            default:
                return "";
        }
    }

    @NonNull
    @Override
    public String getFieldPropertyName(int healthAttributeTypeKey) {
        return getFieldNameFromProductFeatureTypeKey(insurerConfigurationRepository.getProductFeatureTypeKeyFromHealthAttributeTypeKey(healthAttributeTypeKey));
    }

    @NonNull
    @Override
    public String getSelfSubmittedString() {
        return getString(R.string.detail_screen_source_self_submitted_message_202);
    }

    @NonNull
    @Override
    public String getValidationRangeBiggerString() {
        return getString(R.string.error_range_bigger_281);
    }

    @NonNull
    @Override
    public String getValidationRangeSmallerString() {
        return getString(R.string.error_range_smaller_282);
    }

    @NonNull
    @Override
    public String getValidationRange() {
        return getString(R.string.error_range_180);
    }

    @NonNull
    @Override
    public String getEarnMorePointsString() {
        return getString(R.string.detail_screen_earn_more_points_message_201);
    }

    @NonNull
    @Override
    public String getMaxPointsEarnedString() {
        return getString(R.string.detail_screen_you_earned_max_points_message_197);
    }

    @NonNull
    @Override
    public String getValidationRequiredString() {
        return getString(R.string.error_required_289);
    }

    @NonNull
    @Override
    public String getOnboardingTitle() {
        return getString(R.string.home_card_card_section_title_365);
    }

    @NonNull
    @Override
    public String getOnboardingSection1Title() {
        return getString(R.string.SV_onboarding_section_1_title_1002);
    }

    @NonNull
    @Override
    public String getOnboardingSection1Content() {
        return getString(R.string.SV_onboarding_section_1_message_1003);
    }

    @NonNull
    @Override
    public String getOnboardingSection2Title() {
        return getString(R.string.SV_landing_submit_button_1010);
    }

    @NonNull
    @Override
    public String getOnboardingSection2Content() {
        return getString(R.string.SV_onboarding_section_2_message_1005);
    }

    @NonNull
    @Override
    public String getOnboardingSection3Title() {
        return getString(R.string.SV_onboarding_section_3_title_1006);
    }

    @NonNull
    @Override
    public String getOnboardingSection3Content() {
        return getString(R.string.SV_onboarding_section_3_message_1007);
    }

    @NonNull
    @Override
    public String getLearnMoreTitle() {
        return getString(R.string.learn_more_heading_1_title_204);
    }

    @NonNull
    @Override
    public String getLearnMoreContent() {
        return getString(R.string.learn_more_heading_1_message_205);
    }

    @NonNull
    @Override
    public String getLearnMoreSection1Title() {
        return getString(R.string.learn_more_section_1_title_206);
    }

    @NonNull
    @Override
    public String getLearnMoreSection1Content() {
        return getString(R.string.learn_more_section_1_message_207);
    }

    @NonNull
    @Override
    public String getLearnMoreSection2Title() {
        return getString(R.string.learn_more_section_2_title_208);
    }

    @NonNull
    @Override
    public String getLearnMoreSection2Content() {
        return getString(R.string.learn_more_section_2_message_209);
    }

    @NonNull
    @Override
    public String getLearnMoreSection3Title() {
        return getString(R.string.onboarding_section_1_title_99);
    }

    @NonNull
    @Override
    public String getLearnMoreSection3Content() {
        return  getString(R.string.learn_more_section_3_message_210);
    }

    @NonNull
    @Override
    public String getBmiSection1Title() {
        return getString(R.string.learn_more_bmi_detail_section_1_titile_211);
    }

    @NonNull
    @Override
    public String getBmiSection1Content() {
        return getString(R.string.learn_more_bmi_section_1_message_225);
    }

    @Override
    public String getBmiSection2Title() {
        return getString(R.string.learn_more_important_section_2_title_217);
    }

    @Override
    public String getBmiSection2Content() {
        return getString(R.string.learn_more_bmi_section_2_message_226);
    }

    @Override
    public String getBmiSection3Title() {
        return getString(R.string.learn_more_bmi_section_3_title_218);
    }

    @Override
    public String getBmiSection3Content() {
        return getString(R.string.learn_more_bmi_section_3_message_227);
    }

    @Override
    public String getWaistCircumferenceSection1Title() {
        return getString(R.string.learn_more_waist_circumfernece_detail_section_1_title_212);
    }

    @Override
    public String getWaistCircumferenceSection1Content() {
        return getString(R.string.learn_more_waist_cicumfernece_section_1_message_228);
    }

    @Override
    public String getWaistCircumferenceSection2Title() {
        return getString(R.string.learn_more_important_section_2_title_217);
    }

    @Override
    public String getWaistCircumferenceSection2Content() {
        return getString(R.string.learn_more_waist_cicumfernece_section_2_message_229);
    }

    @Override
    public String getWaistCircumferenceSection3Title() {
        return getString(R.string.learn_more_waist_circumferenece_section_3_title_219);
    }

    @Override
    public String getWaistCircumferenceSection3Content() {
        return getString(R.string.learn_more_waist_cicumfernece_section_3_message_230);
    }

    @Override
    public String getGlucoseSection1Title() {
        return getString(R.string.learn_more_glucose_detail_section_1_title_213);
    }

    @Override
    public String getGlucoseSection1Content() {
        return getString(R.string.learn_more_glucose_section_1_message_231);
    }

    @Override
    public String getGlucoseSection2Title() {
        return getString(R.string.learn_more_important_section_2_title_217);
    }

    @Override
    public String getGlucoseSection2Content() {
        return getString(R.string.learn_more_glucose_section_2_message_232);
    }

    @Override
    public String getGlucoseSection3Title() {
        return getString(R.string.learn_more_glucose_section_3_title_220);
    }

    @Override
    public String getGlucoseSection3Content() {
        return getString(R.string.learn_more_glucose_section_3_message_233);
    }

    @Override
    public String getBloodPressureSection1Title() {
        return getString(R.string.learn_more_blood_pressure_detail_section_1_title_214);
    }

    @Override
    public String getBloodPressureSection1Content() {
        return getString(R.string.learn_more_blood_pressure_section_1_message_234);
    }

    @Override
    public String getBloodPressureSection2Title() {
        return getString(R.string.learn_more_important_section_2_title_217);
    }

    @Override
    public String getBloodPressureSection2Content() {
        return getString(R.string.learn_more_blood_pressure_section_2_message_235);
    }

    @Override
    public String getBloodPressureSection3Title() {
        return getString(R.string.learn_more_blood_pressure_section_3_title_221);
    }

    public String getBloodPressureSection3Content() {
        return getString(R.string.learn_more_blood_pressure_section_3_message_236);
    }

    @Override
    public String getCholesterolSection1Title() {
        return getString(R.string.learn_more_cholesterol_detail_section_1_title_215);
    }

    @Override
    public String getCholesterolSection1Content() {
        return getString(R.string.learn_more_cholesterol_section_1_message_237);
    }

    @Override
    public String getCholesterolSection2Title() {
        return getString(R.string.learn_more_important_section_2_title_217);
    }

    @Override
    public String getCholesterolSection2Content() {
        return getString(R.string.learn_more_cholesterol_section_2_message_238);
    }

    @Override
    public String getCholesterolSection3Title() {
        return getString(R.string.learn_more_cholesterol_section_3_title_222);
    }

    @Override
    public String getCholesterolSection3Content() {
        return getString(R.string.learn_more_cholesterol_section_3_message_239);
    }

    @Override
    public String getHba1cSection1Title() {
        return getString(R.string.learn_more_hba1c_detail_section_1_title_216);
    }

    @Override
    public String getHba1cSection1Content() {
        return getString(R.string.learn_more_hba1c_section_1_message_240);
    }

    @Override
    public String getHba1cSection2Title() {
        return getString(R.string.learn_more_important_section_2_title_217);
    }

    @Override
    public String getHba1cSection2Content() {
        return getString(R.string.learn_more_hba1c_section_2_message_241);
    }

    @Override
    public String getHba1cSection3Title() {
        return getString(R.string.learn_more_hba1c_section_3_title_223);
    }

    @Override
    public String getHba1cSection3Content() {
        return getString(R.string.learn_more_hba1c_section_3_message_242);
    }

    @Override
    public String getLandingTitle() {
        return getString(R.string.home_card_card_section_title_365);
    }

    @Override
    public String getLandingSubtitle() {
        return getString(R.string.SV_landing_health_action_message_1009);
    }

    @Override
    public String getMeasurable0CaptureContent() {
        return "";
    }

    @Override
    public String getMeasurable1CaptureContent() {
        return getString(R.string.capture_results_waist_circumference_message_153);
    }

    @Override
    public String getMeasurable2CaptureContent() {
        return getString(R.string.capture_results_blood_glucose_message_154);
    }

    @Override
    public String getMeasurable3CaptureContent() {
        return "";
    }

    @Override
    public String getMeasurable4CaptureContent() {
        return getString(R.string.capture_results_cholesterol_message_155);
    }

    @Override
    public String getMeasurable5CaptureContent() {
        return "";
    }

    @Override
    public String getMeasurable6CaptureContent() {
        return "";
    }

    @Override
    public String getBmiGroupTitle() {
        return getString(R.string.measurement_body_mass_index_title_134);
    }

    @Override
    public String getWaistCircumferenceGroupTitle() {
        return getString(R.string.measurement_waist_circumference_title_135);
    }

    @Override
    public String getGlucoseGroupTitle() {
        return getString(R.string.measurement_glucose_title_136);
    }

    @Override
    public String getBloodPressureGroupTitle() {
        return getString(R.string.measurement_blood_pressure_title_137);
    }

    @Override
    public String getCholesterolGroupTitle() {
        return getString(R.string.measurement_cholesterol_title_138);
    }

    @Override
    public String getHbA1cGroupTitle() {
        return getString(R.string.measurement_hba1c_title_139);
    }

    @Override
    public String getUrinaryProteinGroupTitle() {
        return getString(R.string.measurement_urine_protein_title_283);
    }

    @Override
    public String getUrinaryProteinSection1Title() {
        return getString(R.string.learn_more_urine_protein_section_1_title_344);
    }

    @Override
    public String getUrinaryProteinSection1Content() {
        return getString(R.string.learn_more_urine_protein_section_1_message_345);
    }

    @Override
    public String getUrinaryProteinSection2Title() {
        return getString(R.string.learn_more_important_section_2_title_217);
    }

    @Override
    public String getUrinaryProteinSection2Content() {
        return getString(R.string.learn_more_urine_protein_section_2_message_346);
    }

    @Override
    public String getUrinaryProteinSection3Title() {
        return getString(R.string.learn_more_urine_protein_section_3_title_347);
    }

    @Override
    public String getUrinaryProteinSection3Content() {
        return getString(R.string.learn_more_urine_protein_section_3_message_348);
    }

    @NonNull
    @Override
    public String getGoodCholesterolText() {
        return getString(R.string.capture_results_cholesterol_footnote_message_158);
    }

    @NonNull
    @Override
    public String getBadCholesterolText() {
        return getString(R.string.capture_results_cholesterol_footnote_message_159);
    }

    @NonNull
    @Override
    public String getInHealthyRangeText() {
        return getString(R.string.range_in_healthy_title_190);
    }

    @NonNull
    @Override
    public String getOutOfHealthyRangeText() {
        return getString(R.string.range_out_of_healthy_title_191);
    }

    @NonNull
    @Override
    public String getFieldName(int eventTypeKey) {
        int productFeatureTypeKey = insurerConfigurationRepository.getProductFeatureTypeKeyFromEventTypeKey(eventTypeKey);
        return getFieldNameFromProductFeatureTypeKey(productFeatureTypeKey);
    }

    @NonNull
    private String getFieldNameFromProductFeatureTypeKey(int productFeatureTypeKey) {
        switch (productFeatureTypeKey) {
            case ProductFeature._BMI:
                return getBMIString();
            case ProductFeature._HEIGHT:
                return getHeightString();
            case ProductFeature._WEIGHT:
                return getWeightString();
            case ProductFeature._WAIST:
                return getWaistCircumferenceString();
            case ProductFeature._FASTINGBG:
                return getFastingGlucoseString();
            case ProductFeature._RANDOMBG:
                return getRandomGlucoseString();
            case ProductFeature._SYSTOLICBP:
                return getSystolicBloodPressureString();
            case ProductFeature._BLOODPRESSURE:
                return getBloodPressureString();
            case ProductFeature._DIASTOLICBP:
                return getDiastolicBloodPressureString();
            case ProductFeature._TOTALCH:
                return getTotalCholesterolString();
            case ProductFeature._HDLCH:
                return getHDLCholesterolString();
            case ProductFeature._LDLCH:
                return getLDLCholesterolString();
            case ProductFeature._TGCH:
                return getTriglyceridesCholesterolString();
            case ProductFeature._HBA1C:
                return getHbA1cString();
            case ProductFeature._URINARY:
                return getUrinaryProteinString();
            default:
                return "Unknown";
        }
    }
}
