package com.vitalityactive.va.help;

import android.content.Context;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * Created by christian.j.p.capin on 12/4/2017.
 */

public class HelpSearchView extends SearchView {
    private SearchAutoComplete mSearchAutoComplete;

    public HelpSearchView(Context context) {
        super(context);
        initialize();
    }
    public HelpSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }
    public void initialize() {
        mSearchAutoComplete = findViewById(android.support.v7.appcompat.R.id.search_src_text);
        this.setAdapter(null);
        this.setOnItemClickListener(null);
    }
    @Override
    public void setSuggestionsAdapter(CursorAdapter adapter) {
        // don't let anyone touch this
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mSearchAutoComplete.setOnItemClickListener(listener);
    }

    public void setAdapter(ArrayAdapter<?> adapter) {
        mSearchAutoComplete.setAdapter(adapter);
    }

    public void setText(String text) {

       mSearchAutoComplete.setText(text);
    }
}
