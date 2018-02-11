package com.creative.longlife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.creative.longlife.adapter.ReminderAdapter;
import com.creative.longlife.appdata.GlobalAppAccess;
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

       String alarm_fire_time = getIntent().getStringExtra("time");

        reminderAdapter = new ReminderAdapter(this, reminders,alarm_fire_time);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(reminderAdapter);


    }

    private void loadData(){
        List<Reminder> tempReminders = MydApplication.getInstance().getPrefManger().getReminders();
        Log.d("DEBUG",String.valueOf(tempReminders.size()));
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

    @Override
    public void onBackPressed() {

       String call_from = getIntent().getStringExtra(GlobalAppAccess.KEY_CALL_FROM);
        if(call_from != null && call_from.equals(GlobalAppAccess.TAG_ALARM_RECEIVER)){
            String time = getIntent().getStringExtra("time");

            int count = 0;
            for(Reminder reminder: reminders){
                if(reminder.getTime().equals(time)){
                    reminders.remove(count);
                    break;
                }
                count++;
            }
            MydApplication.getInstance().getPrefManger().setReminders(reminders);

            Intent intent = new Intent(ReminderActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();

        }else{
            super.onBackPressed();
        }

    }
}
