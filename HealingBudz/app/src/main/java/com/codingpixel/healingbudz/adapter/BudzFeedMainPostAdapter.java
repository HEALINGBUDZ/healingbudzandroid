package com.codingpixel.healingbudz.adapter;
/*
 * Created by M_Muzammil Sharif on 06-Mar-18.
 */


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Layout;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.User;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.Utilities.Dialogs;
import com.codingpixel.healingbudz.Utilities.Flags;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.activity.Wall.WallNewPostActivity;
import com.codingpixel.healingbudz.activity.Wall.WallSinglton;
import com.codingpixel.healingbudz.activity.Wall.dialogue.UserLikesAlertDialog;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CircularImageView;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.FontCache;
import com.codingpixel.healingbudz.customeUI.HealingBudTextViewBold;
import com.codingpixel.healingbudz.customeUI.HealingBudTextViewItalic;
import com.codingpixel.healingbudz.customeUI.Preview;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ApiStatusCallBack;
import com.codingpixel.healingbudz.interfaces.AutoLinkTextLinkClickListener;
import com.codingpixel.healingbudz.interfaces.RecyclerViewItemClickListener;
import com.codingpixel.healingbudz.interfaces.ScrollPositionEndCallBack;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.BudzFeedModel.AddComment.AddCommentResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.AddComment.Attachment;
import com.codingpixel.healingbudz.network.BudzFeedModel.AddComment.Comment;
import com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel.FollowingUser;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.File;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.Post;
import com.codingpixel.healingbudz.network.BudzFeedModel.MentionTagJsonModel;
import com.codingpixel.healingbudz.network.BudzFeedModel.ReportModel.FlagPost;
import com.codingpixel.healingbudz.network.BudzFeedModel.TagModel.Tag;
import com.codingpixel.healingbudz.network.BudzFeedModel.submitPostLike.Like;
import com.codingpixel.healingbudz.network.BudzFeedModel.submitPostLike.SubmitPostLikeResponse;
import com.codingpixel.healingbudz.network.RestClient;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;
import com.codingpixel.healingbudz.network.upload_image.UploadVideoAPIcall;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.DiscussQuestionFragment.isNewScreen;
import static com.codingpixel.healingbudz.adapter.BudzFeedPostDetailCommentsAdapter.isLikedCommentUser;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;
import static com.facebook.FacebookSdk.getApplicationContext;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

public class BudzFeedMainPostAdapter extends RecyclerView.Adapter<BudzFeedMainPostAdapter.BudzFeedMainPostVH> implements UserLikesAlertDialog.OnDialogFragmentClickListener {
    private List<Post> dataSet;
    private RecyclerViewItemClickListener listener;
    private User user;
    public static boolean isLikedUser = false;
    Context mContext;
    private AutoLinkTextLinkClickListener linkClickListener;
    public CameraDelegade cameraDelegade;
    Fragment fragment;

    public BudzFeedMainPostAdapter(Fragment fragment, User user, @NonNull RecyclerViewItemClickListener clickListener, @NonNull AutoLinkTextLinkClickListener autoLinkOnClickListener, Context context) {
        this.linkClickListener = autoLinkOnClickListener;
        this.user = user;
        this.listener = clickListener;
        this.mContext = context;
        this.fragment = fragment;

    }

    public List<Post> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<Post> dataSet) {
        this.dataSet = dataSet;
        this.notifyDataSetChanged();
    }

    public void updatePostLike(Integer postId, Like like) {
        if (dataSet == null || dataSet.isEmpty()) {
            return;
        }
        for (int i = 0; i < dataSet.size(); i++) {
            if (dataSet.get(i).getId().equals(postId)) {
                if (dataSet.get(i).getLikes() == null || dataSet.get(i).getLikes().isEmpty()) {
                    dataSet.get(i).setLikes(new ArrayList<Like>());
                    dataSet.get(i).getLikes().add(like);
                    if (like.getIsLike() > 0) {
                        dataSet.get(i).setLikesCount(dataSet.get(i).getLikesCount() + 1);
                    }
                    notifyDataSetChanged();
                    return;
                } else {
                    for (int j = 0; j < dataSet.get(i).getLikes().size(); j++) {
                        if (like.getId() == (dataSet.get(i).getLikes().get(j).getId())) {
                            dataSet.get(i).getLikes().set(j, like);
                            if (like.getIsLike() > 0) {
                                dataSet.get(i).setLikesCount(dataSet.get(i).getLikesCount() + 1);
                            } else {
                                dataSet.get(i).setLikesCount(dataSet.get(i).getLikesCount() - 1);
                            }
                            notifyDataSetChanged();
                            return;
                        }
                    }
                    dataSet.get(i).getLikes().add(like);
                    if (like.getIsLike() > 0) {
                        dataSet.get(i).setLikesCount(dataSet.get(i).getLikesCount() + 1);
                    }
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    public void addToDataSet(@NonNull Post post) {
        if (dataSet == null) {
            dataSet = new ArrayList<>();
        }
        if (dataSet.isEmpty()) {
            dataSet.add(post);
            notifyDataSetChanged();
            return;
        }
        LinkedHashMap<Integer, Post> temp = new LinkedHashMap<>();
        for (Post post1 : dataSet) {
            temp.put(post1.getId(), post1);
        }
        dataSet = new ArrayList<>();
        if (temp.containsKey(post.getId())) {
            temp.put(post.getId(), post);
        } else {
            dataSet.add(post);
        }
        dataSet.addAll(temp.values());
        temp.clear();
        temp = null;
        notifyDataSetChanged();
    }

    public void updateToDataSet(@NonNull List<Post> posts) {
        if (dataSet == null || dataSet.isEmpty()) {
            dataSet = posts;
            notifyDataSetChanged();
            return;
        }
        if (posts == null || posts.isEmpty()) {
            return;
        }
        LinkedHashMap<Integer, Post> temp = new LinkedHashMap<>();
        for (Post post : dataSet) {
            temp.put(post.getId(), post);
        }
        dataSet = new ArrayList<>();
        for (Post post : posts) {
            if (temp.containsKey(post.getId())) {
                temp.put(post.getId(), post);
            } else {
                dataSet.add(post);
            }
        }
        dataSet.addAll(temp.values());
        temp.clear();
        temp = null;
        notifyDataSetChanged();
    }

    public void addToDataSet(@NonNull List<Post> posts) {
        if (dataSet == null || dataSet.isEmpty()) {
            dataSet = posts;
            notifyDataSetChanged();
            return;
        }
        if (posts == null || posts.isEmpty()) {
            return;
        }
        LinkedHashMap<Integer, Post> temp = new LinkedHashMap<>();
        for (Post post : dataSet) {
            temp.put(post.getId(), post);
        }
        for (Post post : posts) {
            temp.put(post.getId(), post);
        }
        dataSet = new ArrayList<>(temp.values());
        temp.clear();
        temp = null;
        notifyDataSetChanged();
    }

    private void onTaggedPeopleClicked(FollowingUser obj) {
        isNewScreen = true;
        GoToProfile(mContext, obj.getId());
//        CustomeToast.ShowCustomToast(WallPostDetailActivity.this, "Tagged Person Clicked with name: " + obj.getFirstName() + "<&> id: " + obj.getId(), Gravity.BOTTOM);
    }

    @NonNull
    @Override
    public BudzFeedMainPostVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BudzFeedMainPostVH(LayoutInflater.from(mContext).inflate(R.layout.vh_budz_feed_main_post, parent, false));

    }

    @Override
    public void onBindViewHolder(final BudzFeedMainPostVH holder, final int position) {
//        if (position != 0 && position % 10 == 0) {
////            holder.main_layout_whole.setVisibility(View.GONE);
//            holder.adView.setVisibility(View.VISIBLE);
//
//        } else {
////            holder.main_layout_whole.setVisibility(View.VISIBLE);
//            holder.adView.setVisibility(View.GONE);
//        }

        holder.adView.setVisibility(View.GONE);
//        cameraDelegade = holder.cameraDelegade;
        final int pos = position;
        holder.personName.setTextColor(Color.parseColor(Utility.getBudColor(dataSet.get(pos).getUser().getPoints())));
        if (dataSet.get(pos).getJson_Data() == null || (dataSet.get(pos).getJson_Data() != null && dataSet.get(pos).getJson_Data().length() < 3)) {
            holder.postText.setMentionModeColor(Color.parseColor("#c4c4c4"));
            holder.postText.setCustomModeColor(Color.parseColor("#c4c4c4"));
            holder.postText.setHashtagModeColor(Color.parseColor("#c4c4c4"));
        } else {

            holder.postText.setMentionModeColor(ContextCompat.getColor(holder.postText.getContext(), R.color.post_description_links_color));
            holder.postText.setHashtagModeColor(ContextCompat.getColor(holder.postText.getContext(), R.color.mention_color));
            holder.postText.setCustomModeColor(ContextCompat.getColor(holder.postText.getContext(), R.color.mention_color));
        }
//        holder.postText.setMentionModeColor(Color.parseColor(Utility.getBudColor(dataSet.get(pos).getUser().getPoints())));
//        holder.likeTxtTemp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (dataSet.get(pos).getLikes().size() > 0 && dataSet.get(pos).getLikesCount() > 0) {
//                    if (dataSet.get(pos).getLikes().size() == dataSet.get(pos).getLikesCount()) {
//                        UserLikesAlertDialog userLikesAlertDialog = UserLikesAlertDialog.newInstance(BudzFeedMainPostAdapter.this, dataSet.get(pos).getLikes());
//                        userLikesAlertDialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "pd");
//                    } else {
//                        dataSet.get(pos).getLikes().remove(dataSet.get(pos).getLikes().size() - 1);
//                        UserLikesAlertDialog userLikesAlertDialog = UserLikesAlertDialog.newInstance(BudzFeedMainPostAdapter.this, dataSet.get(pos).getLikes());
//                        userLikesAlertDialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "pd");
//                        notifyItemChanged(position);
//                    }
////                    for (int i = 0; i < post.getLikes().size(); i++) {
////                        Log.d("onClick: ", "" + post.getLikes().get(i).getUser().toString());
////                    }
//                }
//            }
//        });
        Collections.sort(dataSet.get(pos).getComments(), new Comparator<Comment>() {
            @Override
            public int compare(Comment comment, Comment t1) {
                return t1.getId().compareTo(comment.getId());
            }
        });
        holder.showView(dataSet.get(pos), pos, holder);
        if (callBack != null && pos == 0) {
            callBack.onScrollPositionStart();
        }
        if (callBack != null && pos == dataSet.size() - 1) {
            callBack.onScrollPositionEnd();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(dataSet.get(pos), pos, -1);
            }
        });
        holder.scrapView.setVisibility(View.GONE);
        holder.scrapView.clear();
        if (dataSet.get(pos).getFiles() == null || dataSet.get(pos).getFiles().isEmpty()) {
            holder.many_data.setVisibility(View.GONE);
            holder.scrapView.clear();
            holder.scrapView.setVisibility(View.GONE);
            holder.scrapView.clear();
            holder.itemView.findViewById(R.id.vh_budz_feed_main_post_imgs_viewpager_parent).setVisibility(View.GONE);
            List<String> urls = Utility.extractURL(dataSet.get(pos).getDescription());
            if (urls == null || urls.isEmpty()) {
                holder.scrapView.clear();
                holder.scrapView.setVisibility(View.GONE);
                holder.scrapView.clear();
            } else {
                if (urls.get(0).length() > 8 && (urls.get(0).contains(".com") || urls.get(0).contains(".be") || urls.get(0).contains("http") || urls.get(0).contains("https"))) {
                    holder.scrapView.setVisibility(View.VISIBLE);
                    holder.scrapView.setData(urls.get(0));
                } else {
                    holder.scrapView.clear();
                    holder.scrapView.setVisibility(View.GONE);
                    holder.scrapView.clear();
                }
            }

        } else {
            holder.scrapView.clear();
            holder.scrapView.setVisibility(View.GONE);
            holder.scrapView.clear();
            holder.itemView.findViewById(R.id.vh_budz_feed_main_post_imgs_viewpager_parent).setVisibility(View.VISIBLE);
            List<File> test = new ArrayList<>();
            test.add(dataSet.get(pos).getFiles().get(0));
            if (dataSet.get(pos).getFiles().size() > 1) {
                holder.many_data.setVisibility(View.VISIBLE);
            } else {
                holder.many_data.setVisibility(View.GONE);
            }


            holder.indicatorView.setCount(holder.adapter.getCount());
        }

        holder.scrapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkClickListener.onLinkClick(AutoLinkMode.MODE_URL, holder.scrapView.getUrl(), null);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {


    }

    @Override
    public int getItemCount() {
        if (dataSet == null) {
            return 0;
        }
        return dataSet.size();
    }

    public void removePost(Post o) {
        if (dataSet == null || dataSet.isEmpty() || o == null) {
            return;
        }
        LinkedHashMap<Integer, Post> temp = new LinkedHashMap<Integer, Post>();
        for (Post post : dataSet) {
            temp.put(post.getId(), post);
        }
        temp.remove(o.getId());
        dataSet = new ArrayList<>(temp.values());
        temp.clear();
        temp = null;
        notifyDataSetChanged();
    }

    @Override
    public void onCrossListener(UserLikesAlertDialog dialog) {

    }


    class BudzFeedMainPostVH extends RecyclerView.ViewHolder implements View.OnClickListener
            , ViewPager.OnPageChangeListener, Dialogs.OptionClickListener
            , View.OnLongClickListener
            , Preview.PreviewListener, CameraDelegade {
        private ViewPager pager;
        private CameraDelegade cameraDelegade;
        private AutoLinkTextView postText, vh_budz_feed_main_post_text_repost;
        private HealingBudTextViewBold likeTxt, likeTxtTemp, commentTxt, commentTxtTemp, repostCount;
        private HealingBudTextViewBold personName;
        private HealingBudTextViewBold personExtraInfo;
        private CircularImageView personImg;
        private ImageView profile_img_topi;
        private PageIndicatorView indicatorView;
        private BudzFeedMainPostMediaPagerAdapter adapter;
        private HealingBudTextViewItalic timeAgo;
        private ImageView likeIcon;
        private int pos;
        private ImageView personLeafImg, many_data;
        private View postAsParent, repostParent;
        private HealingBudTextViewBold postedAs, repostedPostUser;
        private Preview scrapView;
        private View withParent;
        private LinearLayout vh_budz_feed_main_post_repost_btn, repost_for, comment_activity;
        LinearLayout list_liked_people, not_found, main_content;
        private com.codingpixel.healingbudz.customeUI.ProgressDialog dialog;
        private RecyclerView taggedPeople;
        MentionsEditText activity_wall_post_detail_comment_ed_txt;
        private WallTagPeopleAdapter tagPeopleAdapter;
        private LinearLayout main_layout_whole;
        private AdView adView;
        private View view;
        //////COMENT SECTION
        private RecyclerView commentsList;
        private View commentUploadedMediaParent, videoPlayIcon;
        private BudzFeedPostDetailCommentsAdapter commentsAdapter;
        private MentionAdapter mentionTagAdapter;
        private WallNewPostActivity.MediaUploadResponce mediaUploadResponce;
        private RecyclerView mentionTagList;
        private MentionsEditText edTxtComment;
        private boolean isImage = false;
        private ImageView commentUploadedMedia;

        private void submitCommentLike(final Integer id, int is_like) {
//        if (this == null) {
//            //fragment die...
//            return;
//        }
            if (!Utility.isNetworkAvailable(mContext)) {
                Dialogs.showNetworkNotAvailibleDialog(mContext);
                return;
            }
            RestClient.getInstance(mContext).getApiService().submitCommentLike(user.getSession_key(), id, is_like).enqueue(new Callback<SubmitPostLikeResponse>() {
                @Override
                public void onResponse(@NonNull Call<SubmitPostLikeResponse> call, @NonNull final Response<SubmitPostLikeResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                            @Override
                            public void success() {
//                            isLikedUser = false;

                                commentsAdapter.updatePostLike(id, response.body().getSuccessData());
                                commentsAdapter.notifyDataSetChanged();
                                notifyItemChanged(getAdapterPosition());
                                notifyDataSetChanged();
                                isLikedCommentUser = false;

                            }

                            @Override
                            public void sessionExpire() {
                                onSessionExpire();
                            }

                            @Override
                            public void knownError(String errorMsg) {
                                CustomeToast.ShowCustomToast(mContext, errorMsg, Gravity.TOP);
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
                    mentionTagList.getLayoutParams().height = Utility.getDeviceHeight(mContext) / 4;
                } else {
                    mentionTagList.getLayoutParams().height = FrameLayout.LayoutParams.WRAP_CONTENT;
                }
                showMentionTagDropDown();
            } else {
                mentionTagList.setVisibility(View.GONE);
                if (scrll != null)
                    scrll.setScrollingEnabled(true);
            }
        }

        private int dw = 0;

        public void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                            dialog.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "Toast");
                            new UploadVideoAPIcall(mContext, URL.addPostVideoURL, path, user.getSession_key()
                                    , new APIResponseListner() {
                                @Override
                                public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                                    dialog.dismiss();

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
                                            Glide.with(mContext).load(URL.images_baseurl + mediaUploadResponce.poster).
//                                                    asBitmap().
        diskCacheStrategy(DiskCacheStrategy.SOURCE).
                                                    placeholder(R.drawable.place_holder_wall).into(commentUploadedMedia);
                                            commentUploadedMediaParent.setVisibility(View.VISIBLE);
                                            videoPlayIcon.setVisibility(View.VISIBLE);
                                            CustomeToast.ShowCustomToast(mContext, "Media Uploaded!", Gravity.TOP);
                                        }
                                    }
                                }

                                @Override
                                public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                    dialog.dismiss();
                                    isImage = false;
                                }
                            }, APIActions.ApiActions.wall_post_video_upload);
                        } else {
                            Bitmap bitmapOrg = BitmapFactory.decodeFile(path);
                            bitmapOrg = checkRotation(bitmapOrg, path);
                            dialog.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "Toast");
                            new UploadImageAPIcall(mContext, URL.addPostImageURL
                                    , path, user.getSession_key()
                                    , new APIResponseListner() {
                                @Override
                                public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                                    dialog.dismiss();
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
                                            Glide.with(mContext).load(URL.images_baseurl + mediaUploadResponce.file).
//                                                    asBitmap().
        diskCacheStrategy(DiskCacheStrategy.SOURCE).
                                                    placeholder(R.drawable.place_holder_wall).into(commentUploadedMedia);
                                            commentUploadedMediaParent.setVisibility(View.VISIBLE);
                                            CustomeToast.ShowCustomToast(mContext, "Media Uploaded!", Gravity.TOP);
                                        }
                                    }

                                }

                                @Override
                                public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                    dialog.dismiss();
                                    isImage = false;
                                }
                            }, APIActions.ApiActions.wall_post_img_upload);
                        }
                    }
                } else {
                    // nothing return Picture action
                }
            }
        }

        private int getCursorX() {
            int pos = edTxtComment.getSelectionStart();
            Layout layout = edTxtComment.getLayout();
            return (int) layout.getPrimaryHorizontal(pos);
        }

        private void showUnknownSeverErrorDialog() {

        }

        private void onUserClick(com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.User clickUser) {
            isNewScreen = true;
            GoToProfile(mContext, clickUser.getId());
//        Toast.makeText(WallPostDetailActivity.this, clickUser.getFirstName() + " " + clickUser.getLastName() + " is clicked", Toast.LENGTH_SHORT).show();
        }

        private void showMentionTagDropDown() {
            mentionTagList.setVisibility(View.VISIBLE);
            if (scrll != null)
                scrll.setScrollingEnabled(false);
            final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mentionTagList.getLayoutParams();
            final int x = getCursorX();
            //reset all params
            params.setMargins(0, 5, 0, params.bottomMargin);
//            params.setgravity = Gravity.LEFT | Gravity.START;
            //get dowpdown list width
            mentionTagList.post(new Runnable() {
                @Override
                public void run() {
                    dw = mentionTagList.getMeasuredWidth();
                    if (x > (dw / 2)) {
                        if (x < (Utility.getDeviceWidth(mContext) - (dw / 2))) {
                            params.leftMargin = (x - (dw / 2));
                        } else {
//                            params.gravity = Gravity.CENTER;//Gravity.END | Gravity.RIGHT;
                        }
                    }
                    mentionTagList.setLayoutParams(params);
                }
            });
            //defining x position of dropdown accounding to cursor x position

        }

        private void onSessionExpire() {
//            Utility.finishWithResult(WallPostDetailActivity.this, null, Flags.SESSION_OUT);
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
//            if (comment != null) {
//                for (int i = 0; i < users.size(); i++) {
//                    if (editable.toString().toLowerCase().trim().contains("@" + users.get(i).getFirstName().toLowerCase().trim())) {
////                    users.remove(i);
//                        usersDelete.add(users.get(i));
//                    }
//                }
//                for (int i = 0; i < usersDelete.size(); i++) {
//                    users.remove(usersDelete.get(i));
//                }
//                usersDelete.clear();
//            }
            if (!users.isEmpty()) {
                mentionTagAdapter.setMentions(users);
                if (users.size() > 3) {
                    mentionTagList.getLayoutParams().height = Utility.getDeviceHeight(mContext) / 4;
                } else {
                    mentionTagList.getLayoutParams().height = FrameLayout.LayoutParams.WRAP_CONTENT;
                }
                showMentionTagDropDown();
            } else {
                mentionTagList.setVisibility(View.GONE);
                if (scrll != null)
                    scrll.setScrollingEnabled(true);
            }
        }

        private void initMentionEditText() {
            edTxtComment.setTokenizer(new WordTokenizer(new WordTokenizerConfig.Builder().
                    setExplicitChars("#@").setThreshold(1).setMaxNumKeywords(1).build()));
            mentionTagAdapter = new MentionAdapter(null, new Dialogs.DialogItemClickListener() {
                @Override
                public void onDialogItemClickListener(DialogInterface dialog, Object obj, int type) {
                    mentionTagList.setVisibility(View.GONE);
                    if (scrll != null)
                        scrll.setScrollingEnabled(true);
                    if (type == 0) {
                        edTxtComment.insertMention((FollowingUser) obj);
                    } else {
                        edTxtComment.insertMention((Tag) obj);
                    }
                }
            });
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
                            if (scrll != null)
                                scrll.setScrollingEnabled(true);
                        }
                    } else if (queryToken.getExplicitChar() == '@') {
                        if (queryToken.getKeywords().toLowerCase().length() > 0)
                            showMentionList(queryToken.getKeywords().toLowerCase());
                        else {
                            mentionTagList.setVisibility(View.GONE);
                            if (scrll != null)
                                scrll.setScrollingEnabled(true);
                        }
                    } else {
                        mentionTagList.setVisibility(View.GONE);
                        if (scrll != null)
                            scrll.setScrollingEnabled(true);
                    }
                    return null;
                }
            });
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
//            if (jsonModels != null && comment != null) {
//                for (MentionTagJsonModel aPreJsonData : jsonModels) {
//                    if (!jsonExtra.toString().trim().toLowerCase().contains(aPreJsonData.getValue().toLowerCase().trim())) {
//                        if (edTxtComment.getText().toString().toLowerCase().trim().contains(aPreJsonData.getTrigger().toLowerCase().trim() + aPreJsonData.getValue().toLowerCase().trim())) {
//                            if (aPreJsonData.getTrigger().trim().equalsIgnoreCase("@")) {
//                                if (aPreJsonData.getType().trim().equalsIgnoreCase("user")) {
//                                    jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(aPreJsonData.getId()), "user", aPreJsonData.getValue(), "@")));
//                                    jsonExtra.append(",");
//                                } else {
//                                    jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(aPreJsonData.getId()), "budz", aPreJsonData.getValue(), "@")));
//                                    jsonExtra.append(",");
//                                }
//                            } else {
//                                jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(aPreJsonData.getId()), "tag", aPreJsonData.getValue(), "#")));
//                                jsonExtra.append(",");
//
//                            }
//                        }
//                    }
//                }
//            }
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

        private void submitComment(final Post post, String comment, String json_data, String attachement, String type, String poster, String thumb) {
            Utility.hideKeyboard((AppCompatActivity) mContext);
            if (!Utility.isNetworkAvailable(mContext)) {
                Dialogs.showNetworkNotAvailibleDialog(mContext);
                return;
            }
//        dialog.setMessage("Submitting Comment...");
            dialog.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "Toast");

            RestClient.getInstance(getApplicationContext()).getApiService().
                    submitPostComment(user.getSession_key(), post.getId(), comment, json_data, attachement, type, poster, thumb).enqueue(new Callback<AddCommentResponse>() {
                @Override
                public void onResponse(Call<AddCommentResponse> call, final Response<AddCommentResponse> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                            @Override
                            public void success() {
                                putComment(post, response.body().getSuccessData().getComments());
                                mediaUploadResponce = null;
                                commentUploadedMediaParent.setVisibility(View.GONE);
//                                commentsList.scrollToPosition(commentsAdapter.getItemCount() - 1);
                            }

                            @Override
                            public void sessionExpire() {
                                onSessionExpire();
                            }

                            @Override
                            public void knownError(String errorMsg) {
                                CustomeToast.ShowCustomToast(mContext, errorMsg, Gravity.TOP);
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

        private void putComment(Post post, final Comment comments) {
            try {
                edTxtComment.setText("");
                if (post.getComments() == null || post.getComments().isEmpty()) {
                    post.setComments(new ArrayList<Comment>());
                    post.getComments().add(comments);
                    post.setCommentsCount(post.getCommentsCount() + 1);
                    commentTxt.setText(String.valueOf(post.getCommentsCount()) + " Comments");
                    commentsAdapter.setComments(post.getComments());
//                        scrollToEnd();
//                    scrollToStart();
                    return;
                }
//                for (int j = 0; j < post.getComments().size(); j++) {
//                    if (comments.getId().equals(post.getComments().get(j).getId())) {
//                        post.getComments().set(j, comments);
//                        commentsAdapter.setComments(post.getComments());
////                            scrollToEnd();
//                        scrollToStart();
//                        return;
//                    }
//                }
//                if (comment != null) {
//                    for (int j = 0; j < post.getComments().size(); j++) {
//                        if (comment.getId().equals(post.getComments().get(j).getId())) {
//                            post.getComments().set(j, comments);
//                            commentsAdapter.setComments(post.getComments());
////                            scrollToEnd();
////                                scrollToStart();
//                            clearEdit();
//                            return;
//                        }
//                    }
//                } else {
//                    clearEdit();
//
//                }
                post.getComments().add(comments);
                post.setCommentsCount(post.getCommentsCount() + 1);
                commentTxt.setText(String.valueOf(post.getCommentsCount()) + " Comments");

                if (post.getComments().size() > 2) {

                    Collections.sort(post.getComments(), new Comparator<Comment>() {
                        @Override
                        public int compare(Comment comment, Comment t1) {
                            return t1.getId().compareTo(comment.getId());
                        }
                    });
                    commentsAdapter.setComments(post.getComments().subList(0, 2));
                } else {
                    commentsAdapter.setComments(post.getComments());
                }

                commentsAdapter.notifyDataSetChanged();
                notifyDataSetChanged();

//                    scrollToEnd();
//                scrollToStart();
            } catch (Exception e) {
                //
            }
        }

        BudzFeedMainPostVH(View itemView) {
            super(itemView);
            view = itemView;
            //COMMENT

            videoPlayIcon = itemView.findViewById(R.id.activity_wall_post_detail_uploaded_media_video);
            commentUploadedMedia = (ImageView) itemView.findViewById(R.id.activity_wall_post_detail_uploaded_media);
            commentUploadedMediaParent = itemView.findViewById(R.id.activity_wall_post_detail_uploaded_media_parent);
            mentionTagList = (RecyclerView) itemView.findViewById(R.id.activity_wall_post_detail_comment_tag_list);
            mentionTagList.setLayoutManager(new LinearLayoutManager(mContext));
            edTxtComment = (MentionsEditText) itemView.findViewById(R.id.activity_wall_post_detail_comment_ed_txt);
            Typeface st = FontCache.getTypeface("Lato-Light.ttf", mContext);
            edTxtComment.setTypeface(st);
            commentsList = (RecyclerView) itemView.findViewById(R.id.activity_wall_post_detail_post_comments_list);
//        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_comment);
//        refreshLayout.setColorSchemeColors(Color.parseColor("#0083cb"));
//        refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
//        refreshLayout.setOnRefreshListener(WallPostDetailActivity.this);
            commentsList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true));
            commentsList.getLayoutManager().setAutoMeasureEnabled(true);
            commentsList.setNestedScrollingEnabled(false);
            //COMMENT
            main_layout_whole = itemView.findViewById(R.id.main_layout_whole);
            activity_wall_post_detail_comment_ed_txt = itemView.findViewById(R.id.activity_wall_post_detail_comment_ed_txt);
//            st = FontCache.getTypeface("Lato-Light.ttf", mContext);
            activity_wall_post_detail_comment_ed_txt.setTypeface(st);
//            comment_activity = itemView.findViewById(R.id.comment_activity);
            many_data = itemView.findViewById(R.id.many_data);
            adView = itemView.findViewById(R.id.adView);
            repost_for = itemView.findViewById(R.id.repost_for);
            vh_budz_feed_main_post_text_repost = itemView.findViewById(R.id.vh_budz_feed_main_post_text_repost);
//            adView.setAdSize(AdSize.BANNER);
//            adView.setAdUnitId(String.valueOf(itemView.getContext().getResources().getString(R.string.ad_mob_id_add)));
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(itemView.getContext().getString(R.string.ad_mob_id_did)).build();
            adView.loadAd(adRequest);
            taggedPeople = itemView.findViewById(R.id.vh_budz_feed_main_post_tag_people_list);
            vh_budz_feed_main_post_repost_btn = itemView.findViewById(R.id.vh_budz_feed_main_post_repost_btn);
            withParent = itemView.findViewById(R.id.vh_budz_feed_main_post_with_parent);
            likeIcon = itemView.findViewById(R.id.vh_budz_feed_main_post_like_img);
            timeAgo = itemView.findViewById(R.id.vh_budz_feed_main_post_time);
            commentTxt = itemView.findViewById(R.id.vh_budz_feed_main_post_comment_txt);
            commentTxtTemp = itemView.findViewById(R.id.commentTxtTemp);
            likeTxt = itemView.findViewById(R.id.vh_budz_feed_main_post_like_txt);
            likeTxtTemp = itemView.findViewById(R.id.vh_budz_feed_main_post_like_txt_temp);
            postText = itemView.findViewById(R.id.vh_budz_feed_main_post_text);
//            Typeface face = Typeface.createFromAsset(mContext.getAssets(),
//                    "fonts/epimodem.ttf");
            Typeface customFont = FontCache.getTypeface("Lato-Light.ttf", mContext);
            postText.setTypeface(customFont);
            pager = itemView.findViewById(R.id.vh_budz_feed_main_post_imgs_viewpager);
            personName = itemView.findViewById(R.id.vh_budz_feed_main_post_person_name);
            personLeafImg = itemView.findViewById(R.id.vh_budz_feed_main_post_person_leaf);
            personExtraInfo = itemView.findViewById(R.id.vh_budz_feed_main_post_person_extra_info);
            personImg = itemView.findViewById(R.id.vh_budz_feed_main_post_person_img);
            profile_img_topi = itemView.findViewById(R.id.profile_img_topi);
            indicatorView = itemView.findViewById(R.id.vh_budz_feed_main_post_imgs_viewpager_indicator);

            repostCount = itemView.findViewById(R.id.vh_budz_feed_main_post_repost_txt);
            postAsParent = itemView.findViewById(R.id.vh_budz_feed_main_post_posted_as_parent);
            repostParent = itemView.findViewById(R.id.vh_budz_feed_main_post_reposted_parent);
            postedAs = itemView.findViewById(R.id.vh_budz_feed_main_post_posted_as);
            repostedPostUser = itemView.findViewById(R.id.vh_budz_feed_main_post_reposted_user_name);

            scrapView = itemView.findViewById(R.id.vh_budz_feed_main_post_link_scrap);
            scrapView.setListener(BudzFeedMainPostVH.this);
//            scrapView.setDescriptionTextColor(Color.GRAY);
//            scrapView.setTitleTextColor(Color.YELLOW);
//            scrapView.setSiteNameTextColor(Color.BLUE);
//            scrapView.getImgViewImage().setCornerRadiiDP(10, 10, 0, 0);
//, AutoLinkMode.MODE_MENTION , AutoLinkMode.MODE_HASHTAG , AutoLinkMode.MODE_CUSTOM
            postText.addAutoLinkMode(AutoLinkMode.MODE_URL, AutoLinkMode.MODE_EMAIL);
            postText.setUrlModeColor(ContextCompat.getColor(itemView.getContext(), R.color.url_color));
            postText.setMentionModeColor(ContextCompat.getColor(itemView.getContext(), R.color.post_description_links_color));
            postText.setCustomModeColor(ContextCompat.getColor(itemView.getContext(), R.color.post_description_links_color));
            postText.setHashtagModeColor(ContextCompat.getColor(itemView.getContext(), R.color.mention_color));
            postText.setEmailModeColor(ContextCompat.getColor(itemView.getContext(), R.color.post_description_links_color));
            postText.setAutoLinkMask(Linkify.WEB_URLS);
            postText.setMovementMethod(BetterLinkMovementMethod.newInstance());
            Linkify.addLinks(postText, Linkify.WEB_URLS);
            postText.setLinkTextColor(ContextCompat.getColor(itemView.getContext(), R.color.post_description_links_color));
            BetterLinkMovementMethod.linkify(Linkify.WEB_URLS, postText)
                    .setOnLinkClickListener(new BetterLinkMovementMethod.OnLinkClickListener() {
                        @Override
                        public boolean onClick(TextView textView, String url) {
                            // Do something with the URL and return true to indicate that
                            // this URL was handled. Otherwise, return false to let Android
                            // handle it.
                            if (url.contains("youtube.com") || url.contains("youtu.be")) {
                                Intent intent = new Intent(postText.getContext(), MediPreview.class);
                                intent.putExtra("path", url);
                                intent.putExtra("isvideo", true);
                                intent.putExtra("isvideoyoutube", true);
                                postText.getContext().startActivity(intent);
                            } else {
                                Utility.launchWebUrl(postText.getContext(), url);
                            }
                            return true;
                        }
                    })
                    .setOnLinkLongClickListener(new BetterLinkMovementMethod.OnLinkLongClickListener() {
                        @Override
                        public boolean onLongClick(TextView textView, String url) {
                            // Handle long-clicks.
                            if (url.contains("youtube.com") || url.contains("youtu.be")) {
                                Intent intent = new Intent(postText.getContext(), MediPreview.class);
                                intent.putExtra("path", url);
                                intent.putExtra("isvideo", true);
                                intent.putExtra("isvideoyoutube", true);
                                postText.getContext().startActivity(intent);
                            } else {
                                Utility.launchWebUrl(postText.getContext(), url);
                            }
                            return true;
                        }
                    });
            pager.addOnPageChangeListener(BudzFeedMainPostVH.this);

            list_liked_people = itemView.findViewById(R.id.list_liked_people);
        }

        void showView(final Post post, final int pos, final BudzFeedMainPostVH holder) {
            initMentionEditText();
            holder.itemView.findViewById(R.id.activity_wall_post_detail_uploaded_media_cross).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentUploadedMediaParent.setVisibility(View.GONE);
                    mediaUploadResponce = null;
                    commentUploadedMedia.setImageResource(R.drawable.place_holder_wall);
                }
            });
            holder.itemView.findViewById(R.id.activity_wall_post_detail_comment_attach).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.cameraDelegade = BudzFeedMainPostVH.this;
                    BudzFeedMainPostAdapter.this.cameraDelegade = holder.cameraDelegade;
                    if (mediaUploadResponce == null || commentUploadedMediaParent.getVisibility() == View.GONE) {
                        Bundle b = new Bundle();
                        b.putBoolean("isVideoCaptureAble", true);
                        Utility.launchActivityForResultFromFragment(fragment, (AppCompatActivity) mContext, HBCameraActivity.class, b, Flags.PICTURE_CAPTUREING_REQUEST);
                    } else {
                        CustomeToast.ShowCustomToast(mContext, "Already attached media!", Gravity.TOP);
                    }
                }
            });

            holder.itemView.findViewById(R.id.activity_wall_post_detail_comment_send).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                            submitComment(post, comment[0], comment[1], "", "", "", "");
                        } else {
                            if (isImage) {
                                submitComment(post, comment[0], comment[1], mediaUploadResponce.file, "image", "", mediaUploadResponce.thumb);
                            } else {
                                submitComment(post, comment[0], comment[1], mediaUploadResponce.file, "video", mediaUploadResponce.poster, "");
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
                            submitComment(post, comment[0], comment[1], mediaUploadResponce.file, "image", "", mediaUploadResponce.thumb);
                        } else {
                            submitComment(post, comment[0], comment[1], mediaUploadResponce.file, "video", mediaUploadResponce.poster, "");
                        }
                    }
                }
            });
            dialog = new com.codingpixel.healingbudz.customeUI.ProgressDialog();
            List<Comment> listImt = post.getComments();
            if (post.getComments().size() > 2) {

                listImt = post.getComments().subList(0, 2);
            }
            commentsAdapter = new BudzFeedPostDetailCommentsAdapter(listImt, new RecyclerViewItemClickListener() {
                @Override
                public void onItemClick(Object obj, int pos, int type) {
                    if (pos == -1) {
                        post.setCommentsCount(type);
//            post.setComments();
                        commentTxt.setText(String.valueOf(post.getCommentsCount()) + " Comments");
                    } else if (type == 1 && obj instanceof com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.User) {
                        onUserClick((com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.User) obj);
                    } else if (type == 2 && obj instanceof Attachment) {
                        Bundle b = new Bundle();
                        Attachment a = (Attachment) obj;
                        boolean isVideo = a.getType().trim().equalsIgnoreCase("video") && a.getPoster() != null && !a.getPoster().trim().isEmpty();
                        b.putString("path", (isVideo ? URL.videos_baseurl : URL.images_baseurl) + a.getFile());
                        b.putBoolean("isVideo", isVideo);
                        Intent intent = new Intent(mContext, MediPreview.class);
                        intent.putExtra("path", (isVideo ? URL.videos_baseurl : URL.images_baseurl) + a.getFile());
                        intent.putExtra("isvideo", isVideo);
                        mContext.startActivity(intent);
//            Utility.launchActivity(WallPostDetailActivity.this, MediPreview.class, false, b);
                    } else if (type == 4 && obj instanceof Comment) {

//EDIT CASE
                    } else if ((type == 0 || type == 1) && obj instanceof Comment) {
                        submitCommentLike(((Comment) obj).getId(), type);
                    } else if (pos == -3) {
                        commentsAdapter.notifyDataSetChanged();
                        notifyDataSetChanged();
                    }
                }

                @Override
                public boolean onItemLongClick(Object obj, int pos, int type) {
                    return false;
                }
            }, new AutoLinkTextLinkClickListener() {
                @Override
                public void onLinkClick(AutoLinkMode autoLinkMode, String matchedText, MentionTagJsonModel extraJson) {

                }
            }, true);

            commentsAdapter.notifyDataSetChanged();
            commentsAdapter.setCallBack(new ScrollPositionEndCallBack() {
                @Override
                public void onScrollPositionEnd() {
//                    callAPI();
                }

                @Override
                public void onScrollPositionStart() {

                }

                @Override
                public void setScrollingEnabled(boolean isEnabled) {

                }
            });
            commentsList.setAdapter(commentsAdapter);
//            AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
//            holder.adView.loadAd(adRequest);
            holder.itemView.findViewById(R.id.vh_budz_feed_main_post_like_btn).setOnClickListener(BudzFeedMainPostVH.this);
//            holder.itemView.findViewById(R.id.vh_budz_feed_main_post_like_img).setOnClickListener(BudzFeedMainPostVH.this);
//            holder.itemView.findViewById(R.id.vh_budz_feed_main_post_like_btn).setOnLongClickListener(BudzFeedMainPostVH.this);
//            holder.comment_activity.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onItemClick(dataSet.get(pos), pos, -1);
//                }
//            });
//            holder.tester.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onItemClick(dataSet.get(pos), pos, -1);
//                }
//            });
            if (post.getSharedPost() != null && post.getSharedUser() != null) {
                holder.postText.setAutoLinkText(String.valueOf(Html.fromHtml(post.getPost_added_comment())));
                holder.vh_budz_feed_main_post_text_repost.setText(String.valueOf(Html.fromHtml(post.getDescription())));
                holder.repost_for.setVisibility(View.VISIBLE);
                if (post.getJsonData() != null) {
                    try {
                        if (holder.vh_budz_feed_main_post_text_repost.getText().toString().length() > 200) {
                            MentionTagJsonModel[] temp = post.getJsonData();
                            MentionTagJsonModel[] abc = new MentionTagJsonModel[temp.length + 1];
                            for (int k = 0; k < temp.length; k++) {
                                abc[k] = temp[k];
                            }
                            abc[temp.length] = new MentionTagJsonModel(String.valueOf(post.getId()), "user", "-See More", "!");
//                            temp[temp.length + 1] = new MentionTagJsonModel(String.valueOf(post.getId()), "user", "-See More", "!");
                            ClickAbleKeywordText.createLink(holder.vh_budz_feed_main_post_text_repost, holder.vh_budz_feed_main_post_text_repost.getText().toString().substring(0, 199) + "-See More", abc, post);
                        } else {
                            ClickAbleKeywordText.createLink(holder.vh_budz_feed_main_post_text_repost, holder.vh_budz_feed_main_post_text_repost.getText().toString(), post.getJsonData());
                        }

                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        ClickAbleKeywordText.createLink(holder.vh_budz_feed_main_post_text_repost, holder.vh_budz_feed_main_post_text_repost.getText().toString());
                    }
                } else {
                    if (holder.vh_budz_feed_main_post_text_repost.getText().toString().length() > 200) {
                        MentionTagJsonModel[] temp = post.getJsonData();
                        MentionTagJsonModel[] abc = new MentionTagJsonModel[temp.length + 1];
                        for (int k = 0; k < temp.length; k++) {
                            abc[k] = temp[k];
                        }
                        abc[temp.length] = new MentionTagJsonModel(String.valueOf(post.getId()), "user", "-See More", "!");
                        ClickAbleKeywordText.createLink(holder.vh_budz_feed_main_post_text_repost, holder.vh_budz_feed_main_post_text_repost.getText().toString().substring(0, 199) + " -See More", abc, post);
                    } else {

                        ClickAbleKeywordText.createLink(holder.vh_budz_feed_main_post_text_repost, holder.vh_budz_feed_main_post_text_repost.getText().toString());
                    }
                    ClickAbleKeywordText.createLink(holder.postText, holder.postText.getText().toString());
                }
            } else {
                holder.postText.setAutoLinkText(String.valueOf(Html.fromHtml(post.getDescription())));
                holder.repost_for.setVisibility(View.GONE);
                if (post.getJsonData() != null) {
                    try {
                        if (holder.postText.getText().toString().length() > 200) {
                            MentionTagJsonModel[] temp = post.getJsonData();
                            MentionTagJsonModel[] abc = new MentionTagJsonModel[temp.length + 1];
                            for (int k = 0; k < temp.length; k++) {
                                abc[k] = temp[k];
                            }
                            abc[temp.length] = new MentionTagJsonModel(String.valueOf(post.getId()), "user", "-See More", "!");
                            ClickAbleKeywordText.createLink(holder.postText, holder.postText.getText().toString().substring(0, 199) + "-See More", abc, post);
                        } else {
                            ClickAbleKeywordText.createLink(holder.postText, holder.postText.getText().toString(), post.getJsonData());
                        }

                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        ClickAbleKeywordText.createLink(holder.postText, holder.postText.getText().toString());
                    }
                } else {
                    if (holder.postText.getText().toString().length() > 200) {
                        MentionTagJsonModel[] temp = post.getJsonData();
                        if (temp != null) {
                            MentionTagJsonModel[] abc = new MentionTagJsonModel[temp.length + 1];
                            for (int k = 0; k < temp.length; k++) {
                                abc[k] = temp[k];
                            }
                            abc[temp.length] = new MentionTagJsonModel(String.valueOf(post.getId()), "user", "-See More", "!");
                            ClickAbleKeywordText.createLink(holder.postText, holder.postText.getText().toString().substring(0, 199) + " -See More", abc, post);
                        } else {
                            MentionTagJsonModel[] abc = new MentionTagJsonModel[1];
                            abc[0] = new MentionTagJsonModel(String.valueOf(post.getId()), "user", "-See More", "!");
                            ClickAbleKeywordText.createLink(holder.postText, holder.postText.getText().toString().substring(0, 199) + " -See More", abc, post);
                        }

                    } else {

                        ClickAbleKeywordText.createLink(holder.postText, holder.postText.getText().toString());
                    }
                }
            }
            if (post.getAllowRepost() == 1) {
                holder.vh_budz_feed_main_post_repost_btn.setVisibility(View.VISIBLE);


            } else {


                holder.vh_budz_feed_main_post_repost_btn.setVisibility(View.INVISIBLE);

            }
            holder.vh_budz_feed_main_post_repost_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user.getUser_id() != post.getUserId()) {
                        onOptionClick(6);
                    } else {
                        CustomeToast.ShowCustomToast(view.getContext(), "You can't repost your own post!", Gravity.TOP);
                    }
                }
            });
            holder.itemView.findViewById(R.id.vh_budz_feed_main_post_options).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialogs.showPostMoreDropDownMenu(view, BudzFeedMainPostVH.this, post);
                }
            });
            holder.timeAgo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialogs.showPostMoreDropDownMenu(view, BudzFeedMainPostVH.this, post);
                }
            });
            this.pos = pos;

//            if (post.getJsonData() != null) {
//                try {
//                    if (holder.postText.getText().toString().length() > 200) {
//                        MentionTagJsonModel[] temp = post.getJsonData();
//                        temp[temp.length + 1] = new MentionTagJsonModel(String.valueOf(post.getId()), "user", "-See More", "!");
//                        ClickAbleKeywordText.createLink(holder.postText, holder.postText.getText().toString().substring(0, 199) + "-See More", temp, post);
//                    } else {
//                        ClickAbleKeywordText.createLink(holder.postText, holder.postText.getText().toString(), post.getJsonData());
//                    }
//
//                } catch (IndexOutOfBoundsException e) {
//                    e.printStackTrace();
//                    ClickAbleKeywordText.createLink(holder.postText, holder.postText.getText().toString());
//                }
//            } else {
//                if (holder.postText.getText().toString().length() > 200) {
//                    MentionTagJsonModel[] temp = new MentionTagJsonModel[1];
//                    temp[0] = new MentionTagJsonModel(String.valueOf(post.getId()), "user", "-See More", "!");
//                    ClickAbleKeywordText.createLink(holder.postText, holder.postText.getText().toString().substring(0, 199) + " -See More", temp, post);
//                } else {
//
//                    ClickAbleKeywordText.createLink(holder.postText, holder.postText.getText().toString());
//                }
//            }
            holder.personName.setText(MessageFormat.format("{0} {1}", post.getUser().getFirstName(), post.getUser().getLastName()));
            holder.personName.setTextColor(Color.parseColor(Utility.getBudColor(post.getUser().getPoints())));

            holder.personLeafImg.setImageResource(Utility.getBudColorDrawable(post.getUser().getPoints()));

            holder.repostCount.setText(MessageFormat.format("Reposts {0}", post.getSharedCount()));
            if (post.getSub_user() != null && post.getSubUserId() != null && post.getSubUserId() != 0) {
                holder.postAsParent.setVisibility(View.VISIBLE);
                holder.postedAs.setText(post.getSub_user().getTitle());
                holder.postedAs.setOnClickListener(BudzFeedMainPostVH.this);
            } else {
                holder.postAsParent.setVisibility(View.GONE);
            }

            if (post.getTagged() == null || post.getTagged().isEmpty()) {
                holder.withParent.setVisibility(View.GONE);
            } else {
                holder.withParent.setVisibility(View.VISIBLE);
                holder.taggedPeople.setLayoutManager(ChipsLayoutManager.newBuilder(holder.itemView.getContext()).setOrientation(ChipsLayoutManager.HORIZONTAL).build());
                holder.tagPeopleAdapter = new WallTagPeopleAdapter(post.get_Tagged(), new RecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(Object obj, int pos, int type) {
                        listener.onItemClick(obj, 0, -10);
                    }

                    @Override
                    public boolean onItemLongClick(Object obj, int pos, int type) {
                        return false;
                    }
                }, false);
                holder.taggedPeople.setAdapter(holder.tagPeopleAdapter);
            }

            if (post.getSharedPost() != null && post.getSharedUser() != null) {
                holder.repostParent.setVisibility(View.VISIBLE);
                holder.repostedPostUser.setText(post.getSharedUser().getFirstName());
                holder.repostedPostUser.setTextColor(Color.parseColor(Utility.getBudColor(post.getSharedUser().getPoints())));
                holder.repostedPostUser.setOnClickListener(BudzFeedMainPostVH.this);
            } else {
                holder.repostParent.setVisibility(View.GONE);
            }

            Glide.with(holder.itemView.getContext()).load(post.getUser().getImagePath()).
                    asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    placeholder(R.drawable.ic_user_profile_green).centerCrop().error(R.drawable.noimage).into(personImg);
            if (post.getUser().getSpecial_icon().length() > 5) {
                profile_img_topi.setVisibility(View.VISIBLE);
                Glide.with(holder.itemView.getContext()).load(post.getUser().getSpecial_icon()).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).
                        placeholder(R.drawable.ic_user_profile_green).error(R.drawable.noimage).into(profile_img_topi);
            } else {
                profile_img_topi.setVisibility(View.GONE);

            }
//            holder.timeAgo.setText(TimesAgo.getTimeAgo(post.getUpdatedAt()));
            holder.timeAgo.setText(DateConverter.convertSpecialWallTop(post.getCreatedAt()));
            holder.personExtraInfo.setText(MessageFormat.format("{0}   |   {1}", String.valueOf(post.getUser().getPoints()), Utility.getBudType(post.getUser().getPoints())));
            holder.personExtraInfo.setTextColor(Color.parseColor(Utility.getBudColor(post.getUser().getPoints())));
            if (post.getUserId() == user.getUser_id()) {
                holder.likeTxtTemp.setText(MessageFormat.format("Likes {0}", String.valueOf(post.getLikesCount())));
            } else {
                holder.likeTxtTemp.setText(MessageFormat.format("Likes {0}", String.valueOf(post.getLikesCount())));
            }

            holder.commentTxt.setText(MessageFormat.format("{0} Comments", String.valueOf(post.getCommentsCount())));
            holder.commentTxtTemp.setText(MessageFormat.format("Comments {0}", String.valueOf(post.getCommentsCount())));
            if (post.getFiles() == null || post.getFiles().isEmpty()) {
                holder.many_data.setVisibility(View.GONE);
                holder.itemView.findViewById(R.id.vh_budz_feed_main_post_imgs_viewpager_parent).setVisibility(View.GONE);
//                List<String> urls = Utility.extractURL(post.getDescription());
//                if (urls == null || urls.isEmpty()) {
//                    holder.scrapView.setVisibility(View.GONE);
//                    holder.scrapView.clear();
//                } else {
//                    if (urls.get(0).length() > 8 && (urls.get(0).contains(".com") || urls.get(0).contains(".be") || urls.get(0).contains("http") || urls.get(0).contains("https"))) {
//                        holder.scrapView.setOnClickListener(BudzFeedMainPostVH.this);
//                        holder.scrapView.setVisibility(View.VISIBLE);
//                        holder.scrapView.clear();
//                        holder.scrapView.setData(urls.get(0));
//                    } else {
//                        holder.scrapView.setVisibility(View.GONE);
//                        holder.scrapView.clear();
//                    }
//                }

            } else {
                holder.scrapView.setVisibility(View.GONE);
                holder.scrapView.clear();
                holder.itemView.findViewById(R.id.vh_budz_feed_main_post_imgs_viewpager_parent).setVisibility(View.VISIBLE);
                List<File> test = new ArrayList<>();
                test.add(post.getFiles().get(0));
                if (post.getFiles().size() > 1) {
                    holder.many_data.setVisibility(View.VISIBLE);
                } else {
                    holder.many_data.setVisibility(View.GONE);
                }
                holder.adapter = new BudzFeedMainPostMediaPagerAdapter(test, new RecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(Object obj, int pos, int type) {
                        listener.onItemClick(post, pos, -2);
                    }

                    @Override
                    public boolean onItemLongClick(Object obj, int pos, int type) {
                        return false;
                    }
                });
                holder.pager.setAdapter(holder.adapter);
                holder.pager.beginFakeDrag();
                FrameLayout.LayoutParams buttonParams = (FrameLayout.LayoutParams) holder.pager.getLayoutParams();
                buttonParams.height = BudzFeedMainPostMediaPagerAdapter.heightDv;
                holder.pager.setLayoutParams(buttonParams);
                float width = Splash.widthDis;

//                float ratio = Float.valueOf(post.getFiles().get(0).getRatio());
//                if (ratio < 1) {
//                    float height = width / ratio;
//                    if (height > 200) {
//                        height = Utility.convertDpToPixel(400, holder.itemView.getContext());
//                    }
                buttonParams = (FrameLayout.LayoutParams) holder.pager.getLayoutParams();
                buttonParams.height = BudzFeedMainPostMediaPagerAdapter.heightDv;
                holder.pager.setLayoutParams(buttonParams);
//
//                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.adapter.notifyDataSetChanged();
//                        float width = holder.pager.getMeasuredWidth();
//                        float ratio = Float.valueOf(post.getFiles().get(0).getRatio());
//
//                        if (ratio < 1) {
//                            float height = width / ratio;
//                            FrameLayout.LayoutParams buttonParams = (FrameLayout.LayoutParams) holder.pager.getLayoutParams();
//                            buttonParams.height = (int) height;
//                            holder.pager.setLayoutParams(buttonParams);
//
//                        } else {
//                            float height = width / ratio;
//                            FrameLayout.LayoutParams buttonParams = (FrameLayout.LayoutParams) holder.pager.getLayoutParams();
//                            buttonParams.height = Utility.convertDpToPixel(180, holder.itemView.getContext());
//                            holder.pager.setLayoutParams(buttonParams);
//
//                        }
                    }
                }, 11);


                holder.indicatorView.setCount(holder.adapter.getCount());
            }
            if (isLiked(post)) {
                holder.likeIcon.setImageResource(R.drawable.un_like_wall);
                holder.likeIcon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.COLOR_YOUR_COLOR), android.graphics.PorterDuff.Mode.SRC_IN);
//                holder.likeTxt.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.feeds_liked_blue_color));
                holder.likeTxtTemp.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.feeds_liked_blue_color));
            } else {
                holder.likeIcon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.feeds_like_gray_color), android.graphics.PorterDuff.Mode.SRC_IN);
                holder.likeIcon.setImageResource(R.drawable.un_like_wall);
                holder.likeTxt.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.feeds_like_gray_color));
                holder.likeTxtTemp.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.feeds_like_gray_color));
            }

            holder.postText.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
                @Override
                public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                    switch (autoLinkMode) {
                        case MODE_MENTION:
                        case MODE_CUSTOM:
                        case MODE_HASHTAG: {
                            MentionTagJsonModel temp = null;
                            MentionTagJsonModel[] data = post.getJsonData();
                            if (data != null) {
                                for (MentionTagJsonModel mentionTagJsonModel : data) {
                                    if (mentionTagJsonModel.getValue().trim().contains(matchedText.trim().substring(1).replace("_", " "))) {
                                        temp = mentionTagJsonModel;
                                        break;
                                    }
                                }
                                linkClickListener.onLinkClick(autoLinkMode, matchedText, temp);
                            }
                        }
                        break;
                        default: {
                            linkClickListener.onLinkClick(autoLinkMode, matchedText, null);
                        }
                    }
                }
            });

            holder.personName.setOnClickListener(BudzFeedMainPostVH.this);
            holder.personLeafImg.setOnClickListener(BudzFeedMainPostVH.this);
            holder.personExtraInfo.setOnClickListener(BudzFeedMainPostVH.this);
            holder.personImg.setOnClickListener(BudzFeedMainPostVH.this);
        }

        @Override
        public void onClick(final View view) {
            switch (view.getId()) {
                case R.id.vh_budz_feed_main_post_like_btn:
//                case R.id.vh_budz_feed_main_post_like_img:
                {
                    if (!isLikedUser) {
                        isLikedUser = true;
                        view.setClickable(false);
                        view.setEnabled(false);
                        itemView.setClickable(false);
                        itemView.setEnabled(false);
                        if (isLiked(dataSet.get(pos))) {
                            listener.onItemClick(dataSet.get(pos), pos, 0);

                            likeIcon.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.feeds_like_gray_color), android.graphics.PorterDuff.Mode.SRC_IN);
                            likeIcon.setImageResource(R.drawable.un_like_wall);
                            likeTxt.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.feeds_like_gray_color));
                            likeTxtTemp.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.feeds_like_gray_color));

                        } else {
                            likeIcon.setImageResource(R.drawable.un_like_wall);
                            likeIcon.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.COLOR_YOUR_COLOR), android.graphics.PorterDuff.Mode.SRC_IN);
                            likeTxtTemp.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.feeds_liked_blue_color));

                            listener.onItemClick(dataSet.get(pos), pos, 1);
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                view.setClickable(true);
                                view.setEnabled(true);
                                itemView.setClickable(true);
                                itemView.setEnabled(true);
                            }
                        }, 150);
                    }
                }
                break;
                case R.id.vh_budz_feed_main_post_person_name: {
                    listener.onItemClick(getDataSet().get(pos).getUser(), pos, -5);
                }
                break;
                case R.id.vh_budz_feed_main_post_person_img: {
                    listener.onItemClick(getDataSet().get(pos).getUser(), pos, -5);
                }
                break;
                case R.id.vh_budz_feed_main_post_person_extra_info: {
                    listener.onItemClick(getDataSet().get(pos).getUser(), pos, -5);
                }
                break;
                case R.id.vh_budz_feed_main_post_person_leaf: {
                    listener.onItemClick(getDataSet().get(pos).getUser(), pos, -5);
                }
                break;
                case R.id.vh_budz_feed_main_post_posted_as: {
                    listener.onItemClick(getDataSet().get(pos).getSub_user(), pos, -6);
                }
                break;
                case R.id.vh_budz_feed_main_post_reposted_user_name: {
                    listener.onItemClick(getDataSet().get(pos).getSharedUser(), pos, -7);
                }
                break;
                case R.id.vh_budz_feed_main_post_link_scrap: {
                    linkClickListener.onLinkClick(AutoLinkMode.MODE_URL, scrapView.getUrl(), null);
                }
                break;
            }
        }

        private boolean isLiked(Post post) {
            if (post.getLikes() == null || post.getLikes().isEmpty()) {
                return false;
            }
            for (Like like : post.getLikes()) {
                if (user.getUser_id() == like.getUserId() && like.getIsLike() > 0) {
                    return true;
                }
            }
            return false;
        }

        private boolean isFlaged(Post post) {
            if (post.getFlags() != null && !post.getFlags().isEmpty()) {
                for (FlagPost reportPost : post.getFlags()) {
                    if (user.getUser_id() == reportPost.getUserId()) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            this.indicatorView.setSelection(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onOptionClick(int pos) {
            //post more popup item click call back i.e. flag/report etc.
            if (pos == 23) {
                if (dataSet.get(BudzFeedMainPostVH.this.pos).getLikes().size() > 0 && dataSet.get(BudzFeedMainPostVH.this.pos).getLikesCount() > 0) {
                    if (dataSet.get(BudzFeedMainPostVH.this.pos).getLikes().size() == dataSet.get(BudzFeedMainPostVH.this.pos).getLikesCount()) {
                        UserLikesAlertDialog userLikesAlertDialog = UserLikesAlertDialog.newInstance(BudzFeedMainPostAdapter.this, dataSet.get(BudzFeedMainPostVH.this.pos).getLikes());
                        userLikesAlertDialog.show(((AppCompatActivity) BudzFeedMainPostVH.this.itemView.getContext()).getSupportFragmentManager(), "pd");
                    } else {
                        dataSet.get(BudzFeedMainPostVH.this.pos).getLikes().remove(dataSet.get(BudzFeedMainPostVH.this.pos).getLikes().size() - 1);
                        UserLikesAlertDialog userLikesAlertDialog = UserLikesAlertDialog.newInstance(BudzFeedMainPostAdapter.this, dataSet.get(BudzFeedMainPostVH.this.pos).getLikes());
                        userLikesAlertDialog.show(((AppCompatActivity) BudzFeedMainPostVH.this.itemView.getContext()).getSupportFragmentManager(), "pd");
                        notifyItemChanged(BudzFeedMainPostVH.this.pos);
                    }
//                    for (int i = 0; i < post.getLikes().size(); i++) {
//                        Log.d("onClick: ", "" + post.getLikes().get(i).getUser().toString());
//                    }
                }
            } else {
                listener.onItemClick(dataSet.get(BudzFeedMainPostVH.this.pos), BudzFeedMainPostVH.this.pos, 21 + pos);
            }

        }

        @Override
        public boolean onLongClick(View view) {
            /*switch (view.getId()) {
                case R.id.vh_budz_feed_main_post_like_btn: {
                    if (!isLiked(dataSet.get(pos))) {
                        Dialogs.showPostLikePopUp(view, new Dialogs.OptionClickListener() {
                            @Override
                            public void onOptionClick(int i) {
                                //facebook type like listener
                                listener.onItemClick(dataSet.get(pos), pos, i);
                            }
                        });
                    }
                    return true;
                }
            }*/
            return false;
        }

        @Override
        public void onDataReady(final Preview preview) {
            new Handler(itemView.getContext().getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    scrapView.setVisibility(View.VISIBLE);
//                    scrapView.setMessage(preview.getLink(), ContextCompat.getColor(itemView.getContext(), R.color.color_link));
                }
            });
        }

        @Override
        public void dataPassed(int requestCode, int resultCode, Intent data) {
            onActivityResult(requestCode, resultCode, data);
        }
    }

    private ScrollPositionEndCallBack callBack;
    private scrollBis scrll;

    public scrollBis getScrll() {
        return scrll;
    }

    public void setScrll(scrollBis scrll) {
        this.scrll = scrll;
    }

    public ScrollPositionEndCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(ScrollPositionEndCallBack callBack) {
        this.callBack = callBack;
    }

    public interface scrollBis {
        public void setScrollingEnabled(boolean isEnabled);
    }

    public interface CameraDelegade {
        public void dataPassed(int requestCode, int resultCode, Intent data);
    }

//    //

}
