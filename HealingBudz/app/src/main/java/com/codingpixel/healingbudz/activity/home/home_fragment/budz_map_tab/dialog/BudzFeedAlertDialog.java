package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.AddNewBudzMapActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapPaidViewActivity;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.network.model.URL.update_pop_up;

public class BudzFeedAlertDialog extends BaseDialogFragment<BudzFeedAlertDialog.OnDialogFragmentClickListener> {

    static OnDialogFragmentClickListener Listener;
    LinearLayout no_display, check_box_tap;
    CheckBox checked_no_show;
    TextView under_small_val, small_val, main_val;
    Button no_thanks;

    public interface OnDialogFragmentClickListener {
        public void onCountinueFreeListingBtnClink(BudzFeedAlertDialog dialog);

        public void onSubcribeNowBtnClick(BudzFeedAlertDialog dialog);
    }

    public static BudzFeedAlertDialog newInstance(OnDialogFragmentClickListener listner, boolean isSubscribe) {
        BudzFeedAlertDialog frag = new BudzFeedAlertDialog();
        Bundle args = new Bundle();
        args.putBoolean("isSubscribe", isSubscribe);
        frag.setArguments(args);
        Listener = listner;
        return frag;
    }

    public static BudzFeedAlertDialog newInstance(OnDialogFragmentClickListener listner, boolean isSubscribe, Bundle isFromMain) {
        BudzFeedAlertDialog frag = new BudzFeedAlertDialog();
        Bundle args = new Bundle();
        args.putBoolean("isSubscribe", isSubscribe);
        args.putBoolean("isFromMain", isSubscribe);
        isFromMain.putBoolean("isSubscribe", isSubscribe);
        frag.setArguments(isFromMain);
        Listener = listner;
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.budz_map_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
//        Bitmap map=takeScreenShot(getActivity());
//        Bitmap fast=fastblur(map, 10);
//        final Drawable draw=new BitmapDrawable(getResources(),fast);
//        dialog.getWindow().setBackgroundDrawable(draw);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
//        int top_y = getResources().getDisplayMetrics().heightPixels / 5;
//        wmlp.y = top_y;   //y position
        Bundle bundle = getArguments();

        Button Learn_more = main_dialog.findViewById(R.id.learn_more_btn);
        no_thanks = main_dialog.findViewById(R.id.no_thanks);
        //under_small_val, small_val, main_val
        under_small_val = main_dialog.findViewById(R.id.under_small_val);
        small_val = main_dialog.findViewById(R.id.small_val);
        main_val = main_dialog.findViewById(R.id.main_val);
        if (getArguments().getBoolean("isSubscribe")) {
            main_val.setText(bundle.getString("main_val"));
            small_val.setText(bundle.getString("small_val"));
            under_small_val.setText(bundle.getString("under_small_val"));
        }
        checked_no_show = main_dialog.findViewById(R.id.checked_no_show);
        no_display = main_dialog.findViewById(R.id.no_display);
        check_box_tap = main_dialog.findViewById(R.id.check_box_tap);
        check_box_tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked_no_show.isChecked()) {
                    checked_no_show.setChecked(false);
                } else {
                    checked_no_show.setChecked(true);
                }
            }
        });
        if (user.isShow_budz_popup()) {
            checked_no_show.setChecked(false);
        } else {
            checked_no_show.setChecked(true);
        }
        if (bundle.containsKey("isFromMain")) {
            if (bundle.getBoolean("isFromMain")) {
                no_display.setVisibility(View.VISIBLE);
            } else {
                no_display.setVisibility(View.GONE);
            }
        } else {
            no_display.setVisibility(View.GONE);
        }

        no_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    if (checked_no_show.isChecked()) {
                        jsonObject.put("show_budz_popup", 0);
                        user.setShow_budz_popup(false);
                    } else {
                        jsonObject.put("show_budz_popup", 1);
                        user.setShow_budz_popup(true);
                    }


                    new VollyAPICall(getActivity(), false, update_pop_up, jsonObject, user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                        @Override
                        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {

                        }

                        @Override
                        public void onRequestError(String response, APIActions.ApiActions apiActions) {

                        }
                    }, APIActions.ApiActions.update_pop_up);
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        final ImageView premium_tag = main_dialog.findViewById(R.id.premium_tag);

        final LinearLayout FirstDialogLayout = main_dialog.findViewById(R.id.first_dialog_layout);
        final LinearLayout SecondDialogLayout = main_dialog.findViewById(R.id.second_dialog_layout);
        Learn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(view.getContext(), BudzMapPaidViewActivity.class);
                startActivity(intent);
//                premium_tag.setVisibility(View.VISIBLE);
////                main_layout.setBackgroundResource(R.drawable.ic_budz_map_premium_dialog_bg);
//                FirstDialogLayout.setVisibility(View.GONE);
//                SecondDialogLayout.setVisibility(View.VISIBLE);
            }
        });
        ImageView Cross_btn = main_dialog.findViewById(R.id.cross_btn);
        Cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        final Button Countinue_with_fre_listing = main_dialog.findViewById(R.id.countinue_with_free_listing);
        Countinue_with_fre_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Listener.onCountinueFreeListingBtnClink(BudzFeedAlertDialog.this);
            }
        });

        final Button sbcsribe_now = main_dialog.findViewById(R.id.sbcsribe_now);
        sbcsribe_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Listener.onSubcribeNowBtnClick(BudzFeedAlertDialog.this);
            }
        });
        if (getArguments().getBoolean("isSubscribe")) {
            premium_tag.setVisibility(View.VISIBLE);
            FirstDialogLayout.setVisibility(View.GONE);
            SecondDialogLayout.setVisibility(View.VISIBLE);
            sbcsribe_now.setVisibility(View.GONE);
            no_display.setVisibility(View.GONE);
        }
        dialog.setView(main_dialog);
        return dialog;
    }

    private static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


}