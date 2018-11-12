package com.codingpixel.healingbudz.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.BudzMapProductDataModal;
import com.codingpixel.healingbudz.DataModel.QuestionAnswersDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.sharedBaseUrl;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareHBContent;


public class BudzMapProductRecylerAdapter extends RecyclerView.Adapter<BudzMapProductRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<BudzMapProductDataModal> mData = new ArrayList<>();
    Context context;
    private BudzMapProductRecylerAdapter.ItemClickListener mClickListener;
    boolean is3 = false;

    public BudzMapProductRecylerAdapter(Context context, ArrayList<BudzMapProductDataModal> dataModals, boolean is3) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.is3 = is3;
        this.mData = dataModals;
    }

    // inflates the cell layout from xml when needed
    @NonNull
    @Override
    public BudzMapProductRecylerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.budz_map_product_recyler_item, parent, false);
        BudzMapProductRecylerAdapter.ViewHolder viewHolder = new BudzMapProductRecylerAdapter.ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final BudzMapProductRecylerAdapter.ViewHolder holder, final int position) {
        if (budz_map_item_clickerd_dataModel.getUser_id() == Splash.user.getUser_id()) {
            holder.edit_view.setVisibility(View.VISIBLE);
        } else {
            holder.edit_view.setVisibility(View.GONE);
        }
        if (!mData.get(position).isProduct()) {
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onServiceDelete(v, position);
                }
            });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onServiceEdit(v, position);
                }
            });
            holder.ind.setVisibility(View.GONE);
            holder.stiv.setVisibility(View.GONE);
            holder.hind.setVisibility(View.GONE);
            if (mData.get(position).isTitle()) {
                holder.Header_title.setVisibility(View.VISIBLE);
                holder.Header_title.setText("Services");
            } else {
                holder.Header_title.setVisibility(View.GONE);
            }
            holder.straint_layout.setVisibility(View.INVISIBLE);
            holder.recyclerView.setVisibility(View.INVISIBLE);
            holder.count.setVisibility(View.INVISIBLE);
            holder.heading.setText(mData.get(position).getName());
            holder.setrain_type.setText("$" + mData.get(position).getCharges());
            holder.discription.setVisibility(View.INVISIBLE);
            holder.share_product_services.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject object1 = new JSONObject();
                    try {
//                    object1.put("msg", "http://139.162.37.73/healingbudz/budz-map");
                        object1.put("id", mData.get(position).getId());
                        object1.put("type", "Budz Service");
                        object1.put("content", budz_map_item_clickerd_dataModel.getTitle());
                        object1.put("BudzCome", "");
                        object1.put("url", sharedBaseUrl + "/get-budz?business_id=" + budz_map_item_clickerd_dataModel.getId() + "&business_type_id=" + budz_map_item_clickerd_dataModel.getBusiness_type_id());
                        object1.put("msg", "Check out Healing Budz for your smartphone: " + URL.sharedBaseUrl + "/get-budz?business_id=" + budz_map_item_clickerd_dataModel.getId() + "&business_type_id=" + budz_map_item_clickerd_dataModel.getBusiness_type_id());// "/get-budz-service/" + mData.get(position).getId() + "/" + budz_map_item_clickerd_dataModel.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ShareHBContent((Activity) v.getContext(), object1);
                }
            });
            if (mData.get(position).getImage_path().length() > 0) {
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ArrayList<QuestionAnswersDataModel.Attachment> attachments = new ArrayList<>();
                        attachments.add(new QuestionAnswersDataModel.Attachment(mData.get(position).getImage_path()));
                        String path = mData.get(position).getImage_path();
                        Intent intent = new Intent(context, MediPreview.class);
                        intent.putExtra("Attachment_array", (Serializable) attachments);
                        path = URL.images_baseurl + path;
                        intent.putExtra("path", path);
                        intent.putExtra("isvideo", false);
                        context.startActivity(intent);
                    }
                });
                Glide.with(context)
                        .load(images_baseurl + mData.get(position).getImage_path())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.noimage)
                        .centerCrop()
//                        .listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                holder.img.setImageDrawable(resource);
//                                return false;
//                            }
//                        })
                        .into(holder.img);

            } else {
                holder.img.setImageResource(R.drawable.placeholder);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }
        } else {
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onProduceDelete(v, position);
                }
            });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onProduceEdit(v, position);
                }
            });
            if (is3) {
                if (mData.get(position).isTitle()) {
                    holder.Header_title.setVisibility(View.VISIBLE);
                    holder.Header_title.setText(mData.get(position).getStrian_type_title());
                } else {
                    holder.Header_title.setText(mData.get(position).getStrian_type_title());
                    holder.Header_title.setVisibility(View.GONE);
                }
            } else {
                if (mData.get(position).isTitle()) {
                    holder.Header_title.setVisibility(View.VISIBLE);
                    holder.Header_title.setText(mData.get(position).getStrian_type_title());
                } else {
                    holder.Header_title.setText(mData.get(position).getStrian_type_title());
                    holder.Header_title.setVisibility(View.GONE);
                }
            }
            holder.setrain_type.setText(mData.get(position).getStrian_type_title());
            if (mData.get(position).isAlsoStrain()) {
                holder.setrain_type.setText(mData.get(position).getAlsoStrain());
            }

            if (mData.get(position).getStrainModel() == null) {
                holder.straint_layout.setVisibility(View.INVISIBLE);
                holder.straint_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                holder.ind.setVisibility(View.GONE);
                holder.stiv.setVisibility(View.GONE);
                holder.hind.setVisibility(View.GONE);
                holder.setrain_type.setText("");

            } else if (mData.get(position).getStrainModel().getTitle().equalsIgnoreCase("Indica")
                    || mData.get(position).getStrainModel().getTitle().equalsIgnoreCase("Hybrid")
                    || mData.get(position).getStrainModel().getTitle().equalsIgnoreCase("Sativa") || mData.get(position).isAlsoStrain()) {
                holder.straint_layout.setVisibility(View.VISIBLE);
                holder.setrain_type.setText(mData.get(position).getStrainModel().getTitle());
                holder.straint_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent strain_intetn = new Intent(v.getContext(), StrainDetailsActivity.class);
                        strain_intetn.putExtra("strain_id", mData.get(position).getStrain_id());
                        v.getContext().startActivity(strain_intetn);
                    }
                });
                if (holder.setrain_type.getText().toString().equalsIgnoreCase("Indica")) {

                    holder.ind.setVisibility(View.VISIBLE);
                    holder.stiv.setVisibility(View.GONE);
                    holder.hind.setVisibility(View.GONE);

                } else if (holder.setrain_type.getText().toString().equalsIgnoreCase("Hybrid")) {

                    holder.ind.setVisibility(View.GONE);
                    holder.stiv.setVisibility(View.GONE);
                    holder.hind.setVisibility(View.VISIBLE);

                } else if (holder.setrain_type.getText().toString().equalsIgnoreCase("Sativa")) {

                    holder.ind.setVisibility(View.GONE);
                    holder.stiv.setVisibility(View.VISIBLE);
                    holder.hind.setVisibility(View.GONE);

                } else {

                    holder.ind.setVisibility(View.GONE);
                    holder.stiv.setVisibility(View.GONE);
                    holder.hind.setVisibility(View.GONE);

                }
            } else {
                holder.ind.setVisibility(View.GONE);
                holder.stiv.setVisibility(View.GONE);
                holder.hind.setVisibility(View.GONE);
                holder.setrain_type.setText("");
                holder.straint_layout.setVisibility(View.INVISIBLE);
                holder.straint_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.recyclerView.setLayoutManager(layoutManager);
            BudzMapProductSubItemRecylerAdapter recyler_adapter = null;
            recyler_adapter = new BudzMapProductSubItemRecylerAdapter(context, mData.get(position).getPriceing());
            holder.recyclerView.setAdapter(recyler_adapter);
            if (mData.get(position).getImage().size() > 0) {
                holder.count.setVisibility(View.VISIBLE);
            } else {
                holder.count.setVisibility(View.GONE);
            }
            if (mData.get(position).getImage().size() == 2) {
                holder.count.setText("1+");
            } else if (mData.get(position).getImage().size() >= 3) {
                if (mData.get(position).getImage().size() == 3) {
                    holder.count.setText("3");
                } else
                    holder.count.setText("3+");
            } else {
                holder.count.setText("1");
            }
            holder.heading.setText(mData.get(position).getName());
            if (mData.get(position).getCbd().length() > 0 || mData.get(position).getThc().length() > 0)
                holder.discription.setText(MessageFormat.format("{0}% CBD | {1}% THC", mData.get(position).getCbd(), mData.get(position).getThc()));
            else {
                holder.discription.setText("");
            }
            holder.share_product_services.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject object1 = new JSONObject();
                    try {
//                    object1.put("msg", "http://139.162.37.73/healingbudz/budz-map");
                        object1.put("id", mData.get(position).getId());
                        object1.put("type", "Budz Service");
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
            if (mData.get(position).getImage().size() > 0) {
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ArrayList<QuestionAnswersDataModel.Attachment> attachments = new ArrayList<>();
                        for (int i = 0; i < mData.get(position).getImage().size(); i++) {
                            attachments.add(new QuestionAnswersDataModel.Attachment(mData.get(position).getImage().get(i)));
                        }
                        String path = mData.get(position).getImage().get(0);
                        Intent intent = new Intent(context, MediPreview.class);
                        intent.putExtra("Attachment_array", (Serializable) attachments);
                        path = URL.images_baseurl + path;
                        intent.putExtra("path", path);
                        intent.putExtra("isvideo", false);
                        context.startActivity(intent);
                    }
                });
                Glide.with(context)
                        .load(images_baseurl + mData.get(position).getImage().get(0))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.noimage)
                        .centerCrop()
//                        .listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                holder.img.setImageDrawable(resource);
//                                return false;
//                            }
//                        })
                        .into(holder.img);

            } else {
                holder.img.setImageResource(R.drawable.placeholder);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }


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

        void onServiceDelete(View view, int position);

        void onServiceEdit(View view, int position);

        void onProduceEdit(View view, int position);

        void onProduceDelete(View view, int position);
    }

    // allows clicks events to be caught
    public void setClickListener(BudzMapProductRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecyclerView recyclerView;
        TextView Header_title, heading, setrain_type, discription;
        Button count;
        ImageView img, share_product_services, edit, delete;
        RelativeLayout inidicator;
        LinearLayout straint_layout, edit_view;
        TextView ind, stiv, hind;

        public ViewHolder(View itemView) {
            super(itemView);
            //edit,delete
            straint_layout = itemView.findViewById(R.id.straint_layout);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
            edit_view = itemView.findViewById(R.id.edit_view);
            stiv = itemView.findViewById(R.id.stiv);
            hind = itemView.findViewById(R.id.hind);
            ind = itemView.findViewById(R.id.ind);
            inidicator = itemView.findViewById(R.id.inidicator);
            share_product_services = itemView.findViewById(R.id.share_product_services);
            recyclerView = itemView.findViewById(R.id.item_product_recyler_ciew);
            Header_title = itemView.findViewById(R.id.header_title);
            setrain_type = itemView.findViewById(R.id.setrain_type);
            discription = itemView.findViewById(R.id.thdd);
            heading = itemView.findViewById(R.id.heading);
            heading.setAutoLinkMask(0);
            discription.setAutoLinkMask(0);
            count = itemView.findViewById(R.id.count);
            img = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }
}