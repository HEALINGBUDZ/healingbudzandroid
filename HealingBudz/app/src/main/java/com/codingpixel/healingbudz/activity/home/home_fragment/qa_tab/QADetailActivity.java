package com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.DataModel.QuestionAnswersDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.side_menu.my_answers.EditAnswerFragment;
import com.codingpixel.healingbudz.interfaces.QAAddSubFragmentListner;

import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

public class QADetailActivity extends AppCompatActivity implements QAAddSubFragmentListner {
    public HomeQAfragmentDataModel dataModel;
    DiscussQuestionFragment discussQuestionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qadetail);
        ChangeStatusBarColor(QADetailActivity.this, "#171717");
        dataModel = (HomeQAfragmentDataModel) getIntent().getExtras().getSerializable("object");
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("Question")) {
            isFromQuestion(bundle.getBoolean("Question"));
        } else if (bundle.containsKey("Answer")) {
            isFromAnswer(bundle.getBoolean("Answer"), bundle.getInt("Answerid"));
        } else {
            isFromMain();
        }
    }

    public void isFromQuestion(boolean isOnlyId) {
        if (isOnlyId) {
            discussQuestionFragment = new DiscussQuestionFragment(this, true, QADetailActivity.this.dataModel.getId());
            discussQuestionFragment.dataModel = QADetailActivity.this.dataModel;
        } else {
            discussQuestionFragment = new DiscussQuestionFragment(this, false);
            discussQuestionFragment.dataModel = QADetailActivity.this.dataModel;
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.main_fragment, discussQuestionFragment, "1");
        transaction.commitAllowingStateLoss();
    }

    public void isFromMain() {
        discussQuestionFragment = new DiscussQuestionFragment(this);
        discussQuestionFragment.dataModel = QADetailActivity.this.dataModel;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.main_fragment, discussQuestionFragment, "1");
        transaction.commitAllowingStateLoss();
    }

    public void isFromAnswer(boolean isOnlyId, int Answerid) {
        if (isOnlyId) {
            discussQuestionFragment = new DiscussQuestionFragment(this, false, true, Answerid, true);
            discussQuestionFragment.dataModel = QADetailActivity.this.dataModel;
        } else {
            discussQuestionFragment = new DiscussQuestionFragment(this, false, true, Answerid + "");
            discussQuestionFragment.dataModel = QADetailActivity.this.dataModel;
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.main_fragment, discussQuestionFragment, "1");
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void AddFirstAnswerBudFragment(HomeQAfragmentDataModel dataModel) {

    }

    @Override
    public void AddDiscussAnswerFragment(HomeQAfragmentDataModel dataModel) {

    }

    @Override
    public void AddReplyAnswerFragment(HomeQAfragmentDataModel dataModel) {
        DiscussQuestionFragment.isRefreshable = true;
        AnswerFragment answerFragment = new AnswerFragment();
        answerFragment.dataModel = dataModel;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.main_fragment, answerFragment, "1");
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void EditReplyAnswerFragment(HomeQAfragmentDataModel dataModel, QuestionAnswersDataModel answersDataModel) {
        DiscussQuestionFragment.isRefreshable = true;
        EditAnswerFragment answerFragment = new EditAnswerFragment();
        answerFragment.dataModel = answersDataModel;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.main_fragment, answerFragment, "1");
        transaction.commitAllowingStateLoss();
    }
}
