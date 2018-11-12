package com.codingpixel.healingbudz.DataModel;


public class GroupsInviteNewBudDataModel {
    private String  Name;
    private boolean isInvited;
    private  int id;
    private String email;
    private String image_path;
    private int points;


//   @API Response
//    {
//        "": 48,
//            "first_name": "Alex",
//            "last_name": null,
//            "email": "testuser@gmail.com",
//            "password": "$2y$10$DIzkLaTlFAKkxMu/iIM.o.DfZUhZr9UIKeK.u2XgiUaoF8XvElpUu",
//            "zip_code": 74728,
//            "image_path": "/profile_pics/1509364697.jpg",
//            "user_type": 1,
//            "avatar": "/icons/4.png",
//            "cover": "/cover/1509214472.jpg",
//            "bio": null,
//            "location": "Broken Bow, OK 74728, USA",
//            "points": 137,
//            "remember_token": "7NTxVje1PqBHSoKMzweJyAiVGjp6bnNWL0ngdtCeOcu9uHMkBMBdlrHwAyQp",
//            "created_at": "2017-10-25 14:06:24",
//            "updated_at": "2017-10-30 11:58:17",
//            "is_following_count": 0
//    }
    public GroupsInviteNewBudDataModel(String name, boolean isInvited){
        this.Name = name;
        this.isInvited = isInvited;
    }
    public GroupsInviteNewBudDataModel(){
        super();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isInvited() {

        return isInvited;
    }

    public void setInvited(boolean invited) {
        isInvited = invited;
    }
}
