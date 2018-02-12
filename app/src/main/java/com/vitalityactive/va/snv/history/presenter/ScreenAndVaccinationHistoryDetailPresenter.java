package com.vitalityactive.va.snv.history.presenter;

import android.content.Context;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.snv.dto.ScreeningVaccinationMetadataDto;
import com.vitalityactive.va.snv.history.views.model.SNVHistoryPresentableProof;
import com.vitalityactive.va.vhc.summary.PresentableProof;

import java.util.List;

/**
 * Created by dharel.h.rosell on 12/10/2017.
 */

public interface ScreenAndVaccinationHistoryDetailPresenter<UserInterface extends ScreenAndVaccinationHistoryDetailPresenter.UI>
        extends Presenter<UserInterface>{

    @Override
    void onUserInterfaceCreated(boolean isNewNavigation);

    @Override
    void onUserInterfaceAppeared();

    @Override
    void onUserInterfaceDisappeared(boolean isFinishing);

    @Override
    void onUserInterfaceDestroyed();

    void setContext(Context context);
    void setMetadaDTO(List<ScreeningVaccinationMetadataDto> metadaDTO);
    void setIsDialogShown(boolean isShown);
    List<ScreeningVaccinationMetadataDto> getMetadaDTO();

    interface UI {
//        void getPresentableProof(SNVHistoryPresentableProof providedPresentableProof);
        void getPresentableProof(List<String> providedPresentableProof);
        void showLoadingIndicator();
        void hideLoadingIndicator();
        void showAlertDialog();
    }
}
