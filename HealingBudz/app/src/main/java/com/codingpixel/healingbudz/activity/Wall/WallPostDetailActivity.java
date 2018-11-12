package com.codingpixel.healingbudz.activity.Wall;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Layout;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.User;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.ReportItView.WallTopDropDowns;
import com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.Utilities.Dialogs;
import com.codingpixel.healingbudz.Utilities.Flags;
import com.codingpixel.healingbudz.Utilities.KeywordClickDialog;
import com.codingpixel.healingbudz.Utilities.SetUserValuesInSP;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.Wall.dialogue.UserLikesAlertDialog;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.BudzFeedMainPostMediaPagerAdapter;
import com.codingpixel.healingbudz.adapter.BudzFeedPostDetailCommentsAdapter;
import com.codingpixel.healingbudz.adapter.MentionAdapter;
import com.codingpixel.healingbudz.adapter.WallTagPeopleAdapter;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CircularImageView;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.FontCache;
import com.codingpixel.healingbudz.customeUI.HealingBudTextViewBold;
import com.codingpixel.healingbudz.customeUI.HealingBudTextViewItalic;
import com.codingpixel.healingbudz.customeUI.Preview;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ApiStatusCallBack;
import com.codingpixel.healingbudz.interfaces.AutoLinkTextLinkClickListener;
import com.codingpixel.healingbudz.interfaces.RecyclerViewItemClickListener;
import com.codingpixel.healingbudz.interfaces.ScrollPositionEndCallBack;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.BudzFeedModel.AddComment.AddCommentResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.AddComment.Attachment;
import com.codingpixel.healingbudz.network.BudzFeedModel.AddComment.Comment;
import com.codingpixel.healingbudz.network.BudzFeedModel.AddComment.GetAllComment;
import com.codingpixel.healingbudz.network.BudzFeedModel.ExtraPost;
import com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel.FollowingUser;
import com.codingpixel.healingbudz.network.BudzFeedModel.GeneralResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.GetPostDetailResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.Post;
import com.codingpixel.healingbudz.network.BudzFeedModel.MentionTagJsonModel;
import com.codingpixel.healingbudz.network.BudzFeedModel.MutePostModel.MutePostResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.ReportModel.FlagPost;
import com.codingpixel.healingbudz.network.BudzFeedModel.ReportModel.ReportPostResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.TagModel.Tag;
import com.codingpixel.healingbudz.network.BudzFeedModel.submitPostLike.Like;
import com.codingpixel.healingbudz.network.BudzFeedModel.submitPostLike.SubmitPostLikeResponse;
import com.codingpixel.healingbudz.network.RestClient;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;
import com.codingpixel.healingbudz.network.upload_image.UploadVideoAPIcall;
import com.codingpixel.healingbudz.static_function.UIModification;
import com.google.gson.Gson;
import com.linkedin.android.spyglass.mentions.MentionSpan;
import com.linkedin.android.spyglass.mentions.MentionsEditable;
import com.linkedin.android.spyglass.tokenization.QueryToken;
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizer;
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizerConfig;
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver;
import com.linkedin.android.spyglass.ui.MentionsEditText;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.rd.PageIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.DiscussQuestionFragment.isNewScreen;
import static com.codingpixel.healingbudz.adapter.BudzFeedPostDetailCommentsAdapter.isLikedCommentUser;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareCallApi;

/*
 * Created by M_Muzammil Sharif on 07-Mar-18.
 */

public class WallPostDetailActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewItemClickListener, Dialogs.OptionClickListener, View.OnLongClickListener, AutoLinkTextLinkClickListener, Dialogs.DialogItemClickListener, APIResponseListner, AutoLinkOnClickListener, Preview.PreviewListener, UserLikesAlertDialog.OnDialogFragmentClickListener, SwipeRefreshLayout.OnRefreshListener {
    private Post post;
    private NestedScrollView scrollView;
    private User user;
    private ExtraPost extraPost;

    private WallTopDropDowns reportView;
    private ViewPager pager;
    private AutoLinkTextView postText;
    private HealingBudTextViewBold likeTxt, likeTxtTemp, likeTxtTotal, commentTxt, repostCount, postedAs, repostedPostUser;
    private HealingBudTextViewBold personName;
    private HealingBudTextViewBold personExtraInfo;
    private CircularImageView personImg;
    private ImageView profile_img_topi;
    private ImageView personLeafImg;
    private PageIndicatorView indicatorView;
    private BudzFeedMainPostMediaPagerAdapter adapter;
    private HealingBudTextViewItalic timeAgo;
    private ImageView likeIcon;
    private MentionsEditText edTxtComment;
    private BudzFeedPostDetailCommentsAdapter commentsAdapter;
    private RecyclerView commentsList;
    private MentionAdapter mentionTagAdapter;
    private RecyclerView mentionTagList;
    private com.codingpixel.healingbudz.customeUI.ProgressDialog dialog;
    private ImageView commentUploadedMedia;
    private View commentUploadedMediaParent;
    private WallNewPostActivity.MediaUploadResponce mediaUploadResponce;
    private Preview scrapView;
    private View postAsParent, repostParent, videoPlayIcon, tagPeopleParent;
    private WallTagPeopleAdapter tagPeopleAdapter;
    private Comment comment;
    private int postId;
    LinearLayout repost_for;
    AutoLinkTextView vh_budz_feed_main_post_text_repost;
    public static boolean isDetailLiked = false;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getNotify()) {
            if (!this.isDestroyed()) {
                finish();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_post_detail);
        user = SetUserValuesInSP.getSavedUser(WallPostDetailActivity.this);
        if (user == null) {
            onSessionExpire();
            return;
        }
        post = null;
        if (getIntent().getExtras() == null) {
            WallPostDetailActivity.this.finish();
            return;
        }
        initView();

        post = (Post) getIntent().getExtras().getSerializable(Constants.POST_EXTRA);
        if (post == null) {
            postId = -1;
            postId = getIntent().getExtras().getInt(Constants.POST_ID_EXTRA, -1);
            if (postId < 0) {
                WallPostDetailActivity.this.finish();
                return;
            } else {
                callPostDetailApi();
            }
            return;
        }
        showView();
    }

    private void callPostDetailApi() {
        if (postId < 0) {
            return;
        }
        if (!Utility.isNetworkAvailable(getApplicationContext())) {
            Dialogs.showNetworkNotAvailibleDialog(WallPostDetailActivity.this);
            return;
        }
//        dialog.setMessage("Loading...");
        dialog.show(getSupportFragmentManager(), "Toast");
        RestClient.getInstance(getApplicationContext()).getApiService().getPostDetail(user.getSession_key(), postId).enqueue(new Callback<GetPostDetailResponse>() {
            @Override
            public void onResponse(Call<GetPostDetailResponse> call, final Response<GetPostDetailResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                        @Override
                        public void success() {
                            post = response.body().getSuccessData().getPost();
                            showView();
                        }

                        @Override
                        public void sessionExpire() {
                            onSessionExpire();
                        }

                        @Override
                        public void knownError(String errorMsg) {
                            CustomeToast.ShowCustomToast(WallPostDetailActivity.this, errorMsg, Gravity.TOP);
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
            public void onFailure(Call<GetPostDetailResponse> call, Throwable t) {
                dialog.dismiss();
                showUnknownSeverErrorDialog();
            }
        });
    }

    private void scrollToEnd() {
        scroll(View.FOCUS_DOWN);
    }

    private void scrollToStart() {
        scroll(View.FOCUS_UP);
    }

    private void scroll(final int toward) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
//                scrollView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        scrollView.fullScroll(toward);
//                    }
//                });
            }
        }, 60);
//        scrollView.post(new Runnable() {
//            @Override
//            public void run() {
//                scrollView.fullScroll(toward);
//            }
//        });
    }

    private void showView() {
        if (post == null) {
            WallPostDetailActivity.this.finish();
            return;
        }
        if (post.getAllowRepost() == 1) {
            findViewById(R.id.act_wall_post_detail_repost_btn).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.act_wall_post_detail_repost_btn).setVisibility(View.INVISIBLE);
        }// AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_MENTION,
        this.postText.addAutoLinkMode(AutoLinkMode.MODE_URL, AutoLinkMode.MODE_EMAIL);
        this.postText.setUrlModeColor(ContextCompat.getColor(WallPostDetailActivity.this, R.color.url_color));
        this.postText.setAutoLinkMask(Linkify.WEB_URLS);
        this.postText.setLinkTextColor(Color.parseColor("#808080"));
        this.postText.setEmailModeColor(ContextCompat.getColor(WallPostDetailActivity.this, R.color.post_description_links_color));
        this.postText.setAutoLinkOnClickListener(WallPostDetailActivity.this);
        if (post.getJson_Data() == null || (post.getJson_Data() != null && post.getJson_Data().length() < 3)) {
            this.postText.setMentionModeColor(ContextCompat.getColor(this, R.color.default_clr));//default_clr
            this.postText.setHashtagModeColor(ContextCompat.getColor(this, R.color.default_clr));
        } else {
            this.postText.setMentionModeColor(ContextCompat.getColor(this, R.color.post_description_links_color));
            this.postText.setHashtagModeColor(ContextCompat.getColor(this, R.color.mention_color));
        }
        this.postText.setAutoLinkText(String.valueOf(Html.fromHtml(post.getDescription())));
        if (post.getJsonData() != null) {
            try {
                ClickAbleKeywordText.createLink(this.postText, this.postText.getText().toString(), post.getJsonData());
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else {
            ClickAbleKeywordText.createLink(this.postText, this.postText.getText().toString());
        }
        if (post.getSharedPost() != null && post.getSharedUser() != null) {
            this.postText.setAutoLinkText(String.valueOf(Html.fromHtml(post.getPost_added_comment())));
            this.vh_budz_feed_main_post_text_repost.setText(String.valueOf(Html.fromHtml(post.getDescription())));
            this.repost_for.setVisibility(View.VISIBLE);
            if (post.getJsonData() != null) {
                try {
                    ClickAbleKeywordText.createLink(this.vh_budz_feed_main_post_text_repost, this.vh_budz_feed_main_post_text_repost.getText().toString(), post.getJsonData());
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    ClickAbleKeywordText.createLink(this.vh_budz_feed_main_post_text_repost, this.vh_budz_feed_main_post_text_repost.getText().toString());
                }
            } else {
                ClickAbleKeywordText.createLink(this.vh_budz_feed_main_post_text_repost, this.vh_budz_feed_main_post_text_repost.getText().toString());
                ClickAbleKeywordText.createLink(this.postText, this.postText.getText().toString());
            }
        } else {
            this.repost_for.setVisibility(View.GONE);
            if (post.getJsonData() != null) {
                try {
                    ClickAbleKeywordText.createLink(this.postText, this.postText.getText().toString(), post.getJsonData());
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            } else {
                ClickAbleKeywordText.createLink(this.postText, this.postText.getText().toString());
            }
        }
        //        this.postText.setAutoLinkText(Html.fromHtml(post.getDescription()).toString());
        //ClickAbleKeywordText.MakeKeywordClickableText(WallPostDetailActivity.this, post.getDescription(), postText);
        personName.setText(MessageFormat.format("{0} {1}", post.getUser().getFirstName(), post.getUser().getLastName()));
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
//        timeAgo.setText(TimesAgo.getTimeAgo(post.getUpdatedAt()));
        timeAgo.setText(DateConverter.convertSpecialWallTop(post.getCreatedAt()));
        personExtraInfo.setText(MessageFormat.format("{0}   |   {1}", String.valueOf(post.getUser().getPoints()), Utility.getBudType(post.getUser().getPoints())));
        personExtraInfo.setTextColor(Color.parseColor(Utility.getBudColor(post.getUser().getPoints())));
        likeTxt.setText(MessageFormat.format("{0} Likes", String.valueOf(post.getLikesCount())));
        likeTxtTotal.setText(MessageFormat.format("{0} Likes", String.valueOf(post.getLikesCount())));
        commentTxt.setText(MessageFormat.format("{0} Comments", String.valueOf(post.getCommentsCount())));

        personLeafImg.setOnClickListener(WallPostDetailActivity.this);
        personExtraInfo.setOnClickListener(WallPostDetailActivity.this);
        personImg.setOnClickListener(WallPostDetailActivity.this);
        personName.setOnClickListener(WallPostDetailActivity.this);

        if (post.getSub_user() != null && post.getSubUserId() != null && post.getSubUserId() != 0) {
            postAsParent.setVisibility(View.VISIBLE);
            postedAs.setText(post.getSub_user().getTitle());
            postedAs.setOnClickListener(WallPostDetailActivity.this);
        } else {
            postAsParent.setVisibility(View.GONE);
        }

        repostCount.setText(post.getSharedCount() + " Reposts");
        if (post.getSharedPost() != null && post.getSharedUser() != null) {
            repostParent.setVisibility(View.VISIBLE);
            repostedPostUser.setText(post.getSharedUser().getFirstName());
            repostedPostUser.setTextColor(Color.parseColor(Utility.getBudColor(post.getSharedUser().getPoints())));
            repostedPostUser.setOnClickListener(WallPostDetailActivity.this);
        } else {
            repostParent.setVisibility(View.GONE);
        }

        if (post.getFiles() == null || post.getFiles().isEmpty()) {
            findViewById(R.id.activity_wall_post_detail_post_imgs_viewpager_parent).setVisibility(View.GONE);
            List<String> urls = Utility.extractURL(post.getDescription());
            if (urls == null || urls.isEmpty()) {
                scrapView.setVisibility(View.GONE);
            } else {
                scrapView.setOnClickListener(WallPostDetailActivity.this);
                scrapView.setData(urls.get(0));
            }
        } else {
            scrapView.setVisibility(View.GONE);
            findViewById(R.id.activity_wall_post_detail_post_imgs_viewpager_parent).setVisibility(View.VISIBLE);
            adapter = new BudzFeedMainPostMediaPagerAdapter(post.getFiles(), new RecyclerViewItemClickListener() {
                @Override
                public void onItemClick(Object obj, int pos, int type) {
                    if (post.getFiles().get(pos).getType().equalsIgnoreCase("video")) {
                        Bundle b = new Bundle();
                        b.putString("path", URL.videos_baseurl + post.getFiles().get(pos).getFile());
                        b.putBoolean("isvideo", true);
                        Utility.launchActivity(WallPostDetailActivity.this, MediPreview.class, false, b);
                    } else {
                        Bundle b = new Bundle();
                        b.putSerializable(Constants.POST_EXTRA, post);
                        b.putInt(Constants.POST_FILE_POS, pos);
                        Utility.launchActivityForResult(WallPostDetailActivity.this, WallPostPhotoDetailActivity.class, b, Flags.ACTIVITIES_COMMUNICATION_FLAG);
                    }
                }

                @Override
                public boolean onItemLongClick(Object obj, int pos, int type) {
                    return false;
                }
            });
            pager.setAdapter(adapter);
            this.indicatorView.setCount(adapter.getCount());
        }

        if (isLiked(post.getLikes())) {
            this.likeIcon.setImageResource(R.drawable.liked_wall);
            this.likeIcon.setColorFilter(ContextCompat.getColor(this, R.color.COLOR_YOUR_COLOR), android.graphics.PorterDuff.Mode.SRC_IN);
//            this.likeTxt.setTextColor(ContextCompat.getColor(WallPostDetailActivity.this, R.color.feeds_liked_blue_color));
            this.likeTxtTemp.setTextColor(ContextCompat.getColor(WallPostDetailActivity.this, R.color.feeds_liked_blue_color));
//            this.likeTxtTotal.setTextColor(ContextCompat.getColor(WallPostDetailActivity.this, R.color.feeds_liked_blue_color));
        } else {
            this.likeIcon.setImageResource(R.drawable.un_like_wall);
            this.likeTxt.setTextColor(ContextCompat.getColor(WallPostDetailActivity.this, R.color.feeds_like_gray_color));
            this.likeIcon.setColorFilter(ContextCompat.getColor(this, R.color.feeds_like_gray_color), android.graphics.PorterDuff.Mode.SRC_IN);
            this.likeTxtTemp.setTextColor(ContextCompat.getColor(WallPostDetailActivity.this, R.color.feeds_like_gray_color));
//            this.likeTxtTotal.setTextColor(ContextCompat.getColor(WallPostDetailActivity.this, R.color.feeds_like_gray_color));
        }

        if (post.getTagged() == null || post.getTagged().isEmpty()) {
            tagPeopleParent.setVisibility(View.GONE);
        } else {
            tagPeopleParent.setVisibility(View.VISIBLE);
            tagPeopleAdapter.setFollowingUsers(post.get_Tagged());
        }

        commentsAdapter = new BudzFeedPostDetailCommentsAdapter(post.getComments(), WallPostDetailActivity.this, WallPostDetailActivity.this, false);

        commentsAdapter.notifyDataSetChanged();
        commentsAdapter.setCallBack(new ScrollPositionEndCallBack() {
            @Override
            public void onScrollPositionEnd() {
                callAPI();
            }

            @Override
            public void onScrollPositionStart() {

            }

            @Override
            public void setScrollingEnabled(boolean isEnabled) {

            }
        });
        commentsList.setAdapter(commentsAdapter);
//        pageNo=0;
//        callAPI();
        scrollToStart();
    }

    private int pageNo = 1;
    //    private SwipeRefreshLayout refreshLayout;
    private boolean isOnFinalPage = false;

    private void callAPI() {

        if (!Utility.isNetworkAvailable(this)) {
            Dialogs.showNetworkNotAvailibleDialog(this);
            return;
        }
        if (isOnFinalPage) {
            return;
        }
//        if (refreshLayout != null) {
//            refreshLayout.setRefreshing(true);
//        }
        RestClient.getInstance(this).getApiService().getUserComments(user.getSession_key(), post.getId(), pageNo).enqueue(new Callback<GetAllComment>() {
            @Override
            public void onResponse(Call<GetAllComment> call, final Response<GetAllComment> response) {
//                if (refreshLayout != null) {
//                    refreshLayout.setRefreshing(true);
//                }
                if (response.isSuccessful() && response.body() != null) {
                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getStatus(), new ApiStatusCallBack() {
                        @Override
                        public void success() {
                            if (response.body().getSuccessData().getComments() == null || response.body().getSuccessData().getComments().isEmpty()) {
                                isOnFinalPage = true;
                                return;
                            }
                            if (pageNo == 0) {
                                commentsAdapter.setDataSet(new ArrayList<Comment>());
                            }
                            isOnFinalPage = false;
                            ++pageNo;
                            List<Comment> posts = response.body().getSuccessData().getComments();
//                            HashMap<String, String> testSaved = new HashMap<>();
//                            for (int i = 0; i < posts.size(); i++) {
//                                MentionTagJsonModel[] test = posts.get(i).getJson_Data();
//                                String tempDesc = posts.get(i).getComment();
//
//                                try {
//                                    if (test != null) {
//                                        int index = 0;
//                                        for (int j = 0; j < test.length; j++) {
//                                            String testVal = test[j].getTrigger() + test[j].getValue();
//                                            if (posts.get(i).getComment().contains(testVal)) {
//                                                posts.get(i).setComment(posts.get(i).getComment().replace(testVal, index + "~"));
//                                                testSaved.put(index + "~", testVal);
//                                                index = index + 1;
//                                            }
//
////                                    if (posts.get(i).getDescription().contains(testVal) && test[j].getTrigger().equalsIgnoreCase("#")) {
////                                        posts.get(i).setDescription(posts.get(i).getDescription().replace(testVal, index + "!"));
////                                        testSaved.put(index + "!", testVal);
////                                    }
//                                        }
//                                        posts.get(i).setComment(posts.get(i).getComment().replace("@", "").replace("#", ""));
////                                int nextRev = 0;
//                                        for (int nextRev = 0; nextRev < index; nextRev++) {
//                                            posts.get(i).setComment(posts.get(i).getComment().replace(nextRev + "~", testSaved.get(nextRev + "~")));
////                                    String testVal = test[j].getTrigger() + test[j].getValue();
////                                    if (tempDesc.contains(testVal) && test[j].getTrigger().equalsIgnoreCase("@")) {
////                                        posts.get(i).setDescription(posts.get(i).getDescription().replace(j + "~", testSaved.get(j + "~")));
////                                    }
////                                    if (tempDesc.contains(testVal) && test[j].getTrigger().equalsIgnoreCase("#")) {
////                                        posts.get(i).setDescription(posts.get(i).getDescription().replace(j + "!", testSaved.get(j + "!")));
////                                    }
//
//                                        }
//                                        testSaved.clear();
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
                            commentsAdapter.addToDataSet(posts);

                        }

                        @Override
                        public void sessionExpire() {
                            onSessionExpire();
                        }

                        @Override
                        public void knownError(String errorMsg) {
                            CustomeToast.ShowCustomToast(WallPostDetailActivity.this, errorMsg, Gravity.TOP);
                        }

                        @Override
                        public void unKnownError() {
                            //unKnown Error
//                            showunKnownSeverErrorDialog();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetAllComment> call, Throwable t) {
//                if (refreshLayout != null) {
//                    refreshLayout.setRefreshing(true);
//                }
            }
        });
    }

    private void initView() {
        dialog = new com.codingpixel.healingbudz.customeUI.ProgressDialog();
        dialog.setCancelable(false);
//        dialog.setMessage("Submitting Comment...");

        findViewById(R.id.activity_wall_post_detail_back_btn).setOnClickListener(WallPostDetailActivity.this);
        findViewById(R.id.vh_budz_feed_main_post_comment_btn).setOnClickListener(WallPostDetailActivity.this);
        findViewById(R.id.act_wall_post_detail_repost_btn).setOnClickListener(WallPostDetailActivity.this);
        findViewById(R.id.activity_wall_post_detail_post_like_btn).setOnClickListener(WallPostDetailActivity.this);
        findViewById(R.id.activity_wall_post_detail_post_like_btn).setOnLongClickListener(WallPostDetailActivity.this);
        findViewById(R.id.activity_wall_post_detail_post_like_btn_total).setOnClickListener(WallPostDetailActivity.this);
        findViewById(R.id.activity_wall_post_detail_post_options).setOnClickListener(WallPostDetailActivity.this);
        findViewById(R.id.activity_wall_post_detail_add_btn).setOnClickListener(WallPostDetailActivity.this);

        pager = (ViewPager) findViewById(R.id.activity_wall_post_detail_post_imgs_viewpager);
        repost_for = findViewById(R.id.repost_for);
        repost_for.setVisibility(View.GONE);
        vh_budz_feed_main_post_text_repost = findViewById(R.id.vh_budz_feed_main_post_text_repost);
        indicatorView = (PageIndicatorView) findViewById(R.id.activity_wall_post_detail_post_imgs_pager_indicator);

        indicatorView.setViewPager(pager);

        postAsParent = findViewById(R.id.act_wall_post_detail_posted_as_parent);
        repostParent = findViewById(R.id.act_wall_post_detail_reposted_parent);
        postedAs = (HealingBudTextViewBold) findViewById(R.id.act_wall_post_detail_posted_as);
        repostedPostUser = (HealingBudTextViewBold) findViewById(R.id.act_wall_post_detail_reposted_user_name);

        scrapView = (Preview) findViewById(R.id.activity_wall_post_detail_post_scrap);
        scrapView.setListener(WallPostDetailActivity.this);
//        scrapView.setDescriptionTextColor(Color.GRAY);
//        scrapView.setTitleTextColor(Color.YELLOW);
//        scrapView.setSiteNameTextColor(Color.BLUE);
        repostCount = (HealingBudTextViewBold) findViewById(R.id.act_wall_post_detail_repost_txt);

        commentsList = (RecyclerView) findViewById(R.id.activity_wall_post_detail_post_comments_list);
//        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_comment);
//        refreshLayout.setColorSchemeColors(Color.parseColor("#0083cb"));
//        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
//        refreshLayout.setOnRefreshListener(WallPostDetailActivity.this);
        commentsList.setLayoutManager(new LinearLayoutManager(WallPostDetailActivity.this, LinearLayoutManager.VERTICAL, true));
        commentsList.getLayoutManager().setAutoMeasureEnabled(true);
        commentsList.setNestedScrollingEnabled(false);
        likeIcon = (ImageView) findViewById(R.id.activity_wall_post_detail_post_like_icon);
        likeTxt = (HealingBudTextViewBold) findViewById(R.id.activity_wall_post_detail_post_like_txt);
        likeTxtTemp = (HealingBudTextViewBold) findViewById(R.id.activity_wall_post_detail_post_like_txt_temp);
        likeTxtTotal = (HealingBudTextViewBold) findViewById(R.id.activity_wall_post_detail_post_like_txt_total);
        timeAgo = (HealingBudTextViewItalic) findViewById(R.id.activity_wall_post_detail_post_time);
        personName = (HealingBudTextViewBold) findViewById(R.id.activity_wall_post_detail_post_person_name);
        personExtraInfo = (HealingBudTextViewBold) findViewById(R.id.activity_wall_post_detail_post_person_extra_info);
        personImg = (CircularImageView) findViewById(R.id.activity_wall_post_detail_post_person_img);
        profile_img_topi = findViewById(R.id.profile_img_topi);
        personLeafImg = (ImageView) findViewById(R.id.activity_wall_post_detail_post_person_leaf);
        commentTxt = (HealingBudTextViewBold) findViewById(R.id.activity_wall_post_detail_post_comment_txt);
        commentTxt.setOnClickListener(this);
        postText = (AutoLinkTextView) findViewById(R.id.activity_wall_post_detail_post_text);
        Typeface customFont = FontCache.getTypeface("Lato-Light.ttf", this);
        postText.setTypeface(customFont);
        edTxtComment = (MentionsEditText) findViewById(R.id.activity_wall_post_detail_comment_ed_txt);
        Typeface st = FontCache.getTypeface("Lato-Light.ttf", this);
        edTxtComment.setTypeface(st);
        RecyclerView tagList = (RecyclerView) findViewById(R.id.activity_wall_post_detail_tag_people_list);
        tagPeopleParent = findViewById(R.id.activity_wall_post_detail_with_parent);
        tagList.setLayoutManager(ChipsLayoutManager.newBuilder(WallPostDetailActivity.this).setOrientation(ChipsLayoutManager.HORIZONTAL).build());
        tagPeopleAdapter = new WallTagPeopleAdapter(null, new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(Object obj, int pos, int type) {
                onTaggedPeopleClicked((FollowingUser) obj);
            }

            @Override
            public boolean onItemLongClick(Object obj, int pos, int type) {
                return false;
            }
        }, false);
        tagList.setAdapter(tagPeopleAdapter);

        findViewById(R.id.activity_wall_post_detail_comment_emoji_btn).setOnClickListener(WallPostDetailActivity.this);
        findViewById(R.id.activity_wall_post_detail_comment_send).setOnClickListener(WallPostDetailActivity.this);
        findViewById(R.id.activity_wall_post_detail_comment_attach).setOnClickListener(WallPostDetailActivity.this);

        reportView = new WallTopDropDowns(WallPostDetailActivity.this);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_wall_post_detail_main_view);
        layout.addView(reportView.getView());

        scrollView = (NestedScrollView) findViewById(R.id.activity_wall_post_detail_nested_scroll);

        commentUploadedMediaParent = findViewById(R.id.activity_wall_post_detail_uploaded_media_parent);

        commentUploadedMedia = (ImageView) findViewById(R.id.activity_wall_post_detail_uploaded_media);
        findViewById(R.id.activity_wall_post_detail_uploaded_media_cross).setOnClickListener(WallPostDetailActivity.this);
        commentUploadedMedia.setOnClickListener(WallPostDetailActivity.this);

        mentionTagList = (RecyclerView) findViewById(R.id.activity_wall_post_detail_comment_tag_list);
        mentionTagList.setLayoutManager(new LinearLayoutManager(WallPostDetailActivity.this));

        videoPlayIcon = findViewById(R.id.activity_wall_post_detail_uploaded_media_video);

        initMentionEditText();
    }

    private void onTaggedPeopleClicked(FollowingUser obj) {
        isNewScreen = true;
        GoToProfile(this, obj.getId());
//        CustomeToast.ShowCustomToast(WallPostDetailActivity.this, "Tagged Person Clicked with name: " + obj.getFirstName() + "<&> id: " + obj.getId(), Gravity.BOTTOM);
    }

    private void initMentionEditText() {
        edTxtComment.setTokenizer(new WordTokenizer(new WordTokenizerConfig.Builder().
                setExplicitChars("#@").setThreshold(1).setMaxNumKeywords(1).build()));
        mentionTagAdapter = new MentionAdapter(null, WallPostDetailActivity.this);
        mentionTagList.setAdapter(mentionTagAdapter);
        mentionTagList.setVisibility(View.GONE);
        edTxtComment.setQueryTokenReceiver(new QueryTokenReceiver() {
            @Override
            public List<String> onQueryReceived(@NonNull QueryToken queryToken) {
                if (queryToken.getExplicitChar() == '#') {
                    if (queryToken.getKeywords().toLowerCase().length() > 0)
                        showHashTagList(queryToken.getKeywords().toLowerCase());
                    else {
                        mentionTagList.setVisibility(View.GONE);
                    }
                } else if (queryToken.getExplicitChar() == '@') {
                    if (queryToken.getKeywords().toLowerCase().length() > 0)
                        showMentionList(queryToken.getKeywords().toLowerCase());
                    else {
                        mentionTagList.setVisibility(View.GONE);
                    }
                } else {
                    mentionTagList.setVisibility(View.GONE);
                }
                return null;
            }
        });
    }

    private int dw = 0;

    private void showMentionTagDropDown() {
        mentionTagList.setVisibility(View.VISIBLE);

        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mentionTagList.getLayoutParams();
        final int x = getCursorX();
        //reset all params
        params.setMargins(0, 5, 0, params.bottomMargin);
        params.gravity = Gravity.LEFT | Gravity.START;
        //get dowpdown list width
        mentionTagList.post(new Runnable() {
            @Override
            public void run() {
                dw = mentionTagList.getMeasuredWidth();
                if (x > (dw / 2)) {
                    if (x < (Utility.getDeviceWidth(WallPostDetailActivity.this) - (dw / 2))) {
                        params.leftMargin = (x - (dw / 2));
                    } else {
                        params.gravity = Gravity.CENTER;//Gravity.END | Gravity.RIGHT;
                    }
                }
                mentionTagList.setLayoutParams(params);
            }
        });
        //defining x position of dropdown accounding to cursor x position

    }

    private void showMentionList(String s) {
        List<FollowingUser> users = new ArrayList<>();
        List<FollowingUser> usersDelete = new ArrayList<>();
        for (FollowingUser user : WallSinglton.getInstance().getFollowingUserList()) {
            if (user.getFirstName().replaceAll(" ", "").toLowerCase().contains(s.toLowerCase()) ||
                    user.getLastName().replaceAll(" ", "").toLowerCase().contains(s.toLowerCase())) {
                users.add(user);
            }
        }
        MentionsEditable editable = edTxtComment.getMentionsText().trim();
        for (MentionSpan span : editable.getMentionSpans()) {
            if (span.getMention() instanceof FollowingUser) {
                FollowingUser user = (FollowingUser) span.getMention();
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getId().equals(user.getId()) && user.isSubUser()) {
                        users.remove(i);
                    } else if (users.get(i).getId().equals(user.getId())) {
                        users.remove(i);
                    }
                }
            }
        }
        if (comment != null) {
            for (int i = 0; i < users.size(); i++) {
                if (editable.toString().toLowerCase().trim().contains("@" + users.get(i).getFirstName().toLowerCase().trim())) {
//                    users.remove(i);
                    usersDelete.add(users.get(i));
                }
            }
            for (int i = 0; i < usersDelete.size(); i++) {
                users.remove(usersDelete.get(i));
            }
            usersDelete.clear();
        }
        if (!users.isEmpty()) {
            mentionTagAdapter.setMentions(users);
            if (users.size() > 3) {
                mentionTagList.getLayoutParams().height = Utility.getDeviceHeight(WallPostDetailActivity.this) / 4;
            } else {
                mentionTagList.getLayoutParams().height = FrameLayout.LayoutParams.WRAP_CONTENT;
            }
            showMentionTagDropDown();
        } else {
            mentionTagList.setVisibility(View.GONE);
        }
    }

    private void showHashTagList(String s) {
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : WallSinglton.getInstance().getHashTagList()) {
            if (tag.getTitle().replaceAll(" ", "").toLowerCase().contains(s.toLowerCase())) {
                tags.add(tag);
            }
        }
        if (!tags.isEmpty()) {
            mentionTagAdapter.setTags(tags);
            if (tags.size() > 3) {
                mentionTagList.getLayoutParams().height = Utility.getDeviceHeight(WallPostDetailActivity.this) / 4;
            } else {
                mentionTagList.getLayoutParams().height = FrameLayout.LayoutParams.WRAP_CONTENT;
            }
            showMentionTagDropDown();
        } else {
            mentionTagList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.act_wall_post_detail_repost_btn: {
                if (user.getUser_id() != post.getUserId()) {
                    onOptionClick(6);
                } else {
                    CustomeToast.ShowCustomToast(view.getContext(), "You can't repost your own post!", Gravity.TOP);
                }
            }
            break;
            case R.id.activity_wall_post_detail_post_comment_txt: {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputMethodManager imm = (InputMethodManager) edTxtComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            assert imm != null;
                            imm.toggleSoftInputFromWindow(edTxtComment.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                            edTxtComment.requestFocus();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 300);
            }
            break;
            case R.id.vh_budz_feed_main_post_comment_btn: {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputMethodManager imm = (InputMethodManager) edTxtComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            assert imm != null;
                            imm.toggleSoftInputFromWindow(edTxtComment.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                            edTxtComment.requestFocus();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 300);
            }
            break;
            case R.id.activity_wall_post_detail_back_btn: {
                finishActivityAndNotifyPrevious();
            }
            break;
            case R.id.activity_wall_post_detail_post_like_btn_total: {
                //TODO DIALOGUE FOR LIST OF LIKES
                if (post.getLikes().size() == post.getLikesCount()) {
                    UserLikesAlertDialog userLikesAlertDialog = UserLikesAlertDialog.newInstance(this, post.getLikes());
                    userLikesAlertDialog.show(((AppCompatActivity) view.getContext()).getSupportFragmentManager(), "pd");
                } else {
                    post.getLikes().remove(post.getLikes().size() - 1);
                    UserLikesAlertDialog userLikesAlertDialog = UserLikesAlertDialog.newInstance(this, post.getLikes());
                    userLikesAlertDialog.show(((AppCompatActivity) view.getContext()).getSupportFragmentManager(), "pd");

                }


            }
            break;
            case R.id.activity_wall_post_detail_post_like_btn: {
                if (!isDetailLiked) {
                    isDetailLiked = true;
                    view.setClickable(false);
                    view.setEnabled(false);
                    if (isLiked(post.getLikes())) {
                        submitPostLike(post.getId(), 0);
                    } else {
                        submitPostLike(post.getId(), 1);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            view.setClickable(true);
                            view.setEnabled(true);
                        }
                    }, 150);
                }
            }
            break;
            case R.id.activity_wall_post_detail_comment_emoji_btn: {
                //todo: emoji icon click
                clearEdit();
            }
            break;
            case R.id.activity_wall_post_detail_comment_send: {
                mentionTagList.setVisibility(View.GONE);
                if (edTxtComment.getText().toString().trim().length() > 0) {

                    String[] comment;
                    try {
                        comment = getCommentDetails();
                    } catch (StringIndexOutOfBoundsException e) {
                        e.printStackTrace();
                        comment = new String[]{edTxtComment.getText().toString().trim(), ""};
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        comment = new String[]{edTxtComment.getText().toString().trim(), ""};
                    }


                    if (mediaUploadResponce == null) {
                        submitComment(comment[0], comment[1], "", "", "", "");
                    } else {
                        if (isImage) {
                            submitComment(comment[0], comment[1], mediaUploadResponce.file, "image", "", mediaUploadResponce.thumb);
                        } else {
                            submitComment(comment[0], comment[1], mediaUploadResponce.file, "video", mediaUploadResponce.poster, "");
                        }
                    }
                } else if (mediaUploadResponce != null) {
                    String[] comment;
                    try {
                        comment = getCommentDetails();
                    } catch (StringIndexOutOfBoundsException e) {
                        e.printStackTrace();
                        comment = new String[]{edTxtComment.getText().toString().trim(), ""};
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        comment = new String[]{edTxtComment.getText().toString().trim(), ""};
                    }
                    if (isImage) {
                        submitComment(comment[0], comment[1], mediaUploadResponce.file, "image", "", mediaUploadResponce.thumb);
                    } else {
                        submitComment(comment[0], comment[1], mediaUploadResponce.file, "video", mediaUploadResponce.poster, "");
                    }
                } else {
                    CustomeToast.ShowCustomToast(this, "Comment Required!", Gravity.TOP);
                }
            }
            break;
            case R.id.activity_wall_post_detail_post_options: {
                Dialogs.showPostMoreDropDownMenu(view, WallPostDetailActivity.this, post);
            }
            break;
            case R.id.activity_wall_post_detail_post_person_extra_info: {
                onUserClick(post.getUser());
            }
            break;
            case R.id.activity_wall_post_detail_post_person_img: {
                onUserClick(post.getUser());
            }
            break;
            case R.id.activity_wall_post_detail_post_person_leaf: {
                onUserClick(post.getUser());
            }
            break;
            case R.id.activity_wall_post_detail_post_person_name: {
                onUserClick(post.getUser());
            }
            break;
            case R.id.activity_wall_post_detail_add_btn: {
                Utility.launchActivityForResult(WallPostDetailActivity.this, WallNewPostActivity.class, Flags.ACTIVITIES_COMMUNICATION_FLAG);
            }
            break;
            case R.id.activity_wall_post_detail_uploaded_media_cross: {
                commentUploadedMediaParent.setVisibility(View.GONE);
                mediaUploadResponce = null;
                commentUploadedMedia.setImageResource(R.drawable.place_holder_wall);
            }
            break;
            case R.id.activity_wall_post_detail_comment_attach: {
                if (mediaUploadResponce == null || commentUploadedMediaParent.getVisibility() == View.GONE) {
                    Bundle b = new Bundle();
                    b.putBoolean("isVideoCaptureAble", true);
                    Utility.launchActivityForResult(WallPostDetailActivity.this, HBCameraActivity.class, b, Flags.PICTURE_CAPTUREING_REQUEST);
                } else {
                    CustomeToast.ShowCustomToast(WallPostDetailActivity.this, "Already attached media!", Gravity.TOP);
                }
            }
            break;
            case R.id.act_wall_post_detail_posted_as: {
                onSubUserClick();
            }
            break;
            case R.id.act_wall_post_detail_reposted_user_name: {
                onRepostedPostUserClick();
            }
            break;
            case R.id.activity_wall_post_detail_post_scrap: {
                onLinkClick(AutoLinkMode.MODE_URL, scrapView.getUrl(), null);
            }
            break;
            case R.id.activity_wall_post_detail_uploaded_media: {
                if (mediaUploadResponce == null || mediaUploadResponce.poster == null || mediaUploadResponce.poster.trim().isEmpty() || mediaUploadResponce.file == null || mediaUploadResponce.file.trim().isEmpty()) {
                    //data is empty or not video
                } else {
                    Bundle b = new Bundle();
                    b.putString("path", URL.videos_baseurl + mediaUploadResponce.file);
                    b.putBoolean("isvideo", true);
                    Utility.launchActivity(WallPostDetailActivity.this, MediPreview.class, false, b);
                }
            }
            break;
        }
    }

    private void onSubUserClick() {
        Intent budzmap_intetn = new Intent(WallPostDetailActivity.this, BudzMapDetailsActivity.class);
        budzmap_intetn.putExtra("budzmap_id", post.getSub_user().getId());
        startActivity(budzmap_intetn);
//        CustomeToast.ShowCustomToast(WallPostDetailActivity.this, "Posted As User Clicked : " + post.getSub_user().getTitle() + " <&> id: " + post.getSub_user().getId() + " <&> userID: " + post.getUserId(), Gravity.BOTTOM);
    }

    private void onRepostedPostUserClick() {
        isNewScreen = true;
        GoToProfile(this, post.getSharedUser().getId());
//        CustomeToast.ShowCustomToast(WallPostDetailActivity.this, "Reposted Post User: " + post.getSharedUser().getFirstName() + " <&> id: " + post.getSharedUser().getId(), Gravity.BOTTOM);
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
            public void onDialogItemClickListener(DialogInterface dialog, Object obj, int type) {
                if (!Utility.isNetworkAvailable(WallPostDetailActivity.this)) {
                    Dialogs.showNetworkNotAvailibleDialog(WallPostDetailActivity.this);
                    return;
                }
                Utility.hideKeyboard(WallPostDetailActivity.this);
                RestClient.getInstance(WallPostDetailActivity.this).getApiService().reportPost(user.getSession_key(), post.getId(), (String) obj).enqueue(new Callback<ReportPostResponse>() {
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
                                    CustomeToast.ShowCustomToast(WallPostDetailActivity.this, "Report Submitted!", Gravity.TOP);
                                }

                                @Override
                                public void sessionExpire() {
                                    reportView.dismissSlide();
                                    onSessionExpire();
                                }

                                @Override
                                public void knownError(String errorMsg) {
                                    if (WallPostDetailActivity.this != null)
                                        CustomeToast.ShowCustomToast(WallPostDetailActivity.this, errorMsg, Gravity.TOP);
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

    private void muteUnmutePost(final Post post) {
        if (!Utility.isNetworkAvailable(getApplicationContext())) {
            Dialogs.showNetworkNotAvailibleDialog(WallPostDetailActivity.this);
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
                            CustomeToast.ShowCustomToast(WallPostDetailActivity.this, errorMsg, Gravity.TOP);
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

    private void finishActivityAndNotifyPrevious() {
        /*Bundle b = new Bundle();
        if (extraPost == null || extraPost.getPosts() == null || extraPost.getPosts().isEmpty()) {
            b.putSerializable(Constants.POST_EXTRA, post);
            Utility.finishWithResult(WallPostDetailActivity.this, b, Flags.NOTIFY);
        } else {
            extraPost.getPosts().add(post);
            b.putSerializable(Constants.POST_EXTRA, extraPost);
            Utility.finishWithResult(WallPostDetailActivity.this, b, Flags.NOTIFY_NEW_ELEMIENT);
        }*/
        Utility.finishWithResult(WallPostDetailActivity.this, null, Flags.ACTIVITY_COMMUNICATION_FOR_UPDATE_RESULT);
    }

    private void submitPostLike(final Integer id, int is_like) {
        if (!Utility.isNetworkAvailable(getApplicationContext())) {
            Dialogs.showNetworkNotAvailibleDialog(WallPostDetailActivity.this);
            return;
        }
        RestClient.getInstance(getApplicationContext()).getApiService().submitPostLike(user.getSession_key(), id, is_like).enqueue(new Callback<SubmitPostLikeResponse>() {
            @Override
            public void onResponse(Call<SubmitPostLikeResponse> call, final Response<SubmitPostLikeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                        @Override
                        public void success() {
                            isDetailLiked = false;
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

    private void putLike(final Like like) {
        WallPostDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (like.getUserId() == user.getUser_id() && like.getIsLike() > 0) {
                        likeIcon.setImageResource(R.drawable.liked_wall);
                        likeIcon.setColorFilter(ContextCompat.getColor(WallPostDetailActivity.this, R.color.COLOR_YOUR_COLOR), android.graphics.PorterDuff.Mode.SRC_IN);
//                        likeTxt.setTextColor(ContextCompat.getColor(WallPostDetailActivity.this, R.color.feeds_liked_blue_color));
                        likeTxtTemp.setTextColor(ContextCompat.getColor(WallPostDetailActivity.this, R.color.feeds_liked_blue_color));
//                        likeTxtTotal.setTextColor(ContextCompat.getColor(WallPostDetailActivity.this, R.color.feeds_liked_blue_color));
                    } else {
                        likeIcon.setColorFilter(ContextCompat.getColor(WallPostDetailActivity.this, R.color.feeds_like_gray_color), android.graphics.PorterDuff.Mode.SRC_IN);
                        likeIcon.setImageResource(R.drawable.un_like_wall);
                        likeTxt.setTextColor(ContextCompat.getColor(WallPostDetailActivity.this, R.color.feeds_like_gray_color));
                        likeTxtTemp.setTextColor(ContextCompat.getColor(WallPostDetailActivity.this, R.color.feeds_like_gray_color));
//                        likeTxtTotal.setTextColor(ContextCompat.getColor(WallPostDetailActivity.this, R.color.feeds_like_gray_color));
                    }
                    if (post.getLikes() == null || post.getLikes().isEmpty()) {
                        post.setLikes(new ArrayList<Like>());
                        post.getLikes().add(like);
                        if (like.getIsLike() > 0) {
                            post.setLikesCount(post.getLikesCount() + 1);
                        }
                        likeTxt.setText(String.valueOf(post.getLikesCount()) + " Likes");
                        likeTxtTotal.setText(String.valueOf(post.getLikesCount()) + " Likes");
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
                            likeTxtTotal.setText(String.valueOf(post.getLikesCount()) + " Likes");
                            return;
                        }
                    }
                    post.getLikes().add(like);
                    if (like.getIsLike() > 0) {
                        post.setLikesCount(post.getLikesCount() + 1);
                    }
                    likeTxt.setText(String.valueOf(post.getLikesCount()) + " Likes");
                    likeTxtTotal.setText(String.valueOf(post.getLikesCount()) + " Likes");
                } catch (Exception e) {
                    //
                }
            }
        });
    }

    private void showUnknownSeverErrorDialog() {

    }

    private void onSessionExpire() {
        Utility.finishWithResult(WallPostDetailActivity.this, null, Flags.SESSION_OUT);
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
    public void onBackPressed() {
        finishActivityAndNotifyPrevious();
    }

    @Override
    protected void onPause() {
        super.onPause();
        UIModification.HideKeyboard(this);
    }

    private void submitComment(String comment, String json_data, String attachement, String type, String poster, String thumb) {
        Utility.hideKeyboard(WallPostDetailActivity.this);
        if (!Utility.isNetworkAvailable(getApplicationContext())) {
            Dialogs.showNetworkNotAvailibleDialog(WallPostDetailActivity.this);
            return;
        }
//        dialog.setMessage("Submitting Comment...");
        dialog.show(getSupportFragmentManager(), "Toast");
        if (WallPostDetailActivity.this.comment != null) {
            RestClient.getInstance(getApplicationContext()).getApiService().
                    submitUpdatePostComment(user.getSession_key(), post.getId(), comment, json_data, attachement, type, poster, WallPostDetailActivity.this.comment.getId(), thumb).enqueue(new Callback<AddCommentResponse>() {
                @Override
                public void onResponse(Call<AddCommentResponse> call, final Response<AddCommentResponse> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                            @Override
                            public void success() {
                                putComment(response.body().getSuccessData().getComments());
                                mediaUploadResponce = null;
                                commentUploadedMediaParent.setVisibility(View.GONE);
                                commentsList.scrollToPosition(commentsAdapter.getItemCount() - 1);
                            }

                            @Override
                            public void sessionExpire() {
                                onSessionExpire();
                            }

                            @Override
                            public void knownError(String errorMsg) {
                                CustomeToast.ShowCustomToast(WallPostDetailActivity.this, errorMsg, Gravity.TOP);
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
                public void onFailure(Call<AddCommentResponse> call, Throwable t) {
                    dialog.dismiss();
                    showUnknownSeverErrorDialog();
                }
            });

        } else
            RestClient.getInstance(getApplicationContext()).getApiService().
                    submitPostComment(user.getSession_key(), post.getId(), comment, json_data, attachement, type, poster, thumb).enqueue(new Callback<AddCommentResponse>() {
                @Override
                public void onResponse(Call<AddCommentResponse> call, final Response<AddCommentResponse> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                            @Override
                            public void success() {
                                putComment(response.body().getSuccessData().getComments());
                                mediaUploadResponce = null;
                                commentUploadedMediaParent.setVisibility(View.GONE);
                                commentsList.scrollToPosition(commentsAdapter.getItemCount() - 1);
                            }

                            @Override
                            public void sessionExpire() {
                                onSessionExpire();
                            }

                            @Override
                            public void knownError(String errorMsg) {
                                CustomeToast.ShowCustomToast(WallPostDetailActivity.this, errorMsg, Gravity.TOP);
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
                public void onFailure(Call<AddCommentResponse> call, Throwable t) {
                    dialog.dismiss();
                    showUnknownSeverErrorDialog();
                }
            });

    }

    private void putComment(final Comment comments) {
        WallPostDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    edTxtComment.setText("");
                    if (post.getComments() == null || post.getComments().isEmpty()) {
                        post.setComments(new ArrayList<Comment>());
                        post.getComments().add(comments);
                        post.setCommentsCount(post.getCommentsCount() + 1);
                        commentTxt.setText(String.valueOf(post.getCommentsCount()) + " Comments");
                        commentsAdapter.setComments(post.getComments());
//                        scrollToEnd();
                        scrollToStart();
                        return;
                    }
                    if (comment != null) {
                        for (int j = 0; j < post.getComments().size(); j++) {
                            if (comment.getId().equals(post.getComments().get(j).getId())) {
                                post.getComments().set(j, comments);
                                commentsAdapter.setComments(post.getComments());
//                            scrollToEnd();
//                                scrollToStart();
                                clearEdit();
                                return;
                            }
                        }
                    } else {
                        clearEdit();
                        for (int j = 0; j < post.getComments().size(); j++) {
                            if (comments.getId().equals(post.getComments().get(j).getId())) {
                                post.getComments().set(j, comments);
                                commentsAdapter.setComments(post.getComments());
//                            scrollToEnd();
                                scrollToStart();
                                return;
                            }
                        }
                    }


                    post.getComments().add(comments);
                    post.setCommentsCount(post.getCommentsCount() + 1);
                    commentTxt.setText(String.valueOf(post.getCommentsCount()) + " Comments");
                    commentsAdapter.setComments(post.getComments());
//                    scrollToEnd();
                    scrollToStart();
                } catch (Exception e) {
                    //
                }
            }
        });
    }

    private void submitCommentLike(final Integer id, int is_like) {
//        if (this == null) {
//            //fragment die...
//            return;
//        }
        if (!Utility.isNetworkAvailable(this)) {
            Dialogs.showNetworkNotAvailibleDialog(this);
            return;
        }
        RestClient.getInstance(this).getApiService().submitCommentLike(user.getSession_key(), id, is_like).enqueue(new Callback<SubmitPostLikeResponse>() {
            @Override
            public void onResponse(@NonNull Call<SubmitPostLikeResponse> call, @NonNull final Response<SubmitPostLikeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                        @Override
                        public void success() {
//                            isLikedUser = false;

                            commentsAdapter.updatePostLike(id, response.body().getSuccessData());
                            isLikedCommentUser = false;
                        }

                        @Override
                        public void sessionExpire() {
                            onSessionExpire();
                        }

                        @Override
                        public void knownError(String errorMsg) {
                            CustomeToast.ShowCustomToast(WallPostDetailActivity.this, errorMsg, Gravity.TOP);
                        }

                        @Override
                        public void unKnownError() {
//                            showunKnownSeverErrorDialog();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<SubmitPostLikeResponse> call, Throwable t) {
//                showunKnownSeverErrorDialog();
            }
        });
    }

    @Override
    public void onItemClick(Object obj, int pos, int type) {
        if (pos == -1) {
            post.setCommentsCount(type);
//            post.setComments();
            commentTxt.setText(MessageFormat.format("{0} Comments", String.valueOf(post.getCommentsCount())));
        } else if (type == 1 && obj instanceof com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.User) {
            onUserClick((com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.User) obj);
        } else if (type == 2 && obj instanceof Attachment) {
            Bundle b = new Bundle();
            Attachment a = (Attachment) obj;
            boolean isVideo = a.getType().trim().equalsIgnoreCase("video") && a.getPoster() != null && !a.getPoster().trim().isEmpty();
            b.putString("path", (isVideo ? URL.videos_baseurl : URL.images_baseurl) + a.getFile());
            b.putBoolean("isVideo", isVideo);
            Intent intent = new Intent(WallPostDetailActivity.this, MediPreview.class);
            intent.putExtra("path", (isVideo ? URL.videos_baseurl : URL.images_baseurl) + a.getFile());
            intent.putExtra("isvideo", isVideo);
            startActivity(intent);
//            Utility.launchActivity(WallPostDetailActivity.this, MediPreview.class, false, b);
        } else if (type == 4 && obj instanceof Comment) {
            comment = (Comment) obj;
            setEditView();
//EDIT CASE
        } else if ((type == 0 || type == 1) && obj instanceof Comment) {
            submitCommentLike(((Comment) obj).getId(), type);
        }
    }

    MentionTagJsonModel[] jsonModels;

    private void clearEdit() {
        findViewById(R.id.activity_wall_post_detail_comment_emoji_btn).setVisibility(View.GONE);
        onClick(findViewById(R.id.activity_wall_post_detail_uploaded_media_cross));
        UIModification.HideKeyboard(this);
        edTxtComment.setText("");
        mentionTagList.setVisibility(View.GONE);
        WallPostDetailActivity.this.comment = null;
        jsonModels = null;
        mediaUploadResponce = null;

    }

    private void setEditView() {
        //SET VIEW FOR EDIT  //TO DO
        findViewById(R.id.activity_wall_post_detail_comment_emoji_btn).setVisibility(View.VISIBLE);
        edTxtComment.setText(comment.getComment());
        edTxtComment.setSelection(edTxtComment.getText().length());
        mentionTagList.setVisibility(View.GONE);
        edTxtComment.requestFocus();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    InputMethodManager imm = (InputMethodManager) edTxtComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInputFromWindow(edTxtComment.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                    edTxtComment.requestFocus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 300);
        jsonModels = comment.getJson_Data();
        if (comment.getAttachment() != null) {
            mediaUploadResponce = new WallNewPostActivity.MediaUploadResponce();
            if (comment.getAttachment().getType().equalsIgnoreCase("image")) {
                mediaUploadResponce.file = comment.getAttachment().getFile();
                mediaUploadResponce.thumb = comment.getAttachment().getThumb();
                isImage = true;
                videoPlayIcon.setVisibility(View.GONE);
                Glide.with(WallPostDetailActivity.this).load(URL.images_baseurl + mediaUploadResponce.file).
                        diskCacheStrategy(DiskCacheStrategy.SOURCE).
                        placeholder(R.drawable.place_holder_wall).into(commentUploadedMedia);
                commentUploadedMediaParent.setVisibility(View.VISIBLE);
//                mediaUploadResponce.
            } else {
                isImage = false;
                mediaUploadResponce.file = comment.getAttachment().getFile();
                mediaUploadResponce.poster = comment.getAttachment().getPoster();
                Glide.with(WallPostDetailActivity.this).load(URL.images_baseurl + mediaUploadResponce.poster).
                        diskCacheStrategy(DiskCacheStrategy.SOURCE).
                        placeholder(R.drawable.place_holder_wall).into(commentUploadedMedia);
                commentUploadedMediaParent.setVisibility(View.VISIBLE);
                videoPlayIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    private void onUserClick(com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.User clickUser) {
        isNewScreen = true;
        GoToProfile(this, clickUser.getId());
//        Toast.makeText(WallPostDetailActivity.this, clickUser.getFirstName() + " " + clickUser.getLastName() + " is clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(Object obj, int pos, int type) {
        return false;
    }

    @Override
    public void onOptionClick(final int pos) {
        switch (pos) {
            case 0: {
                JSONObject data_object = new JSONObject();
                try {
                    data_object.put("id", post.getId());
                    data_object.put("type", "Post");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ShareCallApi(WallPostDetailActivity.this, data_object);
                Utility.shareString(WallPostDetailActivity.this, URL.getPostUrl((post).getId()));
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
                if (!Utility.isNetworkAvailable(WallPostDetailActivity.this)) {
                    Dialogs.showNetworkNotAvailibleDialog(WallPostDetailActivity.this);
                    return;
                }
                RestClient.getInstance(WallPostDetailActivity.this).getApiService().unFollowUser(WallPostDetailActivity.this.user.getSession_key(), post.getUser().getId()).enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        if (response.isSuccessful() || response.body() != null) {
                            Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                                @Override
                                public void success() {
                                    finishActivityAndNotifyPrevious();
                                }

                                @Override
                                public void sessionExpire() {
                                    onSessionExpire();
                                }

                                @Override
                                public void knownError(String errorMsg) {
                                    CustomeToast.ShowCustomToast(WallPostDetailActivity.this, errorMsg, Gravity.TOP);
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
            break;
            case 4: {
                Bundle b = new Bundle();
                b.putSerializable(Constants.POST_EXTRA, post);
                b.putInt("type_int", 1);
                Utility.launchActivityForResult(WallPostDetailActivity.this, WallNewPostActivity.class, b, Flags.ACTIVITY_COMMUNICATION_FOR_UPDATE_FLAG);
            }
            break;
            case 5: {
                new SweetAlertDialog(WallPostDetailActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                                pd.show(((FragmentActivity) WallPostDetailActivity.this).getSupportFragmentManager(), "pd");

                                RestClient.getInstance(WallPostDetailActivity.this).getApiService().deletePost(user.getSession_key(), post.getId()).
                                        enqueue(new Callback<GeneralResponse>() {
                                            @Override
                                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
//                                                                pDialog.dismiss();
                                                pd.dismiss();
                                                if (response.isSuccessful() || response.body() != null) {
                                                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                                                        @Override
                                                        public void success() {
                                                            post = null;
                                                            finishActivityAndNotifyPrevious();
                                                        }

                                                        @Override
                                                        public void sessionExpire() {
                                                            onSessionExpire();
                                                        }

                                                        @Override
                                                        public void knownError(String errorMsg) {
                                                            CustomeToast.ShowCustomToast(WallPostDetailActivity.this, errorMsg, Gravity.TOP);
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
                Utility.launchActivityForResult(WallPostDetailActivity.this, WallNewPostActivity.class, b, Flags.ACTIVITY_COMMUNICATION_FOR_UPDATE_FLAG);
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Flags.SESSION_OUT) {
            onSessionExpire();
            return;
        }
        if (requestCode == Flags.PICTURE_CAPTUREING_REQUEST && data != null && data.getExtras() != null) {
            String path = data.getExtras().getString("file_path_arg", null);
            boolean isVideo = data.getExtras().getBoolean("isVideo", false);
            if (path != null || !path.trim().isEmpty()) {
                if (Utility.isNetworkAvailable(getApplicationContext())) {
                    if (isVideo) {
//                        dialog.setMessage("Uploading Video...");
                        dialog.show(getSupportFragmentManager(), "Toast");
                        new UploadVideoAPIcall(WallPostDetailActivity.this, URL.addPostVideoURL, path, user.getSession_key(), WallPostDetailActivity.this, APIActions.ApiActions.wall_post_video_upload);
                    } else {
                        Bitmap bitmapOrg = BitmapFactory.decodeFile(path);
                        bitmapOrg = checkRotation(bitmapOrg, path);
                        dialog.show(getSupportFragmentManager(), "Toast");
//                        new UploadImageAPIcall(WallPostDetailActivity.this, URL.addPostImageURL, new BitmapDrawable(getResources(), bitmapOrg), user.getSession_key(), WallPostDetailActivity.this, APIActions.ApiActions.wall_post_img_upload);
                        new UploadImageAPIcall(WallPostDetailActivity.this, URL.addPostImageURL, path, user.getSession_key(), WallPostDetailActivity.this, APIActions.ApiActions.wall_post_img_upload);
                    }
                }
            } else {
                // nothing return Picture action
            }
        }
        if (resultCode == Flags.NOTIFY_DELETE || resultCode == Flags.ACTIVITY_COMMUNICATION_FOR_UPDATE_RESULT) {
            finishActivityAndNotifyPrevious();
            return;
        }
        if (requestCode == Flags.ACTIVITIES_COMMUNICATION_FLAG && data != null && data.getExtras() != null) {
            if (resultCode == Flags.NOTIFY && data != null && data.getExtras() != null) {
                Post temp = (Post) data.getExtras().getSerializable(Constants.POST_EXTRA);
                if (temp != null) {
                    post = temp;
                    temp = null;
                }
                showView();
            } else if (resultCode == Flags.NOTIFY_NEW_ELEMIENT && data != null && data.getExtras() != null) {
                if (extraPost == null) {
                    extraPost = new ExtraPost();
                }
                ExtraPost temp = (ExtraPost) data.getExtras().getSerializable(Constants.POST_EXTRA);
                if (temp != null && !temp.getPosts().isEmpty()) {
                    extraPost.getPosts().addAll(temp.getPosts());
                    temp = null;
                }
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        /*if (view.getId() == R.id.activity_wall_post_detail_post_like_btn) {
            Dialogs.showPostLikePopUp(view, new Dialogs.OptionClickListener() {
                @Override
                public void onOptionClick(int pos) {
                    submitPostLike(post.getId(), pos + 1);
                }
            });
            return true;
        }*/
        return false;
    }

    @Override
    public void onLinkClick(AutoLinkMode autoLinkMode, String matchedText, MentionTagJsonModel extraJson) {
        switch (autoLinkMode) {
            case MODE_URL: {
                if (matchedText.contains("youtube.com") || matchedText.contains("youtu.be")) {
                    Intent intent = new Intent(WallPostDetailActivity.this, MediPreview.class);
                    intent.putExtra("path", matchedText);
                    intent.putExtra("isvideo", true);
                    intent.putExtra("isvideoyoutube", true);
                    WallPostDetailActivity.this.startActivity(intent);
                } else
                    Utility.launchWebUrl(WallPostDetailActivity.this, matchedText);
            }
            break;
            case MODE_EMAIL: {
                Utility.launchEmailApplication(WallPostDetailActivity.this, matchedText);
            }
            break;
            case MODE_PHONE: {
                Utility.launchPhoneApplication(WallPostDetailActivity.this, matchedText);
            }
            break;
            case MODE_HASHTAG: {

                //todo: hash tag click listener
                if (extraJson == null) {
//                    CustomeToast.ShowCustomToast(WallPostDetailActivity.this, "invalid HashTag Click", Gravity.BOTTOM);
                    return;
                }
                new KeywordClickDialog(extraJson.getValue(), WallPostDetailActivity.this);
//                CustomeToast.ShowCustomToast(WallPostDetailActivity.this, "HashTag \"" + extraJson.getValue() + " with id: " + extraJson.getId() + "\" is clicked.", Gravity.BOTTOM);
            }
            break;
            case MODE_MENTION: {
                //todo: Mention click listener
                if (extraJson != null) {
                    if (extraJson.getType().equalsIgnoreCase("budz")) {
                        Intent budzmap_intetn = new Intent(this, BudzMapDetailsActivity.class);
                        budzmap_intetn.putExtra("budzmap_id", Integer.parseInt(extraJson.getId()));
                        startActivity(budzmap_intetn);
                    } else {
                        isNewScreen = true;
                        GoToProfile(this, Integer.parseInt(extraJson.getId()));
                    }
                } else {
                    Log.d("onLinkClick: ", matchedText);
                }
//                if (extraJson == null) {
////                    CustomeToast.ShowCustomToast(WallPostDetailActivity.this, "invalid Mention Click", Gravity.BOTTOM);
//                    return;
//                }

//                GoToProfile(WallPostDetailActivity.this, Integer.parseInt(extraJson.getId()));
//                CustomeToast.ShowCustomToast(WallPostDetailActivity.this, "Mention \"" + extraJson.getValue() + " with id: " + extraJson.getId() + "\" is clicked.", Gravity.BOTTOM);
            }
            break;
        }
    }

    @Override
    public void onDialogItemClickListener(DialogInterface dialog, Object obj, int type) {
        mentionTagList.setVisibility(View.GONE);
        if (type == 0) {
            edTxtComment.insertMention((FollowingUser) obj);
        } else {
            edTxtComment.insertMention((Tag) obj);
        }
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        dialog.dismiss();
        if (apiActions == APIActions.ApiActions.wall_post_img_upload) {
            isImage = true;
            if (response.contains("error")) {
                if (response.toLowerCase().contains("Session Expired".toLowerCase())) {
                    onSessionExpire();
                }
                return;
            }
            videoPlayIcon.setVisibility(View.GONE);
            if (!response.trim().isEmpty() && response.contains("file")) {
                mediaUploadResponce = new Gson().fromJson(response, WallNewPostActivity.MediaUploadResponce.class);
                if (mediaUploadResponce != null) {
                    Glide.with(WallPostDetailActivity.this).load(URL.images_baseurl + mediaUploadResponce.file).
                            diskCacheStrategy(DiskCacheStrategy.SOURCE).
                            placeholder(R.drawable.place_holder_wall).into(commentUploadedMedia);
                    commentUploadedMediaParent.setVisibility(View.VISIBLE);
                    CustomeToast.ShowCustomToast(WallPostDetailActivity.this, "Media Uploaded!", Gravity.TOP);
                }
            }
        } else if (apiActions == APIActions.ApiActions.wall_post_video_upload) {
            isImage = false;
            if (response.contains("error")) {
                if (response.toLowerCase().contains("Session Expired".toLowerCase())) {
                    onSessionExpire();
                }
                return;
            }
            if (!response.trim().isEmpty() && response.contains("file")) {
                mediaUploadResponce = new Gson().fromJson(response, WallNewPostActivity.MediaUploadResponce.class);
                if (mediaUploadResponce != null) {
                    Glide.with(WallPostDetailActivity.this).load(URL.images_baseurl + mediaUploadResponce.poster).
                            diskCacheStrategy(DiskCacheStrategy.SOURCE).
                            placeholder(R.drawable.place_holder_wall).into(commentUploadedMedia);
                    commentUploadedMediaParent.setVisibility(View.VISIBLE);
                    videoPlayIcon.setVisibility(View.VISIBLE);
                    CustomeToast.ShowCustomToast(WallPostDetailActivity.this, "Media Uploaded!", Gravity.TOP);
                }
            }
        }
    }

    boolean isImage = false;


    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        dialog.dismiss();
        CustomeToast.ShowCustomToast(WallPostDetailActivity.this, "Media Not Uploaded!", Gravity.TOP);
    }

    private int getCursorY() {
        int pos = edTxtComment.getSelectionStart();
        Layout layout = edTxtComment.getLayout();
        int line = layout.getLineForOffset(pos);
        int baseline = layout.getLineBaseline(line);
        int ascent = layout.getLineAscent(line);
        return (baseline + ascent);
    }

    private int getCursorX() {
        int pos = edTxtComment.getSelectionStart();
        Layout layout = edTxtComment.getLayout();
        return (int) layout.getPrimaryHorizontal(pos);
    }

    private String[] getCommentDetails() throws StringIndexOutOfBoundsException, IndexOutOfBoundsException {
        StringBuilder builder = new StringBuilder();
        StringBuilder jsonExtra = new StringBuilder();
        MentionsEditable editable = edTxtComment.getMentionsText().trim();
        jsonExtra.append("[");
        if (editable.getMentionSpans() == null || editable.getMentionSpans().isEmpty()) {
            String[] arrayT = edTxtComment.getText().toString().trim().split(" ");
            for (String aPreJsonData : Splash.keywordList) {

                if (arrayT != null) {
                    if (arrayT.length > 0) {
                        for (int i = 0; i < arrayT.length; i++) {
                            if (arrayT[i].toLowerCase().equalsIgnoreCase(aPreJsonData.toLowerCase())) {
                                arrayT[i] = "#" + aPreJsonData;
                                jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(-5), "tag", aPreJsonData, "#")));
                                jsonExtra.append(",");
                            }
                        }
                    }
                }
            }
            jsonExtra = new StringBuilder(jsonExtra.substring(0, jsonExtra.lastIndexOf(",")));
            jsonExtra.append("]");
            StringBuffer addedd = new StringBuffer();
            for (int i = 0; i < arrayT.length; i++) {
                if (i == 0)
                    addedd.append(arrayT[i] + " ");
                else if (i == arrayT.length - 1) {
                    addedd.append(" " + arrayT[i]);
                } else if (i < arrayT.length) {
                    addedd.append(" " + arrayT[i] + " ");
                }
            }
            return new String[]{addedd.toString(), ""};
        }
        int start = 0;

        for (MentionSpan span : editable.getMentionSpans()) {
            int temp = editable.getSpanStart(span);
            builder.append(editable.toString().substring(start, temp));
            start = temp;
            if (span.getMention() instanceof FollowingUser) {
                FollowingUser user = (FollowingUser) span.getMention();
                String type = "user";
                if (user.isSubUser()) {
                    type = "budz";
                } else {
                    type = "user";
                }
                jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(user.getId()), type, user.getSuggestiblePrimaryText(), "@")));
                jsonExtra.append(",");
                builder.append("@");
            } else {
                Tag user = (Tag) span.getMention();
                jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(user.getId()), "tag", user.getSuggestiblePrimaryText(), "#")));
                jsonExtra.append(",");
                builder.append("#");
            }
            temp = editable.getSpanEnd(span);
            if (temp > (editable.toString().length() - 1)) {
                builder.append(editable.toString().substring(start));
            } else {
                builder.append(editable.toString().substring(start, temp));
            }
            start = temp;
        }
        if (start < editable.toString().length()) {
            builder.append(editable.toString().substring(start));
        }
        if (jsonModels != null && comment != null) {
            for (MentionTagJsonModel aPreJsonData : jsonModels) {
                if (!jsonExtra.toString().trim().toLowerCase().contains(aPreJsonData.getValue().toLowerCase().trim())) {
                    if (edTxtComment.getText().toString().toLowerCase().trim().contains(aPreJsonData.getTrigger().toLowerCase().trim() + aPreJsonData.getValue().toLowerCase().trim())) {
                        if (aPreJsonData.getTrigger().trim().equalsIgnoreCase("@")) {
                            if (aPreJsonData.getType().trim().equalsIgnoreCase("user")) {
                                jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(aPreJsonData.getId()), "user", aPreJsonData.getValue(), "@")));
                                jsonExtra.append(",");
                            } else {
                                jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(aPreJsonData.getId()), "budz", aPreJsonData.getValue(), "@")));
                                jsonExtra.append(",");
                            }
                        } else {
                            jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(aPreJsonData.getId()), "tag", aPreJsonData.getValue(), "#")));
                            jsonExtra.append(",");

                        }
                    }
                }
            }
        }
        String[] array = builder.toString().trim().split(" ");
        for (String aPreJsonData : Splash.keywordList) {

            if (array != null) {
                if (array.length > 0) {
                    for (int i = 0; i < array.length; i++) {
                        if (array[i].toLowerCase().equalsIgnoreCase(aPreJsonData.toLowerCase())) {
                            array[i] = "#" + aPreJsonData;
                            jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(-5), "tag", aPreJsonData, "#")));
                            jsonExtra.append(",");
                        }
                    }
                }
            }
        }
        jsonExtra = new StringBuilder(jsonExtra.substring(0, jsonExtra.lastIndexOf(",")));
        jsonExtra.append("]");
        StringBuffer addedd = new StringBuffer();
        int ll = array.length - 1;
        for (int i = 0; i < array.length; i++) {
            if (i == 0)
                addedd.append(array[i] + " ");
            else if (i == ll) {
                addedd.append(" " + array[i]);
            } else if (i < array.length) {
                addedd.append(" " + array[i] + " ");
            }
        }
        return new String[]{addedd.toString().trim(), jsonExtra.toString()};
    }

    @Override
    public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
        switch (autoLinkMode) {
            case MODE_MENTION:
            case MODE_HASHTAG: {
                MentionTagJsonModel temp = null;
                if (post.getJsonData() != null)
                    for (MentionTagJsonModel mentionTagJsonModel : post.getJsonData()) {
                        if (mentionTagJsonModel.getValue().trim().contains(matchedText.trim().substring(1).replace("_", " "))) {
                            temp = mentionTagJsonModel;
                            break;
                        }
                    }
                WallPostDetailActivity.this.onLinkClick(autoLinkMode, matchedText, temp);
            }
            break;
            default: {
                WallPostDetailActivity.this.onLinkClick(autoLinkMode, matchedText, null);
            }
        }
    }

    @Override
    public void onDataReady(final Preview preview) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                scrapView.setMessage(preview.getLink(), ContextCompat.getColor(WallPostDetailActivity.this, R.color.color_link));
                scrapView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onCrossListener(UserLikesAlertDialog dialog) {

    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        isOnFinalPage = false;
        callAPI();
    }
}
