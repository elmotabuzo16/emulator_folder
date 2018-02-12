package com.vitalityactive.va.activerewards.rewards.presenters;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.events.RewardVoucherSelectedEvent;
import com.vitalityactive.va.activerewards.rewards.interactor.RewardsInteractor;
import com.vitalityactive.va.activerewards.rewards.persistence.RewardVoucher;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.utilities.PackageManagerUtilities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RewardVoucherPresenterTest extends BaseRewardPresenterTest {
    private RewardVoucherPresenter presenter;
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private RewardsInteractor interactor;
    @Mock
    private MainThreadScheduler scheduler;
    @Mock
    private RewardVoucherPresenter.UserInterface userInterface;
    @Mock
    private PackageManagerUtilities packageManagerUtilities;
    private int rewardUniqueId = 1234;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        eventDispatcher = Mockito.spy(new EventDispatcher());
        scheduler = Mockito.spy(new SameThreadScheduler());

        presenter = new RewardVoucherPresenter(packageManagerUtilities, interactor, eventDispatcher, scheduler);
        presenter.setUserInterface(userInterface);
        presenter.setUniqueId(rewardUniqueId);
    }


    @Test
    public void event_listeners_are_added_when_user_interface_created() {
        presenter.onUserInterfaceCreated(true);

        verify(eventDispatcher).addEventListener(eq(RewardVoucherSelectedEvent.class), Matchers.<EventListener<RewardVoucherSelectedEvent>>any());
    }

    @Test
    public void event_listeners_are_removed_when_user_interface_disappear() {
        presenter.onUserInterfaceDisappeared(true);

        verify(eventDispatcher).removeEventListener(eq(RewardVoucherSelectedEvent.class), Matchers.<EventListener<RewardVoucherSelectedEvent>>any());
    }

    @Test
    public void reward_voucher_is_fetched_if_it_does_not_exist() {
        presenter.onUserInterfaceAppeared();

        verify(interactor).fetchRewardVoucher(rewardUniqueId);
    }

    @Test
    public void reward_not_issued_is_displayed_if_reward_cancelled() {
        RewardVoucherDTO voucherDTO = new RewardVoucherDTO(new RewardVoucher(getAwardedReward(), 9));
        when(interactor.getRewardVoucherById(rewardUniqueId)).thenReturn(voucherDTO);

        presenter.onUserInterfaceAppeared();

        verify(userInterface).showNotAwardedScreen(voucherDTO);
    }

    @Test
    public void pending_state_is_displayed_if_reward_is_pending() {
        RewardVoucherDTO voucherDTO = new RewardVoucherDTO(new RewardVoucher(getAwardedReward(), 2));
        when(interactor.getRewardVoucherById(rewardUniqueId)).thenReturn(voucherDTO);

        presenter.onUserInterfaceAppeared();

        verify(userInterface).showPendingRewardState(voucherDTO);
    }

    @Test
    public void voucher_selected_event_is_handled_if_user_interface_had_disappeared() {
        RewardVoucherDTO voucherDTO = new RewardVoucherDTO(new RewardVoucher(getAwardedReward(), 2));
        when(interactor.getRewardVoucherById(rewardUniqueId)).thenReturn(voucherDTO);

        presenter.onUserInterfaceCreated(true);

        presenter.onUserInterfaceDisappeared(false);

        eventDispatcher.dispatchEvent(new RewardVoucherSelectedEvent(rewardUniqueId));

        presenter.onUserInterfaceAppeared();

        verify(userInterface).showPendingRewardState(voucherDTO);
    }

    @Test
    public void starbucks_state_is_display_if_voucher_is_starbucks() {
        RewardVoucherDTO voucherDTO = new RewardVoucherDTO(new RewardVoucher(getAwardedReward(), 1));
        when(interactor.getRewardVoucherById(rewardUniqueId)).thenReturn(voucherDTO);

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        verify(userInterface).showStarbucksAvailableRewardState(voucherDTO);
    }
}
