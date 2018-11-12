package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.DataModel.ShootOutDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;


public class ShoutOutRecylerAdapter extends RecyclerView.Adapter<ShoutOutRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private ShoutOutRecylerAdapter.ItemClickListener mClickListener;
    ArrayList<ShootOutDataModel> mData = new ArrayList<>();

    public ShoutOutRecylerAdapter(Context context, ArrayList<ShootOutDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ShoutOutRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.shout_out_recyler_item_layout, parent, false);
        ShoutOutRecylerAdapter.ViewHolder viewHolder = new ShoutOutRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ShoutOutRecylerAdapter.ViewHolder holder, final int position) {
        holder.Time.setText(DateConverter.getPrettyTime(mData.get(position).getCreated_at()));
        if (DateConverter.convertDateForShooutOut(mData.get(position).getValidity_date()).contains("Expired")) {
            holder.Validity.setAllCaps(true);
            holder.Validity.setText("Expired");
            holder.Validity.setTextColor(Color.parseColor("#c24a68"));

        } else if (DateConverter.convertDateForShooutOut(mData.get(position).getValidity_date()).contains("Expire Soon!")) {
            String text = "<b ><font color=#c24a68>Expire Soon! </font> </b>" + DateConverter.convertDateShootOut(mData.get(position).getValidity_date());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.Validity.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.Validity.setText(Html.fromHtml(text));
            }
            holder.Validity.setAllCaps(true);
            holder.Validity.setTextColor(Color.parseColor("#c24a68"));
        } else {
            holder.Validity.setAllCaps(false);
            holder.Validity.setText("Valid until : " + DateConverter.convertDateShootOut(mData.get(position).getValidity_date()));
            holder.Validity.setTextColor(Color.parseColor("#c4c6c7"));
        }


//        holder.Title.setText(mData.get(position).getTitle());
        MakeKeywordClickableText(holder.Title.getContext(), mData.get(position).getMessage(), holder.Title);
        holder.Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.Title.isClickable() && holder.Title.isEnabled()) {
                    if (mClickListener != null)
                        mClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });
        if (mData.get(position).getImage().length() > 8) {
            holder.image_icon.setVisibility(View.VISIBLE);
//            holder.Title.setTypeface(null, Typeface.NORMAL);
        } else {
            holder.image_icon.setVisibility(View.GONE);
//            holder.Title.setTypeface(null, Typeface.NORMAL);
        }
        holder.Title.setSelected(true);
        if (mData.get(position).getPublic_location().length() > 3) {
            holder.location_icon.setVisibility(View.VISIBLE);
        } else {
            holder.location_icon.setVisibility(View.GONE);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        ImageView location_icon, image_icon;
        TextView Validity;
        LinearLayout active_layout;
        TextView Title, Time;

        public ViewHolder(View itemView) {
            super(itemView);

            location_icon = itemView.findViewById(R.id.location);
            image_icon = itemView.findViewById(R.id.image_icon);
            active_layout = itemView.findViewById(R.id.active);
            Title = itemView.findViewById(R.id.title_main);
            Time = itemView.findViewById(R.id.time);
            Validity = itemView.findViewById(R.id.validity);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    public void NotifyDataChange(ArrayList<HomeQAfragmentDataModel> data) {
        notifyDataSetChanged();
    }

    // allows clicks events to be caught
    public void setClickListener(ShoutOutRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    public void clear() {
        notifyDataSetChanged();
    }

    public void addAll() {
        notifyDataSetChanged();
    }
}