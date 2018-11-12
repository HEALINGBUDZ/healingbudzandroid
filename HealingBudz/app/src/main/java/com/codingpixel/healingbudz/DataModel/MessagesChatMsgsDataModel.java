package com.codingpixel.healingbudz.DataModel;

import android.graphics.drawable.Drawable;

public class MessagesChatMsgsDataModel {
    boolean isTimeItem;
    boolean isVideoMsg;
    boolean isImageMsg;
    int userImg;
    Drawable media_resourse;
    private String Msg_TExt;
    boolean isAddNewMemberMSg;
    String added_date;
    String image_Path;
    String video_path;
    String video_thumbni;
    boolean isUploadinStart;
    Drawable loacal_image_drawable;
    Drawable local_video_thumbnil;
    String Local_video_path;
    String Local_image_path;
    int receiver_id;
    String receiverName, senderName;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getLocal_image_path() {
        return Local_image_path;
    }

    public void setLocal_image_path(String local_image_path) {
        Local_image_path = local_image_path;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
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

    public boolean isAddNewMemberMSg() {
        return isAddNewMemberMSg;
    }

    public void setAddNewMemberMSg(boolean addNewMemberMSg) {
        isAddNewMemberMSg = addNewMemberMSg;
    }

    public String getImage_Path() {
        if (image_Path != null) {
            return image_Path;
        } else {
            return "";
        }
    }

    public void setImage_Path(String image_Path) {
        this.image_Path = image_Path;
    }

    public String getVideo_path() {
        if (video_path != null) {
            return video_path;
        } else {
            return "";
        }
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getVideo_thumbni() {
        return video_thumbni;
    }

    public void setVideo_thumbni(String video_thumbni) {
        this.video_thumbni = video_thumbni;
    }

    private boolean isReceiver;

    public boolean isTimeItem() {
        return isTimeItem;
    }

    public void setTimeItem(boolean timeItem) {
        isTimeItem = timeItem;
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

    public int getUserImg() {
        return userImg;
    }

    public void setUserImg(int userImg) {
        this.userImg = userImg;
    }

    public Drawable getMedia_resourse() {
        return media_resourse;
    }

    public void setMedia_resourse(Drawable media_resourse) {
        this.media_resourse = media_resourse;
    }

    public String getMsg_TExt() {
        return Msg_TExt;
    }

    public void setMsg_TExt(String msg_TExt) {
        Msg_TExt = msg_TExt;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public boolean isReceiver() {
        return isReceiver;
    }

    public void setReceiver(boolean receiver) {
        isReceiver = receiver;
    }

    public MessagesChatMsgsDataModel() {

    }

    public MessagesChatMsgsDataModel(String msg_Text, int userImg, Drawable media_resourse, boolean isImageMsg, boolean isVideoMsg, boolean isTimeItem, String added_date, boolean isReceiver) {
        this.Msg_TExt = msg_Text;
        this.isTimeItem = isTimeItem;
        this.isVideoMsg = isVideoMsg;
        this.isImageMsg = isImageMsg;
        this.userImg = userImg;
        this.added_date = added_date;
        this.media_resourse = media_resourse;
        this.isReceiver = isReceiver;

    }
}
