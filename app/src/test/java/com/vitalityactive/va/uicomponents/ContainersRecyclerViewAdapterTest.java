package com.vitalityactive.va.uicomponents;

import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class ContainersRecyclerViewAdapterTest {
    private ContainersRecyclerViewAdapter recyclerViewAdapter;
    ArrayList<GenericRecyclerViewAdapter> adapters;

    @Before
    public void setUp() throws Exception {
        adapters = new ArrayList<>();
        adapters.add(new CompositeRecyclerViewAdapterTest.MockAdapter(new ArrayList<Integer>(), 1));
        adapters.add(new CompositeRecyclerViewAdapterTest.MockAdapter(new ArrayList<Integer>(), 2));
        adapters.add(new CompositeRecyclerViewAdapterTest.MockAdapter(new ArrayList<Integer>(), 3));
        recyclerViewAdapter = new ContainersRecyclerViewAdapter(adapters);
    }

    @Test
    public void getItemViewType_match_adapter_view_type() {
        assertEquals(1, recyclerViewAdapter.getItemViewType(0));
        assertEquals(2, recyclerViewAdapter.getItemViewType(1));
        assertEquals(3, recyclerViewAdapter.getItemViewType(2));
    }

    @Test
    public void getItemViewType_ignores_hidden() {
        //view types:  1,2,3 -> 1,3
        recyclerViewAdapter.hideContainer(1);

        assertEquals(1, recyclerViewAdapter.getItemViewType(0));
        assertEquals(3, recyclerViewAdapter.getItemViewType(1));
    }

    @Test
    public void getItemViewType_counts_re_visible() {
        //view types:  1,2,3 -> 1,3 -> 1,2,3
        recyclerViewAdapter.hideContainer(1);
        recyclerViewAdapter.showContainer(1, true);

        assertEquals(1, recyclerViewAdapter.getItemViewType(0));
        assertEquals(2, recyclerViewAdapter.getItemViewType(1));
        assertEquals(3, recyclerViewAdapter.getItemViewType(2));
    }

    @Test
    public void getItemCount_match_number_of_adapters() {
        assertEquals(3, recyclerViewAdapter.getItemCount());
    }

    @Test
    public void getItemCount_ignore_hidden() {
        recyclerViewAdapter.hideContainer(1);

        assertEquals(2, recyclerViewAdapter.getItemCount());
    }

    @Test
    public void onCreateViewHolder_match_adapter_viewResourceId() {
        recyclerViewAdapter.onCreateViewHolder(null, 1);
        assertEquals(1, getMockAdapter(0).onCreateViewHolder_called);

        recyclerViewAdapter.onCreateViewHolder(null, 2);
        assertEquals(1, getMockAdapter(1).onCreateViewHolder_called);

        recyclerViewAdapter.onCreateViewHolder(null, 3);
        assertEquals(1, getMockAdapter(2).onCreateViewHolder_called);
    }

    @Test
    public void onBindViewHolder_bound_to_each_adapter() {
        for (int i = 0; i < 3; i ++) {
            getMockAdapter(i).onBindViewHolder_expected_position = 0;
            recyclerViewAdapter.onBindViewHolder(null, i);
            assertEquals(1, getMockAdapter(i).onBindViewHolder_called);
        }
    }

    @Test
    public void onBindViewHolder_ignore_hidden() {
        recyclerViewAdapter.hideContainer(1);

        getMockAdapter(0).onBindViewHolder_expected_position = 0;
        recyclerViewAdapter.onBindViewHolder(null, 0);
        assertEquals(1, getMockAdapter(0).onBindViewHolder_called);

        // but adapter[2] is at position 1 (and not adapter[3])
        getMockAdapter(2).onBindViewHolder_expected_position = 0;
        recyclerViewAdapter.onBindViewHolder(null, 1);
        assertEquals(1, getMockAdapter(2).onBindViewHolder_called);
    }

    private CompositeRecyclerViewAdapterTest.MockAdapter getMockAdapter(int index) {
        return (CompositeRecyclerViewAdapterTest.MockAdapter) adapters.get(index);
    }
}
