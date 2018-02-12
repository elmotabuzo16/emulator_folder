package com.vitalityactive.va.eventsfeed;

public class EventsFeedTitleProvider {

    public static String getCategoryTitle(int categoryTypeKey) {
        switch (categoryTypeKey) {
            case 999: return "All Events";
            case EventCategoryList.ASSESSMENT: return "Assessment";
            case EventCategoryList.NUTRITION: return "Nutrition";
            case EventCategoryList.SCREENINGS: return "Screening";
            case EventCategoryList.FITNESS: return "Health Data";
            case EventCategoryList.HEALTHATTRIBUTE: return "Get Active";
            case EventCategoryList.DEVICE: return "Devices";
            case EventCategoryList.LOGIN: return "Activation";
            case EventCategoryList.REWARD: return "Rewards";
            case EventCategoryList.STATUS: return "Status";
            case EventCategoryList.DATAPRIVACY: return "Data Sharing and Legal";
            case EventCategoryList.ENROLLMENT: return "Profile Management";
            case EventCategoryList.FINANCE: return "Financials";
            case EventCategoryList.DOCUMENTMANAGEMENT: return "Documents";
            case EventCategoryList.SERVICING: return "Servicing";
            default:
                return "Other";
        }
    }

}
