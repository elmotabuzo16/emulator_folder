package com.vitalityactive.va.menu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.activerewards.menu.ActiveRewardsMenuItemViewHolder;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuContainerViewHolder extends GenericRecyclerViewAdapter.ViewHolder<CardMarginSettings> {
    private static final String TAG = "MenuContainerViewHolder";
    private final View itemView;
    private final int menuItemLayout;

    private final ArrayList<MenuItem> menuItems = new ArrayList<>();

    public MenuContainerViewHolder(View itemView,
                                   Context context,
                                   OnMenuItemClickedListener menuItemClicked,
                                   List<MenuItem> menuItems,
                                   int MenuItemLayout) {
        super(itemView);
        this.itemView = itemView;
        menuItemLayout = MenuItemLayout;
        this.menuItems.addAll(menuItems);
        RecyclerView menu = itemView.findViewById(R.id.recycler_view);
        menu.setAdapter(getMenuAdapter(context, menuItemClicked));
    }

    @Override
    public void bindWith(CardMarginSettings settings) {
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            setContainerMargins(settings, (ViewGroup.MarginLayoutParams) layoutParams);
        }
    }

    private void setContainerMargins(CardMarginSettings settings, ViewGroup.MarginLayoutParams layoutParams) {
        if (!settings.topMargin)
            layoutParams.topMargin = 0;
        if (!settings.bottomMargin)
            layoutParams.bottomMargin = 0;
        itemView.setLayoutParams(layoutParams);
    }

    @NonNull
    private GenericRecyclerViewAdapter<MenuItem, ActiveRewardsMenuItemViewHolder> getMenuAdapter(Context context,
                                                                                                 final OnMenuItemClickedListener menuItemClicked) {
        GenericRecyclerViewAdapter.OnItemClickListener<MenuItem> clickListener =
                menuItemClicked != null ? createOnItemClickListener(menuItemClicked) : null;

        return new GenericRecyclerViewAdapter<>(context,
                menuItems,
                menuItemLayout,
                new ActiveRewardsMenuItemViewHolder.Factory(),
                clickListener);
    }

    @NonNull
    private GenericRecyclerViewAdapter.OnItemClickListener<MenuItem> createOnItemClickListener(final OnMenuItemClickedListener menuItemClicked) {
        return new GenericRecyclerViewAdapter.OnItemClickListener<MenuItem>() {
            @Override
            public void onClicked(int position, MenuItem item) {
                Log.d(TAG, String.format("clicked on menu item @%d, %s", position, item.getType()));
                menuItemClicked.onClicked(item.getType());
            }
        };
    }

    public interface OnMenuItemClickedListener {
        void onClicked(MenuItemType menuItemType);
    }

}
