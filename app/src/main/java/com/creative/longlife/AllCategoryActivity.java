package com.creative.longlife;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

public class AllCategoryActivity extends BaseActivity {

    private Gson gson;

    // private GridView gridView;
    private RecyclerView recyclerView;
    /// private IconGridAdapter iconGridAdapter;
    private AllCategoryAdapter recyclerViewAdapter;

    List<Category> categories = new ArrayList<>();

    //LinearLayoutManager listLayoutManager;
    GridLayoutManager gridLayoutManager;

    private FloatingActionButton fabTopToTheList;

    LinearLayout ll_no_category_warning_container;

    private List<Category> user_categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);

        initToolbar(true);

        init();

        //user_categories = getIntent().getParcelableExtra(HomeActivity.KEY_USER_CATEGORIES);
        user_categories = this.getIntent().getExtras().getParcelableArrayList(HomeActivity.KEY_USER_CATEGORIES);
        Log.d("DEBUG",String.valueOf(user_categories.size()));
        // initialize listView adapter
        initAdapter();

        //load all event from database and show them in the UI
        //initializeUi();
        changeUiForNoCategory(false);

        sendRequestToGetPlaceList(GlobalAppAccess.URL_ALL_CATEGORYLIST);

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


    private void init() {
        gson = new Gson();

        // gridView = (GridView) view.findViewById(R.id.gridview_latestmovie);
        ll_no_category_warning_container = (LinearLayout) findViewById(R.id.ll_no_category_warning_container);

        fabTopToTheList = (FloatingActionButton) findViewById(R.id.fabTopToTheList);
        fabTopToTheList.setVisibility(View.GONE);
        fabTopToTheList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //recyclerView.scrollToPosition(0);
                recyclerView.smoothScrollToPosition(0);
                //recyclerView.setScrollY(0);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }


    private void initAdapter() {
        // iconGridAdapter = new IconGridAdapter(getActivity(), movieList);
        //gridView.setAdapter(iconGridAdapter);

        //listLayoutManager = new LinearLayoutManager(getActivity());
        //listLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        final int numberOfColumns = 2;
        gridLayoutManager = new GridLayoutManager(this, numberOfColumns);

        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerViewAdapter = new AllCategoryAdapter(categories,user_categories,this);
        recyclerViewAdapter.setListStyle(UserCategoryAdapter.ADAPTER_FOR_USER_SELECTED_CATEGORY);
        recyclerView.setAdapter(recyclerViewAdapter);
    }


    public void sendRequestToGetPlaceList(String url) {

      showProgressDialog("loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                      dismissProgressDialog();

                        CategoryList movies = gson.fromJson(response, CategoryList.class);

                        if (movies.getSuccess() == 1) {
                            categories.addAll(movies.getCategories());
                            recyclerViewAdapter.notifyDataSetChanged();
                        } else {
                            changeUiForNoCategory(true);
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgressDialog();
                //progressBar.setVisibility(View.GONE);
                AlertDialogForAnything.showAlertDialogWhenComplte(AllCategoryActivity.this,
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:

                onBackPressed();
                break;

        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        //returnIntent.putExtra(HomeActivity.KEY_USER_CATEGORIES, (Parcelable) user_categories);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(HomeActivity.KEY_USER_CATEGORIES, (ArrayList<? extends Parcelable>) user_categories);
        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK,returnIntent);
        super.onBackPressed();
    }


}
