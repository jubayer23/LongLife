package com.creative.longlife;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.creative.longlife.adapter.ReminderAdapter;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Notification;
import com.creative.longlife.model.Reminder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReminderActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ReminderAdapter reminderAdapter;
    private List<Reminder> reminders = new ArrayList<>();

    TextView tv_no_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

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


        reminderAdapter = new ReminderAdapter(this, reminders);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(reminderAdapter);


    }

    private void loadData(){
        List<Reminder> tempReminders = MydApplication.getInstance().getPrefManger().getReminders();
        Collections.sort(tempReminders, new Reminder.timeComparatorDesc());

        reminders.addAll(tempReminders);

        if(reminders.isEmpty()){
            tv_no_notification.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            tv_no_notification.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        reminderAdapter.notifyDataSetChanged();
    }

}
