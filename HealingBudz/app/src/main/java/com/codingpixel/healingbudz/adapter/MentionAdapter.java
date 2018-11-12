package com.codingpixel.healingbudz.adapter;
/*
 * Created by M_Muzammil Sharif on 22-Mar-18.
 */

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Dialogs;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.customeUI.CircularImageView;
import com.codingpixel.healingbudz.customeUI.HealingBudTextViewBold;
import com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel.FollowingUser;
import com.codingpixel.healingbudz.network.BudzFeedModel.TagModel.Tag;

import java.util.List;

public class MentionAdapter extends RecyclerView.Adapter<MentionAdapter.MentionVH> {
    private List<FollowingUser> users;
    private Dialogs.DialogItemClickListener clickListener;
    private boolean isMention = false;
    private List<Tag> tags;
    private boolean isFromNewScreen = true;

    public MentionAdapter(List<FollowingUser> users, Dialogs.DialogItemClickListener clickListener) {
        this.users = users;
        this.clickListener = clickListener;
        isFromNewScreen = true;
    }

    public MentionAdapter(List<FollowingUser> users, Dialogs.DialogItemClickListener clickListener, boolean isFromMain) {
        this.users = users;
        this.clickListener = clickListener;
        this.isFromNewScreen = isFromMain;
    }

    public List<FollowingUser> getUsers() {
        return users;
    }

    public void setMentions(List<FollowingUser> users) {
        this.users = users;
        tags = null;
        this.isMention = true;
        notifyDataSetChanged();
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
        users = null;
        this.isMention = false;
        notifyDataSetChanged();
    }

    @Override
    public MentionAdapter.MentionVH onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new MentionAdapter.MentionVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_mention_view, parent, false));
        if (isFromNewScreen) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_mention_view, parent, false);
            int value = 5;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
                Log.d("heaight", parent.getMeasuredHeight() / value + "");
                lp.height = parent.getMeasuredHeight() / value;
                int height = parent.getMeasuredHeight() / value;
                if (height > 50) {
                    view.setLayoutParams(lp);
                }
                MentionAdapter.MentionVH viewHolder = new MentionAdapter.MentionVH(view);
                return viewHolder;
            } else {
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
                Log.d("heaight", parent.getMeasuredHeight() / value + "");
                int height = parent.getMeasuredHeight() / value;
                if (height == 0) {
                    lp.height = 158;
                } else {
                    lp.height = height;
                }
                view.setLayoutParams(lp);
                // MentionAdapter.MentionVH()
                MentionAdapter.MentionVH viewHolder = new MentionAdapter.MentionVH(view);
                return viewHolder;
            }
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_mention_view, parent, false);
            MentionAdapter.MentionVH viewHolder = new MentionAdapter.MentionVH(view);
            return viewHolder;
        }
//        LinearLayout layout = new LinearLayout(parent.getContext());
//        View view = mInflater.inflate(R.layout.messages_recyler_veiew_item_layout, parent, false);
//
//        layout.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
//        layout.setOrientation(LinearLayout.HORIZONTAL);
//        layout.setGravity(Gravity.CENTER_VERTICAL);
//        int p = Utility.convertDpToPixel(5, parent.getContext());
//        layout.setPadding(p, p, p, p);
//        layout.setBackgroundColor(Color.parseColor("#252222"));
//
//        return new MentionAdapter.MentionVH(layout);
    }

    @Override
    public void onBindViewHolder(MentionAdapter.MentionVH holder, final int position) {
        int p = Utility.convertDpToPixel(10, holder.itemView.getContext());
        if (getItemViewType(position) == 0) {
            holder.textViewBold.setText(users.get(position).getFirstName() + " " + users.get(position).getLastName());
            holder.textViewBold.setTextColor(Color.parseColor(Utility.getBudColor(users.get(position).getPoints())));
//            holder.circularImageView.getLayoutParams().width = (int) (p * 3.5f);
//            holder.circularImageView.getLayoutParams().height = (int) (p * 3.5f);
            if (users.get(position).isSubUser()) {
                holder.profile_img_topi.setVisibility(View.GONE);
                holder.circularImageView.setImageResource(R.drawable.ic_budz_adn);
            } else {
                if (users.get(position).getSpecial_icon().length() > 6) {
                    holder.profile_img_topi.setVisibility(View.VISIBLE);
                    Glide.with(holder.itemView.getContext()).load(users.get(position).getSpecial_icon()).
                            diskCacheStrategy(DiskCacheStrategy.SOURCE).
                            placeholder(R.drawable.topi_ic).
                            into(holder.profile_img_topi);
                } else {
                    holder.profile_img_topi.setVisibility(View.GONE);

                }
                Glide.with(holder.itemView.getContext()).load(users.get(position).getImagePath()).
                        diskCacheStrategy(DiskCacheStrategy.SOURCE).
                        placeholder(Utility.getProfilePlaceHolder(users.get(position).getPoints())).
                        into(holder.circularImageView);

            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onDialogItemClickListener(null, users.get(position), 0);
                }
            });
            holder.user_image_hash.setVisibility(View.GONE);
            holder.circularImageView.setVisibility(View.VISIBLE);
        } else {
            holder.profile_img_topi.setVisibility(View.GONE);
//            holder.circularImageView.getLayoutParams().width = p * 2;
//            holder.circularImageView.getLayoutParams().height = p * 2;
            holder.textViewBold.setText(tags.get(position).getTitle());
            holder.textViewBold.setTextColor(Color.WHITE);
            holder.user_image_hash.setImageResource(R.drawable.ic_hashtag);
            holder.user_image_hash.setVisibility(View.VISIBLE);
            holder.circularImageView.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onDialogItemClickListener(null, tags.get(position), 1);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (isMention) {
            if (users == null) {
                return 0;
            }
            return users.size();
        } else {
            if (tags == null) {
                return 0;
            }
            return tags.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isMention ? 0 : 1;
    }

    class MentionVH extends RecyclerView.ViewHolder {
        private HealingBudTextViewBold textViewBold;//user_image
        private ImageView profile_img_topi;
        private CircularImageView circularImageView;//user_image
        private ImageView user_image_hash;//user_image

        private MentionVH(View itemView) {
            super(itemView);
            textViewBold = itemView.findViewById(R.id.user_name);
            circularImageView = itemView.findViewById(R.id.user_image);
            user_image_hash = itemView.findViewById(R.id.user_image_hash);
            profile_img_topi = itemView.findViewById(R.id.profile_img_topi);
        }
    }
}