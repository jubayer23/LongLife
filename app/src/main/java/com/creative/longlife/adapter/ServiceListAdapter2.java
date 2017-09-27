package com.creative.longlife.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creative.longlife.R;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Service;

import java.util.HashMap;
import java.util.List;

public class ServiceListAdapter2
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private int listStyle = 0;
    private Context mContext;
    private List<Service> moviesList;
    private List<Service> favServiceList;
    private HashMap<String, Integer> isFav = new HashMap<>();


    public ServiceListAdapter2(List<Service> paramList, Context paramContext) {
        this.moviesList = paramList;
        this.mContext = paramContext;
        this.favServiceList = MydApplication.getInstance().getPrefManger().getFavServices();
        int count = 0;
        for (Service favService : favServiceList) {
            isFav.put(favService.getId(), count);
            count++;
        }
    }

    public int getItemCount() {
        return this.moviesList.size();
    }

    @Override
    public int getItemViewType(int paramInt) {
        return this.listStyle;
    }


    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int paramInt) {
        final Service service = this.moviesList.get(paramInt);

        ((ListViewHolder) holder).tv_service_title.setText(service.getTitle());
        ((ListViewHolder) holder).tv_price.setText("$ " +service.getPrice());
        ((ListViewHolder) holder).btn_see_more.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ListViewHolder) holder).view_btn_see_more.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((ListViewHolder) holder).view_btn_see_more.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                }



                return true;
            }
        });

        ((ListViewHolder) holder).btn_call_now.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ListViewHolder) holder).view_btn_call_now.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((ListViewHolder) holder).view_btn_call_now.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                }

                return true;
            }
        });



    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {

        return new ListViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.row_list_service_2, null));
    }


    public void removeAllAndNotify() {
        int i = this.moviesList.size();
        this.moviesList.clear();
        notifyItemRangeRemoved(0, i);
    }

    public void setListStyle(int paramInt) {
        this.listStyle = paramInt;
    }


    public class ListViewHolder
            extends RecyclerView.ViewHolder {
        public TextView tv_service_title;
        public TextView tv_price;
        public RelativeLayout btn_see_more;
        public RelativeLayout btn_call_now;
        public TextView view_btn_see_more;
        public TextView view_btn_call_now;

        public ListViewHolder(View paramView) {
            super(paramView);
            this.tv_service_title = ((TextView) paramView.findViewById(R.id.tv_service_title));
            this.tv_price = ((TextView) paramView.findViewById(R.id.tv_price));
            this.btn_see_more = ((RelativeLayout) paramView.findViewById(R.id.btn_see_more));
            this.btn_call_now = ((RelativeLayout) paramView.findViewById(R.id.btn_call_now));
            this.view_btn_see_more = ((TextView) paramView.findViewById(R.id.view_btn_see_more));
            this.view_btn_call_now = ((TextView) paramView.findViewById(R.id.view_btn_call_now));
            //this.tv_genre = ((TextView) paramView.findViewById(R.id.tv_genre));
            //this.tv_rating = ((TextView) paramView.findViewById(R.id.tv_rating));
        }
    }

}


/* Location:           E:\APK\dex2jar\classes-dex2jar.jar
 * Qualified Name:     vinitm.yts.Views.InfiniteScrollRecyclerView.Adapter.UserCategoryAdapter
 * JD-Core Version:    0.7.0.1
 */