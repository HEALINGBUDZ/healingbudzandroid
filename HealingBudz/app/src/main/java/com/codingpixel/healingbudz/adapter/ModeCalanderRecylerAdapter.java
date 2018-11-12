package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.ModeCalanderModel;
import com.codingpixel.healingbudz.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ModeCalanderRecylerAdapter extends RecyclerView.Adapter<ModeCalanderRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    public  String loaded_month_year;
    List<ModeCalanderModel> list;

    public ModeCalanderRecylerAdapter(Context context, List<ModeCalanderModel> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }


    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.mode_calander_recyler_view_item_layout, parent, false);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        Log.d("width", parent.getMeasuredWidth() + "");
        lp.height = parent.getMeasuredWidth() / 7;
        view.setLayoutParams(lp);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        if (position == 3) {
        holder.Edit_Count_layout.setVisibility(View.INVISIBLE);
        // holder.Edit_Count_Text.setText("3");

        Calendar calendar = Calendar.getInstance();
        String cDay = calendar.get(Calendar.DAY_OF_MONTH) + "";
        int current_month = calendar.get(calendar.MONTH) + 1;
        int current_Year = calendar.get(Calendar.YEAR);
        Log.d("MYDATE:", cDay + " -" + current_month + "-" + current_Year + " ");


        if (list.get(position).isMonth_Dark()) {
            //GIVING COLOR TO PREVIOUS MONTH
            holder.Edit_Count_layout.setVisibility(View.INVISIBLE);
            holder.Date_Text_View.setTextColor(Color.parseColor("#e4e4e5"));
            holder.Date_Text_View.setBackgroundResource(R.drawable.mode_calaender_recyler_view_item_simple_bg);
        } else {
                //GIVING COLOR TO CURRENT MONTH OF CALENDER
                holder.Date_Text_View.setBackgroundColor(Color.parseColor("#e4e4e5"));
                holder.Date_Text_View.setTextColor(Color.parseColor("#4c4c4c"));
        }


        if (loaded_month_year.equals(currentMonthDateHighlighted())) {
            if (list.get(position).isToDayDate()) {
                //GIVING COLOR TO CURRENT DATE OF CALENDER
                holder.Date_Text_View.setBackgroundColor(Color.parseColor("#ff9001"));
                holder.Date_Text_View.setTextColor(Color.parseColor("#fffffe"));

                if(list.get(position).getDate_entry() > 0 ){
                    holder.Edit_Count_layout.setVisibility(View.VISIBLE);
                    holder.Edit_Count_Text.setText(list.get(position).getDate_entry()+"");
                }else {
                    holder.Edit_Count_layout.setVisibility(View.GONE);
                }


            }
        }
        holder.Date_Text_View.setText(list.get(position).getDate());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Date_Text_View, Edit_Count_Text ;
        LinearLayout Edit_Count_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            Date_Text_View = itemView.findViewById(R.id.text_view);
            Edit_Count_layout = itemView.findViewById(R.id.edit_count_layout);
            Edit_Count_Text = itemView.findViewById(R.id.edit_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public String currentMonthDateHighlighted() {
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("MMM yyyy");
        String today = formatter.format(date).toUpperCase();
        return today;
    }
}