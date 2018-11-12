package com.codingpixel.healingbudz.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.JournalListChildDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.journal_detail_holder.JournalDetailRecylerChildViewHolder;
import com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.journal_detail_holder.JournalDetailRecylerGroupViewHolder;
import com.codingpixel.healingbudz.DataModel.JournalDetailsExpandAbleData;
import com.codingpixel.healingbudz.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.Utilities.DateConverter.convertDate_with_month_name;
import static com.codingpixel.healingbudz.adapter.JournalItemHomeFragmentRecylerAdapter.GetEmojis;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;

public class JournalDetailsRecylerAdapter extends ExpandableRecyclerViewAdapter<JournalDetailRecylerGroupViewHolder, JournalDetailRecylerChildViewHolder> {

    public JournalDetailsRecylerAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public JournalDetailRecylerGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_details_recyler_view_item, parent, false);
        JournalDetailRecylerGroupViewHolder holder = new JournalDetailRecylerGroupViewHolder(view);
        return holder;
    }

    @Override
    public JournalDetailRecylerChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_details_recyler_item_expandable_item, parent, false);
        return new JournalDetailRecylerChildViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(final JournalDetailRecylerChildViewHolder holder, final int flatPosition, ExpandableGroup group, final int childIndex) {
        final List<JournalDetailsExpandAbleData> items = (List<JournalDetailsExpandAbleData>) group.getItems();
        holder.childItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JournalListChildDetailsActivity.journalDetailsExpandAbleData = items.get(childIndex);
                GoTo(getContext(), JournalListChildDetailsActivity.class);
            }
        });
        holder.title_event.setText(items.get(childIndex).getTitle());
//        holder.event_discription.setText(items.get(childIndex).getDescription());
        MakeKeywordClickableText(holder.event_discription.getContext(), items.get(childIndex).getDescription(), holder.event_discription);
        holder.feeling.setImageResource(GetEmojis(items.get(childIndex).getFeeling()));
        if (items.get(childIndex).getGet_image_attachments().size() > 0) {
            holder.image_event.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(images_baseurl + items.get(childIndex).getGet_image_attachments().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.image_event.setBackground(resource);
                            return false;
                        }
                    })
                    .into(400, 400);
        } else {
            holder.image_event.setVisibility(View.GONE);
        }
        holder.image_count.setText(items.get(childIndex).getGet_image_attachments().size() + "");
        holder.video_count.setText(items.get(childIndex).getGet_video_attachments().size() + "");
        holder.tags_count.setText(items.get(childIndex).getTags().size() + "");

        String date = convertDate_with_month_name(items.get(childIndex).getDate());
        holder.day_name.setText(date.split(" ")[0].replace(".", ""));
        holder.day_count.setText(date.split(" ")[1].split(",")[0]);
        holder.time_count.setText(date.split(" ")[2] + " " + date.split(" ")[3]);


    }

    @Override
    public void onBindGroupViewHolder(JournalDetailRecylerGroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGenreTitle(group);
    }
}
