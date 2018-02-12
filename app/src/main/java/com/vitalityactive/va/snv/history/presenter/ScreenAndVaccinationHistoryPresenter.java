package com.vitalityactive.va.snv.history.presenter;

import android.content.Context;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.snv.dto.HistoryDetailDto;
import com.vitalityactive.va.snv.dto.ListHistoryListDto;

import java.util.List;

/**
 * Created by dharel.h.rosell on 12/1/2017.
 */

public interface ScreenAndVaccinationHistoryPresenter<UserInterface extends ScreenAndVaccinationHistoryPresenter.UserInterface>
        extends Presenter<UserInterface> {

    @Override
    void onUserInterfaceCreated(boolean isNewNavigation);

    @Override
    void onUserInterfaceAppeared();

    @Override
    void onUserInterfaceDisappeared(boolean isFinishing);

    @Override
    void onUserInterfaceDestroyed();

    void setContext(Context context);

    interface UserInterface {
        void getHistoryListDto(List<ListHistoryListDto> historyListDtos);
        void showLoadingIndicator();
        void hideLoadingIndicator();
        void showEmptyListMessage();
    }
}
