package com.creative.longlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creative.longlife.HomeActivity;
import com.creative.longlife.R;
import com.creative.longlife.adapter.AllCategoryAdapter;
import com.creative.longlife.adapter.UserCategoryAdapter;
import com.creative.longlife.alertbanner.AlertDialogForAnything;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Category;
import com.creative.longlife.model.CategoryList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jubayer on 8/24/2017.
 */

public class FragmentAllCategory extends android.support.v4.app.Fragment {

    // private GridView gridView;
    private RecyclerView recyclerView;
    /// private IconGridAdapter iconGridAdapter;
    private AllCategoryAdapter allCategoryAdapter;

    List<Category> categories = new ArrayList<>();
    List<Category> user_categories ;

    private Gson gson;


    //LinearLayoutManager listLayoutManager;
    GridLayoutManager gridLayoutManager;

    private FloatingActionButton fabTopToTheList;

    LinearLayout ll_no_category_warning_container;

    TextView tv_choose_category;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_category, container, false);

        user_categories = getArguments().getParcelableArrayList(HomeActivity.KEY_USER_CATEGORIES);

        // Initialize the layout view ids
        init(view);

        // initialize listView adapter
        initAdapter();

        //load all event from database and show them in the UI
        //initializeUi();

        changeUiForNoCategory(false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sendRequestToGetPlaceList(GlobalAppAccess.URL_ALL_CATEGORYLIST);
    }


    private void init(View view) {

        gson = new Gson();

        // gridView = (GridView) view.findViewById(R.id.gridview_latestmovie);
        ll_no_category_warning_container = (LinearLayout) view.findViewById(R.id.ll_no_category_warning_container);


        tv_choose_category = (TextView) view.findViewById(R.id.tv_choose_category);

        fabTopToTheList = (FloatingActionButton) view.findViewById(R.id.fabTopToTheList);
        fabTopToTheList.setVisibility(View.GONE);
        fabTopToTheList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //recyclerView.scrollToPosition(0);
                recyclerView.smoothScrollToPosition(0);
                //recyclerView.setScrollY(0);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

    }

    private void changeUiForNoCategory(boolean isNocategory) {

        if (isNocategory) {
            ll_no_category_warning_container.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            ll_no_category_warning_container.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }


    private void initAdapter() {
        // iconGridAdapter = new IconGridAdapter(getActivity(), movieList);
        //gridView.setAdapter(iconGridAdapter);

        //listLayoutManager = new LinearLayoutManager(getActivity());
        //listLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        final int numberOfColumns = 2;
        gridLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);

        recyclerView.setLayoutManager(gridLayoutManager);

        allCategoryAdapter = new AllCategoryAdapter(categories,user_categories, getActivity());
        allCategoryAdapter.setListStyle(UserCategoryAdapter.ADAPTER_FOR_ALL_CATEGORY);
        recyclerView.setAdapter(allCategoryAdapter);
    }


    public void sendRequestToGetPlaceList(String url) {

        ((HomeActivity) getActivity()).showProgressDialog("loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        ((HomeActivity) getActivity()).dismissProgressDialog();

                        CategoryList movies = gson.fromJson(response, CategoryList.class);

                        if (movies.getSuccess() == 1) {
                            categories.addAll(movies.getCategories());
                            allCategoryAdapter.notifyDataSetChanged();
                        } else {
                            changeUiForNoCategory(true);
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ((HomeActivity) getActivity()).dismissProgressDialog();
                //progressBar.setVisibility(View.GONE);
                AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(),
                        "ERROR",
                        "Something went wrong!!",
                        false);

            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }

    public List<Category> getUser_categories(){
        return user_categories;
    }
}