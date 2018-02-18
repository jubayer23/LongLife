package com.creative.longlife.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creative.longlife.R;
import com.creative.longlife.ServiceDetailsActivity;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Category;
import com.creative.longlife.model.Service;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceListAdapter2
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    public static final String KEY_SERVICE = "service";
    private int listStyle = 0;
    private Context mContext;
    private List<Service> moviesList;
    private List<Service> originalMovieList;
    private List<Service> favServiceList;
    private HashMap<String, Integer> isFav = new HashMap<>();


    public ServiceListAdapter2(List<Service> paramList, Context paramContext) {
        this.moviesList = paramList;
        this.originalMovieList = paramList;
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
        ((ListViewHolder) holder).tv_city.setText(service.getCompany().getCity());
        ((ListViewHolder) holder).tv_price.setText(service.getCompany().getName());

        if(service.getImg_url() != null && !service.getImg_url().isEmpty()){
            Uri imageUri = Uri.parse(GlobalAppAccess.BASE_URL_IMAGE + service.getImg_url());
            ((ListViewHolder) holder).img_cover.setImageURI(imageUri);
        }



        ((ListViewHolder) holder).btn_see_more.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ((ListViewHolder) holder).view_btn_see_more.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((ListViewHolder) holder).view_btn_see_more.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));

                    Intent intent = new Intent(mContext, ServiceDetailsActivity.class);
                    intent.putExtra(KEY_SERVICE, (Serializable)service);
                    mContext.startActivity(intent);
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

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + service.getCompany().getPhone()));
                    mContext.startActivity(intent);
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
        public TextView tv_city;
        public SimpleDraweeView img_cover;

        public ListViewHolder(View paramView) {
            super(paramView);
            this.tv_service_title = ((TextView) paramView.findViewById(R.id.tv_service_title));
            this.tv_price = ((TextView) paramView.findViewById(R.id.tv_price));
            this.btn_see_more = ((RelativeLayout) paramView.findViewById(R.id.btn_see_more));
            this.btn_call_now = ((RelativeLayout) paramView.findViewById(R.id.btn_call_now));
            this.view_btn_see_more = ((TextView) paramView.findViewById(R.id.view_btn_see_more));
            this.view_btn_call_now = ((TextView) paramView.findViewById(R.id.view_btn_call_now));
            this.tv_city = ((TextView) paramView.findViewById(R.id.tv_city));
            this.img_cover = ((SimpleDraweeView) paramView.findViewById(R.id.img_cover));
            //this.tv_genre = ((TextView) paramView.findViewById(R.id.tv_genre));
            //this.tv_rating = ((TextView) paramView.findViewById(R.id.tv_rating));
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                moviesList = (List<Service>) results.values;
                ServiceListAdapter2.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Service> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = originalMovieList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<Service> getFilteredResults(String constraint) {
        List<Service> results = new ArrayList<>();

        for (Service item : originalMovieList) {

            if (item.getTitle().toLowerCase().contains(constraint)) {
                results.add(item);
            }


        }
        return results;
    }

}


/* Location:           E:\APK\dex2jar\classes-dex2jar.jar
 * Qualified Name:     vinitm.yts.Views.InfiniteScrollRecyclerView.Adapter.UserCategoryAdapter
 * JD-Core Version:    0.7.0.1
 */