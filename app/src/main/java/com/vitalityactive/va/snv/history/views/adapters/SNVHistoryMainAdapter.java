package com.vitalityactive.va.snv.history.views.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.snv.dto.HistoryDetailDto;
import com.vitalityactive.va.snv.history.views.model.SNVHistoryPresentableProof;
import com.vitalityactive.va.snv.history.views.model.SNVMainAdapterModel;
import com.vitalityactive.va.utilities.ImageLoader;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vhc.summary.PresentableProof;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dharel.h.rosell on 12/6/2017.
 */

public class SNVHistoryMainAdapter extends RecyclerView.Adapter {

    private static int PROOF_ITEM_DIMENSION = 200;
    private static int NUMBER_OF_COLUMNS = 3;

    @NonNull
    private List<SNVMainAdapterModel> modelItem;
//    private PresentableProof snvPresentableProof;
    private List<String> snvPresentableProof;
    private LayoutInflater inflater;
    private Context context;

    public SNVHistoryMainAdapter(@NonNull List<SNVMainAdapterModel> item, LayoutInflater inflater,
                                 Context contextParam) {
        this.modelItem = item;
        this.inflater = inflater;
        this.context = contextParam;
        this.snvPresentableProof = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(viewType, parent, false);

        if(viewType == R.layout.snv_uploaded_proof_item){
            return new SNVProofViewHolder(itemView);
        }
        return new SNVHistoryMainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position == getItemCount() - 1) {
            ((SNVProofViewHolder) holder).bindWith(this.snvPresentableProof);
            return;
        }

        ((SNVHistoryMainViewHolder) holder).bindWith(modelItem.get(position));
    }

    @Override
    public int getItemCount() {
        return modelItem.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return R.layout.snv_uploaded_proof_item;
        }
        return R.layout.snv_history_item_detail;
    }

    public void setSnvPresentableProof(List<String> snvPresentableProofParam){
        this.snvPresentableProof = snvPresentableProofParam;
    }

    private class SNVHistoryMainViewHolder extends RecyclerView.ViewHolder {
        private final TextView eventTypeTtitle;
        private final RecyclerView historyDetailRecylerView;

        public SNVHistoryMainViewHolder(View itemView) {
            super(itemView);
            eventTypeTtitle = itemView.findViewById(R.id.event_type_title);
            historyDetailRecylerView = itemView.findViewById(R.id.event_type_recycler_view);
        }

        public void bindWith(SNVMainAdapterModel item){
            eventTypeTtitle.setText(item.getEventLabel());
            historyDetailRecylerView.setAdapter(new SNVHistoryDetailAdapter(item.getEventList(),inflater));
        }
    }


    private class SNVHistoryDetailAdapter extends RecyclerView.Adapter {

        @NonNull
        List<HistoryDetailDto> eventList;
        private LayoutInflater inflater;

        public SNVHistoryDetailAdapter(@NonNull List<HistoryDetailDto> eventList, LayoutInflater inflater) {
            this.eventList = eventList;
            this.inflater = inflater;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(viewType, parent, false);
            return new SNVHistoryDetailViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((SNVHistoryDetailViewHolder) holder).bindWith(eventList.get(position));
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        @Override
        public int getItemViewType(int position) {
//        return super.getItemViewType(position);
            return R.layout.snv_history_detail_item;
        }
    }

    private class SNVHistoryDetailViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView dateMessage;

        public SNVHistoryDetailViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.event_title);
            dateMessage = itemView.findViewById(R.id.event_date_message);
        }

        public void bindWith(HistoryDetailDto historyDetail){
            title.setText(historyDetail.getEventTypeName());
            dateMessage.setText(historyDetail.getDateMessage());
        }
    }

    private class SNVProofViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TableLayout table;

        SNVProofViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            table = itemView.findViewById(R.id.snv_proof_items_table);
            initialiseProofItemDimensions();
        }

        public void bindWith(List<String> presentableProof) {
            title.setText("Uploaded Proof");
            table.removeAllViews();
            setUpProofItems(table, presentableProof, itemView);
        }

        private void setUpProofItems(TableLayout table, List<String> proofItemUris, View itemView) {
            int i = 0;
            TableRow tableRow = new TableRow(context);
            table.addView(tableRow, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            for (String proofItem : proofItemUris) {
                ImageView proofItemImageView = (ImageView)inflater.inflate(R.layout.vhc_summary_proof_thumbnail_item, (ViewGroup)itemView, false);
                ImageLoader.loadImageFromUriAndRotateBasedOnExifDataAndCenterInside(Uri.parse("file:" + proofItem), proofItemImageView);
//                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
//                        "://" + context.getResources().getResourcePackageName(R.drawable.add)
//                        + '/' + context.getResources().getResourceTypeName(R.drawable.add) + '/'
//                        + context.getResources().getResourceEntryName(R.drawable.add) );
//                Bitmap bitmap = BitmapFactory.decodeFile(proofItem);
//                proofItemImageView.setImageBitmap(bitmap);
                TableRow.LayoutParams params = new TableRow.LayoutParams(PROOF_ITEM_DIMENSION, PROOF_ITEM_DIMENSION);
                int margins = ViewUtilities.pxFromDp(4);
                params.setMargins(margins, ViewUtilities.pxFromDp(8), margins, 0);
                proofItemImageView.setLayoutParams(params);
                tableRow.addView(proofItemImageView);
                if (++i == NUMBER_OF_COLUMNS) {
                    i = 0;
                    tableRow = new TableRow(context);
                    table.addView(tableRow, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                }
            }
        }

        private int initialiseProofItemDimensions() {
            PROOF_ITEM_DIMENSION = getRoundedProofItemDimension(context.getResources().getDisplayMetrics().widthPixels);
            return PROOF_ITEM_DIMENSION;
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

}
