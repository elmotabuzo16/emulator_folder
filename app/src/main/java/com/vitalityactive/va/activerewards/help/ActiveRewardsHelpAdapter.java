package com.vitalityactive.va.activerewards.help;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import com.vitalityactive.va.R;

/**
 * Created by paule.glenn.s.acuin on 1/5/2018.
 */

public class ActiveRewardsHelpAdapter extends RecyclerView.Adapter<ActiveRewardsHelpAdapter.ViewHolder> {
    private List<String> mDataset;
    private final OnItemClickListener listener;
    public static View parentView;


    //this is text highlight
    private static String q = "";


    public ActiveRewardsHelpAdapter(List<String> mDataset, OnItemClickListener listener) {
        this.mDataset = mDataset;
        this.listener = listener;
    }

    @Override
    public ActiveRewardsHelpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.active_rewards_help_item, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mDataset.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    public void setQ(String q) {
        this.q = q;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        private int start = 0, end = 0;

        public ViewHolder(View v) {
            super(v);
            parentView = v;
            mTextView = v.findViewById(R.id.textView);
        }

        public void bind(final String item, final OnItemClickListener listener) {

            start = item.toLowerCase().indexOf(q.toLowerCase());

            start = (start < 0) ? 0 : start;
            end = q.length() + start;

            SpannableStringBuilder builder = new SpannableStringBuilder(item);
            if (end <= item.length()) {
                builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            mTextView.setText(builder);

        }
    }
}


