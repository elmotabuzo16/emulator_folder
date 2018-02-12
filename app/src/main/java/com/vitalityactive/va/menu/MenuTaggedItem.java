package com.vitalityactive.va.menu;

public class MenuTaggedItem<T> extends MenuItem {
    public MenuTaggedItem(T tag, int logoResourceId, int textResourceId) {
        super(MenuItemType.Custom, logoResourceId, textResourceId);
    }
}
