package com.creative.longlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.creative.longlife.adapter.ServiceListAdapter;
import com.creative.longlife.alertbanner.AlertDialogForAnything;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Category;
import com.creative.longlife.model.Service;
import com.creative.longlife.model.ServiceList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jubayer on 8/24/2017.
 */

public class FragmentServiceList extends android.support.v4.app.Fragment {

    // private GridView gridView;
    private RecyclerView recyclerView;
    /// private IconGridAdapter iconGridAdapter;
    private ServiceListAdapter serviceListAdapter;

    List<Service> services = new ArrayList<>();

    private Gson gson;

    //LinearLayoutManager listLayoutManager;
    LinearLayoutManager listLayoutManager;

    LinearLayout ll_no_category_warning_container, ll_recycler_container;

    TextView tv_choose_category;

    private Category category;

    private TextView tv_category_name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_list, container, false);


        category = getArguments().getParcelable(HomeActivity.KEY_CATEGORY);

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

        if (category.getId().equals(GlobalAppAccess.CAT_FAVOURITE_ID)) {

            services.addAll(MydApplication.getInstance().getPrefManger().getFavServices());
            serviceListAdapter.notifyDataSetChanged();

            if (services.size() == 0)
                changeUiForNoCategory(true);


        } else {
            sendRequestToGetServiceList(GlobalAppAccess.URL_SERVICE);
        }

    }


    private void init(View view) {

        gson = new Gson();

        tv_category_name = (TextView) view.findViewById(R.id.tv_category_name);
        tv_category_name.setText(category.getName());

        // gridView = (GridView) view.findViewById(R.id.gridview_latestmovie);
        ll_no_category_warning_container = (LinearLayout) view.findViewById(R.id.ll_no_category_warning_container);
        ll_recycler_container = (LinearLayout) view.findViewById(R.id.ll_recycler_container);

        tv_choose_category = (TextView) view.findViewById(R.id.tv_choose_category);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

    }

    private void changeUiForNoCategory(boolean isNocategory) {

        if (isNocategory) {
            ll_no_category_warning_container.setVisibility(View.VISIBLE);
            ll_recycler_container.setVisibility(View.GONE);
        } else {
            ll_no_category_warning_container.setVisibility(View.GONE);
            ll_recycler_container.setVisibility(View.VISIBLE);
        }

    }


    private void initAdapter() {
        // iconGridAdapter = new IconGridAdapter(getActivity(), movieList);
        //gridView.setAdapter(iconGridAdapter);

        //listLayoutManager = new LinearLayoutManager(getActivity());
        //listLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        listLayoutManager = new LinearLayoutManager(getActivity());
        listLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        serviceListAdapter = new ServiceListAdapter(services, getActivity());

        recyclerView.setLayoutManager(listLayoutManager);

        recyclerView.setAdapter(serviceListAdapter);
    }


    public void sendRequestToGetServiceList(String url) {

        Log.d("DEBUG", url);

        url = url + "?category_id=" + category.getId();


        // url = url + "?user_id=" + MydApplication.getInstance().getPrefManger().getUserProfile().getId();

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

                        ServiceList movies = gson.fromJson(response, ServiceList.class);


                        if (movies.getSuccess() == 1) {
                            services.addAll(movies.getServices());
                            serviceListAdapter.notifyDataSetChanged();
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
}