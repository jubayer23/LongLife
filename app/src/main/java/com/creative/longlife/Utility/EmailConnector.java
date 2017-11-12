package com.creative.longlife.Utility;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creative.longlife.R;
import com.creative.longlife.alertbanner.AlertDialogForAnything;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.appdata.MydApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jubayer on 7/2/2017.
 */
/* This file is for open email dialog box to user when user click send by method "Email"*/
public class EmailConnector {
    private Context context;
    Dialog dialog_start;
    private static final  int SUCCESS = 1;
    private static  final  int ERROR = 0;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public EmailConnector(Context context) {

        this.context = context;
    }

    public void showEmailDialog() {
        dialog_start = new Dialog(context,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog_start.setCancelable(true);
        dialog_start.setContentView(R.layout.dialog_email);

        final EditText ed_email_address = (EditText) dialog_start.findViewById(R.id.ed_email_address);
        Button btn_send = (Button) dialog_start.findViewById(R.id.btn_send);
        ImageView img_close = (ImageView) dialog_start.findViewById(R.id.img_close);


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ed_email_address.getText().toString();
                if (!email.isEmpty()
                        && email.matches(emailPattern)) {

                    sendRequestToServerToSendEmail(GlobalAppAccess.URL_FORGOT_PASSWORD,
                            ed_email_address.getText().toString());

                }else{
                    ed_email_address.setError("Give valid email address!!");
                }
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

    public void sendRequestToServerToSendEmail(String url, final String email_address) {

        // TODO Auto-generated method stub
        url = url + "?email="+ email_address;

        showProgressDialog("Loading", true, false);

        final StringRequest req = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //Log.d("DEBUG",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int result = jsonObject.getInt("success");
                            if(result == 1){
                                if(dialog_start.isShowing())dialog_start.dismiss();
                                showSuccessDialog(SUCCESS);
                            }else{
                                showSuccessDialog(ERROR);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AlertDialogForAnything.showAlertDialogWhenComplte(
                                    context,"Error","Server Not Responding",false
                            );
                        }
                        dismissProgressDialog();



                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgressDialog();

                AlertDialogForAnything.showAlertDialogWhenComplte(context, "ERROR", "Server Not Responding! Please try again later!", false);

            }
        }) ;

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);
    }

    private void showSuccessDialog(int code){
        switch (code) {
            case SUCCESS:
               AlertDialogForAnything.showAlertDialogWhenComplte(context,"Alert",
                       "Please check your mail to recover your password!",true);
                break;
            case ERROR:
                AlertDialogForAnything.showAlertDialogWhenComplte(context,"Alert",
                        "There is an error! Please try again later!",false);
                break;
        }
    }

    private ProgressDialog progressDialog;

    public void showProgressDialog(String message, boolean isIntermidiate, boolean isCancelable) {
       /**/
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog.setIndeterminate(isIntermidiate);
        progressDialog.setCancelable(isCancelable);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog == null) {
            return;
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}