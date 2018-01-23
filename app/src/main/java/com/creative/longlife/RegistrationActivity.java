package com.creative.longlife;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.creative.longlife.model.Govt;
import com.creative.longlife.model.LocalGovt;
import com.creative.longlife.model.LocalGovts;
import com.creative.longlife.model.LocalState;
import com.creative.longlife.model.Region;
import com.creative.longlife.model.State;
import com.creative.longlife.model.States;
import com.creative.longlife.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private static final int ACTION_CLEAR_DATA = 100;
    RadioGroup rd_sex;
    EditText ed_name, ed_email, ed_password, ed_gd_code, ed_region, ed_year_of_birth;
    TextView tv_region;
    Button btn_submit;

    private Spinner sp_location_states, sp_location_local_govt;

    private List<String> list_location_states = new ArrayList<>();
    private List<String> list_location_local_govt = new ArrayList<>();

    private ArrayAdapter<String> spAdapterStates, spAdapterLocalGovt;

    private static final String KEY_SELECT_STATES = "Select States";
    private static final String KEY_SELECT_LOCAL_GOVT = "Select Local Govt";

    private String state_name = "";
    private String local_govt_name = "";


    private LinearLayout ll_select_region;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();

        setContentView(R.layout.activity_registration);


        initToolbar(true);
      //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);


        init();

        initSpinnerAdapter();

      //  sendRequestToGetStates(GlobalAppAccess.URL_STATES);
    }

    private void init() {

        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_email = (EditText) findViewById(R.id.ed_email);
        ed_password = (EditText) findViewById(R.id.ed_password);
        ed_gd_code = (EditText) findViewById(R.id.ed_gd_code);
        ed_year_of_birth = (EditText) findViewById(R.id.ed_year_of_birth);
        tv_region = (TextView) findViewById(R.id.tv_region);

        rd_sex = (RadioGroup) findViewById(R.id.rd_sex);
        ll_select_region = (LinearLayout) findViewById(R.id.ll_select_region);
        ll_select_region.setOnClickListener(this);


        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        sp_location_states = (Spinner) findViewById(R.id.sp_location_states);
        sp_location_states.setOnItemSelectedListener(this);
        sp_location_local_govt = (Spinner) findViewById(R.id.sp_location_local_govt);
        sp_location_local_govt.setOnItemSelectedListener(this);
    }

    private void initSpinnerAdapter() {

        list_location_states.add(KEY_SELECT_STATES);
        spAdapterStates = new ArrayAdapter<String>
                (this, R.layout.spinner_item, list_location_states);
        //sp_location_states.setAdapter(spAdapterStates);

        list_location_local_govt.add(KEY_SELECT_LOCAL_GOVT);
        spAdapterLocalGovt = new ArrayAdapter<String>
                (this, R.layout.spinner_item, list_location_local_govt);
        // sp_location_local_govt.setAdapter(spAdapterLocalGovt);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();


        if (id == R.id.btn_submit) {

            String name = ed_name.getText().toString();
            String email = ed_email.getText().toString();
            String password = ed_password.getText().toString();
            String gd_code = ed_gd_code.getText().toString();
            String year_of_birth = ed_year_of_birth.getText().toString();
            //String state_name = sp_location_states.getSelectedItem().toString();
            //String local_govt_name = sp_location_local_govt.getSelectedItem().toString();

            if (isValidCredentialsProvided(name, email, password,year_of_birth)) {
                if (state_name.equals(KEY_SELECT_STATES) || state_name.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "You must select region!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (local_govt_name.equals(KEY_SELECT_LOCAL_GOVT) || local_govt_name.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "You must select region!", Toast.LENGTH_LONG).show();
                    return;
                }

                int selectedId = rd_sex.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
                sendRequestForRegister(GlobalAppAccess.URL_REGISTER,
                        name,
                        email,
                        password,
                        year_of_birth,
                        state_name,
                        local_govt_name,
                        radioSexButton.getText().toString());
            }

        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private boolean isValidCredentialsProvided(String name, String email, String password, String year_of_birth) {

        // Store values at the time of the login attempt.


        // Reset errors.
        ed_name.setError(null);
        ed_email.setError(null);
        ed_password.setError(null);
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(name)) {
            ed_name.setError("Required");
            ed_name.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            ed_email.setError("Required");
            ed_email.requestFocus();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ed_email.setError("Invalid");
            ed_email.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            ed_password.setError("Required");
            ed_password.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(year_of_birth)) {
            ed_year_of_birth.setError("Required");
            ed_year_of_birth.requestFocus();
            return false;
        }

        Calendar c = Calendar.getInstance();
        int current_year = c.get(Calendar.YEAR);
        int user_year_of_birth = Integer.parseInt(year_of_birth);
        if((current_year - user_year_of_birth) <= 12 || (current_year - user_year_of_birth) >= 100){
            ed_year_of_birth.setError("Invalid input");
            ed_year_of_birth.requestFocus();
            return false;
        }


        return true;
    }


    Dialog dialog_start;

    public void dialogShowRegion(View view) {

        dialog_start = new Dialog(RegistrationActivity.this,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog_start.setCancelable(true);
        dialog_start.setContentView(R.layout.dialog_show_region);

        final Spinner sp_location_states = (Spinner) dialog_start.findViewById(R.id.sp_location_states);
        final Spinner sp_location_local_govt = (Spinner) dialog_start.findViewById(R.id.sp_location_local_govt);
        Button btn_submit = (Button) dialog_start.findViewById(R.id.btn_send);
        ImageView img_close = (ImageView) dialog_start.findViewById(R.id.img_close);


        sp_location_states.setAdapter(spAdapterStates);
        sp_location_local_govt.setAdapter(spAdapterLocalGovt);


        sp_location_states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String state_name = adapterView.getItemAtPosition(i).toString();

                if (!state_name.equals(KEY_SELECT_STATES)) {
                    // do your stuff
                    sendRequestToGetLocalGovt(GlobalAppAccess.URL_LOCAL_GOVT, state_name);
                } else {
                    updateSpinner(R.id.sp_location_local_govt, ACTION_CLEAR_DATA);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                state_name = sp_location_states.getSelectedItem().toString();
                local_govt_name = sp_location_local_govt.getSelectedItem().toString();

                dialog_start.dismiss();
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_start.dismiss();
            }
        });


        dialog_start.show();
    }


    ExpandableListAdapter listAdapter;
    Region region;

    public void dialogShowRegion2(View view) {

        dialog_start = new Dialog(RegistrationActivity.this,
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
        this.state_name = stateName;
        this.local_govt_name = localGovt;
        tv_region.setText(stateName + ", " + localGovt);
        tv_region.setTextColor(getResources().getColor(R.color.white));

    }

    public void sendRequestForRegister(String url, final String name, final String email, String password,
                                       String gd_code, final String state_name,
                                       final String local_govt_name, final String sex) {

        if (gd_code.isEmpty())
            gd_code = "-100";
        url = url + "?name=" + name + "&email=" + email + "&password=" + password
                + "&gdCode=" + gd_code + "&sex=" + sex + "&stateName=" + state_name
                + "&localGovtName=" + local_govt_name + "&dob=" + "12/13/2017";

        // TODO Auto-generated method stub
        showProgressDialog("Loading..", true, false);

        final String finalGd_code = gd_code;
        final StringRequest req = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.d("DEBUG",response);

                        dismissProgressDialog();

                        try {
                            JSONObject jspJsonObject = new JSONObject(response);


                            if (jspJsonObject.getInt("success") == 1) {

                                String user_id = jspJsonObject.getString("user_id");
                                User user = new User(user_id, name, email, finalGd_code, state_name, local_govt_name, sex, "12/13/2017");
                                MydApplication.getInstance().getPrefManger().setUserProfile(user);
                                startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                String error_msg = jspJsonObject.getString("message");
                                AlertDialogForAnything.showAlertDialogWhenComplte(RegistrationActivity.this, "Error", error_msg, false);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AlertDialogForAnything.showAlertDialogWhenComplte(RegistrationActivity.this, "Error", "Something went wrong! Please try again later.", false);

                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgressDialog();

                AlertDialogForAnything.showAlertDialogWhenComplte(RegistrationActivity.this, "Error", "Network problem. please try again!", false);

            }
        })// {

                // @Override
                // protected Map<String, String> getParams() throws AuthFailureError {
                //    Map<String, String> params = new HashMap<String, String>();
                //    params.put("email", email);
                //     params.put("password", password);
                ///     return params;
                //  }
                //}
                ;

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

    public void sendRequestToGetStates(String url) {

        // TODO Auto-generated method stub
        showProgressDialog("Loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dismissProgressDialog();

                        States states = MydApplication.gson.fromJson(response, States.class);

                        if (states.getSuccess() == 1) {

                            for (State state : states.getStates()) {
                                list_location_states.add(state.getName());
                            }

                            spAdapterStates.notifyDataSetChanged();

                        } else {
                            AlertDialogForAnything.showAlertDialogWhenComplte(RegistrationActivity.this, "Error", "Wrong login information!", false);
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgressDialog();

                AlertDialogForAnything.showAlertDialogWhenComplte(RegistrationActivity.this, "Error", "Network problem. please try again!", false);

            }
        })// {

                // @Override
                // protected Map<String, String> getParams() throws AuthFailureError {
                //    Map<String, String> params = new HashMap<String, String>();
                //    params.put("email", email);
                //     params.put("password", password);
                ///     return params;
                //  }
                //}
                ;

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
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
                            listAdapter = new ExpandableListAdapter(RegistrationActivity.this, region);
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

    public void sendRequestToGetLocalGovt(String url, String state_name) {

        url = url + "?nigerian_state=" + state_name;
        // TODO Auto-generated method stub
        showProgressDialog("Loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dismissProgressDialog();

                        LocalGovts localGovts = MydApplication.gson.fromJson(response, LocalGovts.class);

                        updateSpinner(R.id.sp_location_local_govt, ACTION_CLEAR_DATA);
                        if (localGovts.getSuccess() == 1) {

                            for (Govt govt : localGovts.getGovts()) {
                                list_location_local_govt.add(govt.getName());
                            }

                            spAdapterLocalGovt.notifyDataSetChanged();

                        } else {
                            AlertDialogForAnything.showAlertDialogWhenComplte(RegistrationActivity.this, "Error", localGovts.getMessage(), false);
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgressDialog();

                AlertDialogForAnything.showAlertDialogWhenComplte(RegistrationActivity.this, "Error", "Network problem. please try again!", false);

            }
        })// {

                // @Override
                // protected Map<String, String> getParams() throws AuthFailureError {
                //    Map<String, String> params = new HashMap<String, String>();
                //    params.put("email", email);
                //     params.put("password", password);
                ///     return params;
                //  }
                //}
                ;

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        int id = adapterView.getId();

        String state_name = adapterView.getItemAtPosition(i).toString();
        if (id == R.id.sp_location_states) {
            if (!state_name.equals(KEY_SELECT_STATES)) {
                // do your stuff
                sendRequestToGetLocalGovt(GlobalAppAccess.URL_LOCAL_GOVT, state_name);
            } else {
                updateSpinner(R.id.sp_location_local_govt, ACTION_CLEAR_DATA);
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void updateSpinner(int id, int action) {

        if (id == R.id.sp_location_states) {
            switch (action) {
                case ACTION_CLEAR_DATA:
                    break;
            }
        }
        if (id == R.id.sp_location_local_govt) {
            switch (action) {
                case ACTION_CLEAR_DATA:
                    list_location_local_govt.clear();
                    list_location_local_govt.add(KEY_SELECT_LOCAL_GOVT);
                    spAdapterLocalGovt.notifyDataSetChanged();
                    break;
            }
        }


    }


    private String dummyResponse() {
        return "{\n" +
                "\t\"status\": true,\n" +
                "\t\"local_states\": [{\n" +
                "\t\t\t\"state_name\": \"abc\",\n" +
                "\t\t\t\"local_govt\": [\n" +
                "\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"id\": 1,\n" +
                "\t\t\t\t\t\"govt_name\": \"def\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"id\": 2,\n" +
                "\t\t\t\t\t\"govt_name\": \"def\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"state_name\": \"ghi\",\n" +
                "\t\t\t\"local_govt\": [\n" +
                "\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"id\": 1,\n" +
                "\t\t\t\t\t\"govt_name\": \"sdas\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\n" +
                "\t\t\t\t\t\"id\": 2,\n" +
                "\t\t\t\t\t\"govt_name\": \"dedfdf\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";
    }

}
