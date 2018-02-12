package com.vitalityactive.va.menu;

import android.content.Context;
import android.view.View;

import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuContainerViewHolderFactory
        implements GenericRecyclerViewAdapter.IViewHolderFactory<CardMarginSettings,
            MenuContainerViewHolder> {

    private final Context context;
    private final MenuContainerViewHolder.OnMenuItemClickedListener onMenuItemClickListener;
    private final List<MenuItem> menuItems;
    private final int menuItemLayout;

    public MenuContainerViewHolderFactory(Context context,
                                          MenuContainerViewHolder.OnMenuItemClickedListener onMenuItemClickedListener,
                                          ArrayList<MenuItem> menuItems,
                                          int menuItemLayout) {
        this.context = context;
        this.onMenuItemClickListener = onMenuItemClickedListener;
        this.menuItems = menuItems;
        this.menuItemLayout = menuItemLayout;
    }

    @Override
    public MenuContainerViewHolder createViewHolder(View itemView) {
        return new MenuContainerViewHolder(itemView,
                context,
                onMenuItemClickListener,
                menuItems,
                menuItemLayout);
    }
}
