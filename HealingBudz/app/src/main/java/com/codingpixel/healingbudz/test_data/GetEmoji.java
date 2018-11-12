package com.codingpixel.healingbudz.test_data;

import com.codingpixel.healingbudz.R;

/**
 * Created by jawadali on 9/5/17.
 */

public class GetEmoji {
    public static int[] getEmojis() {
        int[] em = new int[]{
                R.drawable.angry
                , R.drawable.anguished
                , R.drawable.astonished
                , R.drawable.cold_sweat
                , R.drawable.confounded
                , R.drawable.confused
                , R.drawable.cry
                , R.drawable.disappointed_relieved
                , R.drawable.dizzy_face
                , R.drawable.expressionless
                , R.drawable.fearful
                , R.drawable.flushed
                , R.drawable.frowning
                , R.drawable.grin
                , R.drawable.grinning
                , R.drawable.innocent
                , R.drawable.joy
                , R.drawable.joy_cat
                , R.drawable.kissing
                , R.drawable.kissing_closed_eyes
                , R.drawable.kissing_heart
                , R.drawable.laughing
                , R.drawable.mask
                , R.drawable.neutral_face
                , R.drawable.no_mouth
                , R.drawable.pensive
                , R.drawable.persevere
                , R.drawable.rage
                , R.drawable.rolling_eyes
                , R.drawable.scream
                , R.drawable.scream_cat
                , R.drawable.sleeping
                , R.drawable.slight_frown
                , R.drawable.slight_smile
                , R.drawable.smile
                , R.drawable.smile_cat
                , R.drawable.smiley
                , R.drawable.smiling_imp
                , R.drawable.sweat
                , R.drawable.sweat_smile
                , R.drawable.triumph
                , R.drawable.unamused
                , R.drawable.upside_down
                , R.drawable.weary
                , R.drawable.wink

        };
        return em;
    }

    public static String getEmojisName(int position) {
        switch (position) {
            case 0:
                return "angry";
            case 1:
                return "anguished";
            case 2:
                return "astonished";
            case 3:
                return "cold_sweat";
            case 4:
                return "confounded";
            case 5:
                return "confused";
            case 6:
                return "cry";
            case 7:
                return "disappointed_relieved";
            case 8:
                return "dizzy_face";
            case 9:
                return "expressionless";
            case 10:
                return "fearful";
            case 11:
                return "flushed";
            case 12:
                return "frowning";
            case 13:
                return "grin";
            case 14:
                return "grinning";
            case 15:
                return "innocent";
            case 16:
                return "joy";
            case 17:
                return "joy_cat";
            case 18:
                return "kissing";
            case 19:
                return "kissing_closed_eyes";
            case 20:
                return "kissing_heart";
            case 21:
                return "laughing";
            case 22:
                return "mask";
            case 23:
                return "neutral_face";
            case 24:
                return "no_mouth";
            case 25:
                return "pensive";
            case 26:
                return "persevere";
            case 27:
                return "rage";
            case 28:
                return "rolling_eyes";
            case 29:
                return "scream";
            case 30:
                return "scream_cat";
            case 31:
                return "sleeping";
            case 32:
                return "slight_frown";
            case 33:
                return "slight_smile";
            case 34:
                return "smile";
            case 35:
                return "smile_cat";
            case 36:
                return "smiley";
            case 37:
                return "smiling_imp";
            case 38:
                return "sweat";
            case 39:
                return "sweat_smile";
            case 40:
                return "triumph";
            case 41:
                return "unamused";
            case 42:
                return "upside_down";
            case 43:
                return "weary";
            case 44:
                return "wink";
            default:
                return "Happy";

        }
    }
}
