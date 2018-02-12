package com.vitalityactive.va.vhc.addproof;

import android.widget.ImageView;

import com.vitalityactive.va.dto.ProofItemDTO;

public interface GridViewItem {
    boolean isChecked();

    void setChecked(Boolean checked);

    boolean getCheckboxVisible();

    void setCheckboxVisible(Boolean visibility);

    ProofItemDTO getProofItem();

    void loadImageIntoImageView(ImageView imageView);
}
