package com.codingpixel.healingbudz.adapter;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.review_tab.ReviewRecylerChildViewHolder;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.review_tab.ReviewRecylerGroupViewHolder;
import com.codingpixel.healingbudz.DataModel.BudzMapReviews;
import com.codingpixel.healingbudz.DataModel.ReviewsDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity.GetRAtingImg;
import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;

public class UserProfileReviewTabRecylerAdapter extends ExpandableRecyclerViewAdapter<ReviewRecylerGroupViewHolder, ReviewRecylerChildViewHolder> {

    List<ReviewsDataModel> mData = new ArrayList<>();

    public UserProfileReviewTabRecylerAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
        mData = (List<ReviewsDataModel>) groups;
    }

    @Override
    public ReviewRecylerGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile_review_recyler_view_heading_item, parent, false);
        ReviewRecylerGroupViewHolder holder = new ReviewRecylerGroupViewHolder(view);
        return holder;
    }

    @Override
    public ReviewRecylerChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userprofile_review_tab_childrecyler_veiew_item_layout, parent, false);
        return new ReviewRecylerChildViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(final ReviewRecylerChildViewHolder holder, final int flatPosition, ExpandableGroup group, final int childIndex) {
        Log.d("title ", group.getTitle());
        if (group.getTitle().equalsIgnoreCase("My Budz Adz Reviews")) {
            holder.Budz_map_review_layout.setVisibility(View.VISIBLE);
            holder.strains_review_layoutwer_layout.setVisibility(View.GONE);
            final List<BudzMapReviews> dataModels = group.getItems();

//            holder.Comment_img2.setBackground(GetRounderDarbable(R.drawable.ic_text_img_1));
//            holder.Comment_Text2.setText(dataModels.get(childIndex).getReview());
            MakeKeywordClickableText(holder.Comment_Text3.getContext(), dataModels.get(childIndex).getReview(), holder.Comment_Text3);
            holder.date_2.setText(DateConverter.getCustomDateString(dataModels.get(childIndex).getUpdated_at()));

            switch (dataModels.get(childIndex).getBud().getBusiness_type_id()) {
                case 1:
                    holder.title_2.setText("Dispensary");
                    holder.bud_map_catagory_img.setImageResource(R.drawable.ic_dispancry_icon);
                    break;
                case 2:
                case 6:
                case 7:
                    holder.title_2.setText("Medical");
                    holder.bud_map_catagory_img.setImageResource(R.drawable.ic_medical_icon);
                    break;
                case 3:
                    holder.title_2.setText("Cannabites");
                    holder.bud_map_catagory_img.setImageResource(R.drawable.ic_cannabites_icon);
                    break;
                case 4:
                case 8:
                    holder.title_2.setText("Entertainment");
                    holder.bud_map_catagory_img.setImageResource(R.drawable.ic_entertainment_icon);
                    break;
                case 5:
                    holder.title_2.setText("Events");
                    holder.bud_map_catagory_img.setImageResource(R.drawable.ic_events_icon);
                    break;
                case 9:
                    holder.title_2.setText("Other");
                    holder.bud_map_catagory_img.setImageResource(R.drawable.other_item);
                    break;
            }
            Glide.with(holder.icon_img.getContext())
                    .load(images_baseurl + dataModels.get(childIndex).getBud().getLogo())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_budz_adn)
                    .centerCrop()
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            if (e != null) {
//                                Log.d("ready", e.getMessage());
//                            }
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            Log.d("ready", model);
//                            holder.icon_img.setImageDrawable(resource);
//                            return false;
//                        }
//                    })
                    .into( holder.icon_img);
//            holder.description_2.setText(dataModels.get(childIndex).getBud().getTitle());
            MakeKeywordClickableText(holder.description_2.getContext(), dataModels.get(childIndex).getBud().getTitle(), holder.description_2);
            @SuppressLint("DefaultLocale") String Distance = String.format("%.01f", Double.parseDouble(dataModels.get(childIndex).getBud().getDistance() + ""));
            holder.distance_2.setText(Distance + " mi");


            if (dataModels.get(childIndex).getRating() > 0) {
                holder.rating_2.setImageResource(GetRAtingImg(dataModels.get(childIndex).getRating()));
                holder.rating_total_2.setText(dataModels.get(childIndex).getRating() + "");
//                if (dataModels.get(childIndex).getRating() == 1) {
//                    holder.rating_2.setImageResource(R.drawable.rating_one);
//                } else if (dataModels.get(childIndex).getRating() == 2) {
//                    holder.rating_2.setImageResource(R.drawable.rating_two);
//                } else if (dataModels.get(childIndex).getRating() == 3) {
//                    holder.rating_2.setImageResource(R.drawable.rating_three);
//                } else if (dataModels.get(childIndex).getRating() == 4) {
//                    holder.rating_2.setImageResource(R.drawable.rating_four);
//                } else if (dataModels.get(childIndex).getRating() == 5) {
//                    holder.rating_2.setImageResource(R.drawable.rating_five);
//                }
            } else {
                holder.rating_total_2.setText("0.0");
                holder.rating_2.setImageResource(R.drawable.ic_empty_all);
            }

            if (dataModels.get(childIndex).getBud().getIs_organic() == 1) {
                holder.organic_2.setVisibility(View.VISIBLE);
            } else {
                holder.organic_2.setVisibility(View.INVISIBLE);
            }

            if (dataModels.get(childIndex).getBud().getIs_delivery() == 1) {
                holder.deliver_2.setVisibility(View.VISIBLE);
            } else {
                holder.deliver_2.setVisibility(View.INVISIBLE);
            }
            if (dataModels.get(childIndex).getAttatchment_type() != null) {
                if (dataModels.get(childIndex).getAttatchment_type().equalsIgnoreCase("image")) {

                    holder.bck_2.setVisibility(View.VISIBLE);
                    holder.Comment_img2.setVisibility(View.VISIBLE);
                    holder.Comment_vid2.setVisibility(View.INVISIBLE);
//                    comment_one_media_type_icon.setImageResource(R.drawable.ic_gallery_icon);
                    SetImageBackground(holder.Comment_img2, dataModels.get(childIndex).getAttatchment_path());
                } else if (dataModels.get(childIndex).getAttatchment_type().equalsIgnoreCase("video")) {
                    holder.bck_2.setVisibility(View.VISIBLE);
                    holder.Comment_img2.setVisibility(View.VISIBLE);
                    holder.Comment_vid2.setVisibility(View.VISIBLE);
//                    comment_one_media_type_icon.setVisibility(View.VISIBLE);
//                    Comment_one_img.setVisibility(View.VISIBLE);
//                    comment_one_media_type_icon.setImageResource(R.drawable.ic_video_play_icon);
                    SetImageBackground(holder.Comment_vid2, dataModels.get(childIndex).getAttatchment_poster());
                } else {
                    holder.bck_2.setVisibility(View.VISIBLE);
                    holder.Comment_img2.setVisibility(View.VISIBLE);
                    holder.Comment_vid2.setVisibility(View.INVISIBLE);
//                    comment_one_media_type_icon.setVisibility(View.GONE);
//                    Comment_one_img.setVisibility(View.GONE);
//                    First_Attach.setVisibility(View.GONE);
                }
            } else {
                holder.bck_2.setVisibility(View.VISIBLE);
                holder.Comment_img2.setVisibility(View.VISIBLE);
                holder.Comment_vid2.setVisibility(View.INVISIBLE);
//                comment_one_media_type_icon.setVisibility(View.GONE);
//                Comment_one_img.setVisibility(View.GONE);
//                First_Attach.setVisibility(View.GONE);
            }

            holder.Budz_map_review_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent budzmap_intetn = new Intent(view.getContext(), BudzMapDetailsActivity.class);
                    budzmap_intetn.putExtra("budzmap_id", dataModels.get(childIndex).getId());
                    view.getContext().startActivity(budzmap_intetn);
                }
            });


        } else {
            ///////

            final List<BudzMapReviews> dataModels = group.getItems();
            holder.Budz_map_review_layout.setVisibility(View.GONE);
            holder.strains_review_layoutwer_layout.setVisibility(View.VISIBLE);


//            String commment_text_strain = "Lorem ipsum dolor sit met, consectetur <b><font color=#997c26>adipiscing</font></b> elit. Aliquam lorem lacks, efficture at fermentum quis. Vivamos id velho rhoncus.";
//            holder.Comment_img3.setBackground(GetRounderDarbable(R.drawable.ic_text_img_1));
//            holder.Comment_Text3.setText(Html.fromHtml(commment_text_strain));
//            holder.Comment_Text3.setText(dataModels.get(childIndex).getGet_strain().getOverview());
            MakeKeywordClickableText(holder.Comment_Text2.getContext(), dataModels.get(childIndex).getReview(), holder.Comment_Text2);
            holder.date_3.setText(DateConverter.getCustomDateString(dataModels.get(childIndex).getUpdated_at()));
            holder.title_3.setText(dataModels.get(childIndex).getGet_strain().getTitle());
//            holder.description_3.setText(dataModels.get(childIndex).getGet_strain().getType_title());
            MakeKeywordClickableText(holder.description_3.getContext(), dataModels.get(childIndex).getGet_strain().getType_title(), holder.description_3);

            if (dataModels.get(childIndex).getRating() > 0) {
                holder.rating_total_3.setText(dataModels.get(childIndex).getRating() + "");
                if (dataModels.get(childIndex).getRating() == 1) {
                    holder.star_3.setImageResource(R.drawable.ic_strain_rating_one);
                } else if (dataModels.get(childIndex).getRating() == 2) {
                    holder.star_3.setImageResource(R.drawable.ic_strain_rating_two);
                } else if (dataModels.get(childIndex).getRating() == 3) {
                    holder.star_3.setImageResource(R.drawable.ic_strain_rating_three);
                } else if (dataModels.get(childIndex).getRating() == 4) {
                    holder.star_3.setImageResource(R.drawable.ic_strain_rating_four);
                } else if (dataModels.get(childIndex).getRating() == 5) {
                    holder.star_3.setImageResource(R.drawable.ic_strain_rating_five);
                }
            } else {
                holder.rating_total_3.setText("0.0");
                holder.star_3.setImageResource(R.drawable.ic_strain_rating_one);
            }
            if (dataModels.get(childIndex).getAttatchment_type() != null) {
                if (dataModels.get(childIndex).getAttatchment_type().equalsIgnoreCase("image")) {

                    holder.bck_3.setVisibility(View.VISIBLE);
                    holder.Comment_img3.setVisibility(View.VISIBLE);
                    holder.Comment_vid3.setVisibility(View.INVISIBLE);
//                    comment_one_media_type_icon.setImageResource(R.drawable.ic_gallery_icon);
                    SetImageBackground(holder.Comment_img3, dataModels.get(childIndex).getAttatchment_path());
                } else if (dataModels.get(childIndex).getAttatchment_type().equalsIgnoreCase("video")) {
                    holder.bck_3.setVisibility(View.VISIBLE);
                    holder.Comment_img3.setVisibility(View.VISIBLE);
                    holder.Comment_vid3.setVisibility(View.VISIBLE);
//                    comment_one_media_type_icon.setVisibility(View.VISIBLE);
//                    Comment_one_img.setVisibility(View.VISIBLE);
//                    comment_one_media_type_icon.setImageResource(R.drawable.ic_video_play_icon);
                    SetImageBackground(holder.Comment_vid3, dataModels.get(childIndex).getAttatchment_poster());
                } else {
                    holder.bck_3.setVisibility(View.VISIBLE);
                    holder.Comment_img3.setVisibility(View.VISIBLE);
                    holder.Comment_vid3.setVisibility(View.INVISIBLE);
//                    comment_one_media_type_icon.setVisibility(View.GONE);
//                    Comment_one_img.setVisibility(View.GONE);
//                    First_Attach.setVisibility(View.GONE);
                }
            } else {
                holder.bck_3.setVisibility(View.VISIBLE);
                holder.Comment_img3.setVisibility(View.VISIBLE);
                holder.Comment_vid3.setVisibility(View.INVISIBLE);
//                comment_one_media_type_icon.setVisibility(View.GONE);
//                Comment_one_img.setVisibility(View.GONE);
//                First_Attach.setVisibility(View.GONE);
            }

            holder.strains_review_layoutwer_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent strain_intetn = new Intent(view.getContext(), StrainDetailsActivity.class);
                    strain_intetn.putExtra("strain_id", dataModels.get(childIndex).getStrain_id());
                    view.getContext().startActivity(strain_intetn);
                }
            });
        }


    }

    @Override
    public void onBindGroupViewHolder(ReviewRecylerGroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGenreTitle(group);
        if (group.getTitle().equalsIgnoreCase("My Budz Adz Reviews")) {
            holder.Date_Title.setTextColor(Color.parseColor("#822a79"));
        } else {
            holder.Date_Title.setTextColor(Color.parseColor("#f1c22f"));
        }
    }


    public Drawable GetRounderDarbable(int resourse) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), resourse);
        Bitmap bitmapOrg = ((BitmapDrawable) drawable).getBitmap();
        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
        Drawable d = new BitmapDrawable(getContext().getResources(), bitmap);
        return d;
    }

    public void SetImageBackground(final ImageView imageView, String Path) {
        if (imageView.getContext() != null) {
            Glide.with(imageView.getContext())
                    .load(images_baseurl + Path)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.image_plaecholder_bg)
                    .error(R.drawable.noimage)
                    .centerCrop()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("ready", model);
                            try {
                                Drawable d = resource;
                                Bitmap bitmapOrg = ((GlideBitmapDrawable) d).getBitmap();
                                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
                                imageView.setImageDrawable(drawable);
//                                imageView.setBackground(drawable);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return false;
                        }
                    }).into(imageView);
        }
    }
}
