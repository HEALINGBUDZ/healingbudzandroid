package com.codingpixel.healingbudz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.Budz;
import com.codingpixel.healingbudz.DataModel.QuestionAnswersDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.model.URL;

import java.io.Serializable;
import java.util.ArrayList;


public class StrainLocatBudTabRecylerAdapter extends RecyclerView.Adapter<StrainLocatBudTabRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    //    ArrayList<StrainBudzMapDataModal> mData = new ArrayList<>();
    ArrayList<Budz> mData = new ArrayList<>();
    Context context;
    private StrainLocatBudTabRecylerAdapter.ItemClickListener mClickListener;

    public StrainLocatBudTabRecylerAdapter(Context context, ArrayList<Budz> strainBudzMapDataModals) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = strainBudzMapDataModals;
    }

    // inflates the cell layout from xml when needed
    @Override
    public StrainLocatBudTabRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.strain_locate_bud_tab_recyler_item, parent, false);
        StrainLocatBudTabRecylerAdapter.ViewHolder viewHolder = new StrainLocatBudTabRecylerAdapter.ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final StrainLocatBudTabRecylerAdapter.ViewHolder holder, final int position) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
        BudzMapProductSubItemStrainRecylerAdapter recyler_adapter = null;
        recyler_adapter = new BudzMapProductSubItemStrainRecylerAdapter(context, mData.get(position).getPricing());
        holder.recyclerView.setAdapter(recyler_adapter);
//        holder.Header_title.setText(mData.get(position).getTitle());
        holder.Header_title.setVisibility(View.GONE);
        @SuppressLint("DefaultLocale") String Distance = String.format("%.02f", Float.parseFloat(String.valueOf(mData.get(position).getDistance())));
        holder.Distance.setText(Distance + " mi");
        holder.Main_Title.setText(mData.get(position).getName());
        holder.Strain_Type.setText(mData.get(position).getStrainType().getTitle());
//        if (mData.get(position).getProducts().size() > 0) {
        holder.Other_Text.setText(mData.get(position).getCbd() + "% CBD | " + mData.get(position).getThc() + "% THC");
        holder.images_count.setText(mData.get(position).getImages().size() + "");
        if (mData.get(position).getImages().size() > 0) {
            holder.images_count.setVisibility(View.VISIBLE);
        } else {
            holder.images_count.setVisibility(View.GONE);
        }
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<QuestionAnswersDataModel.Attachment> attachments = new ArrayList<>();
                for (int i = 0; i < mData.get(position).getImages().size(); i++) {
                    attachments.add(new QuestionAnswersDataModel.Attachment(mData.get(position).getImages().get(i).getImage()));
                }
                String path = mData.get(position).getImages().get(0).getImage();
                Intent intent = new Intent(context, MediPreview.class);
                intent.putExtra("Attachment_array", (Serializable) attachments);
                path = URL.images_baseurl + path;
                intent.putExtra("path", path);
                intent.putExtra("isvideo", false);
                context.startActivity(intent);
            }
        });
        if (mData.get(position).getImages().size() > 0) {
            Glide.with(context)
                    .load(URL.images_baseurl + mData.get(position).getImages().get(0).getImage())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_item_img)
                    .error(R.drawable.ic_item_img)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            holder.img.setImageDrawable(resource);
//                            return false;
//                        }
//                    })
                    .into(holder.img);
        } else {
            holder.img.setImageResource(R.drawable.ic_item_img);

        }
//        } else {
//            holder.Other_Text.setText("");
//            holder.images_count.setText(0 + "");
//        }
//        if (position == 5) {
//            holder.Bottom_line.setVisibility(View.GONE);
//        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecyclerView recyclerView;
        TextView Header_title, Main_Title, Distance, Strain_Type, Other_Text, images_count;
        View Bottom_line;
        ImageView img;


        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.item_product_recyler_ciew);
            img = itemView.findViewById(R.id.img);
            Header_title = itemView.findViewById(R.id.header_title);
            Bottom_line = itemView.findViewById(R.id.bottom_line);
            Main_Title = itemView.findViewById(R.id.title);
            Distance = itemView.findViewById(R.id.distance);
            Strain_Type = itemView.findViewById(R.id.strain_type);
            Other_Text = itemView.findViewById(R.id.other_text);
            images_count = itemView.findViewById(R.id.images_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(StrainLocatBudTabRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}