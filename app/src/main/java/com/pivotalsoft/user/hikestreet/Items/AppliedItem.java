package com.pivotalsoft.user.hikestreet.Items;

/**
 * Created by Gangadhar on 11/30/2017.
 */

public class AppliedItem {

    private String jobid;
    private String description;
    private String category;
    private String location;
    private String latitude;
    private String longitude;
    private String compensation;
    private String type;
    private String postedon;
    private String userid;
    private String appliedon;
    private String applied;
    private String rejected;
    private String shortlisted;
    private String hired;

    public AppliedItem(String jobid, String description, String category, String location, String latitude, String longitude, String compensation, String type, String postedon, String userid, String appliedon, String applied, String rejected, String shortlisted, String hired) {
        this.jobid = jobid;
        this.description = description;
        this.category = category;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.compensation = compensation;
        this.type = type;
        this.postedon = postedon;
        this.userid = userid;
        this.appliedon = appliedon;
        this.applied = applied;
        this.rejected = rejected;
        this.shortlisted = shortlisted;
        this.hired = hired;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
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

    public String getAppliedon() {
        return appliedon;
    }

    public void setAppliedon(String appliedon) {
        this.appliedon = appliedon;
    }

    public String getApplied() {
        return applied;
    }

    public void setApplied(String applied) {
        this.applied = applied;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }

    public String getShortlisted() {
        return shortlisted;
    }

    public void setShortlisted(String shortlisted) {
        this.shortlisted = shortlisted;
    }

    public String getHired() {
        return hired;
    }

    public void setHired(String hired) {
        this.hired = hired;
    }
}
