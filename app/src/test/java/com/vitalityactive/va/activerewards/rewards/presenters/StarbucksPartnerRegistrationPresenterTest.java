package com.vitalityactive.va.activerewards.rewards.presenters;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.activerewards.rewards.events.PartnerRegisteredEmailFetchedEvent;
import com.vitalityactive.va.activerewards.rewards.events.PartnerRegisteredEmailUpdatedEvent;
import com.vitalityactive.va.activerewards.rewards.events.RewardVoucherSelectedEvent;
import com.vitalityactive.va.activerewards.rewards.interactor.RewardsInteractor;
import com.vitalityactive.va.activerewards.rewards.persistence.RewardVoucher;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StarbucksPartnerRegistrationPresenterTest extends BaseRewardPresenterTest {

    private StarbucksPartnerRegistrationPresenter presenter;
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private RewardsInteractor interactor;
    @Mock
    private MainThreadScheduler scheduler;
    @Mock
    private StarbucksPartnerRegistrationPresenter.UserInterface userInterface;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        eventDispatcher = Mockito.spy(new EventDispatcher());
        scheduler = Mockito.spy(new SameThreadScheduler());

        presenter = new StarbucksPartnerRegistrationPresenter(interactor, scheduler, eventDispatcher);
        presenter.setUserInterface(userInterface);
        presenter.setRewardUniqueId(rewardUniqueId);
    }

    @Test
    public void is_reward_data_set_when_user_interface_created() {
        UnclaimedRewardDTO unclaimedReward = getUnclaimedReward();
        when(interactor.getWheelSpin(rewardUniqueId)).thenReturn(unclaimedReward);

        presenter.onUserInterfaceCreated(true);
        verify(userInterface).setRewardData(unclaimedReward);
    }

    @Test
    public void data_sharing_is_shown_if_applicable() {
        when(interactor.shouldShowDataSharingConsentForWheelSpinOutcome(rewardUniqueId)).thenReturn(true);

        presenter.onUserConfirmsEmailAddress("bobby@jack.com");
        verify(userInterface).showDataSharingConsent();
    }

    @Test
    public void user_email_is_updated_if_it_has_changed() {
        when(interactor.shouldShowRewardPartnerDataSharingConsent(0)).thenReturn(false);
        when(interactor.getPartnerRegisteredEmail()).thenReturn("bobby@jack.com");

        String changedUserEmail = "changed@jack.com";
        presenter.onUserConfirmsEmailAddress(changedUserEmail);
        verify(interactor).updatePartnerRegisteredEmail(changedUserEmail);
    }

    @Test
    public void user_email_is_not_updated_if_it_has_not_changed() {
        when(interactor.shouldShowRewardPartnerDataSharingConsent(0)).thenReturn(false);
        String userEmail = "bobby@jack.com";
        when(interactor.getPartnerRegisteredEmail()).thenReturn(userEmail);

        presenter.onUserConfirmsEmailAddress(userEmail);
        verify(interactor, never()).updatePartnerRegisteredEmail(anyString());
    }

    @Test
    public void data_sharing_is_not_shown_if_inapplicable() {
        when(interactor.shouldShowRewardPartnerDataSharingConsent(0)).thenReturn(false);

        presenter.onUserConfirmsEmailAddress("bobby@jack.com");
        verify(userInterface, never()).showDataSharingConsent();
    }

    @Test
    public void email_is_fetched_on_interface_creation_if_not_present() {
        when(interactor.getPartnerRegisteredEmail()).thenReturn("");

        presenter.onUserInterfaceCreated(true);
        verify(interactor).fetchPartnerRegisteredEmail();
    }

    @Test
    public void only_email_fetched_event_listener_is_added_if_data_consent_will_show() {
        when(interactor.shouldShowDataSharingConsentForWheelSpinOutcome(rewardUniqueId)).thenReturn(true);

        presenter.onUserInterfaceCreated(true);

        verify(eventDispatcher).addEventListener(eq(PartnerRegisteredEmailFetchedEvent.class), Matchers.<EventListener<PartnerRegisteredEmailFetchedEvent>>any());
        verify(eventDispatcher, never()).addEventListener(eq(RewardVoucherSelectedEvent.class), Matchers.<EventListener<RewardVoucherSelectedEvent>>any());
        verify(eventDispatcher, never()).addEventListener(eq(PartnerRegisteredEmailUpdatedEvent.class), Matchers.<EventListener<PartnerRegisteredEmailUpdatedEvent>>any());
    }

    @Test
    public void all_event_listeners_are_added_if_data_consent_will_not_show() {
        when(interactor.shouldShowRewardPartnerDataSharingConsent(rewardUniqueId)).thenReturn(false);

        presenter.onUserInterfaceCreated(true);

        verify(eventDispatcher).addEventListener(eq(PartnerRegisteredEmailFetchedEvent.class), Matchers.<EventListener<PartnerRegisteredEmailFetchedEvent>>any());
        verify(eventDispatcher).addEventListener(eq(RewardVoucherSelectedEvent.class), Matchers.<EventListener<RewardVoucherSelectedEvent>>any());
        verify(eventDispatcher).addEventListener(eq(PartnerRegisteredEmailUpdatedEvent.class), Matchers.<EventListener<PartnerRegisteredEmailUpdatedEvent>>any());
    }

    @Test
    public void all_event_listeners_are_removed_when_activity_is_destroyed() {
        presenter.onUserInterfaceDisappeared(true);

        verify(eventDispatcher).removeEventListener(eq(PartnerRegisteredEmailFetchedEvent.class), Matchers.<EventListener<PartnerRegisteredEmailFetchedEvent>>any());
        verify(eventDispatcher).removeEventListener(eq(RewardVoucherSelectedEvent.class), Matchers.<EventListener<RewardVoucherSelectedEvent>>any());
        verify(eventDispatcher).removeEventListener(eq(PartnerRegisteredEmailUpdatedEvent.class), Matchers.<EventListener<PartnerRegisteredEmailUpdatedEvent>>any());
        verify(eventDispatcher, never()).addEventListener(any(Class.class), Matchers.<EventListener>any());
    }

    @Test
    public void show_registered_email_if_email_fetched_event_is_received() {
        presenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(new PartnerRegisteredEmailFetchedEvent());

        verify(userInterface).showPartnerRegisteredEmail(anyString());
    }

    @Test
    public void reward_selected_event_is_handled() {
        when(interactor.getRewardVoucherById(rewardUniqueId)).thenReturn(new RewardVoucherDTO(new RewardVoucher(getAwardedReward(), 0)));

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(new RewardVoucherSelectedEvent(rewardUniqueId));

        verify(scheduler).schedule(any(Runnable.class));

        isLoadingIndicatorIsHidden();
    }

    @Test
    public void reward_selected_event_is_handled_if_user_interface_was_hidden() {
        when(interactor.getRewardVoucherById(rewardUniqueId)).thenReturn(new RewardVoucherDTO(new RewardVoucher(getAwardedReward(), 0)));

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceDisappeared(false);

        eventDispatcher.dispatchEvent(new RewardVoucherSelectedEvent(rewardUniqueId));

        presenter.onUserInterfaceAppeared();

        verify(scheduler).schedule(any(Runnable.class));

        isLoadingIndicatorIsHidden();
    }

    private void isLoadingIndicatorIsHidden() {
        InOrder inOrder = inOrder(userInterface, userInterface);
        inOrder.verify(userInterface).showLoadingIndicator();
        inOrder.verify(userInterface).hideLoadingIndicator();
    }

    @Test
    public void email_updated_event_is_handled() {
        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(PartnerRegisteredEmailUpdatedEvent.SUCCESSFUL);

        verify(interactor).selectVoucher(rewardUniqueId);

        isLoadingIndicatorIsHidden();
    }

    @Test
    public void email_updated_event_is_handled_if_user_interface_was_hidden() {
        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceDisappeared(false);

        eventDispatcher.dispatchEvent(PartnerRegisteredEmailUpdatedEvent.SUCCESSFUL);

        presenter.onUserInterfaceAppeared();

        verify(interactor).selectVoucher(rewardUniqueId);

        isLoadingIndicatorIsHidden();
    }

    @Test
    public void email_update_displays_connection_error() {
        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(PartnerRegisteredEmailUpdatedEvent.CONNECTION_ERROR);

        verify(userInterface).showConnectionErrorMessage();

        isLoadingIndicatorIsHidden();
    }

    @Test
    public void email_update_displays_generic_error() {
        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(PartnerRegisteredEmailUpdatedEvent.GENERIC_ERROR);

        verify(userInterface).showGenericErrorMessage();

        isLoadingIndicatorIsHidden();
    }

    @Test
    public void reward_selected_displays_connection_error() {
        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(RewardVoucherSelectedEvent.CONNECTION_ERROR);

        verify(userInterface).showConnectionErrorMessage();

        isLoadingIndicatorIsHidden();
    }

    @Test
    public void reward_selected_displays_generic_error() {
        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(RewardVoucherSelectedEvent.GENERIC_ERROR);

        verify(userInterface).showGenericErrorMessage();

        isLoadingIndicatorIsHidden();
    }
}
