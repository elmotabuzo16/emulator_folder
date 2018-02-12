package com.vitalityactive.va.vhc.learnmore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.activerewards.viewholder.TitleSubtitleAndColoredIconViewHolder;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class VHCLearnMoreItemDetailActivity extends BaseActivity {

    public static final String LEARN_MORE_ITEM = "LEARN_MORE_ITEM";

    @Inject
    VHCHealthAttributeContent content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vhc_learn_more_item_detail);

        getDependencyInjector().inject(this);

        setUpActionBarWithTitle(getIntent().getStringExtra(LEARN_MORE_ITEM))
                .setDisplayHomeAsUpEnabled(true);

        createAndSetAdapter(getSectionsList());

        findViewById(R.id.recyclerview).setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
    }

    private List<TitleSubtitleAndIcon> getSectionsList() {
        List<TitleSubtitleAndIcon> sectionsList = new ArrayList<>();
        String itemTag = getIntent().getStringExtra(LEARN_MORE_ITEM);

        if (itemTag.equals(content.getBmiGroupTitle())) {
            setBodyMassIndexData(sectionsList);
        } else if (itemTag.equals(content.getWaistCircumferenceGroupTitle())) {
            setWaistCircumferenceData(sectionsList);
        } else if (itemTag.equals(content.getGlucoseGroupTitle())) {
            setBloodGlucoseData(sectionsList);
        } else if (itemTag.equals(content.getBloodPressureGroupTitle())) {
            setBloodPressureData(sectionsList);
        } else if (itemTag.equals(content.getCholesterolGroupTitle())) {
            setCholesterolData(sectionsList);
        } else if (itemTag.equals(content.getHbA1cGroupTitle())) {
            setHbA1cData(sectionsList);
        } else if (itemTag.equals(content.getUrinaryProteinGroupTitle())) {
            setUrinaryProteinData(sectionsList);
        }
        return sectionsList;
    }

    public void createAndSetAdapter(List<TitleSubtitleAndIcon> titleSubtitleAndIcons) {
        GenericRecyclerViewAdapter genericRecyclerViewAdapter =
                new GenericRecyclerViewAdapter<>(this,
                        titleSubtitleAndIcons,
                        R.layout.vhc_learn_more_list_container,
                        new TitleSubtitleAndColoredIconViewHolder.Factory());

        ((RecyclerView) findViewById(R.id.recyclerview)).setAdapter(genericRecyclerViewAdapter);
    }

    private void setHbA1cData(List<TitleSubtitleAndIcon> sectionsList) {
        sectionsList.add(new TitleSubtitleAndIcon(content.getHba1cSection1Title(), content.getHba1cSection1Content(), R.drawable.health_measure_hba_1_c));
        sectionsList.add(new TitleSubtitleAndIcon(content.getHba1cSection2Title(), content.getHba1cSection2Content(), R.drawable.health_tips));
        sectionsList.add(new TitleSubtitleAndIcon(content.getHba1cSection3Title(), content.getHba1cSection3Content(), R.drawable.onboarding_earn_points));
    }

    private void setCholesterolData(List<TitleSubtitleAndIcon> sectionsList) {
        sectionsList.add(new TitleSubtitleAndIcon(content.getCholesterolSection1Title(), content.getCholesterolSection1Content(), R.drawable.health_measure_cholesterol));
        sectionsList.add(new TitleSubtitleAndIcon(content.getCholesterolSection2Title(), content.getCholesterolSection2Content(), R.drawable.health_tips));
        sectionsList.add(new TitleSubtitleAndIcon(content.getCholesterolSection3Title(), content.getCholesterolSection3Content(), R.drawable.onboarding_earn_points));
    }

    private void setBloodPressureData(List<TitleSubtitleAndIcon> sectionsList) {
        sectionsList.add(new TitleSubtitleAndIcon(content.getBloodPressureSection1Title(), content.getBloodPressureSection1Content(), R.drawable.health_measure_bloodpressure));
        sectionsList.add(new TitleSubtitleAndIcon(content.getBloodPressureSection2Title(), content.getBloodPressureSection2Content(), R.drawable.health_tips));
        sectionsList.add(new TitleSubtitleAndIcon(content.getBloodPressureSection3Title(), content.getBloodPressureSection3Content(), R.drawable.onboarding_earn_points));
    }

    private void setBloodGlucoseData(List<TitleSubtitleAndIcon> sectionsList) {
        sectionsList.add(new TitleSubtitleAndIcon(content.getGlucoseSection1Title(), content.getGlucoseSection1Content(), R.drawable.health_measure_bloodglucose));
        sectionsList.add(new TitleSubtitleAndIcon(content.getGlucoseSection2Title(), content.getGlucoseSection2Content(), R.drawable.health_tips));
        sectionsList.add(new TitleSubtitleAndIcon(content.getGlucoseSection3Title(), content.getGlucoseSection3Content(), R.drawable.onboarding_earn_points));
    }

    private void setWaistCircumferenceData(List<TitleSubtitleAndIcon> sectionsList) {
        sectionsList.add(new TitleSubtitleAndIcon(content.getWaistCircumferenceSection1Title(), content.getWaistCircumferenceSection1Content(), R.drawable.health_measure_waist_circumference));
        sectionsList.add(new TitleSubtitleAndIcon(content.getWaistCircumferenceSection2Title(), content.getWaistCircumferenceSection2Content(), R.drawable.health_tips));
        sectionsList.add(new TitleSubtitleAndIcon(content.getWaistCircumferenceSection3Title(), content.getWaistCircumferenceSection3Content(), R.drawable.onboarding_earn_points));
    }

    private void setBodyMassIndexData(List<TitleSubtitleAndIcon> sectionsList) {
        sectionsList.add(new TitleSubtitleAndIcon(content.getBmiSection1Title(), content.getBmiSection1Content(), R.drawable.health_measure_bmi));
        sectionsList.add(new TitleSubtitleAndIcon(content.getBmiSection2Title(), content.getBmiSection2Content(), R.drawable.health_tips));
        sectionsList.add(new TitleSubtitleAndIcon(content.getBmiSection3Title(), content.getBmiSection3Content(), R.drawable.onboarding_earn_points));
    }

    private void setUrinaryProteinData(List<TitleSubtitleAndIcon> sectionsList) {
        sectionsList.add(new TitleSubtitleAndIcon(content.getUrinaryProteinSection1Title(), content.getUrinaryProteinSection1Content(), R.drawable.health_measure_urine));
        sectionsList.add(new TitleSubtitleAndIcon(content.getUrinaryProteinSection2Title(), content.getUrinaryProteinSection2Content(), R.drawable.health_tips));
        sectionsList.add(new TitleSubtitleAndIcon(content.getUrinaryProteinSection3Title(), content.getUrinaryProteinSection3Content(), R.drawable.onboarding_earn_points));
    }
}
