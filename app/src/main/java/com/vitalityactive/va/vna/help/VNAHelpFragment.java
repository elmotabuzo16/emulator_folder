package com.vitalityactive.va.vna.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.help.HelpFragment;

import java.util.ArrayList;
import java.util.List;


public class VNAHelpFragment extends HelpFragment{

    private TextView txtSuggestions;

    @Override
    protected void activityCreated(@Nullable Bundle savedInstanceState) {
        super.activityCreated(savedInstanceState);

        txtSuggestions = getView().findViewById(R.id.txtSuggestions);
        txtSuggestions.setText(R.string.help_empty_title_316);
    }
    
    protected List<String> fetchResults() {
        List<String> stringQuestion = new ArrayList<>();
        List<String> newStringQ = new ArrayList<>();
        stringQuestion.add("<p>How do I improve my nutrition?</p>");
        stringQuestion.add("<p>How many times can I complete the Vitality Nutrition Assessement?</p>");
        stringQuestion.add("<p>Where can I track my results?</p>");

        for(int i = 0; i < stringQuestion.size(); i++){
            newStringQ.add(stringQuestion.get(i).replaceAll("</?(span|p|div){1}.*?/?>", ""));
        }

        return newStringQ;
    }

    @Override
    public void onItemClick(String item) {

    }
}
