package com.vitalityactive.va.snv.learnmore;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.vitalityactive.va.activerewards.dto.TitleAndSubtitle;

/**
 * Created by stephen.rey.w.avila on 12/6/2017.
 */

public class ScreeningListDescriptionItem extends TitleAndSubtitle {

    private final int iconResourceId;
    private final String link;
    private final int tintColor;
    private final boolean isIconTinted;
    private final View.OnClickListener onClickListener;

    public ScreeningListDescriptionItem(String title, String subtitle) {
        this(title, subtitle, 0);
    }

    public ScreeningListDescriptionItem(String title, String subtitle, int iconResourceId){
        this(title, subtitle, iconResourceId, null, 0, false);
    }

    public ScreeningListDescriptionItem(String title,
                         String subtitle,
                         int iconResourceId,
                         String link,
                         int tintColor,
                         boolean isIconTinted) {
        super(title, subtitle);
        this.iconResourceId = iconResourceId;
        this.link = link;
        this.tintColor = tintColor;
        this.isIconTinted = isIconTinted;
        onClickListener = null;
    }

    public ScreeningListDescriptionItem(Builder builder){
        super(builder.title, builder.subtitle);
        this.iconResourceId = builder.iconResourceId;
        this.link = builder.link;
        this.tintColor = builder.tintColor;
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

    public int getTintColor() {
        return tintColor;
    }

    public boolean isIconTinted() {
        return isIconTinted;
    }

    public static class Builder{
        private String title;
        private String subtitle;
        private int iconResourceId;
        private String link;
        private int tintColor;
        private boolean isIconTinted;
        View.OnClickListener onClickListener;

        public Builder(String title, String subtitle){
            this.title = title;
            this.subtitle = subtitle;
        }

        public Builder icon(@DrawableRes int icon){
            this.iconResourceId = icon;
            return this;
        }

        public Builder link(String link, View.OnClickListener onClickListener){
            this.link = link;
            this.onClickListener = onClickListener;
            return this;
        }

        public Builder tintColor(@ColorRes int color){
            this.tintColor = color;
            return this;
        }

        public Builder iconTinted(boolean isIconTinted){
            this.isIconTinted = isIconTinted;
            return this;
        }
        public ScreeningListDescriptionItem build(){
            return new ScreeningListDescriptionItem(this);
        }
    }
}
