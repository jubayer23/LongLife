package com.creative.longlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.creative.longlife.adapter.RecyclerViewAdapter;
import com.creative.longlife.alertbanner.AlertDialogForAnything;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Category;
import com.creative.longlife.model.CategoryList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jubayer on 8/23/2017.
 */

public class FragmentCategory extends Fragment {

    private static final int KEY_GRID_VIEW = 0;
    private static final int KEY_LIST_VIEW = 1;
    private static int KEY_CURRENT_VIEW = KEY_GRID_VIEW;
    // private GridView gridView;
    private RecyclerView recyclerView;
    /// private IconGridAdapter iconGridAdapter;
    private RecyclerViewAdapter recyclerViewAdapter;

    List<Category> categories = new ArrayList<>();

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
        View view = inflater.inflate(R.layout.fragment_category, container, false);


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

        recyclerViewAdapter = new RecyclerViewAdapter(categories, getActivity());
        recyclerView.setAdapter(recyclerViewAdapter);
    }


    public void sendRequestToGetPlaceList(String url) {

        Log.d("DEBUG", url);


        url = url + "?user_id=" + MydApplication.getInstance().getPrefManger().getUserProfile().getId();

        // TODO Auto-generated method stub
        // final ProgressBar progressBar = (ProgressBar)dialog_add_tag.findViewById(R.id.dialog_progressbar);
        //progressBar.setVisibility(View.VISIBLE);
        ((HomeActivity) getActivity()).showProgressDialog("loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        ((HomeActivity) getActivity()).dismissProgressDialog();


                        // progressBar.setVisibility(View.GONE);
                        Log.d("DEBUG", response);

                        CategoryList movies = gson.fromJson(response, CategoryList.class);


                        if (movies.getSuccess() == 1) {
                            categories.addAll(movies.getCategories());
                            recyclerViewAdapter.notifyDataSetChanged();
                        } else {

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
}
