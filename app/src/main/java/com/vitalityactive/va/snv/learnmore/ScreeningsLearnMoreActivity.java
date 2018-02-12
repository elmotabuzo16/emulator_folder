package com.vitalityactive.va.snv.learnmore;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.snv.learnmore.content.ScreeningDeclarationContent;
import com.vitalityactive.va.snv.shared.SnvConstants;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreActivity;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by stephen.rey.w.avila on 11/30/2017.
 */

public class ScreeningsLearnMoreActivity extends LearnMoreActivity {

    @Inject
    ScreeningDeclarationContent content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.sv_activity_learn_more);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.learn_more_button_title_104;
    }

    @Override
    protected String getHeaderTitle() {
        return content.getLearnMoreTitle();
    }

    @Override
    protected String getHeaderSubTitle() {
        return content.getLearnMoreContent();
    }

    @Override
    protected List<List<LearnMoreItem>> createListData() {
        setViewResourceId(R.layout.view_sv_learn_more_container_flat);

        List<LearnMoreItem> items = new ArrayList<>();
        items.add(new LearnMoreItem(content.getLearnMoreSection1Title(),content.getLearnMoreSection1Content(),R.drawable.stethoscope_med,
                content.getLearnMoreSection1LinkContent(),
                R.color.jungle_green, R.color.jungle_green, true, onclick));

        items.add(new LearnMoreItem(content.getLearnMoreSection2Title(), content.getLearnMoreSection2Content(), R.drawable.checkmark_med));
        items.add(new LearnMoreItem(content.getLearnMoreSection3Title(), content.getLearnMoreSection3Content(), R.drawable.earn_points_40,null, R.color.jungle_green, R.color.jungle_green, true));

        if(checkClass(this)){
            findViewById(R.id.toolbar).setBackgroundColor(ContextCompat.getColor(this, R.color.jungle_green));
        }

        return Collections.singletonList(items);
    }

    private View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navigationCoordinator.navigateToSnvParticipatingPartnersActivity(getActivity());
        }
    };

    @Override
    protected GenericRecyclerViewAdapter createCustomViewAdapter() {
        return new GenericRecyclerViewAdapter<>(ScreeningsLearnMoreActivity.this,
                createSnvItems(),
                R.layout.screening_list_row,
                new ScreeningViewHolder.Factory(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navigationCoordinator.navigateToLearnMoreScreenings(getActivity(),SnvConstants.HEALTH_ACTION_SCREENINGS);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navigationCoordinator.navigateToLearnMoreScreenings(getActivity(),SnvConstants.HEALTH_ACTION_VACCINATIONS);
                    }
                })
        );
    }

private LearnMoreScreeningItem createSnvItems() {
    return new LearnMoreScreeningItem(getString(R.string.SV_screenings_title_1012), R.drawable.screenings,getString(R.string.SV_vaccinations_title_1013), R.drawable.vaccinations);
}

    @Override
    protected void injectDependency() {
        getDependencyInjector().inject(this);
    }

    private boolean checkClass(Activity activity){
        if(activity instanceof ScreeningsLearnMoreActivity){
           return true;
        }
        return false;
    }
    private Activity getActivity() {
        return this;
    }

}
