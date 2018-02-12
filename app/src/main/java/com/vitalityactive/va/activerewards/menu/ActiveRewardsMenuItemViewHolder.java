package com.vitalityactive.va.activerewards.menu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.menu.MenuItem;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

public class ActiveRewardsMenuItemViewHolder extends GenericRecyclerViewAdapter.ViewHolder<MenuItem> {
    private final ImageView logo;
    private final TextView text;
    private final TextView badge;

    private ActiveRewardsMenuItemViewHolder(View itemView) {
        super(itemView);
        logo = itemView.findViewById(R.id.icon);
        text = itemView.findViewById(R.id.label);
        badge = itemView.findViewById(R.id.badge);
    }

    @Override
    public void bindWith(MenuItem dataItem) {
        int color = dataItem.getTintedColor();
        if (color == 0) {
            color = ViewUtilities.getColorPrimaryFromTheme(itemView);
        }

        logo.setImageResource(dataItem.getLogoResourceId());
        logo.setColorFilter(color);

        if (dataItem.getText() == null) {
            text.setText(dataItem.getTextResourceId());
        } else {
            text.setText(dataItem.getText());
        }

        if (dataItem.getBadgeCount() > 0) {
            badge.setVisibility(View.VISIBLE);
            badge.setText(String.valueOf(dataItem.getBadgeCount()));
        } else {
            badge.setVisibility(View.GONE);
        }
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<MenuItem, ActiveRewardsMenuItemViewHolder> {
        @Override
        public ActiveRewardsMenuItemViewHolder createViewHolder(View itemView) {
            return new ActiveRewardsMenuItemViewHolder(itemView);
        }
    }
}
