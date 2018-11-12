package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.DataModel.GroupsDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity.groupsDataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_groups_with_pagination;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;


public class GroupBudzListRecylerViewAdapter extends RecyclerView.Adapter<GroupBudzListRecylerViewAdapter.ViewHolder> implements APIResponseListner {
    private LayoutInflater mInflater;
    private boolean isAdmin;
    private Context context;
    private ArrayList<GroupsDataModel.GroupMembers> mData = new ArrayList<>();

    public GroupBudzListRecylerViewAdapter(Context context, ArrayList<GroupsDataModel.GroupMembers> data, boolean isAdmin) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.isAdmin = isAdmin;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public GroupBudzListRecylerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.group_budz_recylerview_list_item, parent, false);
        GroupBudzListRecylerViewAdapter.ViewHolder viewHolder = new GroupBudzListRecylerViewAdapter.ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final GroupBudzListRecylerViewAdapter.ViewHolder holder, final int position) {
        Glide.with(context)
                .load(images_baseurl + mData.get(position).getImage_path())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_user_profile)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.Profile_Img.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(holder.Profile_Img);

        if (isAdmin) {
            if (mData.get(position).getUser_id() == Splash.user.getUser_id()) {
                holder.Name.setText(mData.get(position).getName() + "  ( Admin )");
                holder.Remove_Bud.setVisibility(View.GONE);
            } else {
                holder.Name.setText(mData.get(position).getName());
                holder.Remove_Bud.setVisibility(View.VISIBLE);
            }
        } else {
            if(mData.get(position).isAdmin()){
                holder.Name.setText(mData.get(position).getName() + "  ( Admin )");
            }else {
                holder.Name.setText(mData.get(position).getName());
            }
            holder.Remove_Bud.setVisibility(View.GONE);
        }
        holder.Remove_Bud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove from group
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("member_id", mData.get(position).getUser_id());
                    jsonObject.put("group_id", mData.get(position).getGroup_id());
                    mData.remove(position);
                    notifyItemRemoved(position);
                    groupsDataModel.setGroupMembers(mData);
                    new VollyAPICall(context, false, URL.remove_member, jsonObject, user.getSession_key(), Request.Method.POST, GroupBudzListRecylerViewAdapter.this, get_groups_with_pagination);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        ImageView Remove_Bud;
        ImageView Profile_Img;

        public ViewHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.bud_name);
            Profile_Img = itemView.findViewById(R.id.profile_img);
            Remove_Bud = itemView.findViewById(R.id.remove_bud);
        }

    }
}