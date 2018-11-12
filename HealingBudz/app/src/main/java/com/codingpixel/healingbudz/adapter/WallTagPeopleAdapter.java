package com.codingpixel.healingbudz.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.customeUI.CircularImageView;
import com.codingpixel.healingbudz.customeUI.HealingBudTextViewBold;
import com.codingpixel.healingbudz.interfaces.RecyclerViewItemClickListener;
import com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel.FollowingUser;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by M_Muzammil Sharif on 05-Apr-18.
 */
public class WallTagPeopleAdapter extends RecyclerView.Adapter<WallTagPeopleAdapter.WallTagTagPeopelVH> {
    private List<FollowingUser> followingUsers;
    private RecyclerViewItemClickListener clickListener;
    private boolean isCancelAble = false;

    public List<FollowingUser> getFollowingUsers() {
        return followingUsers;
    }

    public void setFollowingUsers(List<FollowingUser> followingUsers) {
        this.followingUsers = followingUsers;
        notifyDataSetChanged();
    }

    public WallTagPeopleAdapter(List<FollowingUser> followingUsers, RecyclerViewItemClickListener clickListener, boolean isDeleteable) {
        this.followingUsers = followingUsers;
        this.clickListener = clickListener;
        this.isCancelAble = isDeleteable;
    }

    public boolean isCancelAble() {
        return isCancelAble;
    }

    public void setCancelAble(boolean cancelAble) {
        isCancelAble = cancelAble;
    }

    @Override
    public WallTagTagPeopelVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WallTagTagPeopelVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_tag_people_chip_vh, parent, false));
    }

    public void addFollowingUser(FollowingUser user) {
        if (followingUsers == null) {
            followingUsers = new ArrayList<>();
        }
        followingUsers.add(user);
        notifyDataSetChanged();
    }

    public void addFollowingUsers(List<FollowingUser> users) {
        if (followingUsers == null) {
            followingUsers = users;
            notifyDataSetChanged();
            return;
        }
        followingUsers.addAll(users);
        notifyDataSetChanged();
    }/*

    public void remove(FollowingUser user) {
        if (followingUsers == null || followingUsers.isEmpty()) {
            return;
        }
        followingUsers.remove(user);
        notifyDataSetChanged();
    }*/

    @Override
    public void onBindViewHolder(WallTagTagPeopelVH holder, int position) {
        if (followingUsers == null || followingUsers.isEmpty()) {
            return;
        }
        final FollowingUser user = followingUsers.get(position);
        if (user == null) {
            return;
        }
        holder.textView.setText(user.getFirstName());
        holder.textView.setTextColor(Color.parseColor(Utility.getBudColor(followingUsers.get(position).getPoints())));
        Glide.with(holder.itemView.getContext()).load(user.getImagePath()).
                diskCacheStrategy(DiskCacheStrategy.SOURCE).
                placeholder(R.drawable.ic_user_person).into(holder.imageView);
        if (user.getSpecial_icon().length() > 5) {

            holder.profile_img_topi.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(user.getSpecial_icon()).
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    placeholder(R.drawable.ic_user_person).into(holder.profile_img_topi);
        } else {
            holder.profile_img_topi.setVisibility(View.GONE);
        }
        if (isCancelAble) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    followingUsers.remove(user);
                    if (clickListener != null) {
                        clickListener.onItemClick(user, 0, -1);
                    }
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.delete.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onItemClick(user, 0, 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (followingUsers == null) {
            return 0;
        }
        return followingUsers.size();
    }

    class WallTagTagPeopelVH extends RecyclerView.ViewHolder {
        CircularImageView imageView;
        ImageView profile_img_topi;
        HealingBudTextViewBold textView;
        View delete;

        public WallTagTagPeopelVH(View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.wall_tag_people_chip_vh_delete);
            profile_img_topi = itemView.findViewById(R.id.profile_img_topi);
            imageView = itemView.findViewById(R.id.wall_tag_people_chip_vh_img);
            textView = itemView.findViewById(R.id.wall_tag_people_chip_vh_name);
        }
    }
}
