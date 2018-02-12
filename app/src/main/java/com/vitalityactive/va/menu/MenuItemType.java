package com.vitalityactive.va.menu;

import java.util.Objects;

public class MenuItemType<T> {
    public static final MenuItemType<Integer> Activity = new MenuItemType<>(100000);
    public static final MenuItemType<Integer> Rewards = new MenuItemType<>(100001);
    public static final MenuItemType<Integer> LearnMore = new MenuItemType<>(100010);
    public static final MenuItemType<Integer> History = new MenuItemType<>(100011);
    public static final MenuItemType<Integer> Help = new MenuItemType<>(100100);
    public static final MenuItemType<Integer> Custom = new MenuItemType<>(100101);
    public static final MenuItemType<Integer> Disclaimer = new MenuItemType<>(100110);
    public static final MenuItemType<Integer> HealthCarePDF = new MenuItemType<>(100111);
    public static final MenuItemType<Integer> EarningPoints = new MenuItemType<>(101000);
    public static final MenuItemType<Integer> GetStarted = new MenuItemType<>(-1);
    public static final MenuItemType<Integer> TermsAndConditions = new MenuItemType<>(-2);

    private T value;

    public MenuItemType(T value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        MenuItemType menuItemType = (MenuItemType)o;
        return value.equals(menuItemType.value);
    }

    public T getValue() {
        return value;
    }
}
