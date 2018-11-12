package com.codingpixel.healingbudz.Utilities;
/*
 * Created by M_Muzammil Sharif on 07-Mar-18.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.User;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.Wall.WallPostDetailActivity;
import com.codingpixel.healingbudz.activity.Wall.WallPostPhotoDetailActivity;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.SimpleToolTip.SimpleTooltip;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.network.BudzFeedModel.AddComment.Comment;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.Post;
import com.codingpixel.healingbudz.network.BudzFeedModel.ReportModel.FlagPost;
import com.codingpixel.healingbudz.network.BudzFeedModel.SubUsers.SubUser;

import java.util.List;

public class Dialogs {
    public interface DialogItemClickListener {
        public void onDialogItemClickListener(DialogInterface dialog, Object obj, int type);
    }

    public interface DialogItemLongClickListener {
        public boolean onDialogItemLongClickListener(DialogInterface dialog, Object obj, int type);
    }

    public interface OptionClickListener {
        void onOptionClick(int pos);
    }

    public static void showNetworkNotAvailibleDialog(Context context) {
        if (Utility.DO_SHOW_NETWORK_NOT_AWAILIBLE_DIALOGS && context != null) {
            new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText(context.getString(R.string.network_error_title))
                    .setContentText(context.getString(R.string.network_not_connected_error))
                    .setCustomImage(R.mipmap.ic_app_logo)
                    .show();

//                    new AlertDialog.Builder(context).setIcon(R.mipmap.ic_app_logo).setCancelable(true).
//                            setTitle(R.string.network_error_title).
//                            setMessage(R.string.network_not_connected_error).create().show();
        }
    }

    public static void showCommentMoreDropDownMenu(@NonNull View view, @NonNull final OptionClickListener clickListener, Comment post) {
        User user = SetUserValuesInSP.getSavedUser(view.getContext());

        final SimpleTooltip tooltip = new SimpleTooltip.Builder(view.getContext())
                .anchorView(view)
                .arrowDrawable(null)
                .text("Sample Text")
                .showArrow(false)
                .gravity(Gravity.BOTTOM)
                .dismissOnOutsideTouch(false)
                .dismissOnInsideTouch(false)
                .modal(true)
                .contentView(R.layout.budz_feed_comment_post_more_dropdown_menu)
                .focusable(true)
                .build();


        final int edit = R.id.budz_feed_main_post_dropdown_opt5;
        final int delete = R.id.budz_feed_main_post_dropdown_opt6;
        final int liked = R.id.likes_by;


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case edit: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(4);
                    }
                    break;
                    case delete: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(5);
                    }
                    break;
                    case liked: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(32);
                    }
                    break;

                }
            }
        };


        tooltip.findViewById(R.id.budz_feed_main_post_dropdown_opt5).setOnClickListener(listener);
        tooltip.findViewById(R.id.budz_feed_main_post_dropdown_opt6).setOnClickListener(listener);
        tooltip.findViewById(R.id.likes_by).setOnClickListener(listener);

        tooltip.show();
    }

    public static void showPostMoreDropDownMenu(@NonNull final View view, @NonNull final OptionClickListener clickListener, final Post post) {
        final User user = SetUserValuesInSP.getSavedUser(view.getContext());

        final SimpleTooltip tooltip = new SimpleTooltip.Builder(view.getContext())
                .anchorView(view)
                .arrowDrawable(null)
                .text("Sample Text")
                .showArrow(false)
                .gravity(Gravity.BOTTOM)
                .dismissOnOutsideTouch(false)
                .dismissOnInsideTouch(false)
                .modal(true)
                .contentView(R.layout.budz_feed_main_post_more_dropdown_menu)
                .focusable(true)
                .build();

        final int share = R.id.budz_feed_main_post_dropdown_opt1;
        final int likes_by = R.id.likes_by;
        final int flag_report = R.id.budz_feed_main_post_dropdown_opt2;
        final int mute = R.id.budz_feed_main_post_dropdown_opt3;
        final int unfollow = R.id.budz_feed_main_post_dropdown_opt4;
        final int edit = R.id.budz_feed_main_post_dropdown_opt5;
        final int delete = R.id.budz_feed_main_post_dropdown_opt6;
        final int repost = R.id.budz_feed_main_post_dropdown_opt7;

        ImageView muteImg = tooltip.findViewById(R.id.budz_feed_main_post_dropdown_mute_img);
        ImageView flagImg = tooltip.findViewById(R.id.budz_feed_main_post_dropdown_flag_img);
        ImageView followImg = tooltip.findViewById(R.id.budz_feed_main_post_dropdown_follow_img);
        TextView muteTxt = tooltip.findViewById(R.id.budz_feed_main_post_dropdown_mute);
        TextView followTxt = tooltip.findViewById(R.id.budz_feed_main_post_dropdown_follow_txt);
        TextView flagTxt = tooltip.findViewById(R.id.budz_feed_main_post_dropdown_flag_txt);

        flagImg.setImageResource(R.drawable.ic_flag_budz_feed_main_post);
        flagTxt.setTextColor(Color.parseColor("#000000"));

        if (post.getFlags() != null && !post.getFlags().isEmpty()) {
            for (FlagPost flagPost : post.getFlags()) {
                if (Splash.user.getUser_id() == flagPost.getUserId()) {
                    flagImg.setImageResource(R.drawable.ic_flag_budz_feed_menu_blue);
                    flagImg.setColorFilter(ContextCompat.getColor(flagImg.getContext(), R.color.COLOR_YOUR_COLOR), android.graphics.PorterDuff.Mode.SRC_IN);
                    flagTxt.setTextColor(ContextCompat.getColor(view.getContext(), R.color.feeds_liked_blue_color));
                    break;
                }
            }
        }

        if (post.getMutePostByUserCount() > 0) {
            muteTxt.setText("Unmute this post");
            muteImg.setImageResource(R.drawable.ic_unmute_post);
        } else {
            muteTxt.setText("Mute this post");
            muteImg.setImageResource(R.drawable.mute_post_icon);
        }
        if (user != null) {
            if (user.getUser_id() == post.getUserId()) {
                tooltip.findViewById(unfollow).setVisibility(View.GONE);
                tooltip.findViewById(flag_report).setVisibility(View.GONE);

                if (post.getSharedId() > 0 || post.getSharedUserId() > 0 || post.getAllowRepost() == 0) {
                    tooltip.findViewById(repost).setVisibility(View.GONE);
                } else {
                    tooltip.findViewById(repost).setVisibility(View.GONE);
                }
                tooltip.findViewById(delete).setVisibility(View.VISIBLE);
//                if (post.getCommentsCount() > 0 || post.getFlagedCount() > 0 || post.getSharedId() > 0 || post.getSharedUserId() > 0) {
//                    tooltip.findViewById(edit).setVisibility(View.GONE);
//                } else {
//                    tooltip.findViewById(edit).setVisibility(View.VISIBLE);
//                }
                if (post.getFlagedCount() > 0) {
                    tooltip.findViewById(edit).setVisibility(View.GONE);
                } else {
                    tooltip.findViewById(edit).setVisibility(View.VISIBLE);
                }
            } else {
                if (post.getSharedId() > 0 || post.getSharedUserId() > 0 || post.getAllowRepost() == 0) {
                    tooltip.findViewById(repost).setVisibility(View.GONE);
                } else {
                    tooltip.findViewById(repost).setVisibility(View.GONE);
                }


                tooltip.findViewById(edit).setVisibility(View.GONE);
                tooltip.findViewById(delete).setVisibility(View.GONE);
                tooltip.findViewById(unfollow).setVisibility(View.GONE);
                tooltip.findViewById(flag_report).setVisibility(View.VISIBLE);
            }
        }
        if (clickListener instanceof WallPostDetailActivity || clickListener instanceof WallPostPhotoDetailActivity){
            tooltip.findViewById(likes_by).setVisibility(View.GONE);

        }
        tooltip.findViewById(unfollow).setVisibility(View.GONE);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case share: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(0);
                    }
                    break;
                    case flag_report: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(1);
                    }
                    break;
                    case mute: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(2);
                    }
                    break;
                    case unfollow: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(3);
                    }
                    break;
                    case edit: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(4);
                    }
                    break;
                    case delete: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(5);
                    }
                    break;
                    case repost: {
                        tooltip.dismiss();
                        if (user.getUser_id() != post.getUserId()) {
                            clickListener.onOptionClick(6);
                        } else {
                            CustomeToast.ShowCustomToast(view.getContext(), "You can't repost your own post!", Gravity.TOP);
                        }
                    }
                    case likes_by: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(23);
                    }
                    break;
                }
            }
        };
        tooltip.findViewById(R.id.budz_feed_main_post_dropdown_opt1).setOnClickListener(listener);
        tooltip.findViewById(R.id.budz_feed_main_post_dropdown_opt2).setOnClickListener(listener);
        tooltip.findViewById(R.id.budz_feed_main_post_dropdown_opt3).setOnClickListener(listener);
        tooltip.findViewById(R.id.budz_feed_main_post_dropdown_opt4).setOnClickListener(listener);
        tooltip.findViewById(R.id.budz_feed_main_post_dropdown_opt5).setOnClickListener(listener);
        tooltip.findViewById(R.id.budz_feed_main_post_dropdown_opt6).setOnClickListener(listener);
        tooltip.findViewById(R.id.budz_feed_main_post_dropdown_opt7).setOnClickListener(listener);
        tooltip.findViewById(R.id.likes_by).setOnClickListener(listener);
        tooltip.show();
    }

    public static void showPostAsDropDownMenu(@NonNull final View view, @NonNull final DialogItemClickListener clickListener, List<SubUser> users) {
        User user = SetUserValuesInSP.getSavedUser(view.getContext());

        if (users == null || users.isEmpty()) {
            return;
        }
//        SubUser s = new SubUser();
//        s.setTitle(user.getFirst_name());
//        SubUser del = users.remove(0);
//        users.add(0, s);
//        users.add(del);
        final SimpleTooltip tooltip = new SimpleTooltip.Builder(view.getContext())
                .anchorView(view)
                .arrowDrawable(null)
                .text("Sample Text")
                .showArrow(false)
                .gravity(Gravity.BOTTOM)
                .dismissOnOutsideTouch(false)
                .dismissOnInsideTouch(false)
                .modal(true)
                .contentView(R.layout.spinner_like_dropdown)
                .focusable(true)
                .build();

        final RecyclerView recyclerView = tooltip.findViewById(R.id.spinner_like_dropdown_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        DropDownAdapter adapter = new DropDownAdapter(users, new DialogItemClickListener() {
            @Override
            public void onDialogItemClickListener(DialogInterface dialog, Object obj, int type) {
                tooltip.dismiss();
                clickListener.onDialogItemClickListener(null, obj, type);
            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                int h = Utility.convertDpToPixel(200, view.getContext());
                if (recyclerView.getMeasuredHeight() >= h) {
                    recyclerView.getLayoutParams().height = h;
                }
            }
        });
        tooltip.show();
    }

    public static void showPostLikePopUp(@NonNull View view, @NonNull final OptionClickListener clickListener) {
        final SimpleTooltip tooltip = new SimpleTooltip.Builder(view.getContext())
                .anchorView(view)
                .arrowDrawable(null)
                .text("Sample Text")
                .showArrow(false)
                .gravity(Gravity.TOP)
                .dismissOnOutsideTouch(false)
                .dismissOnInsideTouch(false)
                .modal(true)
                .contentView(R.layout.popup_like_post)
                .focusable(true)
                .build();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.popup_like_post_1: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(0);
                    }
                    break;
                    case R.id.popup_like_post_2: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(1);
                    }
                    break;
                    case R.id.popup_like_post_3: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(2);
                    }
                    break;
                    case R.id.popup_like_post_4: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(3);
                    }
                    break;
                    case R.id.popup_like_post_5: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(4);
                    }
                    break;
                    case R.id.popup_like_post_6: {
                        tooltip.dismiss();
                        clickListener.onOptionClick(5);
                    }
                    break;
                }
            }
        };
        tooltip.findViewById(R.id.popup_like_post_1).setOnClickListener(listener);
        tooltip.findViewById(R.id.popup_like_post_2).setOnClickListener(listener);
        tooltip.findViewById(R.id.popup_like_post_3).setOnClickListener(listener);
        tooltip.findViewById(R.id.popup_like_post_4).setOnClickListener(listener);
        tooltip.findViewById(R.id.popup_like_post_5).setOnClickListener(listener);
        tooltip.findViewById(R.id.popup_like_post_6).setOnClickListener(listener);
        tooltip.show();
    }

    public static void showPostFlagReportDialog(@NonNull Context context, @NonNull final DialogItemClickListener clickListener) {
        if (context == null) {
            return;
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialog.setContentView(R.layout.wall_post_flag_dialog);

        final EditText editText = dialog.findViewById(R.id.wall_post_flag_dialog_reason);

        dialog.findViewById(R.id.wall_post_flag_dialog_cencel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onDialogItemClickListener(dialog, null, -1);
            }
        });

        dialog.findViewById(R.id.wall_post_flag_dialog_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().trim().isEmpty()) {
                    CustomeToast.ShowCustomToast(dialog.getContext(), "Please provide reason before submit report!", Gravity.TOP);
                    return;
                }
                clickListener.onDialogItemClickListener(dialog, editText.getText().toString().trim(), 1);
            }
        });

        dialog.show();
    }

    public static void showAppSettingDialog(@NonNull Context context, @NonNull String title, @NonNull String text, @NonNull final Dialogs.DialogItemClickListener clickListener) {
        if (context != null) {
            new AlertDialog.Builder(context).setIcon(R.mipmap.ic_app_logo).
                    setMessage(title).setMessage(text).setCancelable(true).setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    clickListener.onDialogItemClickListener(dialogInterface, null, 1);
                }
            }).create().show();
        }
    }

    public static void showAppSettingDialog(@NonNull Context context, @StringRes int title, @StringRes int text, @NonNull final Dialogs.DialogItemClickListener clickListener) {
        if (context != null) {
            new AlertDialog.Builder(context).setIcon(R.mipmap.ic_app_logo).
                    setMessage(title).setMessage(text).setCancelable(true).setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    clickListener.onDialogItemClickListener(dialogInterface, null, 1);
                }
            }).create().show();
        }
    }

    public static void showDeleteDialog(@NonNull Context context, @NonNull final Post post, @NonNull final DialogItemClickListener clickListener) {
        if (context == null) {
            return;
        }
        new AlertDialog.Builder(context).setIcon(R.mipmap.ic_app_logo).setCancelable(true).
                setTitle("Confirm!").setMessage("Are you sure you wanted to delete this post?").
                setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickListener.onDialogItemClickListener(dialogInterface, post, -1);
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clickListener.onDialogItemClickListener(dialogInterface, post, 1);
            }
        }).create().show();
    }

    private static class DropDownAdapter extends RecyclerView.Adapter<DropDownAdapter.VH> {
        private List<SubUser> users;
        private DialogItemClickListener dialogItemClickListener;

        public DropDownAdapter(List<SubUser> users, DialogItemClickListener clickListener) {
            this.users = users;
            dialogItemClickListener = clickListener;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, Utility.convertDpToPixel(20, parent.getContext())));
            textView.setPadding(Utility.convertDpToPixel(4, parent.getContext())
                    , Utility.convertDpToPixel(0, parent.getContext()),
                    Utility.convertDpToPixel(4, parent.getContext())
                    , Utility.convertDpToPixel(0, parent.getContext()));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setMaxLines(1);
            textView.setSingleLine(true);
            textView.setMaxWidth(Utility.convertDpToPixel(15, parent.getContext()));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textView.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.new_post_dropdown_text_color));

            return new VH(textView);
        }

        @Override
        public void onBindViewHolder(VH holder, final int position) {
            final int pos = position;
            ((TextView) holder.itemView).setText(users.get(pos).getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogItemClickListener.onDialogItemClickListener(null, users.get(pos), pos);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (users == null) {
                return 0;
            }
            return users.size();
        }

        class VH extends RecyclerView.ViewHolder {
            VH(View itemView) {
                super(itemView);
            }
        }
    }
}
