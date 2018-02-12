package com.vitalityactive.va.help;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;

import java.util.List;

/**
 * Created by christian.j.p.capin on 12/1/2017.
 */

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {
    private List<String> mDataset;
    private final OnItemClickListener listener;
    public static View parentView;
    private int iconColor=0;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        private ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            parentView=v;
            mTextView = v.findViewById(R.id.helpSuggestions);
            imageView= v.findViewById(R.id.help_item_icon);

        }

        public void bind(final String item, final OnItemClickListener listener) {
            mTextView.setText(item);

            parentView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HelpAdapter(List<String> myDataset, OnItemClickListener listener) {
        this.mDataset = myDataset;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HelpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_help, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
       // holder.mTextView.setText(mDataset.get(position));
        //Log.e("onBindViewHolder:",String.valueOf(iconColor));
       // holder.imageView.setColorFilter((this.iconColor == 0)? R.color.vitalityOrange: iconColor);
        holder.bind(mDataset.get(position), listener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String item);
    }
    public void setIconColor(int color){
        this.iconColor=color;
    }
}
