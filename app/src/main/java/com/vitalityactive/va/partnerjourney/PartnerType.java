package com.vitalityactive.va.partnerjourney;

import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.ProductFeatureCategory;

public enum PartnerType {
    HEALTH(R.style.AppTheme_KnowYourHealth,
            R.string.HealthPartners_screen_title_897, R.layout.wellness_partner_header_layout,
            R.drawable.img_placeholder,
            "partner-templates/partner_template.html",
            ProductFeatureCategory._HEALTHPARTNERS),
    WELLNESS(R.style.AppTheme_ImproveYourHealth,
            R.string.WellnessPartners_screen_title_608, R.layout.wellness_partner_header_layout,
            R.drawable.img_placeholder,
            "partner-templates/partner_template.html",
            ProductFeatureCategory._WELLNESSPARTNERS),
    REWARDS(R.style.AppTheme_Rewards,
            R.string.RewardPartners_screen_title_898, R.layout.wellness_partner_header_layout,
            R.drawable.img_placeholder,
            "partner-templates/partner_template.html",
            ProductFeatureCategory._REWARDPARTNERS),
    NON_SMOKERS(R.style.AppTheme_KnowYourHealth,
            R.string.participating_partners_title_262, R.layout.non_smokers_partner_header_layout,
            R.drawable.nonsmokers_noimage_partner,
            "partner-templates/partner_template.html",
            ProductFeatureCategory._WELLNESSPARTNERS),
    CORPORATE(R.style.AppTheme_KnowYourHealth,
            R.string.health_services_title_1331, R.layout.health_services_header_layout,
            R.drawable.img_placeholder,
            "partner-templates/partner_template.html",
            ProductFeatureCategory._CORPORATEBENEFIT);

    public final int listScreenTitle;
    public final int theme;
    public final int productFeatureCategoryTypeKey;
    public final int listHeaderLayout;
    public final int listItemPlaceholderImage;
    public final Class<?> detailsActivityClass;
    public final String detailsHtmlTemplateFile;

    PartnerType(@StyleRes int theme, @StringRes int listScreenTitle, int listHeaderLayout, int listItemPlaceholderImage, String detailsHtmlTemplateFile, int productFeatureCategoryTypeKey) {
        this.listScreenTitle = listScreenTitle;
        this.theme = theme;
        this.listItemPlaceholderImage = listItemPlaceholderImage;
        this.productFeatureCategoryTypeKey = productFeatureCategoryTypeKey;
        this.listHeaderLayout = listHeaderLayout;
        this.detailsHtmlTemplateFile = detailsHtmlTemplateFile;
        this.detailsActivityClass = PartnerDetailsActivity.class;
    }
}
