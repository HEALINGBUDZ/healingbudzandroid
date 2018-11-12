package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.SlideMenuActivityListData;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;

public class ActivityLogRecylerAdapter extends RecyclerView.Adapter<ActivityLogRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<SlideMenuActivityListData> mData = new ArrayList<>();
    private ActivityLogRecylerAdapter.ItemClickListener mClickListener;

    public ActivityLogRecylerAdapter(Context context, ArrayList<SlideMenuActivityListData> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ActivityLogRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_log_recyler_item_layout, parent, false);
        ActivityLogRecylerAdapter.ViewHolder viewHolder = new ActivityLogRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ActivityLogRecylerAdapter.ViewHolder holder, final int position) {
        SlideMenuActivityListData data = mData.get(position);
        holder.icon.setImageResource(data.getIcon());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            holder.Description.setText(Html.fromHtml(data.getDiscription(), Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            holder.Description.setText(Html.fromHtml(data.getDiscription()));
//        }
//        holder.Title.setText(data.getTitle());
        if (data.getModel().equalsIgnoreCase("Strains")) {
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(20);
            holder.Description.setFilters(fArray);
        } else {
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(data.getDiscription().length());
            holder.Description.setFilters(fArray);
        }
        MakeKeywordClickableText(holder.Description.getContext(), data.getDiscription(), holder.Description);
        MakeKeywordClickableText(holder.Title.getContext(), data.getTitle(), holder.Title);
        holder.Description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.Description.isClickable() && holder.Description.isEnabled()) {
                    if (mClickListener != null)
                        mClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });
        holder.Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.Title.isClickable() && holder.Title.isEnabled()) {
                    if (mClickListener != null)
                        mClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });
        holder.Activity_Time.setText(data.getDate_str());
        holder.Title.setTextColor(Color.parseColor(data.getColor()));
        if (data.getTitle().equalsIgnoreCase("You have added a strain.") || data.getTitle().equalsIgnoreCase("You have updated a strain.")) {
            holder.Strain_keyword.setVisibility(View.VISIBLE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                holder.Strain_keyword.setText(Html.fromHtml("#" + data.getDiscription().replace(" ", ""), Html.FROM_HTML_MODE_COMPACT));
//            } else {
//                holder.Strain_keyword.setText(Html.fromHtml("#" + data.getDiscription().replace(" ", "")));
//            }
            MakeKeywordClickableText(holder.Strain_keyword.getContext(), "#" + data.getDiscription().replace(" ", ""), holder.Strain_keyword);
            holder.Strain_keyword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.Strain_keyword.isClickable() && holder.Strain_keyword.isEnabled()) {
                        if (mClickListener != null)
                            mClickListener.onItemClick(view, holder.getAdapterPosition());
                    }
                }
            });
        } else {
            holder.Strain_keyword.setVisibility(View.GONE);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        TextView text_icon;
        TextView Activity_Time;
        TextView Title, Description, Strain_keyword;


        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon_img);
            text_icon = itemView.findViewById(R.id.text_icon);
            Description = itemView.findViewById(R.id.heading_title);
            Title = itemView.findViewById(R.id.title_main);
            Activity_Time = itemView.findViewById(R.id.activity_time);
            Strain_keyword = itemView.findViewById(R.id.strain_keyword);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(ActivityLogRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}