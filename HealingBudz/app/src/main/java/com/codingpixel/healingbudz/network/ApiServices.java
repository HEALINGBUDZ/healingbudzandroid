package com.codingpixel.healingbudz.network;
/*
 * Created by M_Muzammil Sharif on 06-Mar-18.
 */

import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.network.BudzFeedModel.AddComment.AddCommentResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.AddComment.GetAllComment;
import com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel.FollowingUserResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.GeneralResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.GetAllPostResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.GetPostDetailResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.MutePostModel.MutePostResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.ReportModel.ReportPostResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.SubUsers.SubUsersResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.TagModel.TagResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.submitPostLike.SubmitPostLikeResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {

    /**
     * @param skipPage      user_id
     * @param session_token user_session_token
     * @param filters       post filters
     * @return {@link GetAllPostResponse}
     */
    @Headers("app_key:" + Constants.appKey)
    @GET("get_user_posts")
    public Call<GetAllPostResponse> getWallFeeds(@Header("session_token") String session_token,
                                                 @Query("skip") int skipPage,
                                                 @Query("filters") String filters);

    /**
     * @param post_id       post against whitch submiting like
     * @param is_like       0 = unLike | 1 = like
     * @param session_token user
     * @return {@link SubmitPostLikeResponse}
     */
    @Headers("app_key:" + Constants.appKey)
    @FormUrlEncoded
    @POST("add_post_like_dislike")
    public Call<SubmitPostLikeResponse> submitPostLike(@Header("session_token") String session_token,
                                                       @Field("post_id") int post_id,
                                                       @Field("is_like") int is_like);
    /**
     * @param post_id       post against whitch submiting like
     * @param is_like       0 = unLike | 1 = like
     * @param session_token user
     * @return {@link SubmitPostLikeResponse}
     */
    @Headers("app_key:" + Constants.appKey)
    @FormUrlEncoded
    @POST("add_comment_like_dislike")
    public Call<SubmitPostLikeResponse> submitCommentLike(@Header("session_token") String session_token,
                                                       @Field("comment_id") int post_id,
                                                       @Field("is_like") int is_like);

    /**
     * @param post_id       post against whitch submiting comment
     * @param comment       user comment
     * @param json_data     tag and mention details
     * @param session_token user
     * @param attachment    attached file url
     * @param type          file type of attachement
     * @param poster        image url if file is video
     * @return {@link AddCommentResponse}
     */
    @Headers("app_key:" + Constants.appKey)
    @FormUrlEncoded
    @POST("add_comment")
    public Call<AddCommentResponse> submitPostComment(@Header("session_token") String session_token,
                                                      @Field("post_id") int post_id,
                                                      @Field("comment") String comment,
                                                      @Field("json_data") String json_data,
                                                      @Field("attachment") String attachment,
                                                      @Field("type") String type,
                                                      @Field("poster") String poster,
                                                      @Field("thumb") String thumb);

    @Headers("app_key:" + Constants.appKey)
    @FormUrlEncoded
    @POST("add_comment")
    public Call<AddCommentResponse> submitUpdatePostComment(@Header("session_token") String session_token,
                                                            @Field("post_id") int post_id,
                                                            @Field("comment") String comment,
                                                            @Field("json_data") String json_data,
                                                            @Field("attachment") String attachment,
                                                            @Field("type") String type,
                                                            @Field("poster") String poster,
                                                            @Field("comment_id") int comment_id,
                                                            @Field("thumb") String thumb);

    /**
     * @param post_id       post against whitch submiting comment
     * @param session_token user
     * @param reason        report reson
     * @return {@link AddCommentResponse}
     */
    @Headers("app_key:" + Constants.appKey)
    @FormUrlEncoded
    @POST("add_post_flag")
    public Call<ReportPostResponse> reportPost(@Header("session_token") String session_token,
                                               @Field("post_id") int post_id,
                                               @Field("reason") String reason);

    /**
     * @param post_id       post against whitch submiting comment
     * @param session_token user
     * @param is_mute       0 => unMute, 1 => mute
     * @return {@link AddCommentResponse}
     */
    @Headers("app_key:" + Constants.appKey)
    @FormUrlEncoded
    @POST("mute_post")
    public Call<MutePostResponse> mutePost(@Header("session_token") String session_token,
                                           @Field("post_id") int post_id,
                                           @Field("is_mute") int is_mute);

    @Headers("app_key:" + Constants.appKey)
    @FormUrlEncoded
    @POST("save_post")
    public Call<GetAllPostResponse> savePost(@Header("session_token") String session_token,
                                             @Field("description") String description,
                                             @Field("images") String images,
                                             @Field("video") String video,
                                             @Field("poster") String poster,
                                             @Field("tagged") String tagged,
                                             @Field("json_data") String json_data,
                                             @Field("posting_user") String posting_user,
                                             @Field("repost_to_wall") int repost_to_wall,
                                             @Field("url") String url,
                                             @Field("thumb") String thumb,
                                             @Field("ratio") String ratio);

    /**
     * @param userId        user id
     * @param session_token user session
     * @return {@link FollowingUserResponse}
     */
    @Headers("app_key:" + Constants.appKey)
    @GET("get_followings/{USER_ID}")
    public Call<FollowingUserResponse> getFollowingUsers(@Path(value = "USER_ID", encoded = true) int userId,
                                                         @Header("session_token") String session_token);


    /**
     * @param session_token user session
     * @return {@link TagResponse}
     */
    @Headers("app_key:" + Constants.appKey)
    @GET("get_tags")
    public Call<TagResponse> getAllTags(@Header("session_token") String session_token);

    /**
     * @param session_token user session
     * @param post_id       deleting post
     * @return {@link GeneralResponse}
     */
    @Headers("app_key:" + Constants.appKey)
    @FormUrlEncoded
    @POST("delete_post")
    public Call<GeneralResponse> deletePost(@Header("session_token") String session_token,
                                            @Field("post_id") int post_id);


    @Headers("app_key:" + Constants.appKey)
    @FormUrlEncoded
    @POST("delete_comment")
    public Call<GeneralResponse> deleteComment(@Header("session_token") String session_token,
                                               @Field("comment_id") int comment_id);

    /**
     * @param session_token user session
     * @param post_id       reposting post
     * @param posting_user  against which user posting
     * @param tagged_users  tagged users
     * @return {@link GetAllPostResponse}
     */
    @Headers("app_key:" + Constants.appKey)
    @FormUrlEncoded
    @POST("repost")
    public Call<GetAllPostResponse> repost(@Header("session_token") String session_token,
                                           @Field("post_id") int post_id,
                                           @Field("posting_user") String posting_user,
                                           @Field("tagged_users") String tagged_users);

    /**
     * @param session_token user session
     * @param post_id       reposting post
     * @param posting_user  against which user posting
     * @param tagged_users  tagged users
     * @return {@link GetAllPostResponse}
     */
    @Headers("app_key:" + Constants.appKey)
    @FormUrlEncoded
    @POST("repost")
    public Call<GetAllPostResponse> repost(@Header("session_token") String session_token,
                                           @Field("post_id") int post_id,
                                           @Field("posting_user") String posting_user,
                                           @Field("tagged_users") String tagged_users,
                                           @Field("post_added_comment") String post_added_comment
    );


    @Headers("app_key:" + Constants.appKey)
    @GET("get_sub_users")
    public Call<SubUsersResponse> getSubUsers(@Header("session_token") String session_token);

    @Headers("app_key:" + Constants.appKey)
    @GET("get_all_sub_users")
    public Call<SubUsersResponse> getSubAllUsers(@Header("session_token") String session_token);

    @Headers("app_key:" + Constants.appKey)
    @GET("user_posts/{USER_ID}")
    public Call<GetAllPostResponse> getUserPost(@Header("session_token") String session_token,
                                                @Path(value = "USER_ID", encoded = true) int userId,
                                                @Query("skip") int skip);

    @Headers("app_key:" + Constants.appKey)
    @GET("user_post_comment/{POST_ID}")
    public Call<GetAllComment> getUserComments(@Header("session_token") String session_token,
                                               @Path(value = "POST_ID", encoded = true) int userId,
                                               @Query("skip") int skip);

    //    http://139.162.37.73/healingbudz/api/v1/user_post_comment/75?skip=1
    @Headers("app_key:" + Constants.appKey)
    @FormUrlEncoded
    @POST("follow_user")
    public Call<GeneralResponse> followUser(@Header("session_token") String session_token,
                                            @Field("followed_id") int followed_id);

    @Headers("app_key:" + Constants.appKey)
    @FormUrlEncoded
    @POST("un_follow")
    public Call<GeneralResponse> unFollowUser(@Header("session_token") String session_token,
                                              @Field("followed_id") int followed_id);

    @Headers("app_key:" + Constants.appKey)
    @FormUrlEncoded
    @POST("save_post")
    public Call<GetAllPostResponse> updatePost(@Header("session_token") String session_token,
                                               @Field("post_id") int post_id,
                                               @Field("description") String description,
                                               @Field("images") String images,
                                               @Field("video") String video,
                                               @Field("poster") String poster,
                                               @Field("tagged") String tagged,
                                               @Field("json_data") String json_data,
                                               @Field("posting_user") String posting_user,
                                               @Field("repost_to_wall") int repost_to_wall,
                                               @Field("url") String url,
                                               @Field("thumb") String thumb,
                                               @Field("ratio") String ratio);

    @Headers("app_key:" + Constants.appKey)
    @GET("get_post/{POST_ID}")
    public Call<GetPostDetailResponse> getPostDetail(@Header("session_token") String session_token,
                                                     @Path(value = "POST_ID", encoded = true) int postId);

}
