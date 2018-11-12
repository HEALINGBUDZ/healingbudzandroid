package com.codingpixel.healingbudz.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.JournalFragmentDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.mancj.slideup.SlideUp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.favourtire_journal;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareHBContent;

public class JournalHomeFragmentRecylerAdapter extends RecyclerView.Adapter<JournalHomeFragmentRecylerAdapter.ViewHolder> implements APIResponseListner, JournalItemHomeFragmentRecylerAdapter.ItemClickListener {
    private LayoutInflater mInflater;
    Context context;
    private SlideUp slide = null;
    ArrayList<JournalFragmentDataModel> mData = new ArrayList<>();
    private JournalHomeFragmentRecylerAdapter.ItemClickListener mClickListener;

    public JournalHomeFragmentRecylerAdapter(Context context, ArrayList<JournalFragmentDataModel> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public JournalHomeFragmentRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.journal_recyler_view_item, parent, false);
        JournalHomeFragmentRecylerAdapter.ViewHolder viewHolder = new JournalHomeFragmentRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (mData.get(position).isSlideOpen()) {
            mData.get(position).setSlideOpen(true);
            holder.Slide.setVisibility(View.VISIBLE);
            holder.Indicator.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            holder.Journal_list_item_recyler_view.setLayoutManager(layoutManager);
            JournalItemHomeFragmentRecylerAdapter recyler_adapter = new JournalItemHomeFragmentRecylerAdapter(context, mData.get(position).getJournalEvents());
//            recyler_adapter.setClickListener(JournalHomeFragmentRecylerAdapter.this);
            recyler_adapter.setClickListener(new JournalItemHomeFragmentRecylerAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (mClickListener != null)
                        mClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            });
            holder.Journal_list_item_recyler_view.setAdapter(recyler_adapter);
        }

        if (mData.get(position).isFavorite()) {
            holder.Favorite.setImageResource(R.drawable.ic_favorite_green);
        } else {
            holder.Favorite.setImageResource(R.drawable.ic_favorite_border_green);
        }

        holder.Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject object = new JSONObject();
                    object.put("journal_id", mData.get(position).getId());
                    if (mData.get(position).isFavorite()) {
                        holder.Favorite.setImageResource(R.drawable.ic_favorite_border_green);
                        mData.get(position).setFavorite(false);
                        object.put("is_favourtire", 0);
                    } else {
                        object.put("is_favourtire", 1);
                        holder.Favorite.setImageResource(R.drawable.ic_favorite_green);
                        mData.get(position).setFavorite(true);
                    }
                    new VollyAPICall(context, false, URL.favourtire_journal, object, user.getSession_key(), Request.Method.POST, JournalHomeFragmentRecylerAdapter.this, favourtire_journal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.Item_indicator_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData.get(position).isSlideOpen()) {
                    mData.get(position).setSlideOpen(false);
                    holder.Slide.setVisibility(View.GONE);
                    holder.Indicator.setImageResource(R.drawable.ic_navigate_next);
                    RotateAnimation rotate =
                            new RotateAnimation(90, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(300);
                    rotate.setFillAfter(true);
                    holder.Indicator.setAnimation(rotate);

                } else {
                    mData.get(position).setSlideOpen(true);
                    holder.Slide.setVisibility(View.VISIBLE);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                    holder.Journal_list_item_recyler_view.setLayoutManager(layoutManager);
                    JournalItemHomeFragmentRecylerAdapter recyler_adapter = new JournalItemHomeFragmentRecylerAdapter(context, mData.get(position).getJournalEvents());
                    holder.Journal_list_item_recyler_view.setAdapter(recyler_adapter);
                    RotateAnimation rotate = new RotateAnimation(360, 90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(300);
                    rotate.setFillAfter(true);
                    holder.Indicator.setAnimation(rotate);
                    holder.Indicator.setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
                }

            }
        });


//        holder.Title.setText(mData.get(position).getTitle());
        MakeKeywordClickableText(holder.Title.getContext(), mData.get(position).getTitle(), holder.Title);
        holder.Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.Title.isClickable() && holder.Title.isEnabled()) {
                    if (mClickListener != null)
                        mClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });
        holder.UserName.setText(mData.get(position).getUser_first_name());
        Glide.with(context)
                .load(images_baseurl + mData.get(position).getUser_image_path())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_user_profile)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.User_Image.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(holder.User_Image);

        holder.Share_journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                try {
                    object.put("msg", "Check out Healing Budz for your smartphone: " + URL.sharedBaseUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareHBContent((Activity) context, object);
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mClickListener != null) mClickListener.onItemClick(view, position);
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecyclerView Journal_list_item_recyler_view;
        ImageView Indicator, Favorite, User_Image;
        LinearLayout Slide, Share_journal;
        LinearLayout Name_Layout, Item_indicator_btn;
        TextView Title, UserName;

        public ViewHolder(View itemView) {
            super(itemView);
            Journal_list_item_recyler_view = itemView.findViewById(R.id.journal_list_item_recyler_view);
            Indicator = itemView.findViewById(R.id.item_indicator);
            Item_indicator_btn = itemView.findViewById(R.id.item_indicator_btn);
            Favorite = itemView.findViewById(R.id.favorite);
            Slide = itemView.findViewById(R.id.slide);
            Name_Layout = itemView.findViewById(R.id.name_layout);
            User_Image = itemView.findViewById(R.id.user_img);
            UserName = itemView.findViewById(R.id.user_name);
            Title = itemView.findViewById(R.id.journal_title);
            User_Image = itemView.findViewById(R.id.user_img);
            Share_journal = itemView.findViewById(R.id.share_journal);
            Name_Layout.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(JournalHomeFragmentRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}