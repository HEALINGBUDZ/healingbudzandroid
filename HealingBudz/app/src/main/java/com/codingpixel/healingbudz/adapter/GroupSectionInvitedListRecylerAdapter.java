package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.GroupsInviteNewBudDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;


public class GroupSectionInvitedListRecylerAdapter extends RecyclerView.Adapter<GroupSectionInvitedListRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<GroupsInviteNewBudDataModel> mData = new ArrayList<>();
    public GroupSectionInvitedListRecylerAdapter(Context context , ArrayList<GroupsInviteNewBudDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    // inflates the cell layout from xml when needed
    @Override
    public GroupSectionInvitedListRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.invited_group_recylerview_list_item, parent, false);
        GroupSectionInvitedListRecylerAdapter.ViewHolder viewHolder = new GroupSectionInvitedListRecylerAdapter.ViewHolder(view);
        return viewHolder;

    }
    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(GroupSectionInvitedListRecylerAdapter.ViewHolder holder, final int position) {
        holder.Name.setText(mData.get(position).getName());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView Name;
        public ViewHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.bud_name);
        }

    }
}