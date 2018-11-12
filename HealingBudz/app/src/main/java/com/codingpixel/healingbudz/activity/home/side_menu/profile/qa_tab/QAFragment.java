package com.codingpixel.healingbudz.activity.home.side_menu.profile.qa_tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codingpixel.healingbudz.DataModel.UserProfileQATabDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.UserProfileQATabRecylerAdapter;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;

import java.util.List;

public class QAFragment extends Fragment {
    RecyclerView recyclerView;
    List<UserProfileQATabDataModel> question_answers;

    public QAFragment(List<UserProfileQATabDataModel> question_answers) {
        this.question_answers = question_answers;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_profile_qa_tab_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyler_View);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        UserProfileQATabRecylerAdapter adapter = new UserProfileQATabRecylerAdapter(question_answers);
        adapter.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(int flatPos) {
                return true;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        for (int x = 0; x < adapter.getGroups().size(); x++) {
            if (!adapter.isGroupExpanded(adapter.getGroups().get(x))) {
                adapter.toggleGroup(adapter.getGroups().get(x));
            }
        }
        recyclerView.setNestedScrollingEnabled(false);
        if (question_answers.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            view.findViewById(R.id.not_found).setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            view.findViewById(R.id.not_found).setVisibility(View.GONE);
        }
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
}
