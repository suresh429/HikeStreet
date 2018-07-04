package com.pivotalsoft.user.hikestreet.Items;

/**
 * Created by Gangadhar on 11/14/2017.
 */

public class NewJobItem {
    String jobid;
    String title;
    String description;
    String category;
    String location;
    String latitude;
    String longitude;
    String compensation;
    String type;
    String postedon;
    String userid;
    String appliedcount;
    String shortlistedcount ;
    String hiredcount;

    public NewJobItem(String jobid, String title, String description, String category, String location, String latitude, String longitude, String compensation, String type, String postedon, String userid, String appliedcount, String shortlistedcount, String hiredcount) {
        this.jobid = jobid;
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.compensation = compensation;
        this.type = type;
        this.postedon = postedon;
        this.userid = userid;
        this.appliedcount = appliedcount;
        this.shortlistedcount = shortlistedcount;
        this.hiredcount = hiredcount;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCompensation() {
        return compensation;
    }

    public void setCompensation(String compensation) {
        this.compensation = compensation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostedon() {
        return postedon;
    }

    public void setPostedon(String postedon) {
        this.postedon = postedon;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAppliedcount() {
        return appliedcount;
    }

    public void setAppliedcount(String appliedcount) {
        this.appliedcount = appliedcount;
    }

    public String getShortlistedcount() {
        return shortlistedcount;
    }

    public void setShortlistedcount(String shortlistedcount) {
        this.shortlistedcount = shortlistedcount;
    }

    public String getHiredcount() {
        return hiredcount;
    }

    public void setHiredcount(String hiredcount) {
        this.hiredcount = hiredcount;
    }
}
