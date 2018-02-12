package com.vitalityactive.va.vhc.addproof;

import android.net.Uri;
import android.widget.ImageView;

import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.utilities.ImageLoader;

public class GridViewImageItem implements GridViewItem {
    private Boolean checked = false;
    private Boolean checkboxVisible = false;
    private ProofItemDTO proofItem;

    public GridViewImageItem(ProofItemDTO proofItem) {
        this.proofItem = proofItem;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public boolean getCheckboxVisible() {
        return checkboxVisible;
    }

    public void setCheckboxVisible(Boolean checkboxVisible) {
        this.checkboxVisible = checkboxVisible;
    }

    public ProofItemDTO getProofItem() {
        return proofItem;
    }

    @Override
    public void loadImageIntoImageView(ImageView imageView) {
        ImageLoader.loadImageFromUriAndRotateBasedOnExifDataAndCenterInside(Uri.parse(getProofItem().getUri()), imageView);
    }
}
