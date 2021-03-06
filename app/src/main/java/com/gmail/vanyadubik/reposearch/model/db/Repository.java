package com.gmail.vanyadubik.reposearch.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "repository")
public class Repository {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "full_name")
    private String full_name;

    @ColumnInfo(name = "private")
    private boolean priv;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "date_create")
    private String dateCreate;

    @ColumnInfo(name = "date_update")
    private String dateUpdate;

    @ColumnInfo(name = "size")
    private int size;

    @ColumnInfo(name = "watchers")
    private int watchers;

    @ColumnInfo(name = "score")
    private double score;

    @ColumnInfo(name = "def_branch")
    private String defBranch;

    @ColumnInfo(name = "owner_login")
    private String ownerLogin;

    @ColumnInfo(name = "owner_avatar")
    private String ownerAvatar;

    @ColumnInfo(name = "name_filter")
    private String nameFilter;

    public Repository(int id, String name, String full_name, boolean priv, String url,
                      String description, String dateCreate, String dateUpdate, int size,
                      int watchers, double score, String defBranch, String ownerLogin,
                      String ownerAvatar, String nameFilter) {
        this.id = id;
        this.name = name;
        this.full_name = full_name;
        this.priv = priv;
        this.url = url;
        this.description = description;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.size = size;
        this.watchers = watchers;
        this.score = score;
        this.defBranch = defBranch;
        this.ownerLogin = ownerLogin;
        this.ownerAvatar = ownerAvatar;
        this.nameFilter = nameFilter;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public boolean isPriv() {
        return priv;
    }

    public void setPriv(boolean priv) {
        this.priv = priv;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getWatchers() {
        return watchers;
    }

    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getDefBranch() {
        return defBranch;
    }

    public void setDefBranch(String defBranch) {
        this.defBranch = defBranch;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public String getOwnerAvatar() {
        return ownerAvatar;
    }

    public void setOwnerAvatar(String ownerAvatar) {
        this.ownerAvatar = ownerAvatar;
    }

    public String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }
}

