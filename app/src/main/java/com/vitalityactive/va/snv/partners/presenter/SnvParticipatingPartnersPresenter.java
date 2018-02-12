package com.vitalityactive.va.snv.partners.presenter;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.snv.partners.dto.ProductFeatureGroupDto;
import com.vitalityactive.va.utilities.CMSImageLoader;

import java.util.List;

/**
 * Created by kerry.e.lawagan on 12/8/2017.
 */

public interface SnvParticipatingPartnersPresenter<UserInterface extends SnvParticipatingPartnersPresenter.UserInterface> extends Presenter<UserInterface> {
    CMSImageLoader getCmsImageLoader();
    interface UserInterface {
        void updateListItems(List<ProductFeatureGroupDto> eventTypeDtos);
        void showGenericContentRequestErrorMessage();
        void showLoadingIndicator();
        void hideLoadingIndicator();
        void showConnectionContentRequestErrorMessage();
    }
}
