package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;

import java.util.List;

public class NotificationSettingKeywordRecylerAdapter extends RecyclerView.Adapter<NotificationSettingKeywordRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private NotificationSettingKeywordRecylerAdapter.ItemClickListenerKey mClickListener;
    List<String> mData;

    public NotificationSettingKeywordRecylerAdapter(Context context, List<String> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    // inflates the cell layout from xml when needed
    @Override
    public NotificationSettingKeywordRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.notification_setting_keyword_recyler_view_item, parent, false);
        NotificationSettingKeywordRecylerAdapter.ViewHolder viewHolder = new NotificationSettingKeywordRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.text.setText(mData.get(position));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text;
        ImageView cross;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_title);
            cross = itemView.findViewById(R.id.cross);
            cross.setOnClickListener(this);
//            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClickKey(view, getAdapterPosition());
        }
    }

    public void setClickListener(NotificationSettingKeywordRecylerAdapter.ItemClickListenerKey itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setmClickListener(NotificationSettingKeywordRecylerAdapter.ItemClickListenerKey mClickListener) {
        this.mClickListener = mClickListener;

    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListenerKey {
        void onItemClickKey(View view, int position);
    }
}