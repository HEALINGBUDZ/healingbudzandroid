package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.PaymentMethod;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.network.model.URL;

import java.util.ArrayList;
import java.util.List;

public class AddNewPaymentMethodRecylerAdapter extends RecyclerView.Adapter<AddNewPaymentMethodRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private AddNewPaymentMethodRecylerAdapter.ItemClickListener mClickListener;
    List<PaymentMethod> mData = new ArrayList<>();
    boolean isDisplayed = false;

    public AddNewPaymentMethodRecylerAdapter(Context context, List<PaymentMethod> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        isDisplayed = false;
//        this.isDeleteAble = isDeleteAble;
    }

    public AddNewPaymentMethodRecylerAdapter(Context context, List<PaymentMethod> mData, boolean isDisplay) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        isDisplayed = isDisplay;
//        this.isDeleteAble = isDeleteAble;
    }

    // inflates the cell layout from xml when needed
    @Override
    public AddNewPaymentMethodRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.add_new_payment_method_recyler_view_item, parent, false);
        AddNewPaymentMethodRecylerAdapter.ViewHolder viewHolder = new AddNewPaymentMethodRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final PaymentMethod dataTest = mData.get(position);
        if (dataTest.isSelected()) {
            holder.payment_image.setAlpha(1.0F);
        } else {
            holder.payment_image.setAlpha(0.6F);
//            holder.payment_image_above.setBackgroundColor(Color.parseColor("#33000000"));
        }
        if (!isDisplayed) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataTest.isSelected()) {
                        dataTest.setSelected(false);
                    } else {
                        dataTest.setSelected(true);
                    }
                    notifyDataSetChanged();
                    if (mClickListener != null) {
                        mClickListener.onPicSelctionItemClicked(v, position);
                    }
                }
            });
        }
        Glide.with(holder.payment_image.getContext())
                .load(URL.images_baseurl + dataTest.getImage())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
////                        holder.payment_image.setImageDrawable(resource);
//                        return false;
//                    }
//                })
                .into(holder.payment_image);

    }

    // total number of cells
    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }

    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text;
        ImageView payment_image;
        ImageView payment_image_above;
        View itemView;

        //<!--#33000000-->
//        <!--#80ffffff-->
        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_title);
            payment_image = itemView.findViewById(R.id.payment_image);
            payment_image_above = itemView.findViewById(R.id.payment_image_above);
            this.itemView = itemView;
//            payment_image.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onPicSelctionItemClicked(view, getAdapterPosition());
        }
    }

    public void setClickListener(AddNewPaymentMethodRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onPicSelctionItemClicked(View view, int position);
    }
}