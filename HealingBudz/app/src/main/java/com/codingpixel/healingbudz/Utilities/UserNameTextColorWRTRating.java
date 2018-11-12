package com.codingpixel.healingbudz.Utilities;

import android.graphics.Color;

import com.codingpixel.healingbudz.R;

/**
 * Created by jawadali on 12/27/17.
 */

public class UserNameTextColorWRTRating {
    public static int getUserRatingColor(int rating) {
        if (rating >= 0 && rating < 100) {
            return Color.parseColor("#dedede");
        } else if (rating >= 100 && rating < 200) {
            return Color.parseColor("#73ae44");
        } else if (rating >= 200 && rating < 300) {
            return Color.parseColor("#f3c330");
        } else if (rating >= 300 && rating < 400) {
            return Color.parseColor("#df910b");
        } else if (rating >= 400) {
            return Color.parseColor("#cb6acc");
        }else {
            return Color.parseColor("#dedede");
        }
    }

    public static int getUserRatingImage(int rating) {
        if (rating >= 0 && rating < 100) {
            return R.drawable.ic_user_profile_rating_99;
        } else if (rating >= 100 && rating < 200) {
            return R.drawable.ic_user_profile_rating_199;
        } else if (rating >= 200 && rating < 300) {
            return R.drawable.ic_user_profile_rating_299;
        } else if (rating >= 300 && rating < 400) {
            return R.drawable.ic_user_profile_rating_399;
        } else if (rating >= 400) {
            return R.drawable.ic_user_profile_rating_400_plus;
        }else {
            return R.drawable.ic_user_profile_rating_99;
        }
    }
}
