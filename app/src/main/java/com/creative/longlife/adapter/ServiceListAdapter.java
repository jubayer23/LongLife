package com.creative.longlife.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creative.longlife.R;
import com.creative.longlife.alertbanner.AlertDialogForAnything;
import com.creative.longlife.appdata.GlobalAppAccess;
import com.creative.longlife.appdata.MydApplication;
import com.creative.longlife.model.Category;
import com.creative.longlife.model.Service;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ServiceListAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private int listStyle = 0;
    private Context mContext;
    private List<Service> moviesList;
    private List<Service> favServiceList;
    private HashMap<String, Integer> isFav = new HashMap<>();


    public ServiceListAdapter(List<Service> paramList, Context paramContext) {
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

        ((ServiceListAdapter.ListViewHolder) holder).tv_service_title.setText(service.getTitle());
        ((ListViewHolder) holder).tv_price.setText(service.getPrice());

        if (isFav.containsKey(service.getId())) {
            ((ListViewHolder) holder).img_fav.setImageResource(R.drawable.love_fill);
        }else{
            ((ListViewHolder) holder).img_fav.setImageResource(R.drawable.love);
        }

        ((ListViewHolder) holder).img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFav.containsKey(service.getId())) {
                    for(int i =0;i<favServiceList.size();i++){
                        if(favServiceList.get(i).getId().equals(service.getId())){
                            favServiceList.remove(i);
                            isFav.remove(service.getId());
                            MydApplication.getInstance().getPrefManger().setFavServices(favServiceList);
                            ((ListViewHolder) holder).img_fav.setImageResource(R.drawable.love);
                            break;
                        }
                    }

                }else{
                    favServiceList.add(service);
                    isFav.put(service.getId(),favServiceList.size() -1 );
                    MydApplication.getInstance().getPrefManger().setFavServices(favServiceList);
                    ((ListViewHolder) holder).img_fav.setImageResource(R.drawable.love_fill);
                }
            }
        });

    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {

        return new ListViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.row_list_service, null));
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
        public ImageView img_fav;

        public ListViewHolder(View paramView) {
            super(paramView);
            this.tv_service_title = ((TextView) paramView.findViewById(R.id.tv_service_title));
            this.tv_price = ((TextView) paramView.findViewById(R.id.tv_price));
            this.img_fav = ((ImageView) paramView.findViewById(R.id.img_fav));
            //this.tv_genre = ((TextView) paramView.findViewById(R.id.tv_genre));
            //this.tv_rating = ((TextView) paramView.findViewById(R.id.tv_rating));
        }
    }

}


/* Location:           E:\APK\dex2jar\classes-dex2jar.jar
 * Qualified Name:     vinitm.yts.Views.InfiniteScrollRecyclerView.Adapter.UserCategoryAdapter
 * JD-Core Version:    0.7.0.1
 */