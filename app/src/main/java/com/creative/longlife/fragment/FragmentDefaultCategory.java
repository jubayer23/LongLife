package com.creative.longlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creative.longlife.R;
import com.creative.longlife.adapter.DefaultCategoryAdapter;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.eventListener.RecyclerItemClickListener;

/**
 * Created by jubayer on 8/27/2017.
 */

public class FragmentDefaultCategory extends android.support.v4.app.Fragment{

    // private GridView gridView;
    private RecyclerView recyclerView;
    /// private IconGridAdapter iconGridAdapter;
    private DefaultCategoryAdapter defaultCategoryAdapter;
    //LinearLayoutManager listLayoutManager;
    GridLayoutManager gridLayoutManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_category, container, false);

        // Initialize the layout view ids
        init(view);
        // initialize listView adapter
        initAdapter();

        initRecyclerViewOnItemClickListener();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //sendRequestToGetPlaceList(GlobalAppAccess.URL_ALL_CATEGORYLIST);
    }

    private void init(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    private void initAdapter() {
        // iconGridAdapter = new IconGridAdapter(getActivity(), movieList);
        //gridView.setAdapter(iconGridAdapter);

        //listLayoutManager = new LinearLayoutManager(getActivity());
        //listLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        final int numberOfColumns = 2;
        gridLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        recyclerView.setLayoutManager(gridLayoutManager);

        defaultCategoryAdapter = new DefaultCategoryAdapter(GlobalAppAccess.default_categories, getActivity());
        recyclerView.setAdapter(defaultCategoryAdapter);
    }


    private void initRecyclerViewOnItemClickListener() {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        String category_name = GlobalAppAccess.default_categories[position];
                        //intent.putExtra(AppConstant.KEY_EXTRA_MOVIE_JSON,movieResponse);
                        //startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }
}
