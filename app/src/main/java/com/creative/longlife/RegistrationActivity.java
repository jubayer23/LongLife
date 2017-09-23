package com.creative.longlife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creative.longlife.alertbanner.AlertDialogForAnything;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Govt;
import com.creative.longlife.model.LocalGovts;
import com.creative.longlife.model.Login;
import com.creative.longlife.model.State;
import com.creative.longlife.model.States;
import com.creative.longlife.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private static final int ACTION_CLEAR_DATA = 100;
    RadioGroup rd_sex;
    EditText ed_name, ed_email, ed_password, ed_address;
    Button btn_submit;

    private Spinner sp_location_states, sp_location_local_govt;

    private List<String> list_location_states = new ArrayList<>();
    private List<String> list_location_local_govt = new ArrayList<>();

    private ArrayAdapter<String> spAdapterStates, spAdapterLocalGovt;

    private static final String KEY_SELECT_STATES = "Select States";
    private static final String KEY_SELECT_LOCAL_GOVT = "Select Local Govt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        initToolbar(true);

        init();

        initSpinnerAdapter();

        sendRequestToGetStates(GlobalAppAccess.URL_STATES);
    }

    private void init() {

        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_email = (EditText) findViewById(R.id.ed_email);
        ed_password = (EditText) findViewById(R.id.ed_password);
        ed_address = (EditText) findViewById(R.id.ed_address);

        rd_sex = (RadioGroup) findViewById(R.id.rd_sex);


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
        sp_location_states.setAdapter(spAdapterStates);

        list_location_local_govt.add(KEY_SELECT_LOCAL_GOVT);
        spAdapterLocalGovt = new ArrayAdapter<String>
                (this, R.layout.spinner_item, list_location_local_govt);
        sp_location_local_govt.setAdapter(spAdapterLocalGovt);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();


        if (id == R.id.btn_submit) {

            String name = ed_name.getText().toString();
            String email = ed_email.getText().toString();
            String address = ed_password.getText().toString();
            String password = ed_address.getText().toString();

            if (isValidCredentialsProvided(name, email, password, address)) {
                int selectedId = rd_sex.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
                sendRequestForRegister(GlobalAppAccess.URL_REGISTER, name, email, password, address, radioSexButton.getText().toString());
            }

        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private boolean isValidCredentialsProvided(String name, String email, String password, String address) {

        // Store values at the time of the login attempt.


        // Reset errors.
        ed_name.setError(null);
        ed_email.setError(null);
        ed_password.setError(null);
        ed_address.setError(null);
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
        if (TextUtils.isEmpty(address)) {
            ed_address.setError("Required");
            ed_address.requestFocus();
            return false;
        }


        return true;
    }

    public void sendRequestForRegister(String url, final String name, final String email, String password, final String address, final String sex) {

        url = url + "?name=" + name + "&email=" + email + "&password=" + password + "&sex=" + sex + "&location=" + address;
        // TODO Auto-generated method stub
        showProgressDialog("Loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dismissProgressDialog();

                        try {
                            JSONObject jspJsonObject = new JSONObject(response);


                            if (jspJsonObject.getInt("success") == 1) {

                                String user_id = jspJsonObject.getString("user_id");
                                User user = new User(user_id, name, email, sex, address);
                                MydApplication.getInstance().getPrefManger().setUserProfile(user);
                                startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                String error_msg = jspJsonObject.getString("message");
                                AlertDialogForAnything.showAlertDialogWhenComplte(RegistrationActivity.this, "Error", error_msg, false);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

                        updateSpinner(R.id.sp_location_local_govt,ACTION_CLEAR_DATA);
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
                sendRequestToGetLocalGovt(GlobalAppAccess.URL_LOCAL_GOVT,state_name);
            } else {
               updateSpinner(R.id.sp_location_local_govt,ACTION_CLEAR_DATA);
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void updateSpinner(int id,int action){

        if(id == R.id.sp_location_states){
            switch (action){
                case ACTION_CLEAR_DATA :
                    break;
            }
        }
        if(id == R.id.sp_location_local_govt){
            switch (action){
                case ACTION_CLEAR_DATA :
                    list_location_local_govt.clear();
                    list_location_local_govt.add(KEY_SELECT_LOCAL_GOVT);
                    spAdapterLocalGovt.notifyDataSetChanged();
                    break;
            }
        }


    }

}
