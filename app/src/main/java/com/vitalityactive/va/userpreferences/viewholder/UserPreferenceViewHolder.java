package com.vitalityactive.va.userpreferences.viewholder;

import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.userpreferences.entities.DefaultUserPreferencePresenter;
import com.vitalityactive.va.userpreferences.entities.UserPreferenceGroup;
import com.vitalityactive.va.utilities.ViewUtilities;


public class UserPreferenceViewHolder extends GenericRecyclerViewAdapter.ViewHolder<UserPreferenceGroup> {
    private final Button privacyStatementButton;
    private final
    @ColorInt
    int globalTintColor;
    private TextView title;
    private TextView description;

    private UserPreferenceViewHolder(View itemView, @ColorInt int globalTintColor) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.preference_title);
        description = (TextView) itemView.findViewById(R.id.preference_description);
        privacyStatementButton = (Button) itemView.findViewById(R.id.privacy_statement_button);
        this.globalTintColor = globalTintColor;
    }

    @Override
    public void bindWith(UserPreferenceGroup dataItem) {
        title.setText(dataItem.getTitle());
        description.setText(dataItem.getDescription());

        RecyclerView userPreferencesRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);

        userPreferencesRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

        GenericRecyclerViewAdapter<DefaultUserPreferencePresenter, UserPreferenceItemViewHolder> adapter =
                new GenericRecyclerViewAdapter<>(title.getContext(),
                        dataItem.userPreferenceItems,
                        R.layout.preference_item,
                        new UserPreferenceItemViewHolder.Factory(globalTintColor));

        int px = ViewUtilities.pxFromDp(72);

        ViewUtilities.addDividers(itemView.getContext(), userPreferencesRecyclerView, px);
        userPreferencesRecyclerView.setAdapter(adapter);

        if (dataItem.hasPrivacyStatementButton()) {
            privacyStatementButton.setVisibility(View.VISIBLE);
            privacyStatementButton.setTextColor(globalTintColor);
            ViewUtilities.moveButtonLeftByAmountOfPadding(privacyStatementButton);
            ViewUtilities.removeBottomMargin(description);
        }
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<UserPreferenceGroup, UserPreferenceViewHolder> {
        private final int globalTintColor;

        public Factory(@ColorInt int globalTintColor) {

            this.globalTintColor = globalTintColor;
        }

        @Override
        public UserPreferenceViewHolder createViewHolder(View itemView) {
            return new UserPreferenceViewHolder(itemView, globalTintColor);
        }
    }
}
