package com.creative.longlife.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.creative.longlife.R;
import com.creative.longlife.Utility.CommonMethods;
import com.creative.longlife.model.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder> {

    public static final String KEY_EVENT = "key_event";
    private List<Reminder> Displayedplaces;
    private List<Reminder> Originalplaces;
    private LayoutInflater inflater;
    @SuppressWarnings("unused")
    private Activity activity;
    private String call_from;

    private PopupWindow popupwindow_obj;

    private int lastPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_sub_title;
        TextView tv_day;
        TextView tv_month;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_sub_title = (TextView) view.findViewById(R.id.tv_sub_title);
            tv_day = (TextView) view.findViewById(R.id.tv_day);
            tv_month = (TextView) view.findViewById(R.id.tv_month);
        }
    }


    public ReminderAdapter(Activity activity, List<Reminder> attendees) {
        this.activity = activity;
        this.Displayedplaces = attendees;
        this.Originalplaces = attendees;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_reminder_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Reminder event = Displayedplaces.get(position);
        holder.tv_title.setText(event.getTitle());
        holder.tv_sub_title.setText(event.getBody());

        SimpleDateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
        try {
            Date date = readFormat.parse(event.getTime());
            String day =  CommonMethods.formatDate(date, "dd");
            holder.tv_day.setText(day);

            String month =  CommonMethods.formatDate(date, "MMM");
            holder.tv_month.setText(month);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        if (position > lastPosition) {
            lastPosition = position;
            animate(holder);
        }

    }

    public void animate(ReminderAdapter.MyViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(activity, R.anim.slide_from_bottom_in);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }


    @Override
    public int getItemCount() {
        return Displayedplaces.size();
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