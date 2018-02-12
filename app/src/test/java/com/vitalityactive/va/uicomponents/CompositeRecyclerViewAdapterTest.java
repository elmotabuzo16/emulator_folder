package com.vitalityactive.va.uicomponents;

import android.view.ViewGroup;

import com.vitalityactive.va.uicomponents.adapters.CompositeRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class CompositeRecyclerViewAdapterTest {
    HashMap<Integer, GenericRecyclerViewAdapter> adapters;
    private CompositeRecyclerViewAdapter compositeRecyclerViewAdapter;

    @Before
    public void setUp() throws Exception {
        adapters = new HashMap<>();
        adapters.put(1, createMockAdapter(1, 3));
        adapters.put(2, createMockAdapter(2, 2));
        adapters.put(3, createMockAdapter(3, 4));
        compositeRecyclerViewAdapter = new CompositeRecyclerViewAdapter(adapters, new int[]{1, 2, 3});
    }

    @Test
    public void getItemCount_total_count_of_all_adapters() {
        assertEquals(3+2+4, compositeRecyclerViewAdapter.getItemCount());

        // when 2 removed, total is 2 less
        getMockAdapter(2).dummyData.clear();
        assertEquals(3+4, compositeRecyclerViewAdapter.getItemCount());
    }

    private MockAdapter getMockAdapter(int key) {
        return (MockAdapter) adapters.get(key);
    }

    private MockAdapter createMockAdapter(int viewResourceId, int dataCount) {
        ArrayList<Integer> data = new ArrayList<>();
        for (int i = 0; i < dataCount; i++) {
            data.add(i);
        }
        return new MockAdapter(data, viewResourceId);
    }

    public static class MockAdapter extends GenericRecyclerViewAdapter
    {
        ArrayList<Integer> dummyData;
        private final int viewResourceId;
        public int onCreateViewHolder_called = 0;
        public int onBindViewHolder_expected_position = -1;
        public int onBindViewHolder_called = 0;

        @SuppressWarnings("unchecked")
        public MockAdapter(ArrayList<Integer> data, int viewResourceId) {
            super(data, viewResourceId);
            this.dummyData = data;
            this.viewResourceId = viewResourceId;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            assertEquals("called with resource id must match expected", viewResourceId, viewType);
            onCreateViewHolder_called++;
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            if (onBindViewHolder_expected_position > -1)
                assertEquals("invalid position", onBindViewHolder_expected_position, position);
            onBindViewHolder_called++;
        }
    }
}
