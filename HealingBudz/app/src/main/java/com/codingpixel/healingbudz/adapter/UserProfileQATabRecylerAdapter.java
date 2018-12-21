package com.codingpixel.healingbudz.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.DataModel.JournalDetailsExpandAbleData;
import com.codingpixel.healingbudz.DataModel.UserProfileQATabExpandAbleData;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.JournalDetailsActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.qa_tab.QARecylerChildViewHolder;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.qa_tab.QARecylerGroupViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.activity.home.HomeActivity.showSubFragmentListner_object;

public class UserProfileQATabRecylerAdapter extends ExpandableRecyclerViewAdapter<QARecylerGroupViewHolder, QARecylerChildViewHolder> {
    Context context;

    public UserProfileQATabRecylerAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public QARecylerGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile_qa_recyler_view_item, parent, false);
        QARecylerGroupViewHolder holder = new QARecylerGroupViewHolder(view);
        return holder;
    }

    @Override
    public QARecylerChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userprofile_qa_tab_recyler_veiew_item_layout, parent, false);
        final QARecylerChildViewHolder holder = new QARecylerChildViewHolder(view);
        context = view.getContext();
        return holder;
    }

    @Override
    public void onBindChildViewHolder(final QARecylerChildViewHolder holder, final int flatPosition, ExpandableGroup group, final int childIndex) {
        Log.d("title ", group.getTitle() + "d");
        if (group.getTitle().equalsIgnoreCase("My Journals") || group.getTitle().equalsIgnoreCase("Journals I Follow")) {
            holder.question_layout.setVisibility(View.GONE);
            holder.answer_layout.setVisibility(View.GONE);
            holder.Journal_Name_Layout.setVisibility(View.VISIBLE);
            final List<JournalDetailsExpandAbleData> items = (List<JournalDetailsExpandAbleData>) group.getItems();
            holder.Journal_Title.setText(items.get(childIndex).getName());
            holder.Journal_Title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent journals_intetn = new Intent(view.getContext(), JournalDetailsActivity.class);
                    journals_intetn.putExtra("journal_id", items.get(childIndex).getId());
                    view.getContext().startActivity(journals_intetn);
                }
            });
        } else if (group.getTitle().equalsIgnoreCase("My Q’s")) {
            holder.question_layout.setVisibility(View.VISIBLE);
            holder.answer_layout.setVisibility(View.GONE);
            holder.Journal_Name_Layout.setVisibility(View.GONE);
            final List<UserProfileQATabExpandAbleData> items = (List<UserProfileQATabExpandAbleData>) group.getItems();
//            holder.Question_tiltel.setText(items.get(childIndex).getHeading());
            MakeKeywordClickableText(holder.Question_tiltel.getContext(), items.get(childIndex).getHeading(), holder.Question_tiltel);
            holder.Question_tiltel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.Question_tiltel.isClickable() && holder.Question_tiltel.isEnabled()){
                        HomeQAfragmentDataModel model1 = new HomeQAfragmentDataModel();
                        model1.setId(items.get(childIndex).getId());
                        showSubFragmentListner_object.ShowAnswers(model1 , items.get(childIndex).getId(), true);
//                        new android.os.Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                EventBus.getDefault().post(new MessageEvent(true));
//                            }
//                        }, 200);
//                        ((Activity)view.getContext() ).finish();

                    }
                }
            });
            holder.Question_answers_count.setText(items.get(childIndex).getDiscription());
            String givenDateString = items.get(childIndex).getCreated_data();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date mDate = sdf.parse(givenDateString);
                long timeInMilliseconds = mDate.getTime();
//                String agoo = getTimeAgo(timeInMilliseconds);
                holder.Question_time.setText(DateConverter.getPrettyTime(items.get(childIndex).getCreated_data()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.question_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
                    model.setId(items.get(childIndex).getId());
                    showSubFragmentListner_object.ShowQuestions(model , true);
//                    new android.os.Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            EventBus.getDefault().post(new MessageEvent(true));
//                        }
//                    }, 200);
//                    ((Activity)view.getContext() ).finish();
                }
            });
        } else if (group.getTitle().equalsIgnoreCase("My A’s")) {
            final List<UserProfileQATabExpandAbleData> items = (List<UserProfileQATabExpandAbleData>) group.getItems();
//            holder.Answer_tiltel.setText(items.get(childIndex).getHeading());
//            holder.Answer_question.setText(items.get(childIndex).getDiscription());
            MakeKeywordClickableText(holder.Answer_question.getContext(), items.get(childIndex).getDiscription(), holder.Answer_question);
            MakeKeywordClickableText(holder.Answer_tiltel.getContext(), items.get(childIndex).getHeading(), holder.Answer_tiltel);
            holder.Answer_tiltel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.Answer_tiltel.isClickable() && holder.Answer_tiltel.isEnabled()){
                        HomeQAfragmentDataModel model1 = new HomeQAfragmentDataModel();
                        model1.setId(items.get(childIndex).getId());
                        showSubFragmentListner_object.ShowAnswers(model1 , items.get(childIndex).getId(), true);
//                        new android.os.Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                EventBus.getDefault().post(new MessageEvent(true));
//                            }
//                        }, 200);
//                        ((Activity)view.getContext() ).finish();
                    }
                }
            });
            holder.Answer_question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.Answer_question.isClickable() && holder.Answer_question.isEnabled()){
                        HomeQAfragmentDataModel model1 = new HomeQAfragmentDataModel();
                        model1.setId(items.get(childIndex).getId());
                        showSubFragmentListner_object.ShowAnswers(model1 , items.get(childIndex).getId(), true);
//                        new android.os.Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                EventBus.getDefault().post(new MessageEvent(true));
//                            }
//                        }, 200);
//                        ((Activity)view.getContext() ).finish();
                    }
                }
            });
            String givenDateString = items.get(childIndex).getCreated_data();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date mDate = sdf.parse(givenDateString);
                long timeInMilliseconds = mDate.getTime();
//                String agoo = getTimeAgo(timeInMilliseconds);
                holder.Answer_time.setText(DateConverter.getPrettyTime(items.get(childIndex).getCreated_data()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.question_layout.setVisibility(View.GONE);
            holder.answer_layout.setVisibility(View.VISIBLE);
            holder.Journal_Name_Layout.setVisibility(View.GONE);

            holder.answer_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeQAfragmentDataModel model1 = new HomeQAfragmentDataModel();
                    model1.setId(items.get(childIndex).getId());
                    showSubFragmentListner_object.ShowAnswers(model1 , items.get(childIndex).getId(), true);
//                    new android.os.Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            EventBus.getDefault().post(new MessageEvent(true));
//                        }
//                    }, 200);
//                    ((Activity)view.getContext() ).finish();
                }
            });
        }
    }

    @Override
    public void onBindGroupViewHolder(QARecylerGroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGenreTitle(group);
        if (group.getTitle().equalsIgnoreCase("My Journals") || group.getTitle().equalsIgnoreCase("Journals I Follow")) {
            holder.Date_Title.setTextColor(Color.parseColor("#73b241"));
        } else {
            holder.Date_Title.setTextColor(Color.parseColor("#017fc3"));
        }
    }
}
