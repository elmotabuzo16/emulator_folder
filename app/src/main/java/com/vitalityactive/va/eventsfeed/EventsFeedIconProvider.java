package com.vitalityactive.va.eventsfeed;

import com.vitalityactive.va.R;

import javax.inject.Inject;

public class EventsFeedIconProvider {

    @Inject
    public EventsFeedIconProvider() {

    }

    public int getIconResourceId(int categoryTypeKey) {
        switch (categoryTypeKey) {
            case 999: return R.drawable.pmfilter_all;
            case EventCategoryList.LOGIN: return R.drawable.activation_40;
            case EventCategoryList.AGREEMENT: return R.drawable.pmfilter_other; //TODO: change to correct icon
            case EventCategoryList.SERVICING: return R.drawable.pmfilter_other; //TODO: change to correct icon
            case EventCategoryList.TARGET: return R.drawable.pmfilter_other; //TODO: change to correct icon
            case EventCategoryList.ASSESSMENT: return R.drawable.assessments_40;
            case EventCategoryList.REWARD: return R.drawable.rewards_40;
            case EventCategoryList.ERROR: return R.drawable.pmfilter_other; //TODO: change to correct icon
            case EventCategoryList.NOTIFICATION: return R.drawable.pmfilter_other; //TODO: change to correct icon
            case EventCategoryList.FINANCE: return R.drawable.events_financials_40;
            case EventCategoryList.SOCIAL: return R.drawable.pmfilter_other; //TODO: change to correct icon
            case EventCategoryList.POINTS: return R.drawable.pmfilter_other; //TODO: change to correct icon
            case EventCategoryList.DEVICE: return R.drawable.events_devices_40;
            case EventCategoryList.INTEGRATION: return R.drawable.pmfilter_other;//TODO: change to correct icon
            case EventCategoryList.PRODUCT: return R.drawable.pmfilter_other;//TODO: change to correct icon
            case EventCategoryList.STATUS: return R.drawable.events_status_40;
            case EventCategoryList.DOCUMENTMANAGEMENT: return R.drawable.doc_icon_40;
            case EventCategoryList.LEGAL: return R.drawable.pmfilter_other;//TODO: change to correct icon
            case EventCategoryList.HEALTHATTRIBUTE: return R.drawable.healthdata_40;
            case EventCategoryList.TERMSANDCONDITIONS: return R.drawable.pmfilter_other;//TODO: change to correct icon
            case EventCategoryList.DATAPRIVACY: return R.drawable.events_legal_40;
            case EventCategoryList.ENROLLMENT: return R.drawable.events_profile_management_40;
            case EventCategoryList.VHC_CAT: return R.drawable.pmfilter_other;//TODO: change to correct icon
            case EventCategoryList.SCREENINGS: return R.drawable.pmfilter_screening;
            case EventCategoryList.VACCINATIONS: return R.drawable.pmfilter_screening;//TODO: change to correct icon
            case EventCategoryList.SCREENANDVACC: return R.drawable.pmfilter_screening;//TODO: change to correct icon
            case EventCategoryList.NUTRITION: return R.drawable.pmfilter_nutrition;
            case EventCategoryList.FITNESS: return R.drawable.pmfilter_getactive;
            case EventCategoryList.DISCLAIMER: return R.drawable.pmfilter_other;//TODO: change to correct icon
            case EventCategoryList.UNKNOWN_CAT: return R.drawable.pmfilter_other;//TODO: change to correct icon
            default:
                return R.drawable.pmfilter_other;
        }
    }
}
