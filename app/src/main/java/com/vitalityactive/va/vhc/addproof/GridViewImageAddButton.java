package com.vitalityactive.va.vhc.addproof;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.utilities.ImageLoader;

public class GridViewImageAddButton implements GridViewItem {

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public void setChecked(Boolean checked) {
    }

    @Override
    public boolean getCheckboxVisible() {
        return false;
    }

    @Override
    public void setCheckboxVisible(Boolean visibility) {
    }

    public ProofItemDTO getProofItem() {
        return null;
    }

    @Override
    public void loadImageIntoImageView(ImageView imageView) {

    }
}
