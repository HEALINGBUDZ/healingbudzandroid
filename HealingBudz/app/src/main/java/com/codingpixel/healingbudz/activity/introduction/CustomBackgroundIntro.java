package com.codingpixel.healingbudz.activity.introduction;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.codingpixel.healingbudz.static_function.UIModification;
import com.github.paolorotolo.appintro.AppIntro;


/**
 * Created by andrew on 11/17/16.
 */

public class CustomBackgroundIntro extends AppIntro {
    IntoductionFinalFragment intoductionFinalFragment = new IntoductionFinalFragment();
    WelcomeFragment welcomeFragment = new WelcomeFragment();
    StrainFragment strainFragment = new StrainFragment();
    QAFragment qaFragment = new QAFragment();
    BuzzIntoFragment buzzIntoFragment = new BuzzIntoFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIModification.FullScreen(this);

        setIndicatorColor(Color.parseColor("#FFFFFF"), Color.parseColor("#c2000000"));
//        setProgressIndicator();
        setBackButtonVisibilityWithDone(false);
        setColorDoneText(Color.parseColor("#00000000"));
        setColorSkipButton(Color.parseColor("#00000000"));
        setNextArrowColor(Color.parseColor("#00000000"));
//
//        SliderPage sliderPage2 = new SliderPage();
//        sliderPage2.setTitle("App Intros");
//        sliderPage2.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
//        sliderPage2.setImageDrawable(R.drawable.icon_main);
//        sliderPage2.setBgColor(Color.TRANSPARENT);
//        sliderPage2.setDescColor(Color.parseColor("#000000"));
//        sliderPage2.setTitleColor(Color.parseColor("#000000"));
//
//        addSlide(AppIntroFragment.newInstance(sliderPage2));
//
//        SliderPage sliderPage3 = new SliderPage();
//        sliderPage3.setTitle("Simple, yet Customizable");
//        sliderPage3.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
//        sliderPage3.setImageDrawable(R.drawable.icon_main);
//        sliderPage3.setBgColor(Color.TRANSPARENT);
//        sliderPage3.setDescColor(Color.parseColor("#000000"));
//        sliderPage3.setTitleColor(Color.parseColor("#000000"));
//
//        addSlide(AppIntroFragment.newInstance(sliderPage3));
//
//        SliderPage sliderPage4 = new SliderPage();
//        sliderPage4.setTitle("Explore");
//        sliderPage4.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
//        sliderPage4.setImageDrawable(R.drawable.icon_main);
//        sliderPage4.setBgColor(Color.TRANSPARENT);
//        sliderPage4.setDescColor(Color.parseColor("#000000"));
//        sliderPage4.setTitleColor(Color.parseColor("#000000"));
//
//        addSlide(AppIntroFragment.newInstance(sliderPage4));
//        addSlide(FragmentIntroContent.newInstance("Welcome!"
//                , "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
//                R.drawable.icon_main,
//                Color.TRANSPARENT));
//        addSlide(FragmentIntroContent.newInstance("App Intros"
//                , "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
//                R.drawable.icon_main,
//                Color.TRANSPARENT));
//
//        addSlide(FragmentIntroContent.newInstance("Simple, yet Customizable"
//                , "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
//                R.drawable.icon_main,
//                Color.TRANSPARENT));
//
        addSlide(welcomeFragment);
        addSlide(buzzIntoFragment);
        addSlide(qaFragment);
        addSlide(strainFragment);
        addSlide(intoductionFinalFragment);

        setFadeAnimation();

        setGoBackLock(true);

    }

    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
//        startActivity(new Intent(this, LoginActivity.class));
//        finish();
    }

    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
//        startActivity(new Intent(this, LoginActivity.class));
//        finish();
    }


}
