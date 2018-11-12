package com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.journal_detail_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class JournalDetailRecylerChildViewHolder extends ChildViewHolder {
    public View childItemView;
    public TextView day_name;
    public TextView day_count;
    public TextView time_count;
    public TextView title_event;
    public TextView event_discription;
    public TextView image_count;
    public TextView video_count;
    public TextView tags_count;
    public ImageView feeling;
    public ImageView image_event;

    public JournalDetailRecylerChildViewHolder(View itemView) {
        super(itemView);
        childItemView = itemView;
        day_name = itemView.findViewById(R.id.day_name);
        day_count = itemView.findViewById(R.id.day_count);
        time_count = itemView.findViewById(R.id.time_count);
        title_event = itemView.findViewById(R.id.title_event);
        event_discription = itemView.findViewById(R.id.event_discription);
        image_count = itemView.findViewById(R.id.image_count);
        video_count = itemView.findViewById(R.id.video_count);
        tags_count = itemView.findViewById(R.id.tags_count);
        feeling = itemView.findViewById(R.id.feeling);
        image_event = itemView.findViewById(R.id.image_event);
    }
}
