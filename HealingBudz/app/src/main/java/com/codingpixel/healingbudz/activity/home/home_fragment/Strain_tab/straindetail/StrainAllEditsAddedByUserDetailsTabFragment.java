package com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;

import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class StrainAllEditsAddedByUserDetailsTabFragment extends Fragment implements View.OnClickListener {
    View Back_view_slide_bar;
    TextView Degree_text;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.strain_all_edite_by_user_fragment_layout, container, false);
        HideKeyboard(getActivity());
        Init(view);
        return view;
    }

    public void Init(View view) {
        final GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, generate_gradient(30));
        gd.setCornerRadius(0f);
        Back_view_slide_bar = view.findViewById(R.id.back_view);
        Back_view_slide_bar.setBackground(gd);
        SeekBar customSeekBar = (SeekBar) view.findViewById(R.id.customSeekBar);
        customSeekBar.setEnabled(false);
        Degree_text = view.findViewById(R.id.degree_text);
        Degree_text.setText("12\"");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_more_ifon_strain:
                GoTo(getContext(), AddMoreInfoAboutStrain.class);
                break;
        }
    }

    public int[] generate_gradient(int progress) {
        if (progress < 15) {
            return new int[]{0xFFffb101, 0xFFf4262d, 0xFFf4262d, 0xFFf4262d, 0xFFf4262d, 0xFFf4262d, 0xFFf4262d};
        } else if (progress >= 15 && progress <= 30) {
            return new int[]{0xFFffb101, 0xFFffb101, 0xFFf4262d, 0xFFf4262d, 0xFFf4262d, 0xFFf4262d, 0xFFf4262d};
        } else if (progress >= 30 && progress <= 45) {
            return new int[]{0xFFffb101, 0xFFffb101, 0xFFffb101, 0xFFf4262d, 0xFFf4262d, 0xFFf4262d, 0xFFf4262d};
        } else if (progress >= 45 && progress <= 60) {
            return new int[]{0xFFffb101, 0xFFffb101, 0xFFffb101, 0xFFffb101, 0xFFf4262d, 0xFFf4262d, 0xFFf4262d};
        } else if (progress >= 60 && progress <= 75) {
            return new int[]{0xFFffb101, 0xFFffb101, 0xFFffb101, 0xFFffb101, 0xFFffb101, 0xFFf4262d, 0xFFf4262d};
        } else if (progress >= 75 && progress <= 90) {
            return new int[]{0xFFffb101, 0xFFffb101, 0xFFffb101, 0xFFffb101, 0xFFffb101, 0xFFffb101, 0xFFf4262d};
        } else if (progress >= 90 && progress <= 100) {
            return new int[]{0xFFffb101, 0xFFffb101, 0xFFffb101, 0xFFffb101, 0xFFffb101, 0xFFffb101, 0xFFf4262d};
        } else {
            return new int[]{0xFFffb101, 0xFFf4262d};
        }
    }
}

