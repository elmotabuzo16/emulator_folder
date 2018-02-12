package com.vitalityactive.va.activerewards.help;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.menu.ActiveRewardsMenuItemViewHolder;
import com.vitalityactive.va.activerewards.viewholder.TitleViewHolder;
import com.vitalityactive.va.menu.MenuItem;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.search.SearchResultsActivity;
import com.vitalityactive.va.uicomponents.adapters.CompositeRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveRewardsHelpActivity extends BaseActivity {
    public enum HelpType {ACTIVE_REWARDS, ALL_HELP}
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private LinearLayout llTabs;
    private CardView cardView;
    private SearchView searchView;

    private ActiveRewardsFragment activeRewardsFragment;
    private ActiveRewardsFragment allHelpFragment;

    private List<String> activeRewardsList;
    private List<String> allHelpList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_active_rewards_help);

        setUpActionBarWithTitle(R.string.help_button_title_141)
                .setDisplayHomeAsUpEnabled(true);

        setUpRecyclerView();
        assignSearchList();

        tabLayout = findViewById(R.id.tlHelp);
        viewPager = findViewById(R.id.vpHelp);
        llTabs = findViewById(R.id.llTabs);
        cardView = findViewById(R.id.cardView);

        activeRewardsFragment = ActiveRewardsFragment.newIntance(new ArrayList<>(activeRewardsList));
        allHelpFragment = ActiveRewardsFragment.newIntance(new ArrayList<>(allHelpList));


        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    private void assignSearchList(){
//        activeRewardsList = Arrays.asList(getResources().getStringArray(R.array.active_rewards_list));
        activeRewardsList = new ArrayList<>();
//        allHelpList = Arrays.asList(getResources().getStringArray(R.array.all_help_list));
        allHelpList = new ArrayList<>();
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == HelpType.ACTIVE_REWARDS.ordinal()) {
                return activeRewardsFragment;
            } else if (position == HelpType.ALL_HELP.ordinal()) {
                return allHelpFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == HelpType.ACTIVE_REWARDS.ordinal()) {
                return "Active Rewards";
            } else if (position == HelpType.ALL_HELP.ordinal()) {
                return "All Help";
            }
            return null;
        }
    }


    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        recyclerView.setAdapter(createAdapter());
    }

    private RecyclerView.Adapter createAdapter() {
        return new CompositeRecyclerViewAdapter(createChildAdapters(), new int[]{
                R.layout.active_rewards_title,
                R.layout.menu_item_small_text,
                R.layout.menu_item_small_text,
                R.layout.menu_item_small_text,
                R.layout.menu_item_small_text
        });
    }

    private Map<Integer, GenericRecyclerViewAdapter> createChildAdapters() {
        HashMap<Integer, GenericRecyclerViewAdapter> map = new HashMap<>();
        map.put(R.layout.active_rewards_title, new GenericRecyclerViewAdapter<>(this, Collections.singletonList(getString(R.string.active_rewards_help_suggestions_title)), R.layout.active_rewards_title, new TitleViewHolder.Factory()));
        map.put(R.layout.menu_item_small_text, new GenericRecyclerViewAdapter<>(this, createSuggestionsData(), R.layout.menu_item_small_text, new ActiveRewardsMenuItemViewHolder.Factory()));
        return map;
    }

    private List<MenuItem> createSuggestionsData() {
        return Arrays.asList(
                new MenuItem(MenuItemType.Activity, R.drawable.help_suggestion, R.string.active_rewards_help_suggestion_1),
                new MenuItem(MenuItemType.Activity, R.drawable.help_suggestion, R.string.active_rewards_help_suggestion_2),
                new MenuItem(MenuItemType.Activity, R.drawable.help_suggestion, R.string.active_rewards_help_suggestion_3),
                new MenuItem(MenuItemType.Activity, R.drawable.help_suggestion, R.string.active_rewards_help_suggestion_4)
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        setSearchViewSearchableInfo(menu);

        return true;
    }

    private void setSearchViewSearchableInfo(Menu menu) {
        android.view.MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                llTabs.setVisibility(hasFocus ? View.VISIBLE:View.GONE);
                cardView.setVisibility(hasFocus ? View.GONE:View.VISIBLE);



            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {




                List<String> activeRewardsListTemp = updateItem(activeRewardsList, newText);
                List<String> allHelpListTemp = updateItem(allHelpList, newText);


                activeRewardsFragment.updateList(activeRewardsListTemp);
                allHelpFragment.updateList(allHelpListTemp);
                activeRewardsFragment.showRecyclerview();
                activeRewardsFragment.hideConfirmation();
                allHelpFragment.showRecyclerview();
                allHelpFragment.hideConfirmation();

                activeRewardsFragment.setQ(newText);
                allHelpFragment.setQ(newText);
                return false;
            }
        });
    }

    private List<String> updateItem(List<String> list, String searchStr){

        List<String> newItems = new ArrayList<>();
        for(String item: list){
            if(item.toLowerCase().indexOf(searchStr.toLowerCase()) > -1){
                newItems.add(item);
            }
        }

        return newItems;
    }
}
