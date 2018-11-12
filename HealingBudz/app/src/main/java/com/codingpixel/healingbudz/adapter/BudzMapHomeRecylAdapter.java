package com.codingpixel.healingbudz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.BudzMapHomeDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity.GetRAtingImg;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;


public class BudzMapHomeRecylAdapter extends RecyclerView.Adapter<BudzMapHomeRecylAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<BudzMapHomeDataModel> mData = new ArrayList<>();
    private BudzMapHomeRecylAdapter.ItemClickListener mClickListener;
    Context context;
    private boolean isEditIViewGone = true;

    public BudzMapHomeDataModel getData(int position) {
        return mData.get(position);
    }


    public BudzMapHomeRecylAdapter(Context context, ArrayList<BudzMapHomeDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        isEditIViewGone = true;

    }

    public BudzMapHomeRecylAdapter(Context context, ArrayList<BudzMapHomeDataModel> data, boolean isEditIViewGone) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.isEditIViewGone = isEditIViewGone;
    }

    // inflates the cell layout from xml when needed
    @Override
    public BudzMapHomeRecylAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.budz_map_recyler_view_item_layout, parent, false);
        BudzMapHomeRecylAdapter.ViewHolder viewHolder = new BudzMapHomeRecylAdapter.ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final BudzMapHomeRecylAdapter.ViewHolder holder, final int position) {
        BudzMapHomeDataModel data = mData.get(position);
        if (!data.isPending()) {
            if (isEditIViewGone) {
                holder.edit_layout.setVisibility(View.GONE);
            } else {
                holder.edit_layout.setVisibility(View.VISIBLE);
            }
            if (data.getIs_featured() == 1) {
                holder.main_layout.setBackgroundColor(Color.parseColor("#000000"));
                holder.Featured_business_layout.setVisibility(View.VISIBLE);
//            holder.rating_imgs.setImageResource(R.drawable.ic_rating_img);
            } else {
                holder.main_layout.setBackgroundColor(Color.parseColor("#232323"));
                holder.Featured_business_layout.setVisibility(View.GONE);
//            holder.rating_imgs.setImageResource(R.drawable.ic_rating_img_2);
            }

            if (mData.get(position).getIs_featured() == 0) {
                holder.doted_line.setImageDrawable(null);
                holder.doted_line.setBackgroundColor(Color.parseColor("#3c3c3c"));
            } else {
                holder.doted_line.setImageResource(R.drawable.doted_line);
                holder.doted_line.setBackgroundColor(Color.parseColor("#00000000"));
            }

            if (data.getIs_organic() == 1) {
                holder.Organic.setVisibility(View.VISIBLE);
            } else {
                holder.Organic.setVisibility(View.GONE);
            }

            if (data.getIs_delivery() == 1) {
                holder.Delivery.setVisibility(View.VISIBLE);
            } else {
                holder.Delivery.setVisibility(View.GONE);
            }
            holder.Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onEditClick(view, position);
                }
            });

            holder.Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onDeleteClick(view, position);
                }
            });

            Glide.with(context)
                    .load(images_baseurl + mData.get(position).getLogo())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_budz_adn)
                    .error(R.drawable.ic_budz_adn)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            Log.d("ready", model);
//                            holder.Main_Icon.setImageDrawable(resource);
//                            return false;
//                        }
//                    })
                    .into(holder.Main_Icon);
            switch (data.getBusiness_type_id()) {
                case 1:
                    holder.type.setText("Dispensary");
                    holder.BudCategory_small_icon.setImageResource(R.drawable.ic_dispancry_icon);
                    break;
                case 2:
                case 6:
                case 7:
                    holder.type.setText("Medical");
                    holder.BudCategory_small_icon.setImageResource(R.drawable.ic_medical_icon);
                    break;
                case 3:
                    holder.type.setText("Cannabites");
                    holder.BudCategory_small_icon.setImageResource(R.drawable.ic_cannabites_icon);
                    break;
                case 4:
                case 8:
                    holder.type.setText("Entertainment");
                    holder.BudCategory_small_icon.setImageResource(R.drawable.ic_entertainment_icon);
                    break;
                case 5:
                    holder.type.setText("Events");
                    holder.BudCategory_small_icon.setImageResource(R.drawable.ic_events_icon);
                    break;
                case 9:
                    holder.type.setText("Other");
                    holder.BudCategory_small_icon.setImageResource(R.drawable.other_item);
                    break;
            }


//        holder.Heading_Title.setText(data.getTitle());
            MakeKeywordClickableText(holder.Heading_Title.getContext(), data.getTitle(), holder.Heading_Title);
            holder.Heading_Title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.Heading_Title.isClickable() && holder.Heading_Title.isEnabled()) {
                        if (mClickListener != null)
                            mClickListener.onItemClick(view, holder.getAdapterPosition());
                    }
                }
            });
            @SuppressLint("DefaultLocale") String Distance = String.format("%.01f", Double.parseDouble(mData.get(position).getDistance() + ""));
            holder.distance.setText(Distance + " mi");
            if (data.getReviews() != null) {
                holder.reviewss.setText(data.getReviews().size() + " Reviews");
            }

            if (data.getRating_sum() > 0) {
                holder.rating_img.setImageResource(GetRAtingImg(data.getRating_sum()));
//            if (data.getRating_sum() >= 1 && data.getRating_sum() < 2) {
//                holder.rating_img.setImageResource(R.drawable.rating_one);
//            } else if (data.getRating_sum() >= 2 && data.getRating_sum() < 3) {
//                holder.rating_img.setImageResource(R.drawable.rating_two);
//            } else if (data.getRating_sum() >= 3 && data.getRating_sum() < 4) {
//                holder.rating_img.setImageResource(R.drawable.rating_three);
//            } else if (data.getRating_sum() >= 4 && data.getRating_sum() < 5) {
//                holder.rating_img.setImageResource(R.drawable.rating_four);
//            } else if (data.getRating_sum() == 5) {
//                holder.rating_img.setImageResource(R.drawable.rating_five);
//            }
            } else {
                holder.rating_img.setImageResource(R.drawable.ic_empty_all);
            }
        } else {
            if (isEditIViewGone) {
                holder.edit_layout.setVisibility(View.GONE);
            } else {
                holder.edit_layout.setVisibility(View.VISIBLE);
            }
            holder.Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onEditPendingClick(view, position);
                }
            });

            holder.Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onDeletePendingClick(view, position);
                }
            });
        }
    }


    // total number of cells
    @Override
    public int getItemCount() {
        return this.mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout main_layout;
        ImageView doted_line, Main_Icon;
        ImageView rating_imgs;
        LinearLayout Featured_business_layout, edit_layout;
        ImageView Organic, Delivery;
        ImageView BudCategory_small_icon, rating_img;
        TextView type, Heading_Title, distance, reviewss;
        ImageView Edit, Delete;

        public ViewHolder(View itemView) {
            super(itemView);
            main_layout = itemView.findViewById(R.id.main_layout);
            edit_layout = itemView.findViewById(R.id.edit_layout);
            doted_line = itemView.findViewById(R.id.top_line);
//            rating_imgs = itemView.findViewById(R.id.rating_img);
            Featured_business_layout = itemView.findViewById(R.id.featured_business_layout);
            Organic = itemView.findViewById(R.id.organic_img);
            Delivery = itemView.findViewById(R.id.delivery_img);
            BudCategory_small_icon = itemView.findViewById(R.id.bud_map_catagory_img);
            type = itemView.findViewById(R.id.type);
            Main_Icon = itemView.findViewById(R.id.icon_img);
            Heading_Title = itemView.findViewById(R.id.heading_title);
            Heading_Title.setAutoLinkMask(0);
            distance = itemView.findViewById(R.id.distance);
            reviewss = itemView.findViewById(R.id.reviewss);
            rating_img = itemView.findViewById(R.id.rating_img);
            Edit = itemView.findViewById(R.id.edit_btn);
            Delete = itemView.findViewById(R.id.delete_btn);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            BudzMapHomeDataModel budzMapHomeDataModel = mData.get(getAdapterPosition());
            if (!budzMapHomeDataModel.isPending()) {
                if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

    }

    // allows clicks events to be caught
    public void setClickListener(BudzMapHomeRecylAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onEditClick(View view, int position);

        void onDeleteClick(View view, int position);

        void onEditPendingClick(View view, int position);

        void onDeletePendingClick(View view, int position);
    }
}