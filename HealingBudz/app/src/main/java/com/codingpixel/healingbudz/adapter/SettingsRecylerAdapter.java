package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import java.util.ArrayList;


public class SettingsRecylerAdapter extends RecyclerView.Adapter<SettingsRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    boolean isPublic = false, isPrivate = true;
    ArrayList<String> mData = new ArrayList<>();
    Context context;
    private SettingsRecylerAdapter.ItemClickListener mClickListener;

    public SettingsRecylerAdapter(Context context, ArrayList<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;

    }

    // inflates the cell layout from xml when needed
    @Override
    public SettingsRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.setting_recyler_view_item, parent, false);
        SettingsRecylerAdapter.ViewHolder viewHolder = new SettingsRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final SettingsRecylerAdapter.ViewHolder holder, final int position) {
        holder.Setting_Title.setText(mData.get(position));
        if (position == 3) {
            holder.Second_setting_item.setVisibility(View.VISIBLE);
            holder.Setting_Title.setVisibility(View.GONE);

            if (SharedPrefrences.getBoolWithTrueDefault("first_launch_overview_screen", context)) {
                isPrivate = false;
                isPublic = true;
                holder.btn_on.setText("ON");
                holder.btn_off.setText("");
                holder.btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
                holder.btn_off.setBackground(null);
            } else {
                isPublic = false;
                isPrivate = true;
                holder.btn_on.setText("");
                holder.btn_off.setText("OFF");
                holder.btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                holder.btn_on.setBackground(null);
            }
        } else {
            holder.Second_setting_item.setVisibility(View.GONE);
            holder.Setting_Title.setVisibility(View.VISIBLE);
        }

        holder.btn_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPrivate) {
                    SharedPrefrences.setBool("first_launch_overview_screen", false, context);
                    isPublic = false;
                    isPrivate = true;
                    holder.btn_on.setText("");
                    holder.btn_off.setText("OFF");
                    holder.btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                    holder.btn_on.setBackground(null);
                }
            }
        });

        holder.btn_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPublic) {
                    SharedPrefrences.setBool("first_launch_overview_screen", true, context);
                    isPrivate = false;
                    isPublic = true;
                    holder.btn_on.setText("ON");
                    holder.btn_off.setText("");
                    holder.btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
                    holder.btn_off.setBackground(null);
                }
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return 4;
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Setting_Title;
        LinearLayout Second_setting_item;
        Button btn_off, btn_on;

        public ViewHolder(View itemView) {
            super(itemView);
            Setting_Title = itemView.findViewById(R.id.setting_title);
            Second_setting_item = itemView.findViewById(R.id.second_setting_item);

            btn_off = itemView.findViewById(R.id.private_btn);
            btn_on = itemView.findViewById(R.id.public_btn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(SettingsRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}