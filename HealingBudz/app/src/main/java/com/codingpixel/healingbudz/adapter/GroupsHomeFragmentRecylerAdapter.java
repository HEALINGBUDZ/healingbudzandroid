package com.codingpixel.healingbudz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.GroupsDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.mancj.slideup.SlideUp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.join_group;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;

public class GroupsHomeFragmentRecylerAdapter extends RecyclerView.Adapter<GroupsHomeFragmentRecylerAdapter.ViewHolder> implements APIResponseListner {
    private LayoutInflater mInflater;
    Context context;
    private SlideUp slide = null;
    ArrayList<GroupsDataModel> mData = new ArrayList<>();
    private GroupsHomeFragmentRecylerAdapter.ItemClickListener mClickListener;

    public GroupsHomeFragmentRecylerAdapter(Context context, ArrayList<GroupsDataModel> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public GroupsHomeFragmentRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.groups_recyler_view_item, parent, false);
        Animation startAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        view.startAnimation(startAnimation);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Glide.with(context)
                .load(images_baseurl + mData.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_groups_icon)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d("ready", model);
                        Drawable d = resource;
                        Bitmap bitmapOrg = ((GlideBitmapDrawable) d).getBitmap();
                        bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                        holder.icon.setImageDrawable(drawable);
                        return false;
                    }
                }).into(400, 400);
        if (mData.get(position).getIs_following_count() == 1) {
            holder.Follow_btn.setVisibility(View.GONE);
        } else {
            holder.Follow_btn.setVisibility(View.VISIBLE);
        }
        holder.Follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //wait for follwo call
                mData.get(position).setIs_following_count(1);
                mData.get(position).setGet_members_count(mData.get(position).getGet_members_count() + 1);
                notifyItemChanged(position);
                JSONObject object = new JSONObject();
                try {
                    object.put("group_id", mData.get(position).getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new VollyAPICall(context, false, URL.join_group, object, user.getSession_key(), Request.Method.POST, GroupsHomeFragmentRecylerAdapter.this, join_group);
            }
        });
        holder.Name.setText(mData.get(position).getName());
        holder.No_of_budz.setText(mData.get(position).getGet_members_count() + " BUDZ");
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

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        RelativeLayout Follow_btn;
        TextView Name, No_of_budz;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon_img);
            Name = itemView.findViewById(R.id.name);
            No_of_budz = itemView.findViewById(R.id.no_of_budz);
            Follow_btn = itemView.findViewById(R.id.follow_btn);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(GroupsHomeFragmentRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}