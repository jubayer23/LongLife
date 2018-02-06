package com.creative.longlife.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creative.longlife.HomeActivity;
import com.creative.longlife.ProfileActivity;
import com.creative.longlife.R;
import com.creative.longlife.Utility.DeviceInfoUtils;
import com.creative.longlife.adapter.ExpandableListAdapter;
import com.creative.longlife.adapter.ServiceListAdapter2;
import com.creative.longlife.alertbanner.AlertDialogForAnything;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Category;
import com.creative.longlife.model.Region;
import com.creative.longlife.model.Service;
import com.creative.longlife.model.ServiceList;
import com.creative.longlife.model.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jubayer on 8/24/2017.
 */

public class FragmentServiceList extends android.support.v4.app.Fragment {

    // private GridView gridView;
    private RecyclerView recyclerView;
    /// private IconGridAdapter iconGridAdapter;
    private ServiceListAdapter2 serviceListAdapter;

    List<Service> services = new ArrayList<>();

    private Gson gson;

    //LinearLayoutManager listLayoutManager;
    LinearLayoutManager listLayoutManager;

    LinearLayout ll_no_category_warning_container, ll_recycler_container;

    TextView tv_choose_category;

    private Category category;

    private TextView tv_category_name, tv_region, tv_change_region;

    private static final int SERVICE_TYPE_EMR = 0;
    private static final int SERVICE_TYPE_OTHER = 1;

    private String state_name = "";
    private String local_govt_name = "";

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


        } else if (category.getId().equals(GlobalAppAccess.CAT_EMERGENCY_ID)) {
            if (DeviceInfoUtils.isConnectingToInternet(getActivity())) {
                sendRequestToGetServiceList(GlobalAppAccess.URL_SERVICE);
            } else {
                services.addAll(MydApplication.getInstance().getPrefManger().getEmergencyServices());
                serviceListAdapter.notifyDataSetChanged();

                if (services.size() == 0)
                    changeUiForNoCategory(true);
            }

        } else {
            sendRequestToGetServiceList(GlobalAppAccess.URL_SERVICE);
        }

    }


    private void init(View view) {

        gson = new Gson();
        User user = MydApplication.getInstance().getPrefManger().getUserProfile();

        tv_category_name = (TextView) view.findViewById(R.id.tv_category_name);
        tv_category_name.setText(category.getName());
        tv_region = (TextView) view.findViewById(R.id.tv_region);
        tv_region.setText(user.getStateName() + ", " + user.getLocalGovtName());
        tv_change_region = (TextView) view.findViewById(R.id.tv_change_region);
        tv_change_region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShowRegion2();
            }
        });
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
        serviceListAdapter = new ServiceListAdapter2(services, getActivity());

        recyclerView.setLayoutManager(listLayoutManager);

        recyclerView.setAdapter(serviceListAdapter);
    }

    Dialog dialog_start;
    ExpandableListAdapter listAdapter;
    Region region;

    public void dialogShowRegion2() {

        dialog_start = new Dialog(getActivity(),
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog_start.setCancelable(true);
        dialog_start.setContentView(R.layout.dialog_show_region_2);

        Button btn_submit = (Button) dialog_start.findViewById(R.id.btn_send);
        ImageView img_close = (ImageView) dialog_start.findViewById(R.id.img_close);
        ProgressBar loading_spinner = (ProgressBar) dialog_start.findViewById(R.id.loading_spinner);


        ExpandableListView expListView = (ExpandableListView) dialog_start.findViewById(R.id.lvExp);
        region = new Region();
        listAdapter = new ExpandableListAdapter(getActivity(), region);
        expListView.setAdapter(listAdapter);

        sendRequestToGetRegoin(GlobalAppAccess.URL_LOCAL_STATES_GOVTS, expListView, loading_spinner);

        final String[] state = new String[1];
        final String[] local_govt = new String[1];

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ExpandableListAdapter.color_group_pos >= 0 && ExpandableListAdapter.color_child_pos >= 0) {
                    state[0] = region.getLocalStates().get(ExpandableListAdapter.color_group_pos).getStateName();
                    local_govt[0] = region.getLocalStates().
                            get(ExpandableListAdapter.color_group_pos).
                            getLocalGovts().
                            get(ExpandableListAdapter.color_child_pos).
                            getGovtName();


                    updateEditTextRegion(state[0], local_govt[0]);
                    dialog_start.dismiss();

                } else {

                    Log.d("DEBUG", "called 01");
                    dialog_start.dismiss();
                }


                //state_name = sp_location_states.getSelectedItem().toString();
                // local_govt_name = sp_location_local_govt.getSelectedItem().toString();


            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_start.dismiss();
            }
        });


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                Log.d("DEBUG", state[0]);
                Log.d("DEBUG", local_govt[0]);

                return false;
            }
        });


        dialog_start.show();
    }

    private void updateEditTextRegion(String stateName, String localGovt) {
        User user = MydApplication.getInstance().getPrefManger().getUserProfile();
        this.state_name = stateName;
        this.local_govt_name = localGovt;
        tv_region.setText(stateName + ", " + localGovt);

        if (!state_name.equals(user.getStateName()) || !local_govt_name.equals(user.getLocalGovtName())) {
            sendRequestToUpdateProfile(GlobalAppAccess.URL_UPDATE_PROFILE,user.getId(), state_name, local_govt_name);
        }
    }

    public void sendRequestToGetRegoin(String url, final ExpandableListView expandableListView, final ProgressBar progressBar) {

        //url = url + "?nigerian_state=" + state_name;
        // TODO Auto-generated method stub
        //showProgressDialog("Loading..", true, false);
        progressBar.setVisibility(View.VISIBLE);

        final StringRequest req = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.d("DEBUG", response);

                        region = MydApplication.gson.fromJson(response, Region.class);

                        if (region.getStatus()) {
                            listAdapter = new ExpandableListAdapter(getActivity(), region);
                            expandableListView.setAdapter(listAdapter);
                        }

                        //dismissProgressDialog();
                        progressBar.setVisibility(View.INVISIBLE);


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //  dismissProgressDialog();

                progressBar.setVisibility(View.INVISIBLE);


                // AlertDialogForAnything.showAlertDialogWhenComplte(RegistrationActivity.this, "Error", "Network problem. please try again!", false);

            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }

    public void sendRequestToGetServiceList(String url) {


        url = url + "?category_id=" + category.getId() + "&local_govt_id=" +
                MydApplication.getInstance().getPrefManger().getUserProfile().getLocalGovtId();


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
                        Log.d("DEBUG_service", response);

                        ServiceList movies = gson.fromJson(response, ServiceList.class);


                        if (movies.getSuccess() == 1) {
                            services.clear();
                            services.addAll(movies.getServices());
                            serviceListAdapter.notifyDataSetChanged();
                        } else {
                            changeUiForNoCategory(true);
                        }

                        if (category.getId().equals(GlobalAppAccess.CAT_EMERGENCY_ID)) {
                            MydApplication.getInstance().getPrefManger().setEmergencyServices(services);
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

    public void sendRequestToUpdateProfile(String url, String user_id, final String state_name, final String local_govt_name) {


        url = url + "?user_id=" + user_id + "&state=" + state_name + "&local_govt=" + local_govt_name;

        // TODO Auto-generated method stub
        ((HomeActivity) getActivity()).showProgressDialog("Loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.d("DEBUG", response);

                        //TODO

                        ((HomeActivity) getActivity()).dismissProgressDialog();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int isSucess = jsonObject.getInt("success");
                            if (isSucess == 1) {
                                String local_govt_id = jsonObject.getString("local_govt_id");

                                User user = MydApplication.getInstance().getPrefManger().getUserProfile();

                                user.setStateName(state_name);
                                user.setLocalGovtName(local_govt_name);
                                user.setLocalGovtId(local_govt_id);

                                MydApplication.getInstance().getPrefManger().setUserProfile(user);

                                sendRequestToGetServiceList(GlobalAppAccess.URL_SERVICE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ((HomeActivity) getActivity()).dismissProgressDialog();

                AlertDialogForAnything.showAlertDialogWhenComplte(getActivity(), "Error", "Network problem. please try again!", false);

            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }
}