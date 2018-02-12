package com.vitalityactive.va.membershippass.presenter;

import com.vitalityactive.va.Presenter;

/**
 * Created by christian.j.p.capin on 11/14/2017.
 */
public interface MembershipPassPresenter extends Presenter<MembershipPassPresenter.UI> {

    interface UI {
        void showMembershipInfo(long partyId, String vitalityNumber, String membershipNumber, String customerNumber, String vitalityStatus, String membershipStartDate, String membershipStatus);
        void showProfileInitials(String initials);

        void showProfileImage(String imagePath);
        void activityDestroyed();
    }
}
