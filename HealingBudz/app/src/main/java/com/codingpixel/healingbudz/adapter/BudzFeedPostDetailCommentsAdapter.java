package com.codingpixel.healingbudz.adapter;
/*
 * Created by M_Muzammil Sharif on 08-Mar-18.
 */

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.Utilities.Dialogs;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.activity.Wall.dialogue.UserLikesAlertDialog;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.customeUI.CircularImageView;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.FontCache;
import com.codingpixel.healingbudz.customeUI.HealingBudTextViewBold;
import com.codingpixel.healingbudz.customeUI.HealingBudTextViewItalic;
import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.interfaces.ApiStatusCallBack;
import com.codingpixel.healingbudz.interfaces.AutoLinkTextLinkClickListener;
import com.codingpixel.healingbudz.interfaces.RecyclerViewItemClickListener;
import com.codingpixel.healingbudz.interfaces.ScrollPositionEndCallBack;
import com.codingpixel.healingbudz.network.BudzFeedModel.AddComment.Comment;
import com.codingpixel.healingbudz.network.BudzFeedModel.GeneralResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.MentionTagJsonModel;
import com.codingpixel.healingbudz.network.BudzFeedModel.submitPostLike.Like;
import com.codingpixel.healingbudz.network.BudzFeedModel.submitPostLike.SubmitPostLikeResponse;
import com.codingpixel.healingbudz.network.RestClient;
import com.codingpixel.healingbudz.network.model.URL;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.codingpixel.healingbudz.customeUI.ProgressDialog.newInstance;

public class BudzFeedPostDetailCommentsAdapter extends RecyclerView.Adapter<BudzFeedPostDetailCommentsAdapter.PostDetailCommentVH> implements UserLikesAlertDialog.OnDialogFragmentClickListener {
    private List<Comment> comments = new ArrayList<>();
    private RecyclerViewItemClickListener listener;
    private AutoLinkTextLinkClickListener linkClickListener;
    private boolean isFromPost;
    public static boolean isLikedCommentUser = false;

    public BudzFeedPostDetailCommentsAdapter(List<Comment> comments, @NonNull RecyclerViewItemClickListener clickListener, @NonNull AutoLinkTextLinkClickListener linkClickListener, boolean isFromPost) {
        this.comments = comments;
        this.linkClickListener = linkClickListener;
        this.listener = clickListener;
        this.isFromPost = isFromPost;
        sortData();
    }

    @Override
    public PostDetailCommentVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostDetailCommentVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_wall_post_detail_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PostDetailCommentVH holder, final int position) {

//        if (comments.get(pos).getJsonData() == null || (comments.get(pos).getJsonData() != null && comments.get(pos).getJsonData().length() < 3)) {
//            holder.tvComment.setMentionModeColor(Color.parseColor("#c4c4c4"));
//            holder.tvComment.setHashtagModeColor(Color.parseColor("#c4c4c4"));
//        } else {
//            holder.tvComment.setMentionModeColor(ContextCompat.getColor(holder.tvComment.getContext(), R.color.post_description_links_color));
//            holder.tvComment.setHashtagModeColor(ContextCompat.getColor(holder.tvComment.getContext(), R.color.mention_color));
//        }
        if (callBack != null && position == 0) {
            callBack.onScrollPositionStart();
        }
        if (callBack != null && position == comments.size() - 1) {
            callBack.onScrollPositionEnd();
        }
        if (isFromPost) {
            holder.like_layout.setVisibility(View.VISIBLE);
        } else {
            holder.like_layout.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(comments.get(position), position, 0);
            }
        });
        holder.like_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!isLikedCommentUser) {
                    isLikedCommentUser = true;
                    holder.itemView.setClickable(false);
                    holder.itemView.setEnabled(false);
                    view.setClickable(false);
                    view.setEnabled(false);
                    int var = 0;
                    if (isLiked(comments.get(position))) {
                        var = 0;
//                        listener.onItemClick(comments.get(position), position, 0);
                        holder.like_comment.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.feeds_like_gray_color), android.graphics.PorterDuff.Mode.SRC_IN);
                        holder.like_comment.setImageResource(R.drawable.un_like_wall);
                        holder.count_like.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.feeds_like_gray_color));
                        holder.count_like.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.feeds_like_gray_color));
                    } else {
                        var = 1;
                        holder.like_comment.setImageResource(R.drawable.un_like_wall);
                        holder.like_comment.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.COLOR_YOUR_COLOR), android.graphics.PorterDuff.Mode.SRC_IN);
                        holder.count_like.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.feeds_liked_blue_color));
//                        listener.onItemClick(comments.get(position), position, 1);
                    }
                    RestClient.getInstance(holder.itemView.getContext()).getApiService().submitCommentLike(Splash.user.getSession_key(), comments.get(position).getId(), var).enqueue(new Callback<SubmitPostLikeResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<SubmitPostLikeResponse> call, @NonNull final Response<SubmitPostLikeResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                                    @Override
                                    public void success() {
//                            isLikedUser = false;

                                        updatePostLike(comments.get(position).getId(), response.body().getSuccessData());
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                isLikedCommentUser = false;
                                                view.setClickable(true);
                                                view.setEnabled(true);
                                                listener.onItemClick(null, -3, -3);
                                            }
                                        }, 150);

                                    }

                                    @Override
                                    public void sessionExpire() {
//                                        onSessionExpire();
                                    }

                                    @Override
                                    public void knownError(String errorMsg) {
                                        CustomeToast.ShowCustomToast(holder.itemView.getContext(), errorMsg, Gravity.TOP);
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
            }
        });
        holder.loadView(comments.get(position), position, holder);
    }

    public void addToDataSet(@NonNull Comment post) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        if (comments.isEmpty()) {
            comments.add(post);
            notifyDataSetChanged();
            return;
        }
        LinkedHashMap<Integer, Comment> temp = new LinkedHashMap<>();
        for (Comment post1 : comments) {
            temp.put(post1.getId(), post1);
        }
        comments = new ArrayList<>();
        if (temp.containsKey(post.getId())) {
            temp.put(post.getId(), post);
        } else {
            comments.add(post);
        }
        comments.addAll(temp.values());
        temp.clear();
        temp = null;
        notifyDataSetChanged();
    }

    public void updateToDataSet(@NonNull List<Comment> posts) {
        if (comments == null || comments.isEmpty()) {
            comments = posts;
            notifyDataSetChanged();
            return;
        }
        if (posts == null || posts.isEmpty()) {
            return;
        }
        LinkedHashMap<Integer, Comment> temp = new LinkedHashMap<>();
        for (Comment post : comments) {
            temp.put(post.getId(), post);
        }
        comments = new ArrayList<>();
        for (Comment post : posts) {
            if (temp.containsKey(post.getId())) {
                temp.put(post.getId(), post);
            } else {
                comments.add(post);
            }
        }
        comments.addAll(temp.values());
        temp.clear();
        temp = null;
        notifyDataSetChanged();
    }

    public void updatePostLike(Integer postId, Like like) {
        if (comments == null || comments.isEmpty()) {
            return;
        }
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getId().equals(postId)) {
                if (comments.get(i).getLikes() == null || comments.get(i).getLikes().isEmpty()) {
                    comments.get(i).setLikes(new ArrayList<Like>());
                    comments.get(i).getLikes().add(like);
                    if (like.getIsLike() > 0) {
                        comments.get(i).setLikesCount(comments.get(i).getLikesCount() + 1);
                    }
                    notifyDataSetChanged();
                    return;
                } else {
                    for (int j = 0; j < comments.get(i).getLikes().size(); j++) {
                        if (like.getId() == comments.get(i).getLikes().get(j).getId()) {
                            comments.get(i).getLikes().set(j, like);
                            if (like.getIsLike() > 0) {
                                comments.get(i).setLikesCount(comments.get(i).getLikesCount() + 1);
                            } else {
                                if (comments.get(i).getLikesCount() > 0)
                                    comments.get(i).setLikesCount(comments.get(i).getLikesCount() - 1);
                            }
//                            comments.get(i).setLikesCount(comments.get(i).getLikes().size());
                            notifyDataSetChanged();
                            return;
                        }
                    }
                    comments.get(i).getLikes().add(like);
                    if (like.getIsLike() > 0) {
                        comments.get(i).setLikesCount(comments.get(i).getLikesCount() + 1);
                    } else {
//                        if (comments.get(i).getLikesCount() > 0)
//                            comments.get(i).setLikesCount(comments.get(i).getLikesCount() - 1);
                    }
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    private ScrollPositionEndCallBack callBack;

    public ScrollPositionEndCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(ScrollPositionEndCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public int getItemCount() {
        if (comments == null) {
            return 0;
        }
        return comments.size();
    }

    @Override
    public void onCrossListener(UserLikesAlertDialog dialog) {

    }

    class PostDetailCommentVH extends RecyclerView.ViewHolder {
        private CircularImageView civPersonImg;
        private ImageView profile_img_topi, like_comment;
        private HealingBudTextViewBold tvPersonName;
        private HealingBudTextViewItalic tvTimeAgo;
        TextView count_like;
        LinearLayout like_layout;
        private AutoLinkTextView tvComment;
        ImageView vh_budz_feed_comment_post_options;
        private ImageView attachment;
        private View attachmentParent, videoIcon;

        PostDetailCommentVH(View itemView) {
            super(itemView);
            this.civPersonImg = itemView.findViewById(R.id.vh_wall_post_detail_comment_person_img);
            this.count_like = itemView.findViewById(R.id.count_like);
            this.like_layout = itemView.findViewById(R.id.like_layout);
            this.like_comment = itemView.findViewById(R.id.like_comment);
            this.profile_img_topi = itemView.findViewById(R.id.profile_img_topi);
            this.vh_budz_feed_comment_post_options = itemView.findViewById(R.id.vh_budz_feed_comment_post_options);
            this.tvComment = itemView.findViewById(R.id.vh_wall_post_detail_comment_txt);
            Typeface customFont = FontCache.getTypeface("Lato-Light.ttf", itemView.getContext());
            tvComment.setTypeface(customFont);
            this.tvTimeAgo = itemView.findViewById(R.id.vh_wall_post_detail_comment_time_ago);
            this.tvPersonName = itemView.findViewById(R.id.vh_wall_post_detail_comment_person_name);
            this.attachment = itemView.findViewById(R.id.vh_wall_post_detail_comment_attachement);

            videoIcon = itemView.findViewById(R.id.vh_wall_post_detail_comment_video_ic);
            attachmentParent = itemView.findViewById(R.id.vh_wall_post_detail_comment_attachement_parent);
//AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_MENTION,
            this.tvComment.addAutoLinkMode(AutoLinkMode.MODE_URL, AutoLinkMode.MODE_EMAIL);
            this.tvComment.setUrlModeColor(Color.parseColor("#7CC245"));
            this.tvComment.setAutoLinkMask(Linkify.WEB_URLS);
            this.tvComment.setLinkTextColor(Color.parseColor("#7CC245"));

            this.tvComment.setMentionModeColor(ContextCompat.getColor(itemView.getContext(), R.color.post_description_links_color));
            this.tvComment.setHashtagModeColor(ContextCompat.getColor(itemView.getContext(), R.color.mention_color));
            this.tvComment.setEmailModeColor(ContextCompat.getColor(itemView.getContext(), R.color.post_description_links_color));
        }

        void loadView(final Comment comment, final int itemPosition, final PostDetailCommentVH holder) {
            if (comment.getUserId().equals(Splash.user.getUser_id())) {
                if (isFromPost) {
                    holder.vh_budz_feed_comment_post_options.setVisibility(View.GONE);
                } else {
                    holder.vh_budz_feed_comment_post_options.setVisibility(View.VISIBLE);
                }

                holder.vh_budz_feed_comment_post_options.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Dialogs.showCommentMoreDropDownMenu(v, new Dialogs.OptionClickListener() {
                            @Override
                            public void onOptionClick(final int pos) {
                                if (pos == 5) {
                                    new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Are you sure?")
                                            .setContentText("You want to delete this comment?")
                                            .setConfirmText("Yes, delete it!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                    final ProgressDialog pd = newInstance();
                                                    pd.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "pd");

                                                    RestClient.getInstance(v.getContext()).getApiService().deleteComment(Splash.user.getSession_key(), comment.getId()).
                                                            enqueue(new Callback<GeneralResponse>() {
                                                                @Override
                                                                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
//                                                                pDialog.dismiss();
                                                                    pd.dismiss();
                                                                    if (response.isSuccessful() || response.body() != null) {
                                                                        Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                                                                            @Override
                                                                            public void success() {
                                                                                comments.remove(itemPosition);
                                                                                notifyDataSetChanged();
                                                                                listener.onItemClick(null, -1, comments.size());
                                                                            }

                                                                            @Override
                                                                            public void sessionExpire() {
//                                                                                onSessionExpire();
                                                                            }

                                                                            @Override
                                                                            public void knownError(String errorMsg) {
                                                                                CustomeToast.ShowCustomToast(v.getContext(), errorMsg, Gravity.BOTTOM);
                                                                            }

                                                                            @Override
                                                                            public void unKnownError() {
//                                                                                showunKnownSeverErrorDialog();
                                                                            }
                                                                        });
                                                                    } else {
//                                                                        showunKnownSeverErrorDialog();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<GeneralResponse> call, Throwable t) {

                                                                    comments.remove(itemPosition);
                                                                    notifyDataSetChanged();
                                                                    listener.onItemClick(null, -1, comments.size());
                                                                    pd.dismiss();
//                                                                    showunKnownSeverErrorDialog();
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
                                } else if (pos == 32) {
                                    if (comments.get(itemPosition).getLikes().size() > 0 && comments.get(itemPosition).getLikesCount() > 0) {
                                        if (comments.get(itemPosition).getLikes().size() == comments.get(itemPosition).getLikesCount()) {
                                            UserLikesAlertDialog userLikesAlertDialog = UserLikesAlertDialog.newInstance(BudzFeedPostDetailCommentsAdapter.this, comments.get(itemPosition).getLikes());
                                            userLikesAlertDialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "pd");
                                        } else {
                                            comments.get(itemPosition).getLikes().remove(comments.get(itemPosition).getLikes().size() - 1);
                                            UserLikesAlertDialog userLikesAlertDialog = UserLikesAlertDialog.newInstance(BudzFeedPostDetailCommentsAdapter.this, comments.get(itemPosition).getLikes());
                                            userLikesAlertDialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "pd");
                                            notifyItemChanged(itemPosition);
                                        }
//                    for (int i = 0; i < post.getLikes().size(); i++) {
//                        Log.d("onClick: ", "" + post.getLikes().get(i).getUser().toString());
//                    }
                                    }
                                } else {
                                    listener.onItemClick(comment, itemPosition, 4);
                                }
                            }
                        }, comment);
                    }
                });
            } else {
                holder.vh_budz_feed_comment_post_options.setVisibility(View.GONE);
            }

            if (isLiked(comments.get(itemPosition))) {
                holder.like_comment.setImageResource(R.drawable.un_like_wall);
                holder.like_comment.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.COLOR_YOUR_COLOR), android.graphics.PorterDuff.Mode.SRC_IN);
//                likeTxt.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.feeds_liked_blue_color));
                holder.count_like.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.feeds_liked_blue_color));
            } else {
                holder.like_comment.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.feeds_like_gray_color), android.graphics.PorterDuff.Mode.SRC_IN);
                holder.like_comment.setImageResource(R.drawable.un_like_wall);
                holder.count_like.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.feeds_like_gray_color));
                holder.count_like.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.feeds_like_gray_color));
            }
            Glide.with(holder.itemView.getContext()).load(comment.getUser().getImagePath()).asBitmap().
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.noimage).
                    placeholder(R.drawable.ic_user_profile_green).into(civPersonImg);
            if (comment.getUser().getSpecial_icon().length() > 5) {
                holder.profile_img_topi.setVisibility(View.VISIBLE);
                Glide.with(holder.itemView.getContext()).load(comment.getUser().getSpecial_icon()).asBitmap().
                        diskCacheStrategy(DiskCacheStrategy.SOURCE).
                        placeholder(R.drawable.topi_ic).into(profile_img_topi);
            } else {
                holder.profile_img_topi.setVisibility(View.GONE);
            }
            holder.count_like.setText(MessageFormat.format("({0})", String.valueOf(comment.getLikesCount())));
            holder.tvTimeAgo.setText(DateConverter.convertSpecialWallTop(comment.getCreatedAt()));
            holder.tvPersonName.setText(MessageFormat.format("{0} {1}", comment.getUser().getFirstName(), comment.getUser().getLastName()));
            holder.tvPersonName.setTextColor(Color.parseColor(Utility.getBudColor(comment.getUser().getPoints())));
            holder.tvComment.setAutoLinkText(String.valueOf(Html.fromHtml(comment.getComment())));
            if (comment.getJson_Data() != null) {
                try {
                    ClickAbleKeywordText.createLink(holder.tvComment, tvComment.getText().toString(), comment.getJson_Data());
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            } else {
                ClickAbleKeywordText.createLink(holder.tvComment, tvComment.getText().toString());
            }
//            holder.tvComment.setAutoLinkText(String.valueOf(Html.fromHtml(testSH)));

            if (comment.getAttachment() == null) {
                holder.attachmentParent.setVisibility(View.GONE);
            } else {
                holder.attachmentParent.setVisibility(View.VISIBLE);
                if (comment.getAttachment().getType().trim().equalsIgnoreCase("video")) {
                    holder.videoIcon.setVisibility(View.VISIBLE);
                    Glide.with(holder.itemView.getContext()).load(URL.images_baseurl + comment.getAttachment().getPoster()).
//                            asBitmap().
        diskCacheStrategy(DiskCacheStrategy.SOURCE).
                            placeholder(R.drawable.place_holder_wall).into(attachment);
                } else {
                    holder.videoIcon.setVisibility(View.GONE);
                    Glide.with(holder.itemView.getContext()).load(URL.images_baseurl + comment.getAttachment().getFile()).
//                            asBitmap().
        diskCacheStrategy(DiskCacheStrategy.SOURCE).
                            placeholder(R.drawable.place_holder_wall).into(attachment);
                }
                holder.attachmentParent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClick(comment.getAttachment(), 0, 2);
                    }
                });
            }

            holder.tvComment.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
                @Override
                public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                    switch (autoLinkMode) {
                        case MODE_MENTION:
                        case MODE_HASHTAG: {
                            MentionTagJsonModel temp = null;

                            for (MentionTagJsonModel mentionTagJsonModel : comment.getJson_Data()) {
                                if (mentionTagJsonModel.getValue().trim().contains(matchedText.trim().substring(1).replace("_", " "))) {
                                    temp = mentionTagJsonModel;
                                    break;
                                }
                            }
                            linkClickListener.onLinkClick(autoLinkMode, matchedText, temp);
                        }
                        break;
                        default: {
                            linkClickListener.onLinkClick(autoLinkMode, matchedText, null);
                        }
                    }
                }
            });

            //ClickAbleKeywordText.MakeKeywordClickableText(holder.itemView.getContext(), comment.getComment(), tvComment);
            View.OnClickListener personClick = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(comment.getUser(), 0, 1);
                }
            };
            holder.tvPersonName.setOnClickListener(personClick);
            holder.civPersonImg.setOnClickListener(personClick);
        }


    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        sortData();
        notifyDataSetChanged();
    }

    public void removeAt(int index) {
        this.comments.remove(index);
        notifyDataSetChanged();
    }

    public void addToDataSet(@NonNull List<Comment> posts) {
        if (comments == null || comments.isEmpty()) {
            comments = posts;
            notifyDataSetChanged();
            return;
        }
        if (posts == null || posts.isEmpty()) {
            return;
        }
        LinkedHashMap<Integer, Comment> temp = new LinkedHashMap<>();
        for (Comment post : comments) {
            temp.put(post.getId(), post);
        }
        for (Comment post : posts) {
            temp.put(post.getId(), post);
        }
        comments = new ArrayList<>(temp.values());
        temp.clear();
        temp = null;
        sortData();
        notifyDataSetChanged();
    }

    public void setDataSet(List<Comment> dataSet) {
        this.comments = dataSet;
        sortData();
        this.notifyDataSetChanged();
    }

    private boolean isLiked(Comment post) {
        if (post.getLikes() == null || post.getLikes().isEmpty()) {
            return false;
        }
        for (Like like : post.getLikes()) {
            if (Splash.user.getUser_id() == like.getUserId() && like.getIsLike() > 0) {
                return true;
            }
        }
        return false;
    }

    private void sortData() {
        if (this.comments == null || this.comments.size() <= 1) {
            return;
        }
        if (isFromPost) {
            Collections.sort(this.comments, new Comparator<Comment>() {
                @Override
                public int compare(Comment comment, Comment t1) {
                    return comment.getId().compareTo(t1.getId());

                }
            });
        } else {
            Collections.sort(this.comments, new Comparator<Comment>() {
                @Override
                public int compare(Comment comment, Comment t1) {
                    return comment.getCreatedAt().trim().compareToIgnoreCase(t1.getCreatedAt().trim());
                }
            });
        }

    }
}
