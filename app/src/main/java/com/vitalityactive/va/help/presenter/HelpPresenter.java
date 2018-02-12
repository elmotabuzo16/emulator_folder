package com.vitalityactive.va.help.presenter;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.dto.HelpDTO;

import java.util.List;


public interface HelpPresenter  <UI extends HelpPresenter.UI> extends Presenter<UI> {

    void loadSuggestion();

    interface UI {
        void showSuggestions(List<HelpDTO> fiveHelp, List<HelpDTO> allQuestions);
        void activityDestroyed();

        void showLoadingIndicator();

        void hideLoadingIndicator();
    }
}
