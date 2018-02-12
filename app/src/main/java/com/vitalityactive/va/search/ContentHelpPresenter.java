package com.vitalityactive.va.search;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.dto.ContentHelpDTO;

import java.util.List;

/**
 * Created by chelsea.b.pioquinto on 1/29/2018.
 */

public interface ContentHelpPresenter <UserInterface extends ContentHelpPresenter.UserInterface> extends Presenter<UserInterface> {

    void loadHelpDetails(String tagkey, String tagName);

    interface UserInterface {
        void loadDetails();
        void setDetailsView(List<ContentHelpDTO> contents);
        void setRelatedHelp(List<ContentHelpDTO> relatedHelp);
        void setHelpFeedback();
        void showLoadingIndicator();
        void hideLoadingIndicator();
    }
}
