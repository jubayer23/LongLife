package com.creative.longlife.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.creative.longlife.R;
import com.creative.longlife.model.Category;

import java.util.ArrayList;
import java.util.List;

public class UserCategoryAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    public static final int ADAPTER_FOR_USER_SELECTED_CATEGORY = 0;
    public static final int ADAPTER_FOR_ALL_CATEGORY = 1;
    private int listStyle = 0;
    private Context mContext;
    private List<Category> categories;
    private List<Category> originalCategories;

    public UserCategoryAdapter(List<Category> paramList, Context paramContext) {
        this.categories = paramList;
        this.originalCategories = paramList;
        this.mContext = paramContext;
    }

    public int getItemCount() {
        return this.categories.size();
    }

    @Override
    public int getItemViewType(int paramInt) {
        return this.listStyle;
    }


    public void onBindViewHolder(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
        Category category = this.categories.get(paramInt);


        if (paramViewHolder.getItemViewType() == 0) {
            ((GridViewHolder) paramViewHolder).tv_category_name.setText(category.getName());
            //((GridViewHolder) paramViewHolder).img_cover.setImageURI(category.getMediumCoverImage());
            return;
        }
        //((ListViewHolder) paramViewHolder).tv_movie_name.setText(category.getTitleEnglish());
        //((ListViewHolder) paramViewHolder).img_cover.setImageURI(category.getMediumCoverImage());
        //if(category.getGenres() != null && !category.getGenres().isEmpty()){
        //    ((ListViewHolder) paramViewHolder).tv_genre.setText(category.getGenres().get(0));
        // }else{
        //     ((ListViewHolder) paramViewHolder).tv_genre.setText("No genre");
        //  }

        // ((ListViewHolder) paramViewHolder).tv_rating.setText("Rating : " +String.valueOf(category.getRating()));
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        if (paramInt == ADAPTER_FOR_USER_SELECTED_CATEGORY) {
            return new GridViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.row_grid_user_selected_category_item, null));
        }
        return new ListViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.row_grid_all_category_item, null));
    }

    public void removeAllAndNotify() {
        int i = this.categories.size();
        this.categories.clear();
        notifyItemRangeRemoved(0, i);
    }

    public void setListStyle(int paramInt) {
        this.listStyle = paramInt;
    }


    public class GridViewHolder
            extends RecyclerView.ViewHolder {
        //public SimpleDraweeView img_cover;
        public TextView tv_category_name;

        public GridViewHolder(View paramView) {
            super(paramView);
            this.tv_category_name = ((TextView) paramView.findViewById(R.id.tv_category_name));
            //this.img_cover = ((SimpleDraweeView) paramView.findViewById(R.id.img_cover));
        }
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


    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                categories = (List<Category>) results.values;
                UserCategoryAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Category> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = originalCategories;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<Category> getFilteredResults(String constraint) {
        List<Category> results = new ArrayList<>();

        for (Category item : originalCategories) {

            if (item.getName().toLowerCase().contains(constraint)) {
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