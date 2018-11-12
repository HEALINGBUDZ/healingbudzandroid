package com.codingpixel.healingbudz.activity.introduction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.side_menu.help_support.TermAndConditionsOnIntorduction;
import com.codingpixel.healingbudz.static_function.IntentFunction;


public class IntoductionFinalFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.intro_activity_six, container, false);

        final Button Get_Started = view.findViewById(R.id.get_start);
        Get_Started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (SharedPrefrences.getBool("is_user_login", getContext())) {
//                    SharedPrefrences.setBool("first_launch_overview_screen", false, view.getContext());
//                    user = getSavedUser(getContext());
//                    GoToHome(getContext(), false);
//                    getActivity().finish();
//                } else {
//                    GoTo(getActivity(), LoginEntrance.class);
//                    getActivity().finish();
//                }
                IntentFunction.GoTo(getActivity(), TermAndConditionsOnIntorduction.class);
                getActivity().finish();

            }
        });
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
}
