package com.creative.longlife.adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.creative.longlife.NotificationActivity;
import com.creative.longlife.R;
import com.creative.longlife.Utility.CommonMethods;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.fragment.DatePickerFragment;
import com.creative.longlife.fragment.TimePickerFragment;
import com.creative.longlife.model.Notification;
import com.creative.longlife.model.Reminder;
import com.creative.longlife.receiver.AlarmReceiver;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    public static final String KEY_EVENT = "key_event";
    private List<Notification> Displayedplaces;
    private List<Notification> Originalplaces;
    private LayoutInflater inflater;
    @SuppressWarnings("unused")
    private Activity activity;
    private String call_from;

    private PopupWindow popupwindow_obj;

    private int newNotificationCounter;

    private int lastPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_sub_title;
        TextView tv_add_reminder;
        SimpleDraweeView img;
        ImageView img_new_tag;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_sub_title = (TextView) view.findViewById(R.id.tv_sub_title);
            tv_add_reminder = (TextView) view.findViewById(R.id.tv_add_reminder);
            img = (SimpleDraweeView) view.findViewById(R.id.img);
            img_new_tag = (ImageView) view.findViewById(R.id.img_new_tag);

        }
    }


    public NotificationAdapter(Activity activity, List<Notification> attendees, int newNotificaionCounter) {
        this.activity = activity;
        this.Displayedplaces = attendees;
        this.Originalplaces = attendees;
        this.newNotificationCounter = newNotificaionCounter;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_notification_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Notification event = Displayedplaces.get(position);
        holder.tv_title.setText(event.getTitle());
        holder.tv_sub_title.setText(event.getBody());

        if(event.getImg_url() != null && !event.getImg_url().isEmpty()){
            Uri uri = Uri.parse(event.getImg_url());
            holder.img.setImageURI(uri);
        }

        holder.tv_add_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReminderDialog(event.getTitle(),event.getBody());
            }
        });

        if(position < newNotificationCounter){
            holder.img_new_tag.setVisibility(View.VISIBLE);
        }else{
            holder.img_new_tag.setVisibility(View.GONE);
        }

        if (position > lastPosition) {
            lastPosition = position;
            animate(holder);
        }

    }

    public void animate(NotificationAdapter.MyViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(activity, R.anim.slide_from_bottom_in);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }


    @Override
    public int getItemCount() {
        return Displayedplaces.size();
    }


    private void showReminderDialog(final String title, final String body){
        final Dialog dialog_start = new Dialog(activity,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog_start.setCancelable(true);
        dialog_start.setContentView(R.layout.dialog_set_reminder);

        final TextView tv_set_date = (TextView) dialog_start.findViewById(R.id.tv_set_date);
        final TextView tv_set_time = (TextView) dialog_start.findViewById(R.id.tv_set_time);
        Button btn_submit = (Button) dialog_start.findViewById(R.id.btn_submit);

        final Calendar calendar = Calendar.getInstance();
        calendar.clear();

        final boolean[] isDateSet = {false};
        final boolean[] isTimeSet = {false};


        final DatePickerDialog.OnDateSetListener onStartDateChange = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                isDateSet[0] = true;
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date date = calendar.getTime();
                tv_set_date.setText(CommonMethods.formatDate(date, "y-MMM-d"));
                //event_date[0] = AppConstant.getDateTimeFormat().format(date);
            }
        };

        final TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                isTimeSet[0] = true;
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                Date date = calendar.getTime();
                tv_set_time.setText(CommonMethods.formatDate(date, "HH:mm:ss"));
            }
        };

        tv_set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.callBack(onStartDateChange);
                datePicker.show(((NotificationActivity)activity).getSupportFragmentManager(), "datePicker");
            }
        });

        tv_set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePicker = new TimePickerFragment();
                timePicker.callBack(onTimeSetListener);
                timePicker.show(((NotificationActivity)activity).getSupportFragmentManager(), "timepicker");
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDateSet[0] && isTimeSet[0]){
                    setAlarm(calendar,title,body);
                    dialog_start.dismiss();
                }
            }
        });

        dialog_start.show();

    }


    private void setAlarm(Calendar targetCal,String title, String body){

        Date date = targetCal.getTime();
        String time = CommonMethods.formatDate(date, "yyyy-MM-dd HH:mm:ss");
        Reminder reminder = new Reminder(title,body,time);

        List<Reminder> reminders = MydApplication.getInstance().getPrefManger().getReminders();
        reminders.add(reminder);
        MydApplication.getInstance().getPrefManger().setReminders(reminders);


        Intent intent = new Intent(activity, AlarmReceiver.class);
        intent.putExtra("title",title);
        intent.putExtra("body",body);
        intent.putExtra("time",time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 101, intent, 0);
        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }

    private ProgressDialog progressDialog;
    public void showProgressDialog(String message, boolean isIntermidiate, boolean isCancelable) {
       /**/
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
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