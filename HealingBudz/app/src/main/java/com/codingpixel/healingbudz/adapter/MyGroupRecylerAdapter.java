package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.GroupsDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;

public class MyGroupRecylerAdapter extends RecyclerView.Adapter<MyGroupRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    Context context;
    ArrayList<GroupsDataModel> mData;
    private MyGroupRecylerAdapter.ItemClickListener mClickListener;

    public MyGroupRecylerAdapter(Context context, ArrayList<GroupsDataModel> group_data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = group_data;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public MyGroupRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.my_groups_recyler_veiew_item_layout, parent, false);
        MyGroupRecylerAdapter.ViewHolder viewHolder = new MyGroupRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
                        Drawable d = resource;
                        Bitmap bitmapOrg = ((GlideBitmapDrawable) d).getBitmap();
                        bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                        holder.group_img.setImageDrawable(drawable);
                        return false;
                    }
                }).into(400, 400);

        holder.Edit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onEditClickListner(view, position);
            }
        });

        if (mData.get(position).getIs_private() == 1) {
            holder.public_icon.setImageResource(R.drawable.ic_private_group);
        } else {
            holder.public_icon.setImageResource(R.drawable.ic_public_group);

        }
        holder.group_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.OnMemberClick(view, position);
            }
        });
        holder.GroupName.setText(mData.get(position).getName());
        holder.GroupBudz.setText(mData.get(position).getGet_members_count() + " BUDZ");

        if(!mData.get(position).isCurrentUserAdmin()){
            holder.Edit_Btn.setVisibility(View.GONE);
        }else {
            holder.Edit_Btn.setVisibility(View.VISIBLE);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView group_img;
        ImageView Edit_Btn, public_icon , group_members;
        TextView GroupName, GroupBudz;

        public ViewHolder(View itemView) {
            super(itemView);
            group_img = itemView.findViewById(R.id.group_img);
            Edit_Btn = itemView.findViewById(R.id.edite_group_btn);
            GroupName = itemView.findViewById(R.id.group_name);
            GroupBudz = itemView.findViewById(R.id.group_budz);
            public_icon = itemView.findViewById(R.id.public_icon);
            group_members = itemView.findViewById(R.id.group_members);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(MyGroupRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void OnMemberClick(View view, int position);
        void onEditClickListner(View view, int position);
    }
}