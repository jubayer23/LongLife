package com.creative.longlife;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creative.longlife.adapter.ExpandableListAdapter;
import com.creative.longlife.alertbanner.AlertDialogForAnything;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Region;
import com.creative.longlife.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends BaseActivity implements View.OnClickListener{

    private EditText ed_name;

    TextView tv_email, tv_state, tv_local_govt, tv_change_region;

    Button btn_update;

    private String state_name = "";
    private String local_govt_name = "";

    private boolean isRegionChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initToolbar();

        init();

        initializeValue();
    }

    private void init(){

        ed_name = (EditText) findViewById(R.id.ed_name);
        tv_email = (TextView) findViewById(R.id.tv_email);

        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_local_govt = (TextView) findViewById(R.id.tv_local_govt);
        tv_change_region = (TextView) findViewById(R.id.tv_change_region);

        tv_change_region.setOnClickListener(this);

        btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(this);


    }

    private void initializeValue(){
        User user = MydApplication.getInstance().getPrefManger().getUserProfile();
        ed_name.setText(user.getName());
        tv_email.setText(user.getEmail());
        tv_state.setText(user.getStateName());
        tv_local_govt.setText(user.getLocalGovtName());

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btn_update){
            User user = MydApplication.getInstance().getPrefManger().getUserProfile();
            String name = ed_name.getText().toString();
            if(name != null && name.isEmpty()){
                ed_name.setError("Required");
                return;
            }

            if(name != null && user.getName().equals(name) && !isRegionChanged){
                /**
                 * if this condition is true that means there has not been made any new changes
                 * */
                finish();
                return;
            }

            sendRequestToUpdateProfile(GlobalAppAccess.URL_UPDATE_PROFILE,user.getId(),name,state_name,local_govt_name);

        }

        if(id == R.id.tv_change_region){
            dialogShowRegion2();
        }
    }

    Dialog dialog_start;
    ExpandableListAdapter listAdapter;
    Region region;
    public void dialogShowRegion2() {

        dialog_start = new Dialog(ProfileActivity.this,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog_start.setCancelable(true);
        dialog_start.setContentView(R.layout.dialog_show_region_2);

        Button btn_submit = (Button) dialog_start.findViewById(R.id.btn_send);
        ImageView img_close = (ImageView) dialog_start.findViewById(R.id.img_close);
        ProgressBar loading_spinner = (ProgressBar) dialog_start.findViewById(R.id.loading_spinner);


        ExpandableListView expListView = (ExpandableListView) dialog_start.findViewById(R.id.lvExp);
        region = new Region();
        listAdapter = new ExpandableListAdapter(this, region);
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
        tv_state.setText(stateName);
        tv_local_govt.setText(localGovt);

        if(!state_name.equals(user.getStateName()) || !local_govt_name.equals(user.getLocalGovtName())){
            isRegionChanged = true;
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
                            listAdapter = new ExpandableListAdapter(ProfileActivity.this, region);
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

    public void sendRequestToUpdateProfile(String url, String user_id,final String name, final String state_name, final String local_govt_name) {

        url = url + "?user_id=" + user_id + "&name=" + name;

        if(!state_name.isEmpty() && !local_govt_name.isEmpty()){
            url = url + "&state=" + state_name + "&local_govt=" + local_govt_name;
        }
        // TODO Auto-generated method stub
        showProgressDialog("Loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("DEBUG", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int isSucess = jsonObject.getInt("success");
                            if(isSucess == 1){
                                String local_govt_id = jsonObject.getString("local_govt_id");

                                User user = MydApplication.getInstance().getPrefManger().getUserProfile();
                                user.setName(name);
                                if(!state_name.isEmpty() && !local_govt_name.isEmpty()){
                                    user.setStateName(state_name);
                                    user.setLocalGovtName(local_govt_name);
                                }
                                user.setLocalGovtId(local_govt_id);
                                MydApplication.getInstance().getPrefManger().setUserProfile(user);
                                Toast.makeText(ProfileActivity.this,"Successfully profile updated!",Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dismissProgressDialog();


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                  dismissProgressDialog();

               AlertDialogForAnything.showAlertDialogWhenComplte(ProfileActivity.this, "Error", "Network problem. please try again!", false);

            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }
}
