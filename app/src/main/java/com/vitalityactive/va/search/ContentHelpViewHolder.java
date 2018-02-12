package com.vitalityactive.va.search;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dto.ContentHelpDTO;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import javax.inject.Inject;

import static com.vitalityactive.va.utilities.ViewUtilities.getColorPrimaryFromTheme;
import static com.vitalityactive.va.utilities.ViewUtilities.setResourceOfView;

/**
 * Created by chelsea.b.pioquinto on 2/1/2018.
 */

public class ContentHelpViewHolder extends GenericRecyclerViewAdapter.ViewHolder<ContentHelpDTO> {

    private final ImageView icon;
    private final TextView title;
    private int globalTintColor;

    @Inject
    AppConfigRepository appConfigRepository;

    private ContentHelpViewHolder(View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.icon);
        title = itemView.findViewById(R.id.label);
    }

    @Override
    public void bindWith(ContentHelpDTO dataItem) {
        title.setText(dataItem.getQuestion());
        setResourceOfView(icon, R.drawable.health_tips_24);
        icon.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
    }


    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<ContentHelpDTO, ContentHelpViewHolder> {
        @Override
        public ContentHelpViewHolder createViewHolder(View itemView) {
            return new ContentHelpViewHolder(itemView);
        }
    }

}
