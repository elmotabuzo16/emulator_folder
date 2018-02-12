package com.vitalityactive.va.menu;

import android.content.Context;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuBuilder {
    private static final int RESOURCE_ID = R.layout.menu_container;

    private final Context context;
    private final ArrayList<MenuItem> menuItems;
    private MenuContainerViewHolder.OnMenuItemClickedListener clickListener;
    private CardMarginSettings menuSettings;
    private int layoutResourceId = R.layout.menu_item_small_text;

    public MenuBuilder(Context context) {
        this.context = context;
        clickListener = null;
        menuItems = new ArrayList<>();
        menuSettings = new CardMarginSettings();
    }

    public GenericRecyclerViewAdapter<CardMarginSettings, MenuContainerViewHolder> build() {
        MenuContainerViewHolderFactory factory =
                new MenuContainerViewHolderFactory(context,
                        clickListener,
                        menuItems,
                        layoutResourceId);
        return new GenericRecyclerViewAdapter<>(context,
                menuSettings,
                RESOURCE_ID,
                factory);
    }

    public MenuBuilder setClickListener(MenuContainerViewHolder.OnMenuItemClickedListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public MenuBuilder setMenuItems(MenuItemSet menuItemSet) {
        cleanMenuItems();
        setupMenuItems(menuItemSet);
        return this;
    }

    public MenuBuilder setMenuItemLayout(int layoutResourceId) {
        this.layoutResourceId = layoutResourceId;
        return this;
    }

    public void cleanMenuItems() {
        this.menuItems.clear();
    }

    public MenuBuilder addMenuItem(MenuItem menuItem) {
        if (menuItem != null) menuItems.add(menuItem);
        return this;
    }

    public MenuBuilder setCardMarginSettings(CardMarginSettings settings) {
        this.menuSettings = settings;
        return this;
    }

    private void setupMenuItems(MenuItemSet menuItemSet) {
        switch (menuItemSet) {
            case LearnMoreHelp:
                addMenuItem(MenuItem.Builder.learnMore());
                addMenuItem(MenuItem.Builder.help());
                break;
            case Help:
                addMenuItem(MenuItem.Builder.help());
                break;
            case HistoryLearnMoreHelp:
                addMenuItem(MenuItem.Builder.history());
                addMenuItem(MenuItem.Builder.learnMore());
                addMenuItem(MenuItem.Builder.help());
                break;
            case HealthCareLearnMoreHelp:
                addMenuItem(MenuItem.Builder.healthCarePDF());
                addMenuItem(MenuItem.Builder.learnMore());
                addMenuItem(MenuItem.Builder.help());
                break;
            case HealthCareLearnMore:
                addMenuItem(MenuItem.Builder.healthCarePDF());
                addMenuItem(MenuItem.Builder.learnMore());
                break;
            case LearnMore:
                addMenuItem(MenuItem.Builder.learnMore());
                break;
            case ActivityRewardsLearnMore:
                addMenuItem(MenuItem.Builder.activity());
                addMenuItem(MenuItem.Builder.rewards());
                addMenuItem(MenuItem.Builder.learnMore());
                break;
            case All:
            default:
                addMenuItem(MenuItem.Builder.activity());
                addMenuItem(MenuItem.Builder.rewards());
                addMenuItem(MenuItem.Builder.learnMore());
                addMenuItem(MenuItem.Builder.help());
                break;
        }
    }

    public MenuBuilder addMenuItems(List<MenuItem> configuredMenuItems) {
        menuItems.addAll(configuredMenuItems);

        return this;
    }

    public enum MenuItemSet {
        HistoryLearnMoreHelp,
        LearnMoreHelp,
        Help,
        HealthCareLearnMoreHelp,
        LearnMore,
        HealthCareLearnMore,
        ActivityRewardsLearnMore,
        All}
}
