package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.HomeSearchListData;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;

public class HomeSearchListRecylerAdapter extends RecyclerView.Adapter<HomeSearchListRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    List<HomeSearchListData> mData = new ArrayList<>();
    Context mContext;
    private HomeSearchListRecylerAdapter.ItemClickListener mClickListener;

    public HomeSearchListRecylerAdapter(Context context, List<HomeSearchListData> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public HomeSearchListRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.home_search_recyler_item_layout, parent, false);
        HomeSearchListRecylerAdapter.ViewHolder viewHolder = new HomeSearchListRecylerAdapter.ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final HomeSearchListRecylerAdapter.ViewHolder holder, final int position) {
        HomeSearchListData data = mData.get(position);
        holder.icon.setImageResource(data.getIcon());
        if (data.getType().equalsIgnoreCase("u") || data.getType().equalsIgnoreCase("bm")) {
            holder.userImg.setVisibility(View.VISIBLE);
            holder.icon.setVisibility(View.GONE);
            if (data.getType().equalsIgnoreCase("u")) {
                Glide.with(mContext)
                        .load(data.getImagePath())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.ic_user_person)
                        .error(R.drawable.ic_user_person)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                holder.userImg.setImageDrawable(resource);
                                return false;
                            }
                        }).into(holder.userImg);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.ic_budz_adn)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.ic_budz_adn)
                        .error(R.drawable.ic_budz_adn)
                        .into(holder.userImg);
            }
        } else {
            holder.userImg.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);
        }
        if (data.isPremium()) {
            holder.featured_business_layout.setVisibility(View.VISIBLE);
        } else {
            holder.featured_business_layout.setVisibility(View.GONE);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            holder.Description.setText(Html.fromHtml(data.getDiscription() , Html.FROM_HTML_MODE_COMPACT));
//        }else {
//            holder.Description.setText(Html.fromHtml(data.getDiscription()));
//        }
        MakeKeywordClickableText(holder.Description.getContext(), data.getDiscription(), holder.Description);
        holder.Description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.Description.isClickable() && holder.Description.isEnabled()) {
                    if (mClickListener != null)
                        mClickListener.onGlobalSearchItemClick(view, holder.getAdapterPosition());
                }
            }
        });

//        holder.Title.setText(data.getTitle());
        MakeKeywordClickableText(holder.Title.getContext(), data.getTitle(), holder.Title);
        holder.Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.Title.isClickable() && holder.Title.isEnabled()) {
                    if (mClickListener != null)
                        mClickListener.onGlobalSearchItemClick(view, holder.getAdapterPosition());
                }
            }
        });
        holder.Title.setTextColor(Color.parseColor(data.getColor()));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return this.mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon, userImg;
        TextView text_icon;
        TextView Title, Description;
        LinearLayout featured_business_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon_img);
            featured_business_layout = itemView.findViewById(R.id.featured_business_layout);
            userImg = itemView.findViewById(R.id.userImg);
            text_icon = itemView.findViewById(R.id.text_icon);
            Description = itemView.findViewById(R.id.heading_title);
            Title = itemView.findViewById(R.id.title_main);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onGlobalSearchItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(HomeSearchListRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onGlobalSearchItemClick(View view, int position);
    }
}