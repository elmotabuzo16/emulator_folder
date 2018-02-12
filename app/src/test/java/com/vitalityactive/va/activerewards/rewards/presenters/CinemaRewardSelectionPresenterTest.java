package com.vitalityactive.va.activerewards.rewards.presenters;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.activerewards.rewards.dto.RewardSelectionDTO;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.events.RewardVoucherSelectedEvent;
import com.vitalityactive.va.activerewards.rewards.interactor.RewardsInteractor;
import com.vitalityactive.va.activerewards.rewards.persistence.RewardSelection;
import com.vitalityactive.va.activerewards.rewards.persistence.RewardVoucher;
import com.vitalityactive.va.activerewards.rewards.service.RewardsServiceResponse;
import com.vitalityactive.va.events.EventDispatcher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CinemaRewardSelectionPresenterTest {

    private static final int REWARD_ID = 260003;
    private static final long AWARDED_REWARD_ID = 12345;
    private static final int SELECTED_REWARD_ID = 12346;

    @Mock
    private RewardsInteractor interactor;
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private MainThreadScheduler scheduler;
    @Mock
    private CinemaRewardSelectionPresenter.UserInterface userInterface;

    private CinemaRewardSelectionPresenterImpl presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = spy(new CinemaRewardSelectionPresenterImpl(interactor, eventDispatcher, scheduler));
        presenter.setUserInterface(userInterface);
        presenter.setRewardUniqueId(AWARDED_REWARD_ID);
    }

    @Test
    public void should_schedule_reward_voucher_selected_event_for_handling() throws Exception {
        presenter.onEvent(mock(RewardVoucherSelectedEvent.class));
        verify(scheduler).schedule(any(Runnable.class));
    }

    @Test
    public void should_show_generic_error_message_when_voucher_selection_request_fails() throws Exception {
        presenter.setRewardSelectionsRequestResult(RewardVoucherSelectedEvent.GENERIC_ERROR);
        presenter.handleRewardSelectionsRequestResult();
        verify(userInterface).showGenericErrorMessage();
    }

    @Test
    public void should_show_connection_error_message_when_voucher_selection_request_fails() throws Exception {
        presenter.setRewardSelectionsRequestResult(RewardVoucherSelectedEvent.CONNECTION_ERROR);
        presenter.handleRewardSelectionsRequestResult();
        verify(userInterface).showConnectionErrorMessage();
    }

    @Test
    public void should_show_reward_selections() throws Exception {
        List<RewardSelectionDTO> selections = Collections.emptyList();
        when(interactor.getSelectionsForCinemaRewardWithId(anyLong())).thenReturn(selections);
        presenter.setRewardSelectionsRequestResult(new RewardVoucherSelectedEvent(AWARDED_REWARD_ID));

        presenter.handleRewardSelectionsRequestResult();

        InOrder inOrder = Mockito.inOrder(userInterface, interactor);
        inOrder.verify(userInterface).hideLoadingIndicator();
        inOrder.verify(interactor).getSelectionsForCinemaRewardWithId(AWARDED_REWARD_ID);
        inOrder.verify(userInterface).showRewardSelections(selections);
    }

    @Test
    public void should_show_generic_error_message_when_selected_voucher_not_found() throws Exception {
        presenter.setRewardSelectionsRequestResult(new RewardVoucherSelectedEvent(SELECTED_REWARD_ID));
        presenter.handleRewardSelectionsRequestResult();
        verify(userInterface).showGenericErrorMessage();
    }

    @Test
    public void should_navigate_after_reward_voucher_selected() throws Exception {
        long selectedRewardId = 23456;
        RewardVoucherDTO voucherDto = new RewardVoucherDTO(createVoucher());
        when(interactor.getRewardVoucherById(anyLong())).thenReturn(voucherDto);
        presenter.setRewardSelectionsRequestResult(new RewardVoucherSelectedEvent(selectedRewardId));

        presenter.handleRewardSelectionsRequestResult();
        verify(userInterface).navigateAfterRewardVoucherSelected(voucherDto.uniqueId);
    }

    @Test
    public void should_add_event_listener_and_start_fetching_voucher_when_ui_created() throws Exception {
        presenter.onUserInterfaceCreated(true);

        InOrder inOrder = Mockito.inOrder(eventDispatcher, interactor);
        inOrder.verify(eventDispatcher).removeEventListener(RewardVoucherSelectedEvent.class, presenter);
        inOrder.verify(eventDispatcher).addEventListener(RewardVoucherSelectedEvent.class, presenter);
        inOrder.verify(interactor).fetchRewardVoucher(AWARDED_REWARD_ID);
    }

    @Test
    public void should_show_loading_indicator_when_ui_appears_and_we_are_fetching_reward_selections() throws Exception {
        when(interactor.isFetchingRewardSelections(anyLong())).thenReturn(true);
        presenter.onUserInterfaceAppeared();
        verify(userInterface).showLoadingIndicator();
    }

    @Test
    public void should_handle_request_result_when_ui_appears_and_and_we_are_not_fetching_reward_selections() throws Exception {
        when(interactor.isFetchingRewardSelections(anyLong())).thenReturn(false);
        presenter.onUserInterfaceAppeared();
        verify(userInterface).hideLoadingIndicator();
        verify(presenter).handleRewardSelectionsRequestResult();
    }

    @Test
    public void should_remove_event_listener_when_ui_disappears() throws Exception {
        presenter.onUserInterfaceDisappeared(true);
        verify(eventDispatcher).removeEventListener(RewardVoucherSelectedEvent.class, presenter);
    }

    @Test
    public void should_fetch_reward_voucher() throws Exception {
        presenter.fetchRewardVoucher();
        verify(interactor).fetchRewardVoucher(AWARDED_REWARD_ID);
    }

    @Test
    public void should_confirm_reward_voucher_selection() throws Exception {
        RewardSelectionDTO rewardSelection = new RewardSelectionDTO(new RewardSelection());
        presenter.onConfirm(rewardSelection);

        InOrder inOrder = Mockito.inOrder(userInterface, interactor);
        inOrder.verify(userInterface).showLoadingIndicator();
        inOrder.verify(interactor).selectVoucher(AWARDED_REWARD_ID, rewardSelection.rewardValueLinkId);
    }

    private RewardVoucher createVoucher() {
        RewardsServiceResponse.AwardedReward awardedReward = new RewardsServiceResponse.AwardedReward();
        awardedReward.effectiveFrom = "1970-01-01";
        awardedReward.effectiveTo = "9999-12-31";
        awardedReward.effectiveFrom = "0000-12-31";
        awardedReward.id = SELECTED_REWARD_ID;
        RewardsServiceResponse.Reward reward = new RewardsServiceResponse.Reward();
        reward.name = "Test Reward";
        reward.id = REWARD_ID;
        awardedReward.reward = reward;
        return new RewardVoucher(awardedReward, 1);
    }

}