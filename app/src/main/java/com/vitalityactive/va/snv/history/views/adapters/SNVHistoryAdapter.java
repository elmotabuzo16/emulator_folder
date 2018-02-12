package com.vitalityactive.va.snv.history.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vitalityactive.va.NavigationCoordinator;
import com.vitalityactive.va.R;
import com.vitalityactive.va.snv.dto.ListHistoryListDto;
import com.vitalityactive.va.snv.history.views.SNVHistoryActivity;
import com.vitalityactive.va.snv.history.views.SNVHistoryDetailActivity;

import java.util.List;

/**
 * Created by dharel.h.rosell on 12/5/2017.
 */

public class SNVHistoryAdapter extends RecyclerView.Adapter {

    private Context context;
    @NonNull
    List<ListHistoryListDto> historyListDtos;
    private LayoutInflater inflater;
    private NavigationCoordinator navigationCoordinator;

    public SNVHistoryAdapter(Context context, @NonNull List<ListHistoryListDto> historyListDtos,
                             LayoutInflater inflaterParam) {
        this.context = context;
        this.historyListDtos = historyListDtos;
        this.inflater = inflaterParam;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(viewType, parent, false);
        return new SNVHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SNVHistoryViewHolder) holder).bindWith(this.historyListDtos.get(position));

        final ListHistoryListDto listDtoItem = this.historyListDtos.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, SNVHistoryDetailActivity.class);
//                intent.putExtra(SNVHistoryDetailActivity.DATE_STRING, listDtoItem.getDateString());
//                intent.putExtra(SNVHistoryDetailActivity.DATE_MESSAGE, listDtoItem.getDateStringMessage());
//                context.startActivity(intent);
                if(navigationCoordinator != null) {
                    Activity activity = (Activity) context;
//                    Toast.makeText(activity, listDtoItem.getDateStringActual(), Toast.LENGTH_LONG).show();
                    navigationCoordinator.navigateToScreenAndVacctionHistoryDetail(activity,
                            listDtoItem.getDateString(), listDtoItem.getDateStringMessage());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.historyListDtos.size();
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        return R.layout.svn_history_item;
    }

    public void setNavigationCoordinator(NavigationCoordinator navigationCoordinatorParam) {
        this.navigationCoordinator = navigationCoordinatorParam;
    }

    private class SNVHistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;

        public SNVHistoryViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.snv_title_item);
        }

        public void bindWith(ListHistoryListDto historyDetail){
            title.setText(historyDetail.getDateStringMessage());
        }
    }
}
