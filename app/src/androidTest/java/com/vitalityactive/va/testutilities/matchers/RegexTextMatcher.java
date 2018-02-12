package com.vitalityactive.va.testutilities.matchers;

import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class RegexTextMatcher extends TypeSafeMatcher<View> {
    private final int resourceIdWithParams;
    private String regex = null;

    public RegexTextMatcher(int resourceIdWithParams) {
        this.resourceIdWithParams = resourceIdWithParams;
    }

    @Override
    protected boolean matchesSafely(View item) {
        if (!(item instanceof TextView))
            return false;
        TextView textView = (TextView) item;
        String actualText = textView.getText().toString();

        String regexString = getRegexString(item);

        return actualText.matches(regexString);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with text matching regex from resourceId ");
        description.appendValue(resourceIdWithParams);
        if (regex != null)
            description.appendText(" (regex: " + regex + " )");
    }

    private String getRegexString(View item) {
        if (regex == null) {
            String format = item.getResources().getString(resourceIdWithParams);
            regex = format.replaceAll("%\\d\\$s", ".+");
            regex = "^" + regex + "";
        }
        return regex;
    }
}
