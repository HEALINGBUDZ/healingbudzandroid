package com.codingpixel.healingbudz.activity.home.side_menu.profile.qa_tab;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class QARecylerChildViewHolder extends ChildViewHolder {
  public  View childItemView;
  public LinearLayout question_layout , answer_layout , Journal_Name_Layout;
  public TextView Question_tiltel, Question_time, Question_answers_count;
  public TextView Answer_tiltel, Answer_time, Answer_question , Journal_Title;

  public QARecylerChildViewHolder(View itemView) {
    super(itemView);
    childItemView = itemView;
    question_layout = itemView.findViewById(R.id.question_layout);
    answer_layout = itemView.findViewById(R.id.answer_layout);
    Journal_Name_Layout = itemView.findViewById(R.id.journal_name_item_layout);
    Question_tiltel = itemView.findViewById(R.id.question_title);
    Question_time = itemView.findViewById(R.id.question_time);
    Question_answers_count = itemView.findViewById(R.id.question_answers_count);


      Answer_question = itemView.findViewById(R.id.answer_discription);
      Answer_time = itemView.findViewById(R.id.answer_time);
      Answer_tiltel = itemView.findViewById(R.id.answer_title);
      Journal_Title = itemView.findViewById(R.id.title_main);
  }
}
