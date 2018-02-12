package com.vitalityactive.va.help;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by christian.j.p.capin on 12/4/2017.
 */

public class ArrayAdapterSearch extends ArrayAdapter<String> {
    private String q = "";
    private int start=0,end=0;

    static class ViewHolder {
        private TextView myTv;
    }

    public ArrayAdapterSearch(Context context, int resource, List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            String item = getItem(position);
            if (convertView == null) {
                convertView= super.getView(position, convertView, parent);
                holder = new ViewHolder();
                holder.myTv= convertView.findViewById(android.R.id.text1);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            // Lookup view for data population

            start = item.indexOf(q);
            start= (start < 0)? 0 : start;
            end = q.length()+ start;

            SpannableStringBuilder builder = new SpannableStringBuilder(item);
            if(end <=item.length()) {
                builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            holder.myTv.setText(builder);
            return convertView;
    }

    public void setQ(String q) {
        this.q = q;
    }
}