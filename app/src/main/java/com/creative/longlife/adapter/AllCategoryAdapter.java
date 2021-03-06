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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AllCategoryAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private int listStyle = 0;
    private Context mContext;
    private List<Category> moviesList;
    private List<Category> user_categories;
    private boolean isOnProgress = false;


    public AllCategoryAdapter(List<Category> paramList, List<Category> user_categories, Context paramContext) {
        this.moviesList = paramList;
        this.user_categories = user_categories;
        this.mContext = paramContext;
    }

    public int getItemCount() {
        return this.moviesList.size();
    }

    @Override
    public int getItemViewType(int paramInt) {
        return this.listStyle;
    }


    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int paramInt) {
        final Category category = this.moviesList.get(paramInt);

        ((ListViewHolder) holder).tv_category_name.setText(category.getName());
        ((ListViewHolder) holder).chk_category.setChecked(false);
        for (Category user_category : user_categories) {
            if (user_category.getId().equals(category.getId())) {
                ((ListViewHolder) holder).chk_category.setChecked(true);
                break;
            }
        }

        ((ListViewHolder) holder).chk_category.setClickable(false);
        ((ListViewHolder) holder).chk_category.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!isOnProgress){
                    isOnProgress = true;
                    boolean isChecked = ((ListViewHolder) holder).chk_category.isChecked();
                    sendRequestToGetPlaceList(GlobalAppAccess.URL_SELECT_CATEGORY, category, !isChecked, view);
                }
                return false;
            }
        });

    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {

        return new ListViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.row_grid_all_category_item, null));
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
        public TextView tv_category_name;
        public CheckBox chk_category;

        public ListViewHolder(View paramView) {
            super(paramView);
            this.tv_category_name = ((TextView) paramView.findViewById(R.id.tv_category_name));
            this.chk_category = ((CheckBox) paramView.findViewById(R.id.chk_category));
            //this.tv_genre = ((TextView) paramView.findViewById(R.id.tv_genre));
            //this.tv_rating = ((TextView) paramView.findViewById(R.id.tv_rating));
        }
    }


    public void sendRequestToGetPlaceList(String url, final Category category, final boolean status, final View buttonView) {
        url = url + "?user_id=" + MydApplication.getInstance().getPrefManger().getUserProfile().getId()
                + "&category_id=" + category.getId() + "&status=" + status;

        Log.d("DEBUG",url);

        showProgressDialog("loading..", true, false);

        final StringRequest req = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        isOnProgress = false;

                        dismissProgressDialog();

                        Log.d("DEBUG",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getInt("success") == 1) {

                                if (status) {
                                    user_categories.add(category);
                                } else {
                                    for (int i = 0; i < user_categories.size(); i++) {
                                        if (user_categories.get(i).getId().equals(category.getId())) {
                                            user_categories.remove(i);
                                            break;
                                        }
                                    }
                                }

                                if (status) ((CheckBox)buttonView).setChecked(true);
                                else ((CheckBox)buttonView).setChecked(false);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AlertDialogForAnything.showAlertDialogWhenComplte(mContext,
                                    "ERROR",
                                    "Something went wrong!!",
                                    false);

                            if (!status) ((CheckBox)buttonView).setChecked(true);
                            else ((CheckBox)buttonView).setChecked(false);

                        }


                    }
                }, new com.android.volley.Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (!status) ((CheckBox)buttonView).setChecked(true);
                else ((CheckBox)buttonView).setChecked(false);
                isOnProgress = false;
                dismissProgressDialog();
                //progressBar.setVisibility(View.GONE);
                AlertDialogForAnything.showAlertDialogWhenComplte(mContext,
                        "ERROR",
                        "Something went wrong!!",
                        false);



            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MydApplication.getInstance().addToRequestQueue(req);

    }


    private ProgressDialog progressDialog;

    public void showProgressDialog(String message, boolean isIntermidiate, boolean isCancelable) {
       /**/
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
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


/* Location:           E:\APK\dex2jar\classes-dex2jar.jar
 * Qualified Name:     vinitm.yts.Views.InfiniteScrollRecyclerView.Adapter.UserCategoryAdapter
 * JD-Core Version:    0.7.0.1
 */