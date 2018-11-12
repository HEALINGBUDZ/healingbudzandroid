package com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.journal_detail_holder;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class JournalDetailRecylerGroupViewHolder extends GroupViewHolder {
    private ImageView arrow;
    private TextView Date_Title;

    public JournalDetailRecylerGroupViewHolder(View itemView) {
        super(itemView);
        arrow = itemView.findViewById(R.id.arrow_journal_list);
        Date_Title = itemView.findViewById(R.id.date_title);
    }

    public void setGenreTitle(ExpandableGroup genre) {
        Date_Title.setText(genre.getTitle());
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate = new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
//    arrow.setAnimation(rotate);
        arrow.setImageResource(R.drawable.ic_journal_details_collapse);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
//    arrow.setAnimation(rotate);
        arrow.setImageResource(R.drawable.ic_journal_details_expand);
    }
}
