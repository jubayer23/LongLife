package com.creative.longlife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.creative.longlife.adapter.NotificationAdapter;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Notification;
import com.creative.longlife.model.Reminder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notifications = new ArrayList<>();

    TextView tv_no_notification;

    private int newNotificationCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initToolbar(true);
        
        init();

        checkNewNotificationAndResetCounter();

        initAdapter();

        loadData();
    }

    private void init() {
        tv_no_notification = (TextView)findViewById(R.id.tv_no_notification);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
    }

    private void checkNewNotificationAndResetCounter(){
        newNotificationCounter = MydApplication.getInstance().getPrefManger().getNewNotificationCounter();
        MydApplication.getInstance().getPrefManger().setNewNotificationCounter(0);
    }

    private void initAdapter() {


        notificationAdapter = new NotificationAdapter(this, notifications,newNotificationCounter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(notificationAdapter);


    }

    private void loadData(){
        List<Notification> tempNotifications = MydApplication.getInstance().getPrefManger().getNotifications();
        Collections.reverse(tempNotifications);

        notifications.addAll(tempNotifications);

        if(notifications.isEmpty()){
            tv_no_notification.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            tv_no_notification.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        notificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {

        String call_from = getIntent().getStringExtra(GlobalAppAccess.KEY_CALL_FROM);
        if(call_from != null && call_from.equals(GlobalAppAccess.TAG_NOTIFICATION)){
            Intent intent = new Intent(NotificationActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();

        }else{
            super.onBackPressed();
        }
    }

}
