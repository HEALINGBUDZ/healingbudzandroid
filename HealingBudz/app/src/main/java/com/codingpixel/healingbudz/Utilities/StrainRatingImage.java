package com.codingpixel.healingbudz.Utilities;

import com.codingpixel.healingbudz.R;

/**
 * Created by jawadali on 11/14/17.
 */

public class StrainRatingImage {
    public static int Strain_Rating(double rating) {
        if (rating <= 0) {
            return R.drawable.ic_strain_rating_zero;
        } else if (rating > 0 && rating <= 1.5) {
            return R.drawable.ic_strain_rating_one;
        } else if (rating > 1.5 && rating <= 2.5) {
            return R.drawable.ic_strain_rating_two;
        } else if (rating > 2.5 && rating <= 3.5) {
            return R.drawable.ic_strain_rating_three;
        } else if (rating > 3.5 && rating <= 4.5) {
            return R.drawable.ic_strain_rating_four;
        } else {
            return R.drawable.ic_strain_rating_five;
        }
    }

    public static int Strain_Rating_digit(double rating) {
        if (rating <= 0) {
            return 0;
        } else if (rating > 0 && rating <= 1.5) {
            return 1;
        } else if (rating > 1.5 && rating <= 2.5) {
            return 2;
        } else if (rating > 2.5 && rating <= 3.5) {
            return 3;
        } else if (rating > 3.5 && rating <= 4.5) {
            return 4;
        } else {
            return 5;
        }
    }
}
