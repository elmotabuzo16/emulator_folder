package com.vitalityactive.va.snv.confirmandsubmit.viewholder;

import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.utilities.ImageLoader;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.List;

public class SNVProofItemSummaryAdapter {

    private static int PROOF_ITEM_DIMENSION = 200;
    private static int NUMBER_OF_COLUMNS = 3;

    private final TableLayout table;
    private Callback callback;

    public interface Callback {
        TableRow getNewTableRow();

        ImageView getNewImageView();

        Resources getResourcesForTable();

        View findViewByIdInActivity(int resId);
    }

    public SNVProofItemSummaryAdapter(Callback callback) {
        table = (TableLayout) callback.findViewByIdInActivity(R.id.snv_proof_items_table);
        this.callback = callback;
        initializeProofItemDimensions();
    }

    public void setUpProofItems(List<ProofItemDTO> proofItemUris) {
        int i = 0;
        table.removeAllViews();
        TableRow tableRow = callback.getNewTableRow();

        table.addView(tableRow, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        if (proofItemUris != null) {
            for (ProofItemDTO proofItem : proofItemUris) {
                ImageView proofItemImageView = callback.getNewImageView();
                ImageLoader.loadImageFromUriAndRotateBasedOnExifDataAndCenterInside(Uri.parse(proofItem.getUri()), proofItemImageView);
                TableRow.LayoutParams params = new TableRow.LayoutParams(PROOF_ITEM_DIMENSION, PROOF_ITEM_DIMENSION);
                int margins = ViewUtilities.pxFromDp(4);
                params.setMargins(margins, ViewUtilities.pxFromDp(8), margins, 0);
                proofItemImageView.setLayoutParams(params);
                tableRow.addView(proofItemImageView);
                if (++i == NUMBER_OF_COLUMNS) {
                    i = 0;
                    tableRow = callback.getNewTableRow();
                    table.addView(tableRow, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                }
            }
        }
    }

    private void initializeProofItemDimensions() {
        PROOF_ITEM_DIMENSION = getRoundedProofItemDimension(callback.getResourcesForTable().getDisplayMetrics().widthPixels);
    }

    private int getRoundedProofItemDimension(int screenWidth) {
        return (int) (getRawProofItemDimension(screenWidth) + 0.5f);
    }

    private float getRawProofItemDimension(int screenWidth) {
        return (screenWidth - getTotalHorizontalMargins()) / NUMBER_OF_COLUMNS;
    }

    private float getTotalHorizontalMargins() {
        return ViewUtilities.pxFromDp(16) * 2 + (NUMBER_OF_COLUMNS - 1) * ViewUtilities.pxFromDp(8);
    }
}
