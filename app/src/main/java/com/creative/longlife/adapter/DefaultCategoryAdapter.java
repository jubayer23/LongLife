package com.creative.longlife.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.creative.longlife.R;
import com.creative.longlife.appdata.GlobalAppAccess;

/**
 * Created by jubayer on 8/27/2017.
 */

public class DefaultCategoryAdapter extends RecyclerView.Adapter<DefaultCategoryAdapter.MyViewHolder> {

    private String[] originalList;
    private String[] receiveCardList;

    private Context activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView img_icon;


        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_category_name);
            img_icon = (ImageView) view.findViewById(R.id.img_icon);
        }
    }


    public DefaultCategoryAdapter(String[] receiveCardList, Context activity) {
        this.receiveCardList = receiveCardList;
        this.originalList = receiveCardList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_grid_home_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String category_name = receiveCardList[position];
        holder.tv_name.setText(category_name);
        holder.img_icon.setImageResource(getImageResources(category_name));
    }


    @Override
    public int getItemCount() {
        return receiveCardList.length;
    }


    private int getImageResources(String category_name){
        int id = R.drawable.ic_launcher;

        if(category_name.equals(GlobalAppAccess.CAT_FAVOURITE))
            id = R.drawable.ic_launcher;
        if(category_name.equals(GlobalAppAccess.CAT_EMERGENCY))
            id = R.drawable.ic_launcher;
        if(category_name.equals(GlobalAppAccess.CAT_OTHERS))
            id = R.drawable.ic_launcher;

        return id;
    }

}