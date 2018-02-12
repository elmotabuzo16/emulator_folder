package com.vitalityactive.va.help;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.vitalityactive.va.BasePresentedFragment;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.HelpDTO;
import com.vitalityactive.va.help.presenter.HelpPresenter;
import com.vitalityactive.va.membershippass.MembershipPassHelpActivity;
import com.vitalityactive.va.search.SearchResultsActivity;
import com.vitalityactive.va.uicomponents.LoadingIndicatorFragment;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by christian.j.p.capin on 11/30/2017.
 */


public class HelpFragment extends BasePresentedFragment<HelpPresenter.UI, HelpPresenter<HelpPresenter.UI>>
        implements HelpPresenter.UI , SearchView.OnQueryTextListener, HelpAdapter.OnItemClickListener {

    public static final String ITEM_QUERY = "ITEM_QUERY";
    private static final String LOADING_INDICATOR = "LOADING_INDICATOR";
    public static String TAG = "HelpFragment";

    @Inject
    HelpPresenter presenter;

    private RecyclerView helpRecyclerView;
    private ArrayAdapterSearch arrayAdapterSearch;
    private HelpSearchView searchView;
    private String isToolBarMarginHide;
    private LinearLayout helpFragmentWrapper;
    private HelpAdapter helpAdapter;
    private List<String> suggestions = new ArrayList<>();
    private List<String> recommendation = new ArrayList<>();
    private View parentView;
    private Menu actionMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void activityCreated(@Nullable Bundle savedInstanceState) {

        parentView = getView();
        if (parentView == null) {
            return;
        }
        if (getArguments() != null) {
            isToolBarMarginHide = getArguments().getString(MembershipPassHelpActivity.GLOBAL_HELP_KEY);
            if(isToolBarMarginHide != null){
                if(isToolBarMarginHide.equals("REMOVE_MARGIN_TOP"))
                        hideToolBar(parentView);
            }
        }
        presenter.loadSuggestion();
    }
    @Override
    public void showSuggestions(List<HelpDTO> fiveHelp, List<HelpDTO> allQuestions) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("HelpDTO", String.valueOf(fiveHelp.size()));
                for(int i = 0; i < fiveHelp.size(); i++){
                    suggestions.add(fiveHelp.get(i).getQuestion());
                }

                for(int i = 0; i < allQuestions.size(); i++){
                    recommendation.add(allQuestions.get(i).getQuestion());
                }

                Log.d("HelpDTO suggestion size", String.valueOf(suggestions.size()));
                Log.d("HelpDTO recommendation", String.valueOf(suggestions.size()));

                setUpRecyclerView(parentView);
                setSearchViewSearchableInfo(actionMenu);
            }
        });
    }

    @Override
    public void activityDestroyed() {

    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected HelpPresenter.UI getUserInterface() {
        Log.i("LOG THIS FROM GET", "LOG THIS FROM GET");
        return this;
    }

    @Override
    protected HelpPresenter getPresenter() {
        return presenter;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.help_menu, menu);

        actionMenu = menu;
    }

    private void setUpRecyclerView(@NonNull View parentView) {
        helpRecyclerView=  parentView.findViewById(R.id.helpRecyclerView);
        helpAdapter = new HelpAdapter(suggestions,this);
        Log.d("HelpDTO helpAdapter", String.valueOf(suggestions.size()));
        helpRecyclerView.setAdapter(helpAdapter);
        helpRecyclerView.setItemAnimator(new DefaultItemAnimator());
        helpRecyclerView.addItemDecoration(new DividerItemDecoration(parentView.getContext(), LinearLayoutManager.VERTICAL));
    }

    private void hideToolBar(@NonNull View parentView){
        helpFragmentWrapper= parentView.findViewById(R.id.fragment_help_parent);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 0, 0, 0);
        helpFragmentWrapper.setLayoutParams(params);
    }

    private void setSearchViewSearchableInfo(Menu menu) {
        android.view.MenuItem searchItem = menu.findItem(R.id.help_search);

        searchView = (HelpSearchView) searchItem.getActionView();
        arrayAdapterSearch =new ArrayAdapterSearch(getActivity(),android.R.layout.simple_list_item_1, recommendation);
        Log.d("HelpDTO searchAdapter", String.valueOf(recommendation.size()));
        searchView.setAdapter(arrayAdapterSearch);
        searchView.setOnQueryTextListener(this);
        searchView.setSuggestionsAdapter(null);
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchView.setText(arrayAdapterSearch.getItem(position));
                Intent intent= new Intent(getActivity(), SearchResultsActivity.class);
                intent.putExtra(ITEM_QUERY, arrayAdapterSearch.getItem(position));
                startActivity(intent);

            }
        });
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo= searchManager.getSearchableInfo(new ComponentName(getActivity(), SearchResultsActivity.class));

     /*
     //this will disable the hint upon searching
        try {
            Field mHintId = searchableInfo.getClass().getDeclaredField("mHintId");
            mHintId.setAccessible(true);
            mHintId.setInt(searchableInfo, android.R.id.empty);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        */
        searchView.setSearchableInfo(searchableInfo);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("onQueryTextSubmit:",query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        arrayAdapterSearch.setQ(newText);
        Log.e("Query:", String.valueOf(searchView.getQuery()));
        arrayAdapterSearch.notifyDataSetChanged();
        return false;
    }

    @Override
    public void onItemClick(String item) {
        Intent intent= new Intent(getActivity(), SearchResultsActivity.class);
        intent.putExtra(ITEM_QUERY, item);
        startActivity(intent);

    }

    @Override
    public void showLoadingIndicator() {
        if (getView() != null) {
            if (getFragmentManager().findFragmentByTag(LOADING_INDICATOR) != null) {
                return;
            }
            ViewUtilities.hideKeyboard(getActivity(), getView());
            new LoadingIndicatorFragment().show(getFragmentManager(), LOADING_INDICATOR);
        }
    }

    @Override
    public void hideLoadingIndicator() {
        if (getView() != null) {
            LoadingIndicatorFragment loadingIndicatorFragment = (LoadingIndicatorFragment) getFragmentManager().findFragmentByTag(LOADING_INDICATOR);
            if (loadingIndicatorFragment != null) {
                loadingIndicatorFragment.dismiss();
            }
        }
    }
}
