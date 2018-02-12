package com.vitalityactive.va;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.utilities.ViewUtilities;

public class EmptyStateViewHolder extends RecyclerView.ViewHolder {
    private final TextView titleView;
    private final TextView subtitleView;
    private final AppCompatButton buttonView;
    private final ImageView imageView;

    public EmptyStateViewHolder(View containerView) {
        super(containerView);
        titleView = getTextViewById(R.id.empty_state_title);
        subtitleView = getTextViewById(R.id.empty_state_subtitle);
        buttonView = itemView.findViewById(R.id.empty_state_button);
        imageView = containerView.findViewById(R.id.empty_state_image);
    }

    public EmptyStateViewHolder setup(final int title ,final int subtitle,final int button_title, final Object... args) {
        titleView.setText(title);
        if (args != null) {
            subtitleView.setText(subtitleView.getResources().getString(subtitle, args));
        } else {
            subtitleView.setText(subtitle);
        }

        buttonView.setText(button_title);

        if (!BuildConfig.SHOW_HELP && buttonView.getText().toString().toUpperCase().equalsIgnoreCase(buttonView.getContext().getString(R.string.events_help))){
            buttonView.setVisibility(View.GONE);
        }
        return this;
    }

    public EmptyStateViewHolder setup(int title, int subtitle, int button_title) {
        Object object = null;
        return setup(title, subtitle, button_title, object);

    }

    public EmptyStateViewHolder setup(int title, int subtitle, int button_title, final EmptyStatusButtonClickedListener listener) {
        buttonView.setOnClickListener(view -> listener.onEmptyStateButtonClicked(EmptyStateViewHolder.this));
        return setup(title, subtitle, button_title);
    }

    public EmptyStateViewHolder setImage(int drawableResourceId, int contentDescriptionResourceId) {
        Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), drawableResourceId);
        String contentDescription = itemView.getContext().getString(contentDescriptionResourceId);
        return setImage(drawable, contentDescription);
    }

    public EmptyStateViewHolder setImage(Drawable drawable, String contentDescription) {
        imageView.setImageDrawable(drawable);
        imageView.setContentDescription(contentDescription);
        imageView.setVisibility(View.VISIBLE);
        return this;
    }

    public EmptyStateViewHolder setButtonClickListener(final EmptyStatusButtonClickedListener clickListener) {
        buttonView.setOnClickListener(view -> clickListener.onEmptyStateButtonClicked(EmptyStateViewHolder.this));
        return this;
    }

    private TextView getTextViewById(int resourceId) {
        return (TextView) itemView.findViewById(resourceId);
    }

    public void hideEmptyStateAndShowOtherIfHasData(boolean hasData, View other) {
        if (hasData) {
            hideEmptyStateViewAndShowOtherView(other);
        } else {
            showEmptyStateViewAndHideOtherView(other);
        }
    }

    public void hideEmptyStateViewAndShowOtherView(View otherView) {
        ViewUtilities.swapVisibility(otherView, itemView);
    }

    public void showEmptyStateViewAndHideOtherView(View otherView) {
        ViewUtilities.swapVisibility(itemView, otherView);
    }

    public void setButtonColor(@ColorInt int color) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_empty},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{-android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_activated},
                        new int[]{android.R.attr.state_active},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{color, color, color, color, color, color, color}
        );
        ViewCompat.setBackgroundTintList(buttonView, colorStateList);
    }

    public interface EmptyStatusButtonClickedListener {
        void onEmptyStateButtonClicked(EmptyStateViewHolder emptyStateViewHolder);
    }
}
