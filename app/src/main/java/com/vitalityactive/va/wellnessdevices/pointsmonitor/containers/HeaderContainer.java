package com.vitalityactive.va.wellnessdevices.pointsmonitor.containers;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.ImageView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.wellnessdevices.WdEventType;

public class HeaderContainer extends GenericRecyclerViewAdapter.ViewHolder<WdEventType> {
    private final ImageView ivIcon;

    private HeaderContainer(View itemView) {
        super(itemView);
        ivIcon = itemView.findViewById(R.id.iv_wd_points);
    }

    @Override
    public void bindWith(WdEventType eventType) {
        ivIcon.setImageDrawable(getTintedDrawable(eventType.getDrawableIconSmall(), eventType.getDrawableIconColour()));
    }

    private Drawable getTintedDrawable(@DrawableRes int drawableId, int colorId) {
        int color = ResourcesCompat.getColor(itemView.getContext().getResources(), colorId, itemView.getContext().getTheme());
        return ViewUtilities.tintDrawable(ContextCompat.getDrawable(itemView.getContext(), drawableId), color);
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<WdEventType, HeaderContainer> {

        public Factory() {
        }

        @Override
        public HeaderContainer createViewHolder(View itemView) {
            return new HeaderContainer(itemView);
        }
    }

}
