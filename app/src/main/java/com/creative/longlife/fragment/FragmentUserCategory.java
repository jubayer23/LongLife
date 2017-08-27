package com.creative.longlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.creative.longlife.adapter.UserCategoryAdapter;
import com.creative.longlife.alertbanner.AlertDialogForAnything;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.eventListener.RecyclerItemClickListener;
import com.creative.longlife.model.Category;
import com.creative.longlife.model.CategoryList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jubayer on 8/23/2017.
 */

public class FragmentUserCategory extends Fragment implements View.OnClickListener {

    private static final int KEY_GRID_VIEW = 0;
    private static final int KEY_LIST_VIEW = 1;
    private static int KEY_CURRENT_VIEW = KEY_GRID_VIEW;
    // private GridView gridView;
    private RecyclerView recyclerView;
    /// private IconGridAdapter iconGridAdapter;
    private UserCategoryAdapter userCategoryAdapter;

    List<Category> categories = new ArrayList<>();

    private Gson gson;


    //LinearLayoutManager listLayoutManager;
    GridLayoutManager gridLayoutManager;

    private FloatingActionButton fab_add_more_categories;

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

        initRecyclerViewClickListener();

        //load all event from database and show them in the UI
        //initializeUi();

        changeUiForNoCategory(false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sendRequestToGetPlaceList(GlobalAppAccess.URL_USER_CATEGORYLIST);
    }


    private void init(View view) {

        gson = new Gson();

        // gridView = (GridView) view.findViewById(R.id.gridview_latestmovie);
        ll_no_category_warning_container = (LinearLayout) view.findViewById(R.id.ll_no_category_warning_container);


        tv_choose_category = (TextView) view.findViewById(R.id.tv_choose_category);

        fab_add_more_categories = (FloatingActionButton) view.findViewById(R.id.fab_add_more_category);
        fab_add_more_categories.setOnClickListener(this);

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

        userCategoryAdapter = new UserCategoryAdapter(categories, getActivity());
        userCategoryAdapter.setListStyle(UserCategoryAdapter.ADAPTER_FOR_USER_SELECTED_CATEGORY);
        recyclerView.setAdapter(userCategoryAdapter);
    }

    private void initRecyclerViewClickListener() {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        Log.d("DEBUG","called");
                        Category category = categories.get(position);
                        ((HomeActivity) getActivity()).proceedToDisplayServiceListPage(category);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
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
                            userCategoryAdapter.notifyDataSetChanged();
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

    public List<Category> userCategories() {
        return categories;
    }

    public void setUserCategories(List<Category> newUserCategories) {
        categories.clear();

        categories.addAll(newUserCategories);

        if (categories.size() > 0) {
            changeUiForNoCategory(false);
        } else {
            changeUiForNoCategory(true);
        }

        userCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fab_add_more_category) {
            ((HomeActivity) getActivity()).proceedToAllCategoryActivity(categories);
        }
    }

    public void filterSearch(String text){

        //userCategoryAdapter.filter(text);
        userCategoryAdapter.getFilter().filter(text);
    }


}
