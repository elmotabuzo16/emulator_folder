package com.vitalityactive.va.vhc.addproof;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dto.ProofItemDTO;

import java.util.ArrayList;

public class CompositeGridViewAdapter extends ArrayAdapter {
    private int itemLayout;
    private int buttonAddItemLayout;
    private ArrayList<GridViewItem> gridViewImageItems = new ArrayList<>();

    private GridViewImageAddButton gridViewImageAddButton;
    private int imageDimension;

    public CompositeGridViewAdapter(Context context, int itemLayout, int addItemLayout, int imageDimension) {
        super(context, itemLayout);
        this.itemLayout = itemLayout;
        this.buttonAddItemLayout = addItemLayout;
        this.imageDimension = imageDimension;
    }

    public void setItemChecked(int position, boolean itemChecked) {
        if (position == -1)
            return;

        gridViewImageItems.get(position).setChecked(itemChecked);
    }

    public void enterSelectionMode() {
        setAllCheckboxesAndRefresh(true, false);
    }

    public void exitSelectionMode() {
        setAllCheckboxesAndRefresh(false, false);
    }

    public int deleteSelectedImages() {
        ArrayList<GridViewItem> deletionList = new ArrayList<>();

        for (int i = 0; i < gridViewImageItems.size(); i++) {
            GridViewItem item = gridViewImageItems.get(i);

            if (item.isChecked()) {
                deletionList.add(item);
                this.setItemChecked(i, false);
            }
        }

        gridViewImageItems.removeAll(deletionList);

        return gridViewImageItems.size() - 1;
    }

    public void createAddImageButtonAsLastItem() {
        if (gridViewImageAddButton == null) {
            gridViewImageAddButton = new GridViewImageAddButton();
        } else {
            gridViewImageItems.remove(gridViewImageAddButton);
        }

        gridViewImageItems.add(gridViewImageAddButton);
    }

    public void removeAddProofButtonAsLastItem() {
        gridViewImageItems.remove(gridViewImageAddButton);
        gridViewImageAddButton = null;
    }

    public void clearList() {
        gridViewImageItems.clear();
    }

    @Override
    public int getCount() {
        return gridViewImageItems.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row;
        ViewHolder holder;

        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        if (position < gridViewImageItems.size() - 1 || gridViewImageAddButton == null) {
            row = inflater.inflate(itemLayout, parent, false);
        } else {
            row = inflater.inflate(buttonAddItemLayout, parent, false);
        }

        ViewGroup.LayoutParams layoutParams = row.getLayoutParams();
        layoutParams.width = imageDimension;
        layoutParams.height = imageDimension;
        row.setLayoutParams(layoutParams);

        holder = new ViewHolder();
        holder.image = row.findViewById(R.id.image);
        row.setTag(holder);

        final GridViewItem currentGridViewImageItem = gridViewImageItems.get(position);
        holder.checkbox = row.findViewById(R.id.vhc_image_selected_checkbox);
        holder.checkbox.setChecked(currentGridViewImageItem.isChecked());

        if (currentGridViewImageItem.getCheckboxVisible()) {
            holder.checkbox.setVisibility(View.VISIBLE);
        } else {
            holder.checkbox.setVisibility(View.GONE);
        }

        currentGridViewImageItem.loadImageIntoImageView(holder.image);

        return row;
    }

    private void setAllCheckboxesAndRefresh(Boolean visibility, Boolean checked) {
        for (GridViewItem item : gridViewImageItems) {
            item.setCheckboxVisible(visibility);
            item.setChecked(checked);
        }

        this.notifyDataSetChanged();
    }

    public void addImageItem(ProofItemDTO proofItem) {
        gridViewImageItems.add(new GridViewImageItem(proofItem));
    }

    public ProofItemDTO getImageItemProofItem(int position) {
        return gridViewImageItems.get(position).getProofItem();
    }

    static class ViewHolder {
        ImageView image;
        CheckBox checkbox;
    }
}
