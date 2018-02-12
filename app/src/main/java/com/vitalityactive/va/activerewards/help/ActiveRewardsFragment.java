package com.vitalityactive.va.activerewards.help;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vitalityactive.va.R;

import java.util.ArrayList;
import java.util.List;

public class ActiveRewardsFragment extends Fragment{

    private RecyclerView helpRecyclerView;
    private ActiveRewardsHelpAdapter listHelpAdapter;
    private Button btnConfrimation;


    private ArrayList<String> helpList;

    public static ActiveRewardsFragment newIntance(ArrayList<String> helpList){
        ActiveRewardsFragment fragment = new ActiveRewardsFragment();
        fragment.helpList = helpList;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_active_rewards, container, false);

        helpRecyclerView =  parentView.findViewById(R.id.recycler_view);
        btnConfrimation = parentView.findViewById(R.id.btnconfirmation);
        helpRecyclerView.setItemAnimator(new DefaultItemAnimator());
        helpRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        helpRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listHelpAdapter = new ActiveRewardsHelpAdapter(helpList, null);
        helpRecyclerView.setAdapter(listHelpAdapter);


        return parentView;
    }

    public void showRecyclerview(){

        helpRecyclerView.setVisibility(View.VISIBLE);
    }

    public  void hideConfirmation(){

        btnConfrimation.setVisibility(View.GONE);
    }
    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setQ(String q){
        listHelpAdapter.setQ(q);
        listHelpAdapter.notifyDataSetChanged();
    }

    public void updateList(List<String> helpList) {
        this.helpList.clear();
        this.helpList.addAll(helpList);

        listHelpAdapter.notifyDataSetChanged();
    }
}