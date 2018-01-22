package com.creative.longlife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.User;

public class Profile extends BaseActivity implements View.OnClickListener{

    private EditText ed_name;

    TextView tv_email, tv_state, tv_local_govt, tv_change_region;

    Button btn_update;

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

        }

        if(id == R.id.tv_change_region){

        }
    }
}
