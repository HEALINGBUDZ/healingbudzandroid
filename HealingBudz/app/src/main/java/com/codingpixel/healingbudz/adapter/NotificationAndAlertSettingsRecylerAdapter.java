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

import com.codingpixel.healingbudz.DataModel.NotificationAndAlertSettingDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import java.util.ArrayList;


public class NotificationAndAlertSettingsRecylerAdapter extends RecyclerView.Adapter<NotificationAndAlertSettingsRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    Context context;
    ArrayList<NotificationAndAlertSettingDataModel> mData = new ArrayList<>();
    private NotificationAndAlertSettingsRecylerAdapter.ItemClickListener mClickListener;

    public NotificationAndAlertSettingsRecylerAdapter(Context context, ArrayList<NotificationAndAlertSettingDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    @Override
    public NotificationAndAlertSettingsRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.notification_alert_setting_recyler_view_item, parent, false);
        NotificationAndAlertSettingsRecylerAdapter.ViewHolder viewHolder = new NotificationAndAlertSettingsRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final NotificationAndAlertSettingsRecylerAdapter.ViewHolder holder, final int position) {
        holder.Switch_Layout.setClickable(true);
        holder.Switch_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData.get(position).isCheck()) {
                    SharedPrefrences.setBool("notification_setting_" + position, false, context);
                    holder.btn_on.setText("");
                    holder.btn_off.setText("OFF");
                    holder.btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                    holder.btn_on.setBackground(null);
                    mData.get(position).setCheck(false);
                } else {
                    SharedPrefrences.setBool("notification_setting_" + position, true, context);
                    holder.btn_on.setText("ON");
                    holder.btn_off.setText("");
                    holder.btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
                    holder.btn_off.setBackground(null);
                    mData.get(position).setCheck(true);
                }
            }
        });

        holder.Second_Switch_Layout.setClickable(true);
        holder.Second_Switch_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData.get(position).isCheck()) {
                    SharedPrefrences.setBool("notification_setting_" + position, false, context);
                    holder.second_btn_on.setText("");
                    holder.second_btn_off.setText("OFF");
                    holder.second_btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                    holder.second_btn_on.setBackground(null);
                    mData.get(position).setCheck(false);
                } else {
                    SharedPrefrences.setBool("notification_setting_" + position, true, context);
                    holder.second_btn_on.setText("ON");
                    holder.second_btn_off.setText("");
                    holder.second_btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
                    holder.second_btn_off.setBackground(null);
                    mData.get(position).setCheck(true);
                }
            }
        });

        if (!mData.get(position).isCheck()) {
            holder.second_btn_on.setText("");
            holder.second_btn_off.setText("OFF");
            holder.second_btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
            holder.second_btn_on.setBackground(null);
            mData.get(position).setCheck(false);

            holder.btn_on.setText("");
            holder.btn_off.setText("OFF");
            holder.btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
            holder.btn_on.setBackground(null);
            mData.get(position).setCheck(false);
        } else {
            holder.second_btn_on.setText("ON");
            holder.second_btn_off.setText("");
            holder.second_btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
            holder.second_btn_off.setBackground(null);
            mData.get(position).setCheck(true);


            holder.btn_on.setText("ON");
            holder.btn_off.setText("");
            holder.btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
            holder.btn_off.setBackground(null);
            mData.get(position).setCheck(true);

        }


        if (position == 0 || position == 1) {
            holder.Item_One.setVisibility(View.VISIBLE);
            holder.Item_Two.setVisibility(View.GONE);
            holder.Item_Three.setVisibility(View.GONE);
            holder.Item_One_Heading.setText(mData.get(position).getHeading());
            holder.Item_One_title.setText(mData.get(position).getTitle());
        } else if (position == 3 || position == 3 || position == 7 || position == 10) {
            holder.Item_One.setVisibility(View.GONE);
            holder.Item_Two.setVisibility(View.VISIBLE);
            holder.Item_Three.setVisibility(View.GONE);
            holder.Item_Two_Heading.setText(mData.get(position).getHeading());
        } else if (position == 3 || position == 4 || position == 4 || position == 5 ||
                position == 8 || position == 12 || position == 9 ||
                position == 11 || position == 12 ) {
            holder.Item_One.setVisibility(View.GONE);
            holder.Item_Two.setVisibility(View.GONE);
            holder.Item_Three.setVisibility(View.VISIBLE);
            holder.Item_Three_Title.setText(mData.get(position).getTitle());
        } else if (position == 2 || position == 6) {
            holder.Item_One.setVisibility(View.VISIBLE);
            holder.Item_Two.setVisibility(View.GONE);
            holder.Item_Three.setVisibility(View.GONE);
            holder.Item_One_Heading.setVisibility(View.GONE);
            holder.Item_One_title.setText(mData.get(position).getTitle());
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Setting_Title;
        LinearLayout Item_One, Item_Two, Item_Three;
        Button btn_off, btn_on;
        Button second_btn_off, second_btn_on;
        LinearLayout Switch_Layout;
        LinearLayout Second_Switch_Layout;
        TextView Item_One_Heading, Item_One_title, Item_Two_Heading, Item_Three_Title;

        public ViewHolder(View itemView) {
            super(itemView);
            Setting_Title = itemView.findViewById(R.id.setting_title);
            Item_One = itemView.findViewById(R.id.item_one);
            Item_Two = itemView.findViewById(R.id.item_two);
            Item_Three = itemView.findViewById(R.id.item_three);


            Item_One_Heading = itemView.findViewById(R.id.item_one_heading);
            Item_One_title = itemView.findViewById(R.id.item_one_title);

            Item_Two_Heading = itemView.findViewById(R.id.item_two_heading);
            Item_Three_Title = itemView.findViewById(R.id.item_three_title);

            btn_off = itemView.findViewById(R.id.private_btn);
            btn_on = itemView.findViewById(R.id.public_btn);

            second_btn_off = itemView.findViewById(R.id.item_three_off_btn);
            second_btn_on = itemView.findViewById(R.id.item_three_on_btn);

            Switch_Layout = itemView.findViewById(R.id.switch_layout);
            Second_Switch_Layout = itemView.findViewById(R.id.second_switch_layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(NotificationAndAlertSettingsRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}