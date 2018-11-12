package com.codingpixel.healingbudz.DataModel;

import android.graphics.drawable.Drawable;

public class GroupsChatMsgsDataModel {
    boolean isTimeItem;
    boolean isNewMemberItem;
    boolean isVideoMsg;
    boolean isImageMsg;
    int msgLikes;
    int userImg;
    Drawable media_resourse;
    private String Member_Name;
    private String Msg_TExt;
    boolean isCurrentUserLiked;
    String created_at;
    String updated_at;
    int id;
    int user_id;
    String file_path;
    String poster;
    String user_image_path;
    String user_avatar;
    int group_id;

    boolean isUploadinStart;
    Drawable loacal_image_drawable;
    Drawable local_video_thumbnil;
    String Local_video_path;
    String Local_image_path;

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public boolean isUploadinStart() {
        return isUploadinStart;
    }

    public void setUploadinStart(boolean uploadinStart) {
        isUploadinStart = uploadinStart;
    }

    public Drawable getLoacal_image_drawable() {
        return loacal_image_drawable;
    }

    public void setLoacal_image_drawable(Drawable loacal_image_drawable) {
        this.loacal_image_drawable = loacal_image_drawable;
    }

    public Drawable getLocal_video_thumbnil() {
        return local_video_thumbnil;
    }

    public void setLocal_video_thumbnil(Drawable local_video_thumbnil) {
        this.local_video_thumbnil = local_video_thumbnil;
    }

    public String getLocal_video_path() {
        return Local_video_path;
    }

    public void setLocal_video_path(String local_video_path) {
        Local_video_path = local_video_path;
    }

    public String getLocal_image_path() {
        return Local_image_path;
    }

    public void setLocal_image_path(String local_image_path) {
        Local_image_path = local_image_path;
    }


    public String getUser_image_path() {
        return user_image_path;
    }

    public void setUser_image_path(String user_image_path) {
        this.user_image_path = user_image_path;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public GroupsChatMsgsDataModel() {

    }

    public GroupsChatMsgsDataModel(String member_Name, String msg_Text, int userImg, Drawable media_resourse, int msgLikes, boolean isImageMsg, boolean isVideoMsg, boolean isTimeItem, boolean isNewMemberItem, boolean isCurrentUserLiked, String added_date) {
        this.Member_Name = member_Name;
        this.Msg_TExt = msg_Text;
        this.isTimeItem = isTimeItem;
        this.isNewMemberItem = isNewMemberItem;
        this.isVideoMsg = isVideoMsg;
        this.isImageMsg = isImageMsg;
        this.msgLikes = msgLikes;
        this.userImg = userImg;
        this.media_resourse = media_resourse;
        this.isCurrentUserLiked = isCurrentUserLiked;
        this.created_at = added_date;
    }

    public String getMember_Name() {
        return Member_Name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public boolean isCurrentUserLiked() {
        return isCurrentUserLiked;
    }

    public void setCurrentUserLiked(boolean currentUserLiked) {
        isCurrentUserLiked = currentUserLiked;
    }

    public Drawable getMedia_resourse() {
        return media_resourse;
    }

    public void setMedia_resourse(Drawable media_resourse) {
        this.media_resourse = media_resourse;
    }

    public void setMember_Name(String member_Name) {
        Member_Name = member_Name;
    }

    public String getMsg_TExt() {
        return Msg_TExt;
    }

    public void setMsg_TExt(String msg_TExt) {
        Msg_TExt = msg_TExt;
    }

    public boolean isTimeItem() {
        return isTimeItem;
    }

    public void setTimeItem(boolean timeItem) {
        isTimeItem = timeItem;
    }

    public boolean isNewMemberItem() {
        return isNewMemberItem;
    }

    public void setNewMemberItem(boolean newMemberItem) {
        isNewMemberItem = newMemberItem;
    }

    public boolean isVideoMsg() {
        return isVideoMsg;
    }

    public void setVideoMsg(boolean videoMsg) {
        isVideoMsg = videoMsg;
    }

    public boolean isImageMsg() {
        return isImageMsg;
    }

    public void setImageMsg(boolean imageMsg) {
        isImageMsg = imageMsg;
    }

    public int getMsgLikes() {
        return msgLikes;
    }

    public void setMsgLikes(int msgLikes) {
        this.msgLikes = msgLikes;
    }

    public int getUserImg() {
        return userImg;
    }

    public void setUserImg(int userImg) {
        this.userImg = userImg;
    }
}
