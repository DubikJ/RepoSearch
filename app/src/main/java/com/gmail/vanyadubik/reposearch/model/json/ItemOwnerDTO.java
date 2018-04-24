package com.gmail.vanyadubik.reposearch.model.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ItemOwnerDTO{

    @Expose
    String login;

    @Expose
    int id;

    @Expose
    @SerializedName("avatar_url")
    String avatar;

    @Expose
    @SerializedName("html_url")
    String url;

    public ItemOwnerDTO(String login, int id,
                              String avatar, String url1) {
        this.login = login;
        this.id = id;
        this.avatar = avatar;
        this.url = url1;
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUrl() {
        return url;
    }
}
