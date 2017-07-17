package com.boostcamp.miniproject2.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boostcamp.miniproject2.R;
import com.boostcamp.miniproject2.adapter.RestaurantRecyclerViewAdapter;
import com.boostcamp.miniproject2.database.DatabaseHelper;
import com.boostcamp.miniproject2.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moon on 2017. 7. 13..
 */

public class SortedFragment extends Fragment {

    public static final int METHOD_LINEAR = 1;
    public static final int METHOD_STAGGERED = 2;

    private RecyclerView recyclerView;
    private RestaurantRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private DatabaseHelper db;
    private List<Restaurant> restaurantList = new ArrayList<>();
    private int currentMethod = METHOD_STAGGERED;
    private int condition = DatabaseHelper.ORDER_BY_NEARBY;

    public static SortedFragment newInstance() {
        return new SortedFragment();
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public int getCurrentMethod() {
        return currentMethod;
    }

    public void setCurrentMethod(int currentMethod) {
        this.currentMethod = currentMethod;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_sorted, container, false);

        recyclerView = layout.findViewById(R.id.recyclerview_nearby);

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViewData();
    }

    public void initViewData() {
        initRestaurantDummy();
        initAdapter();
    }

    public void initRestaurantDummy() {
        db = DatabaseHelper.getInstance(getContext());

        restaurantList.clear();
        restaurantList = db.selectAllBySortCondition(getCondition());
    }

    private void initAdapter() {
        adapter = new RestaurantRecyclerViewAdapter(restaurantList, getContext());
        if (getCurrentMethod() == METHOD_LINEAR) {
            manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        } else {
            manager = new StaggeredGridLayoutManager(2, 1);
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }

    public void changePrintMethod(int method) {
        switch (method) {
            case METHOD_LINEAR:
                manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                break;
            case METHOD_STAGGERED:
                manager = new StaggeredGridLayoutManager(2, 1);
                break;
        }
        recyclerView.setLayoutManager(manager);
        setCurrentMethod(method);
    }
}
