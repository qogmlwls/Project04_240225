package com.example.project04_240225.Network.DTO;

public class UserInfoData {

    private String name;
    private String profileImageUrl;
    private String authData;
    private String authType;



    public UserInfoData(){
        name = null;
        profileImageUrl = null;
        authData = null;
        authType = null;
    }
    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getAuthData() {
        return authData;
    }

    public String getAuthType() {
        return authType;
    }

    public boolean hasProfileImage(){

        return profileImageUrl!=null?true:false;

    }

    public void setAuthData(String authData) {
        this.authData = authData;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
