package com.creative.longlife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.creative.longlife.adapter.NotificationAdapter;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notifications = new ArrayList<>();

    TextView tv_no_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initToolbar(true);
        
        init();

        initAdapter();

        loadData();
    }

    private void init() {
        tv_no_notification = (TextView)findViewById(R.id.tv_no_notification);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
    }

    private void initAdapter() {


        notificationAdapter = new NotificationAdapter(this, notifications);
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

}
