package com.codingpixel.healingbudz.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.BudzMapTicketsDataModal;
import com.codingpixel.healingbudz.DataModel.QuestionAnswersDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.sharedBaseUrl;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareHBContent;


public class BudzMapEventTypeRecylerAdapter extends RecyclerView.Adapter<BudzMapEventTypeRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<BudzMapTicketsDataModal> mData = new ArrayList<>();
    Context context;
    private BudzMapEventTypeRecylerAdapter.ItemClickListener mClickListener;

    public BudzMapEventTypeRecylerAdapter(Context context, ArrayList<BudzMapTicketsDataModal> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = mData;
    }

    // inflates the cell layout from xml when needed
    @Override
    public BudzMapEventTypeRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.budz_map_event_type_recyler_item, parent, false);
        BudzMapEventTypeRecylerAdapter.ViewHolder viewHolder = new BudzMapEventTypeRecylerAdapter.ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull final BudzMapEventTypeRecylerAdapter.ViewHolder holder, final int position) {
        if (position == 0) {
            holder.Header_title.setVisibility(View.VISIBLE);
        } else {
            holder.Header_title.setVisibility(View.GONE);
        }
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemEdit(v, position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemDelete(v, position);
            }
        });
        if (budz_map_item_clickerd_dataModel.getUser_id() == Splash.user.getUser_id()) {
            holder.edit_view.setVisibility(View.VISIBLE);
        } else {
            holder.edit_view.setVisibility(View.GONE);
        }
        holder.titlee.setText(mData.get(position).getTitle());
        holder.price.setText(mData.get(position).getPrice());
        holder.share_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object1 = new JSONObject();
                try {
//                    object1.put("msg", "http://139.162.37.73/healingbudz/budz-map");
                    object1.put("id", mData.get(position).getId());
                    object1.put("type", "Budz Ticket");
                    object1.put("content", budz_map_item_clickerd_dataModel.getTitle());
                    object1.put("url", sharedBaseUrl + "/get-budz?business_id=" + budz_map_item_clickerd_dataModel.getId() + "&business_type_id=" + budz_map_item_clickerd_dataModel.getBusiness_type_id());
                    object1.put("BudzCome", "");
                    object1.put("msg", "Check out Healing Budz for your smartphone: " + URL.sharedBaseUrl + "/get-budz?business_id=" + budz_map_item_clickerd_dataModel.getId() + "&business_type_id=" + budz_map_item_clickerd_dataModel.getBusiness_type_id());//"/get-budz-product/" + mData.get(position).getId() + "/" + budz_map_item_clickerd_dataModel.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareHBContent((Activity) v.getContext(), object1);
            }
        });
        holder.linke.setText(mData.get(position).getLinkee());
        holder.linke.setAutoLinkMask(Linkify.ALL);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position).getImage().length() > 4) {
                    final ArrayList<QuestionAnswersDataModel.Attachment> attachments = new ArrayList<>();
                    attachments.add(new QuestionAnswersDataModel.Attachment(mData.get(position).getImage()));
                    String path = mData.get(position).getImage();
                    Intent intent = new Intent(context, MediPreview.class);
                    intent.putExtra("Attachment_array", (Serializable) attachments);
                    path = URL.images_baseurl + path;
                    intent.putExtra("path", path);
                    intent.putExtra("isvideo", false);
                    context.startActivity(intent);
                }
            }
        });
        if (mData.get(position).getImage().length() > 4) {

            Glide.with(context)
                    .load(images_baseurl + mData.get(position).getImage())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_doctor_icon)
                    .centerCrop()
                    .error(R.drawable.noimage)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
////                            holder.img.setImageDrawable(resource);
//                            return false;
//                        }
//                    })
                    .into(holder.img);

        } else {

            Glide.with(context)
                    .load(images_baseurl + mData.get(position).getImage())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_doctor_icon)
                    .centerCrop()
                    .error(R.drawable.ic_doctor_icon)
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

        }

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onItemDelete(View view, int position);

        void onItemEdit(View view, int position);
    }

    // allows clicks events to be caught
    public void setClickListener(BudzMapEventTypeRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Header_title, titlee, linke, price;
        ImageView img, share_ticket, edit, delete;
        LinearLayout edit_view;

        public ViewHolder(View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
            share_ticket = itemView.findViewById(R.id.share_ticket);
            edit_view = itemView.findViewById(R.id.edit_view);
            linke = itemView.findViewById(R.id.linke);
            Header_title = itemView.findViewById(R.id.header_title);
            Header_title.setAutoLinkMask(0);
            titlee = itemView.findViewById(R.id.titlee);
            titlee.setAutoLinkMask(0);
            price = itemView.findViewById(R.id.price);
            img = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }
}