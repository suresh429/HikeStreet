package com.pivotalsoft.user.hikestreet.Items;

/**
 * Created by Gangadhar on 11/11/2017.
 */

public class SearchItem {

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
    String mobileno;
    String role;
    String status;
    String lastlogintime;
    String usercode;
    String firebasekey;
    String recruitername;

    public SearchItem(String jobid, String title, String description, String category, String location, String latitude, String longitude, String compensation, String type, String postedon, String userid, String mobileno, String role, String status, String lastlogintime, String usercode, String firebasekey, String recruitername) {
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
        this.mobileno = mobileno;
        this.role = role;
        this.status = status;
        this.lastlogintime = lastlogintime;
        this.usercode = usercode;
        this.firebasekey = firebasekey;
        this.recruitername = recruitername;
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

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(String lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getFirebasekey() {
        return firebasekey;
    }

    public void setFirebasekey(String firebasekey) {
        this.firebasekey = firebasekey;
    }

    public String getRecruitername() {
        return recruitername;
    }

    public void setRecruitername(String recruitername) {
        this.recruitername = recruitername;
    }
}
