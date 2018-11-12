package com.codingpixel.healingbudz.activity.Wall;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.ReportItView.WallTopDropDowns;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.Utilities.Dialogs;
import com.codingpixel.healingbudz.Utilities.FileUtils;
import com.codingpixel.healingbudz.Utilities.Flags;
import com.codingpixel.healingbudz.Utilities.PermissionHandler;
import com.codingpixel.healingbudz.Utilities.SetUserValuesInSP;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.activity.Wall.dialogue.UserLikesAlertDialog;
import com.codingpixel.healingbudz.adapter.BudzFeedMainPostMediaPagerAdapter;
import com.codingpixel.healingbudz.customeUI.CircularImageView;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.HealingBudTextViewBold;
import com.codingpixel.healingbudz.customeUI.ZoomProblemViewPager;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.interfaces.ApiStatusCallBack;
import com.codingpixel.healingbudz.interfaces.RecyclerViewItemClickListener;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.BudzFeedModel.GeneralResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.File;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.Post;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.User;
import com.codingpixel.healingbudz.network.BudzFeedModel.MutePostModel.MutePostResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.ReportModel.FlagPost;
import com.codingpixel.healingbudz.network.BudzFeedModel.ReportModel.ReportPostResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.submitPostLike.Like;
import com.codingpixel.healingbudz.network.BudzFeedModel.submitPostLike.SubmitPostLikeResponse;
import com.codingpixel.healingbudz.network.RestClient;
import com.codingpixel.healingbudz.network.model.URL;
import com.rd.PageIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareCallApi;

/*
 * Created by M_Muzammil Sharif on 08-Mar-18.
 */

public class WallPostPhotoDetailActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, Dialogs.OptionClickListener, View.OnLongClickListener, UserLikesAlertDialog.OnDialogFragmentClickListener {
    private Post post;
    private WallTopDropDowns reportView;
    private com.codingpixel.healingbudz.DataModel.User user;

    private ZoomProblemViewPager pager;
    //private HealingBudTextViewRegular postText;
    private HealingBudTextViewBold likeTxt, likeTxtTemp, commentTxt;
    private HealingBudTextViewBold personName;
    private HealingBudTextViewBold personExtraInfo;
    private CircularImageView personImg;
    private ImageView profile_img_topi;
    private ImageView personLeafImg;
    private PageIndicatorView indicatorView;
    private BudzFeedMainPostMediaPagerAdapter adapter;
    private ImageView likeIcon, download_image;
    private View btnNext, btnPrev;
    private View pagerOverlay;
    LinearLayout liked_dialogue;
    private ProgressDialog dialog;
    static boolean isPhotoLiked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_post_photo_detail);
        user = SetUserValuesInSP.getSavedUser(WallPostPhotoDetailActivity.this);
        if (user == null) {
            onSessionExpire();
            return;
        }
        if (getIntent().getExtras() == null) {
            WallPostPhotoDetailActivity.this.finish();
            return;
        }
        post = null;
        post = (Post) getIntent().getExtras().getSerializable(Constants.POST_EXTRA);
        if (post == null) {
            WallPostPhotoDetailActivity.this.finish();
            return;
        }

        initView();

        setUpUI();
    }

    private void setUpUI() {
        //postText.setText(post.getDescription());
        personName.setText(post.getUser().getFirstName() + " " + post.getUser().getLastName());
        personName.setTextColor(Color.parseColor(Utility.getBudColor(post.getUser().getPoints())));
        personLeafImg.setImageResource(Utility.getBudColorDrawable(post.getUser().getPoints()));
        Glide.with(getApplicationContext()).load(post.getUser().getImagePath()).
                diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.noimage).
                placeholder(R.drawable.ic_user_profile_green).into(personImg);
        if (post.getUser().getSpecial_icon().length() > 5) {
            profile_img_topi.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(post.getUser().getSpecial_icon()).
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    placeholder(R.drawable.topi_ic).into(profile_img_topi);
        } else {
            profile_img_topi.setVisibility(View.GONE);
        }
        personExtraInfo.setText(String.valueOf(post.getUser().getPoints()) + "   |   " + Utility.getBudType(post.getUser().getPoints()));
        personExtraInfo.setTextColor(Color.parseColor(Utility.getBudColor(post.getUser().getPoints())));
        likeTxt.setText(String.valueOf(post.getLikesCount()) + " Likes");
        commentTxt.setText(String.valueOf(post.getCommentsCount()) + " Comments");

        if (post.getFiles() == null || post.getFiles().isEmpty()) {
            findViewById(R.id.activity_wall_post_photo_detail_pager_overlay).setVisibility(View.GONE);
        } else {
            findViewById(R.id.activity_wall_post_photo_detail_pager_overlay).setVisibility(View.VISIBLE);
            adapter = new BudzFeedMainPostMediaPagerAdapter(post.getFiles(), new RecyclerViewItemClickListener() {
                @Override
                public void onItemClick(Object obj, int pos, int type) {
                    File f = (File) obj;
                    if (f != null && f.getType().trim().toLowerCase().equals("video")) {
                        Bundle b = new Bundle();
                        b.putString("path", URL.videos_baseurl + f.getFile());
                        b.putBoolean("isvideo", true);
                        Utility.launchActivity(WallPostPhotoDetailActivity.this, MediPreview.class, false, b);
                    } else {
                        toggleOverlays();
                    }
                }

                @Override
                public boolean onItemLongClick(Object obj, int pos, int type) {
                    return false;
                }
            });
            pager.setAdapter(adapter);
            this.indicatorView.setCount(adapter.getCount());
            if (adapter.getCount() <= 1) {
                btnPrev.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);
            } else {
                btnPrev.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);
            }
            if (getIntent().getExtras() != null) {
                pager.setCurrentItem(getIntent().getExtras().getInt(Constants.POST_FILE_POS, 0), true);
            }
        }

        if (isLiked(post.getLikes())) {
            this.likeIcon.setImageResource(R.drawable.liked_wall);
            this.likeIcon.setColorFilter(ContextCompat.getColor(this, R.color.COLOR_YOUR_COLOR), android.graphics.PorterDuff.Mode.SRC_IN);
//            this.likeTxt.setTextColor(ContextCompat.getColor(WallPostPhotoDetailActivity.this, R.color.feeds_liked_blue_color));
            this.likeTxtTemp.setTextColor(ContextCompat.getColor(WallPostPhotoDetailActivity.this, R.color.feeds_liked_blue_color));
        } else {
            this.likeIcon.setImageResource(R.drawable.un_like_wall);
            this.likeTxt.setTextColor(ContextCompat.getColor(WallPostPhotoDetailActivity.this, R.color.feeds_like_gray_color));
            this.likeIcon.setColorFilter(ContextCompat.getColor(this, R.color.feeds_like_gray_color), android.graphics.PorterDuff.Mode.SRC_IN);
            this.likeTxtTemp.setTextColor(ContextCompat.getColor(WallPostPhotoDetailActivity.this, R.color.feeds_like_gray_color));
        }
        liked_dialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post.getLikes().size() == post.getLikesCount()) {
                    UserLikesAlertDialog userLikesAlertDialog = UserLikesAlertDialog.newInstance(WallPostPhotoDetailActivity.this, post.getLikes());
                    userLikesAlertDialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "pd");
                } else {
                    post.getLikes().remove(post.getLikes().size() - 1);
                    UserLikesAlertDialog userLikesAlertDialog = UserLikesAlertDialog.newInstance(WallPostPhotoDetailActivity.this, post.getLikes());
                    userLikesAlertDialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "pd");

                }
            }
        });
    }

    private void initView() {
        findViewById(R.id.activity_wall_post_photo_detail_like_btn).setOnClickListener(WallPostPhotoDetailActivity.this);
        findViewById(R.id.activity_wall_post_photo_detail_like_btn).setOnLongClickListener(WallPostPhotoDetailActivity.this);
        findViewById(R.id.activity_wall_post_photo_detail_back_btn).setOnClickListener(WallPostPhotoDetailActivity.this);
        findViewById(R.id.activity_wall_post_photo_detail_more_btn).setOnClickListener(WallPostPhotoDetailActivity.this);

        pagerOverlay = findViewById(R.id.activity_wall_post_photo_detail_pager_overlay);
        download_image = findViewById(R.id.download_image);
        liked_dialogue = findViewById(R.id.liked_dialogue);

        btnPrev = findViewById(R.id.activity_wall_post_photo_detail_prev_btn);
        btnNext = findViewById(R.id.activity_wall_post_photo_detail_next_btn);

        btnNext.setOnClickListener(WallPostPhotoDetailActivity.this);
        btnPrev.setOnClickListener(WallPostPhotoDetailActivity.this);

        pager = (ZoomProblemViewPager) findViewById(R.id.activity_wall_post_photo_detail_imgs_pager);
        pager.addOnPageChangeListener(WallPostPhotoDetailActivity.this);
        indicatorView = (PageIndicatorView) findViewById(R.id.activity_wall_post_photo_detail_imgs_pager_indicator);

        indicatorView.setViewPager(pager);

        likeIcon = (ImageView) findViewById(R.id.activity_wall_post_photo_detail_like_icon);
        likeTxt = (HealingBudTextViewBold) findViewById(R.id.activity_wall_post_photo_detail_like_txt);
        likeTxtTemp = (HealingBudTextViewBold) findViewById(R.id.activity_wall_post_photo_detail_like_txt_temp);
        personName = (HealingBudTextViewBold) findViewById(R.id.activity_wall_post_photo_detail_person_name);
        personExtraInfo = (HealingBudTextViewBold) findViewById(R.id.activity_wall_post_photo_detail_person_extra_info);
        personImg = (CircularImageView) findViewById(R.id.activity_wall_post_photo_detail_person_img);
        profile_img_topi = findViewById(R.id.profile_img_topi);
        personLeafImg = (ImageView) findViewById(R.id.activity_wall_post_photo_detail_person_leaf);
        commentTxt = (HealingBudTextViewBold) findViewById(R.id.activity_wall_post_photo_detail_comment_txt);
        //postText = (HealingBudTextViewRegular) findViewById(R.id.activity_wall_post_photo_detail_post_text);

        reportView = new WallTopDropDowns(WallPostPhotoDetailActivity.this);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_wall_post_photo_detail_layout);
        layout.addView(reportView.getView());

        dialog = new ProgressDialog(WallPostPhotoDetailActivity.this);
        dialog.setCancelable(false);
        download_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (urlPath.length() > 2) {
                    PermissionHandler.checkPermission(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, v.getContext(), new PermissionHandler.CheckPermissionResponse() {
                        @Override
                        public void permissionGranted() {
                            FileUtils.StartDownload(urlPath, fileName.replace("/", ""), WallPostPhotoDetailActivity.this);
                        }

                        @Override
                        public void showNeededPermissionDialog() {

                        }

                        @Override
                        public void requestPermission() {

                        }
                    });
                }

            }
        });
    }

    String urlPath = "", fileName = "";

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (post.getFiles().get(position).getFileType() == 0) {
            download_image.setVisibility(View.VISIBLE);
            urlPath = URL.images_baseurl + post.getFiles().get(position).getFile();
            fileName = post.getFiles().get(position).getFile();
        } else {
            download_image.setVisibility(View.GONE);
            urlPath = "";
            fileName = "";
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (post.getFiles().get(position).getFileType() == 0) {
            download_image.setVisibility(View.VISIBLE);
            urlPath = URL.images_baseurl + post.getFiles().get(position).getFile();
            fileName = post.getFiles().get(position).getFile();
        } else {
            download_image.setVisibility(View.GONE);
            urlPath = "";
            fileName = "";
        }
        if (position == 0) {
            btnPrev.setVisibility(View.GONE);
        } else {
            btnPrev.setVisibility(View.VISIBLE);
        }
        if (position < (adapter.getCount() - 1)) {
            btnNext.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void finishActivityAndNotifyPrevious() {
        Bundle b = new Bundle();
        b.putSerializable(Constants.POST_EXTRA, post);
        Utility.finishWithResult(WallPostPhotoDetailActivity.this, b, Flags.NOTIFY);
    }

    private boolean isLiked(List<Like> likes) {
        if (likes == null || likes.isEmpty()) {
            return false;
        }
        for (Like like : likes) {
            if (user.getUser_id() == like.getUserId() && like.getIsLike() > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_wall_post_photo_detail_like_btn: {
                if (!isPhotoLiked) {
                    isPhotoLiked = true;
                    if (isLiked(post.getLikes())) {
                        submitPostLike(post.getId(), 0);
                    } else {
                        submitPostLike(post.getId(), 1);
                    }
                }
            }
            break;
            case R.id.activity_wall_post_photo_detail_back_btn: {
                finishActivityAndNotifyPrevious();
            }
            break;
            case R.id.activity_wall_post_photo_detail_more_btn: {
                Dialogs.showPostMoreDropDownMenu(view, WallPostPhotoDetailActivity.this, post);
            }
            break;
            case R.id.activity_wall_post_photo_detail_prev_btn: {
                if (pager == null) {
                    return;
                }
                if (pager.getCurrentItem() != 0) {
                    pager.setCurrentItem(pager.getCurrentItem() - 1, true);
                } else {
                    btnPrev.setVisibility(View.GONE);
                }
            }
            break;
            case R.id.activity_wall_post_photo_detail_next_btn: {
                if (pager == null) {
                    return;
                }
                if (pager.getCurrentItem() != (adapter.getCount() - 1)) {
                    pager.setCurrentItem((pager.getCurrentItem() + 1), true);
                } else {
                    btnNext.setVisibility(View.GONE);
                }
            }
            break;
        }
    }

    private void submitPostLike(final Integer id, int is_like) {
        if (!Utility.isNetworkAvailable(getApplicationContext())) {
            Dialogs.showNetworkNotAvailibleDialog(WallPostPhotoDetailActivity.this);
            return;
        }
        RestClient.getInstance(getApplicationContext()).getApiService().submitPostLike(user.getSession_key(), id, is_like).enqueue(new Callback<SubmitPostLikeResponse>() {
            @Override
            public void onResponse(Call<SubmitPostLikeResponse> call, final Response<SubmitPostLikeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                        @Override
                        public void success() {
                            isPhotoLiked = false;
                            putLike(response.body().getSuccessData());
                        }

                        @Override
                        public void sessionExpire() {
                            onSessionExpire();
                        }

                        @Override
                        public void knownError(String errorMsg) {
                            CustomeToast.ShowCustomToast(getApplicationContext(), errorMsg, Gravity.TOP);
                        }

                        @Override
                        public void unKnownError() {
                            showUnknownSeverErrorDialog();
                        }
                    });
                } else {
                    showUnknownSeverErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<SubmitPostLikeResponse> call, Throwable t) {
                showUnknownSeverErrorDialog();
            }
        });
    }

    private void showUnknownSeverErrorDialog() {

    }

    private void onSessionExpire() {
        Utility.finishWithResult(WallPostPhotoDetailActivity.this, null, Flags.SESSION_OUT);
    }

    private void putLike(final Like like) {
        WallPostPhotoDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (like.getUserId() == user.getUser_id() && like.getIsLike() > 0) {
                        likeIcon.setImageResource(R.drawable.liked_wall);
                        likeIcon.setColorFilter(ContextCompat.getColor(WallPostPhotoDetailActivity.this, R.color.COLOR_YOUR_COLOR), android.graphics.PorterDuff.Mode.SRC_IN);
//                        likeTxt.setTextColor(ContextCompat.getColor(WallPostPhotoDetailActivity.this, R.color.feeds_liked_blue_color));
                        likeTxtTemp.setTextColor(ContextCompat.getColor(WallPostPhotoDetailActivity.this, R.color.feeds_liked_blue_color));
                    } else {
                        likeIcon.setImageResource(R.drawable.un_like_wall);
                        likeIcon.setColorFilter(ContextCompat.getColor(WallPostPhotoDetailActivity.this, R.color.feeds_like_gray_color), android.graphics.PorterDuff.Mode.SRC_IN);
                        likeTxt.setTextColor(ContextCompat.getColor(WallPostPhotoDetailActivity.this, R.color.feeds_like_gray_color));
                        likeTxtTemp.setTextColor(ContextCompat.getColor(WallPostPhotoDetailActivity.this, R.color.feeds_like_gray_color));
                    }
                    if (post.getLikes() == null || post.getLikes().isEmpty()) {
                        post.setLikes(new ArrayList<Like>());
                        post.getLikes().add(like);
                        post.setLikesCount(post.getLikesCount() + 1);
                        likeTxt.setText(String.valueOf(post.getLikesCount()) + " Likes");
                        return;
                    }
                    for (int j = 0; j < post.getLikes().size(); j++) {
                        if (like.getId() == (post.getLikes().get(j).getId())) {
                            post.getLikes().set(j, like);
                            if (like.getIsLike() > 0) {
                                post.setLikesCount(post.getLikesCount() + 1);
                            } else {
                                post.setLikesCount(post.getLikesCount() - 1);
                            }
                            likeTxt.setText(String.valueOf(post.getLikesCount()) + " Likes");
                            return;
                        }
                    }
                    post.getLikes().add(like);
                    post.setLikesCount(post.getLikesCount() + 1);
                    likeTxt.setText(String.valueOf(post.getLikesCount()) + " Likes");
                } catch (Exception e) {
                    //
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishActivityAndNotifyPrevious();
    }

    @Override
    public void onOptionClick(int pos) {
        switch (pos) {
            case 0: {
                JSONObject data_object = new JSONObject();
                try {
                    data_object.put("id", post.getId());
                    data_object.put("type", "Post");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ShareCallApi(WallPostPhotoDetailActivity.this, data_object);
                Utility.shareString(WallPostPhotoDetailActivity.this, URL.getPostUrl((post).getId()));
            }
            break;
            case 1: {
                flagPost(post);
            }
            break;
            case 2: {
                muteUnmutePost(post);
            }
            break;
            case 3: {
                followUnfollowUser(post.getUser());
            }
            break;
            case 4: {
                Bundle b = new Bundle();
                b.putSerializable(Constants.POST_EXTRA, post);
                b.putInt("type_int", 1);
                Utility.launchActivityForResult(WallPostPhotoDetailActivity.this, WallNewPostActivity.class, b, Flags.ACTIVITY_COMMUNICATION_FOR_UPDATE_FLAG);
            }
            break;
            case 5: {
                new SweetAlertDialog(WallPostPhotoDetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete this post?")
                        .setConfirmText("Yes, delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
//                                                JSONObject jsonObject = new JSONObject();
//                                                new VollyAPICall(MyAnswersActivity.this, false, delete_answer + "/" + main_data.get(position).getId(), jsonObject, user.getSession_key(), Request.Method.GET, MyAnswersActivity.this, APIActions.ApiActions.delete_answer);
//                                                main_data.remove(position);
//                                                recyler_adapter.notifyDataSetChanged();
                                final com.codingpixel.healingbudz.customeUI.ProgressDialog pd = com.codingpixel.healingbudz.customeUI.ProgressDialog.newInstance();
                                pd.show(((FragmentActivity) WallPostPhotoDetailActivity.this).getSupportFragmentManager(), "pd");

                                RestClient.getInstance(WallPostPhotoDetailActivity.this).getApiService().deletePost(user.getSession_key(), post.getId()).
                                        enqueue(new Callback<GeneralResponse>() {
                                            @Override
                                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                                pd.dismiss();
                                                if (response.isSuccessful() || response.body() != null) {
                                                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                                                        @Override
                                                        public void success() {
                                                            post = null;
                                                            finishActivityAndNotifyPreviousDelete();
                                                        }

                                                        @Override
                                                        public void sessionExpire() {
                                                            onSessionExpire();
                                                        }

                                                        @Override
                                                        public void knownError(String errorMsg) {
                                                            CustomeToast.ShowCustomToast(WallPostPhotoDetailActivity.this, errorMsg, Gravity.TOP);
                                                        }

                                                        @Override
                                                        public void unKnownError() {
                                                            showUnknownSeverErrorDialog();
                                                        }
                                                    });
                                                } else {
                                                    showUnknownSeverErrorDialog();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                                                pd.dismiss();
                                                showUnknownSeverErrorDialog();

                                            }
                                        });
                            }
                        })
                        .setCancelText("Close!")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }
            break;
            case 6: {
                Bundle b = new Bundle();
                b.putSerializable(Constants.POST_EXTRA, post);
                b.putInt("type_int", 2);
                Utility.launchActivityForResult(WallPostPhotoDetailActivity.this, WallNewPostActivity.class, b, Flags.ACTIVITY_COMMUNICATION_FOR_UPDATE_FLAG);
            }
            break;
        }
    }

    private void finishActivityAndNotifyPreviousDelete() {
        Utility.finishWithResult(WallPostPhotoDetailActivity.this, null, Flags.NOTIFY_DELETE);
    }

    private void followUnfollowUser(User user) {
        if (!Utility.isNetworkAvailable(WallPostPhotoDetailActivity.this)) {
            Dialogs.showNetworkNotAvailibleDialog(WallPostPhotoDetailActivity.this);
            return;
        }
        RestClient.getInstance(WallPostPhotoDetailActivity.this).getApiService().unFollowUser(WallPostPhotoDetailActivity.this.user.getSession_key(), user.getId()).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful() || response.body() != null) {
                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                        @Override
                        public void success() {
                            Utility.finishWithResult(WallPostPhotoDetailActivity.this, null, Flags.ACTIVITY_COMMUNICATION_FOR_UPDATE_RESULT);
                        }

                        @Override
                        public void sessionExpire() {
                            onSessionExpire();
                        }

                        @Override
                        public void knownError(String errorMsg) {
                            CustomeToast.ShowCustomToast(WallPostPhotoDetailActivity.this, errorMsg, Gravity.TOP);
                        }

                        @Override
                        public void unKnownError() {
                            showUnknownSeverErrorDialog();
                        }
                    });
                } else {
                    showUnknownSeverErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                showUnknownSeverErrorDialog();
            }
        });
    }

    private void muteUnmutePost(final Post post) {
        if (!Utility.isNetworkAvailable(getApplicationContext())) {
            Dialogs.showNetworkNotAvailibleDialog(WallPostPhotoDetailActivity.this);
            return;
        }
        RestClient.getInstance(getApplicationContext()).getApiService().mutePost(user.getSession_key(),
                post.getId(), post.getMutePostByUserCount() > 0 ? 0 : 1).enqueue(new Callback<MutePostResponse>() {
            @Override
            public void onResponse(Call<MutePostResponse> call, final Response<MutePostResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                        @Override
                        public void success() {
                            post.setMutePostByUserCount(response.body().getSuccessData().getIsMute());
                        }

                        @Override
                        public void sessionExpire() {
                            onSessionExpire();
                        }

                        @Override
                        public void knownError(String errorMsg) {
                            CustomeToast.ShowCustomToast(WallPostPhotoDetailActivity.this, errorMsg, Gravity.TOP);
                        }

                        @Override
                        public void unKnownError() {
                            showUnknownSeverErrorDialog();
                        }
                    });
                } else {
                    showUnknownSeverErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<MutePostResponse> call, Throwable t) {
                showUnknownSeverErrorDialog();
            }
        });
    }

    @Override
    public boolean onLongClick(View view) {
        /*if (view.getId() == R.id.activity_wall_post_photo_detail_like_btn) {
            if (!isLiked(post.getLikes())) {
                Dialogs.showPostLikePopUp(view, new Dialogs.OptionClickListener() {
                    @Override
                    public void onOptionClick(int pos) {
                        submitPostLike(post.getId(), pos + 1);
                    }
                });
            }
            return true;
        }*/
        return false;
    }

    private void flagPost(final Post post) {
        if (post.getFlags() != null && !post.getFlags().isEmpty()) {
            for (FlagPost reportPost : post.getFlags()) {
                if (user.getUser_id() == reportPost.getUserId()) {
                    return;
                }
            }
        }
        reportView.showReportPost(post.getId(), new Dialogs.DialogItemClickListener() {
            @Override
            public void onDialogItemClickListener(final DialogInterface dialog, Object obj, int type) {
                if (!Utility.isNetworkAvailable(WallPostPhotoDetailActivity.this)) {
                    Dialogs.showNetworkNotAvailibleDialog(WallPostPhotoDetailActivity.this);
                    return;
                }
                Utility.hideKeyboard(WallPostPhotoDetailActivity.this);
                RestClient.getInstance(WallPostPhotoDetailActivity.this).getApiService().reportPost(user.getSession_key(), post.getId(), (String) obj).enqueue(new Callback<ReportPostResponse>() {
                    @Override
                    public void onResponse(Call<ReportPostResponse> call, final Response<ReportPostResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                                @Override
                                public void success() {
                                    reportView.dismissSlide();
                                    if (post.getFlags() == null) {
                                        post.setFlags(new ArrayList<FlagPost>());
                                    }
                                    post.getFlags().add(response.body().getSuccessData());

                                    CustomeToast.ShowCustomToast(WallPostPhotoDetailActivity.this, "Report Submitted!", Gravity.TOP);
                                }

                                @Override
                                public void sessionExpire() {
                                    reportView.dismissSlide();
                                    onSessionExpire();
                                }

                                @Override
                                public void knownError(String errorMsg) {
                                    if (WallPostPhotoDetailActivity.this != null)
                                        CustomeToast.ShowCustomToast(WallPostPhotoDetailActivity.this, errorMsg, Gravity.TOP);
                                }

                                @Override
                                public void unKnownError() {
                                    showUnknownSeverErrorDialog();
                                }
                            });
                        } else {
                            showUnknownSeverErrorDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportPostResponse> call, Throwable t) {
                        showUnknownSeverErrorDialog();
                    }
                });
            }
        });
    }

    private void toggleOverlays() {
        if (pagerOverlay.getVisibility() == View.VISIBLE) {
//            hideViews();//
            showViews();
        } else {
            showViews();
        }
    }

    private void hideViews() {
        if (pagerOverlay.getVisibility() == View.VISIBLE) {
            pagerOverlay.animate().alpha(0).setDuration(500).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    pagerOverlay.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();
        }
    }

    private void showViews() {
        if (pagerOverlay.getVisibility() == View.GONE) {
            pagerOverlay.animate().alpha(1f).setDuration(500).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    pagerOverlay.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();
        }
    }

    @Override
    public void onCrossListener(UserLikesAlertDialog dialog) {

    }
}
