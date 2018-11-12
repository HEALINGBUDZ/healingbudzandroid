package com.codingpixel.healingbudz.activity.home.side_menu.profile.review_tab;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.CircularImageView;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ReviewRecylerChildViewHolder extends ChildViewHolder {
    public View childItemView;
    public TextView Comment_Text2, title_3, description_3, date_3, healingBudTextView3, share_2;
    public TextView Comment_Text3, title_2, description_2, distance_2, date_2, healingBudTextView2, share_3, rating_total_2, rating_total_3;
    public ImageView Comment_img3, Comment_img2, star_2, star_3, deliver_2, organic_2, bud_map_catagory_img,rating_2,Comment_vid2,Comment_vid3;

    public CircularImageView icon_img;
    public LinearLayout Budz_map_review_layout, strains_review_layoutwer_layout,bck_2,bck_3;

    public ReviewRecylerChildViewHolder(View itemView) {
        super(itemView);
        childItemView = itemView;
        icon_img = itemView.findViewById(R.id.icon_img);
        bck_2 = itemView.findViewById(R.id.bck_2);
        bck_3 = itemView.findViewById(R.id.bck_3);
        rating_2 = itemView.findViewById(R.id.rating_2);
        Budz_map_review_layout = itemView.findViewById(R.id.budz_map_review_layout);
        strains_review_layoutwer_layout = itemView.findViewById(R.id.strain_review_layout);
        Comment_img2 = itemView.findViewById(R.id.comment_img_2);
        Comment_img3 = itemView.findViewById(R.id.comment_img_3);
        Comment_vid2 = itemView.findViewById(R.id.comment_vid_2);
        Comment_vid3 = itemView.findViewById(R.id.comment_vid_3);
        Comment_Text2 = itemView.findViewById(R.id.comment_text);
        Comment_Text3 = itemView.findViewById(R.id.comment_text_2);
        title_3 = itemView.findViewById(R.id.title_3);
        title_2 = itemView.findViewById(R.id.title_2);
        description_3 = itemView.findViewById(R.id.descriptions_3);
        description_2 = itemView.findViewById(R.id.descriptions_2);
        date_3 = itemView.findViewById(R.id.date_3);
        date_2 = itemView.findViewById(R.id.date_2);
        healingBudTextView3 = itemView.findViewById(R.id.healingBudTextView3);
        healingBudTextView2 = itemView.findViewById(R.id.healingBudTextView2);
        share_3 = itemView.findViewById(R.id.share_3);
        share_2 = itemView.findViewById(R.id.share_2);
        distance_2 = itemView.findViewById(R.id.distance_2);
        star_2 = itemView.findViewById(R.id.star_2);
        star_3 = itemView.findViewById(R.id.star_3);
        rating_total_2 = itemView.findViewById(R.id.rating_total_2);
        rating_total_3 = itemView.findViewById(R.id.rating_total_3);
        deliver_2 = itemView.findViewById(R.id.deliver_2);
        organic_2 = itemView.findViewById(R.id.organic_2);
        bud_map_catagory_img = itemView.findViewById(R.id.bud_map_catagory_img);

    }
}
