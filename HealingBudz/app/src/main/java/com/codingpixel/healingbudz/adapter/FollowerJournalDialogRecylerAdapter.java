package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.FollowerJournalDialogDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

public class FollowerJournalDialogRecylerAdapter extends RecyclerView.Adapter<FollowerJournalDialogRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<FollowerJournalDialogDataModel> mData = new ArrayList<>();
    private FollowerJournalDialogRecylerAdapter.ItemClickListener mClickListener;
    public FollowerJournalDialogRecylerAdapter(Context context, ArrayList<FollowerJournalDialogDataModel> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    // inflates the cell layout from xml when needed
    @Override
    public FollowerJournalDialogRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.followe_journal_dialog_recyler_view_item, parent, false);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 6;
        view.setLayoutParams(lp);
        FollowerJournalDialogRecylerAdapter.ViewHolder viewHolder = new FollowerJournalDialogRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.Name.setText(mData.get(position).getName());
        if(mData.get(position).isFollow()){
         holder.Follow_text.setText("unfollow");
            holder.Follow_text.setTextColor(Color.parseColor("#66993e"));
            holder.F_btn.setBackgroundResource(R.drawable.journal_unfollow_bg);
            holder.Follow_icon.setImageResource(R.drawable.ic_unfollow_journal);
        }else {
            holder.Follow_text.setText("follow");
            holder.Follow_text.setTextColor(Color.parseColor("#464645"));
            holder.F_btn.setBackgroundResource(R.drawable.add_journal_entry_btn);
            holder.Follow_icon.setImageResource(R.drawable.ic_profile_add);
        }


        holder.Follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(mData.get(position).isFollow()){
                   mData.get(position).setFollow(false);
               }else {
                   mData.get(position).setFollow(true);
               }
                notifyDataSetChanged();
            }
        });
    }
    // total number of cells
    @Override
    public int getItemCount() {

        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView Name, Follow_text;
        LinearLayout Follow_btn;
        LinearLayout F_btn;
        ImageView Follow_icon;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            Name = itemView.findViewById(R.id.name);
            Follow_text = itemView.findViewById(R.id.follow_text);
            Follow_btn =itemView.findViewById(R.id.follow_btn);
            F_btn =itemView.findViewById(R.id.f_btn);
            Follow_icon = itemView.findViewById(R.id.followe_icon);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    public void setClickListener(FollowerJournalDialogRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}