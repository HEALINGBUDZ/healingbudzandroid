package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Url.StringUtils;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.dialog.UserFollowFollwingAlertDialog;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.static_function.IntentFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.side_menu.profile.UserProfileActivity.isGoneToOther;
import static com.codingpixel.healingbudz.activity.home.side_menu.profile.UserProfileActivity.profiledataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_followers;

public class UserFollowFollwingDialogRecylerAdapter extends RecyclerView.Adapter<UserFollowFollwingDialogRecylerAdapter.ViewHolder> implements APIResponseListner {
    private LayoutInflater mInflater;
    Context context;
    ArrayList<UserFollowFollwingAlertDialog.UserFollowDataModel> mData = new ArrayList<>();
    private UserFollowFollwingDialogRecylerAdapter.ItemClickListener mClickListener;

    public UserFollowFollwingDialogRecylerAdapter(Context context, ArrayList<UserFollowFollwingAlertDialog.UserFollowDataModel> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public UserFollowFollwingDialogRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.followe_journal_dialog_recyler_view_item, parent, false);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 6;
        view.setLayoutParams(lp);
        UserFollowFollwingDialogRecylerAdapter.ViewHolder viewHolder = new UserFollowFollwingDialogRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.Name.setText(StringUtils.capitalizeFirstLetter(mData.get(position).getName()));
        holder.Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentFunction.GoToProfile(v.getContext(), mData.get(position).getId());
                isGoneToOther = true;
            }
        });
        if (mData.get(position).isIs_follow()) {
            holder.Follow_text.setText("unfollow");
            holder.Follow_text.setTextColor(Color.parseColor("#66993e"));
            holder.F_btn.setBackgroundResource(R.drawable.journal_unfollow_bg);
            holder.Follow_icon.setImageResource(R.drawable.ic_unfollow_journal);
        } else {
            holder.Follow_text.setText("follow");
            holder.Follow_text.setTextColor(Color.parseColor("#464645"));
            holder.F_btn.setBackgroundResource(R.drawable.add_journal_entry_btn);
            holder.Follow_icon.setImageResource(R.drawable.ic_profile_add);
        }
        holder.Follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData.get(position).isFollowing_Api()) {

                    profiledataModel.setFollowings_count(profiledataModel.getFollowings_count() - 1);
                    JSONObject object = new JSONObject();
                    try {
                        object.put("followed_id", mData.get(position).getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mData.remove(position);
                    notifyDataSetChanged();
                    new VollyAPICall(context, false, URL.un_follow, object, user.getSession_key(), Request.Method.POST, UserFollowFollwingDialogRecylerAdapter.this, get_followers);
                } else {
                    JSONObject object = new JSONObject();
                    if (mData.get(position).isIs_follow()) {
                        mData.get(position).setIs_follow(false);
                        try {
                            object.put("followed_id", mData.get(position).getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        profiledataModel.setFollowings_count(profiledataModel.getFollowings_count() - 1);
                        new VollyAPICall(context, false, URL.un_follow, object, user.getSession_key(), Request.Method.POST, UserFollowFollwingDialogRecylerAdapter.this, get_followers);
                    } else {
                        mData.get(position).setIs_follow(true);
                        try {
                            object.put("followed_id", mData.get(position).getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        profiledataModel.setFollowings_count(profiledataModel.getFollowings_count() + 1);
                        new VollyAPICall(context, false, URL.follow_user, object, user.getSession_key(), Request.Method.POST, UserFollowFollwingDialogRecylerAdapter.this, get_followers);
                    }
                    notifyItemChanged(position);
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
        Log.d("respisne", response);
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("respisne", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Name, Follow_text;
        LinearLayout Follow_btn;
        LinearLayout F_btn;
        ImageView Follow_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            Name = itemView.findViewById(R.id.name);
            Follow_text = itemView.findViewById(R.id.follow_text);
            Follow_btn = itemView.findViewById(R.id.follow_btn);
            F_btn = itemView.findViewById(R.id.f_btn);
            Follow_icon = itemView.findViewById(R.id.followe_icon);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(UserFollowFollwingDialogRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}