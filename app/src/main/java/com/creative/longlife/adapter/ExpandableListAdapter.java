package com.creative.longlife.adapter;

/**
 * Created by comsol on 17-Nov-17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.creative.longlife.R;
import com.creative.longlife.model.LocalGovt;
import com.creative.longlife.model.LocalState;
import com.creative.longlife.model.Region;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private Region region;

    public ExpandableListAdapter(Context context, Region region) {
        this._context = context;
        this.region = region;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        //return this._listDataChild.get(this._listDataHeader.get(groupPosition))
        //        .get(childPosititon);

        return this.region.getLocalStates().get(groupPosition).getLocalGovts().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public static int color_group_pos = -1;
    public static int color_child_pos = -1;

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final LocalGovt localGovt = (LocalGovt) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(localGovt.getGovtName());

        txtListChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_child_pos = childPosition;
                color_group_pos = groupPosition;
                notifyDataSetChanged();
            }
        });

        if(color_child_pos == childPosition && color_group_pos == groupPosition){
            txtListChild.setBackgroundColor(_context.getResources().getColor(R.color.green_dark));
        }else{
            txtListChild.setBackgroundColor(_context.getResources().getColor(R.color.white));
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
       // return this._listDataChild.get(this._listDataHeader.get(groupPosition))
        //        .size();

        return this.region.getLocalStates().get(groupPosition).getLocalGovts().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
       // return this._listDataHeader.get(groupPosition);
        return this.region.getLocalStates().get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        //return this._listDataHeader.size();
        return this.region.getLocalStates().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final LocalState localState = (LocalState) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(localState.getStateName());



        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}