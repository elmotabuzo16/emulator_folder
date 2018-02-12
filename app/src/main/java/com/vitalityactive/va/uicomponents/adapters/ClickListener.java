package com.vitalityactive.va.uicomponents.adapters;

import android.view.View;

/**
 * Created by christian.j.p.capin on 12/4/2017.
 */

public interface ClickListener {
    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);
}
