package com.vitalityactive.va.uicomponents.learnmore;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.vitalityactive.va.activerewards.dto.TitleAndSubtitle;

public class LearnMoreItem extends TitleAndSubtitle {
    private final int iconResourceId;
    private final String link;
    private final int resourceTintColor;
    private final int hexTintColor;
    private final boolean isIconTinted;
    private final View.OnClickListener onClickListener;

    public LearnMoreItem(String title, String subtitle) {
        this(title, subtitle, 0);
    }

    public LearnMoreItem(String title, String subtitle, int iconResourceId) {
        this(title, subtitle, iconResourceId, null, 0, 0, false);
    }

    public LearnMoreItem(String title,
                         String subtitle,
                         int iconResourceId,
                         String link,
                         int resourceTintColor,
                         int hexTintColor,
                         boolean isIconTinted) {
        super(title, subtitle);
        this.iconResourceId = iconResourceId;
        this.link = link;
        this.resourceTintColor = resourceTintColor;
        this.hexTintColor = hexTintColor;
        this.isIconTinted = isIconTinted;
        onClickListener = null;
    }

    public LearnMoreItem(String title,
                         String subtitle,
                         int iconResourceId,
                         String link,
                         int resourceTintColor,
                         int hexTintColor,
                         boolean isIconTinted,
                         View.OnClickListener onClickListener) {
        super(title, subtitle);
        this.iconResourceId = iconResourceId;
        this.link = link;
        this.resourceTintColor = resourceTintColor;
        this.hexTintColor = hexTintColor;
        this.isIconTinted = isIconTinted;
        this.onClickListener = onClickListener;
    }

    public LearnMoreItem(Builder builder){
        super(builder.title, builder.subtitle);
        this.iconResourceId = builder.iconResourceId;
        this.link = builder.link;
        this.resourceTintColor = builder.resourceTintColor;
        this.hexTintColor = builder.hexTintColor;
        this.isIconTinted = builder.isIconTinted;
        this.onClickListener = builder.onClickListener;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public String getLink() {
        return link;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public int getResourceTintColor() {
        return resourceTintColor;
    }

    public int getHexTintColor() {
        return hexTintColor;
    }

    public boolean isIconTinted() {
        return isIconTinted;
    }

    public static class Builder {
        private String title;
        private String subtitle;
        private int iconResourceId;
        private String link;
        private int resourceTintColor;
        private int hexTintColor;
        private boolean isIconTinted;
        View.OnClickListener onClickListener;

        public Builder(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }

        public Builder icon(@DrawableRes int icon) {
            this.iconResourceId = icon;
            return this;
        }

        public Builder link(String link, View.OnClickListener onClickListener) {
            this.link = link;
            this.onClickListener = onClickListener;
            return this;
        }

        public Builder resourceTintColor(@ColorRes int color) {
            this.resourceTintColor = color;
            return this;
        }

        public Builder hexTintColor(@ColorRes int color) {
            this.hexTintColor = color;
            return this;
        }

        public Builder iconTinted(boolean isIconTinted) {
            this.isIconTinted = isIconTinted;
            return this;
        }

        public LearnMoreItem build() {
            return new LearnMoreItem(this);
        }
    }
}
