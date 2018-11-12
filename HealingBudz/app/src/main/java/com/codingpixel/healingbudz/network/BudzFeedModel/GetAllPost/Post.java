
package com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost;

import com.codingpixel.healingbudz.network.BudzFeedModel.AddComment.Comment;
import com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel.FollowingUser;
import com.codingpixel.healingbudz.network.BudzFeedModel.MentionTagJsonModel;
import com.codingpixel.healingbudz.network.BudzFeedModel.ReportModel.FlagPost;
import com.codingpixel.healingbudz.network.BudzFeedModel.SubUsers.SubUser;
import com.codingpixel.healingbudz.network.BudzFeedModel.submitPostLike.Like;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("sub_user_id")
    @Expose
    private Integer subUserId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("post_added_comment")
    @Expose
    private String post_added_comment;
    @SerializedName("json_data")
    @Expose
    private String jsonData;
    @SerializedName("allow_repost")
    @Expose
    private Integer allowRepost;
    @SerializedName("shared_id")
    @Expose
    private Integer sharedId;
    @SerializedName("shared_user_id")
    @Expose
    private Integer sharedUserId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("liked_count")
    @Expose
    private Integer likedCount;
    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @SerializedName("shared_count")
    @Expose
    private Integer sharedCount;
    @SerializedName("flaged_count")
    @Expose
    private Integer flagedCount;
    @SerializedName("comments_count")
    @Expose
    private Integer commentsCount;
    @SerializedName("mute_post_by_user_count")
    @Expose
    private Integer mutePostByUserCount;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("shared_post")
    @Expose
    private SharedPost sharedPost;
    @SerializedName("shared_user")
    @Expose
    private SharedUser sharedUser;
    @SerializedName("files")
    @Expose
    private List<File> files = null;
    @SerializedName("tagged")
    @Expose
    private List<Tag> tagged = null;
    @SerializedName("likes")
    @Expose
    private List<Like> likes = null;
    @SerializedName("flags")
    @Expose
    private List<FlagPost> flags = null;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;
    @SerializedName("scraped_url")
    @Expose
    private Object scrapedUrl;
    @SerializedName("sub_user")
    @Expose
    private SubUser sub_user;

    public String getPost_added_comment() {
        if (post_added_comment == null)
            return "";
        return post_added_comment;
    }

    public void setPost_added_comment(String post_added_comment) {
        this.post_added_comment = post_added_comment;
    }

    public SubUser getSub_user() {
        return sub_user;
    }

    public void setSub_user(SubUser sub_user) {
        this.sub_user = sub_user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        if (userId == null) {
            return 0;
        }
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSubUserId() {
        return subUserId;
    }

    public void setSubUserId(Integer subUserId) {
        this.subUserId = subUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJson_Data() {
        if (jsonData == null) {
            return "";
        }
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Integer getAllowRepost() {
        if (allowRepost == null) {
            return 0;
        }
        return allowRepost;
    }

    public void setAllowRepost(Integer allowRepost) {
        this.allowRepost = allowRepost;
    }

    public Integer getSharedId() {
        if (sharedId == null) {
            return 0;
        }
        return sharedId;
    }

    public void setSharedId(Integer sharedId) {
        this.sharedId = sharedId;
    }

    public Integer getSharedUserId() {
        if (sharedUserId == null) {
            return 0;
        }
        return sharedUserId;
    }

    public void setSharedUserId(Integer sharedUserId) {
        this.sharedUserId = sharedUserId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getLikedCount() {
        if (likedCount == null) {
            return 0;
        }
        if (likedCount < 0) {
            return 0;
        }
        return likedCount;
    }

    public void setLikedCount(Integer likedCount) {
        this.likedCount = likedCount;
    }

    public Integer getLikesCount() {
        if (likesCount == null) {
            return 0;
        }
        if (likesCount < 0) {
            return 0;
        }
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getSharedCount() {
        if (sharedCount == null) {
            return 0;
        }
        return sharedCount;
    }

    public void setSharedCount(Integer sharedCount) {
        this.sharedCount = sharedCount;
    }

    public Integer getFlagedCount() {
        if (flagedCount == null) {
            return 0;
        }
        return flagedCount;
    }

    public void setFlagedCount(Integer flagedCount) {
        this.flagedCount = flagedCount;
    }

    public Integer getCommentsCount() {
        if (commentsCount == null || commentsCount < 0) {
            return 0;
        }
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Integer getMutePostByUserCount() {
        if (mutePostByUserCount == null) {
            return 0;
        }
        return mutePostByUserCount;
    }

    public void setMutePostByUserCount(Integer mutePostByUserCount) {
        this.mutePostByUserCount = mutePostByUserCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SharedPost getSharedPost() {
        return sharedPost;
    }

    public void setSharedPost(SharedPost sharedPost) {
        this.sharedPost = sharedPost;
    }

    public SharedUser getSharedUser() {
        return sharedUser;
    }

    public void setSharedUser(SharedUser sharedUser) {
        this.sharedUser = sharedUser;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<Tag> getTagged() {
        return tagged;
    }

    public List<FollowingUser> get_Tagged() {
        List<FollowingUser> users = new ArrayList<>();
        if (tagged == null || tagged.isEmpty()) {
            return users;
        }
        for (Tag tag : tagged) {
            users.add(tag.getUser());
        }
        return users;
    }

    public void setTagged(List<Tag> tagged) {
        this.tagged = tagged;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<FlagPost> getFlags() {
        return flags;
    }

    public void setFlags(List<FlagPost> flags) {
        this.flags = flags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Object getScrapedUrl() {
        return scrapedUrl;
    }

    public void setScrapedUrl(Object scrapedUrl) {
        this.scrapedUrl = scrapedUrl;
    }

    public MentionTagJsonModel[] getJsonData() {
        return new Gson().fromJson(getJson_Data(), MentionTagJsonModel[].class);
    }
}
