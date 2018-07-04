package com.pivotalsoft.user.hikestreet.Items;

/**
 * Created by Gangadhar on 11/13/2017.
 */

public class HireSearchItem {

    String fullname;
    String qualification;
    String location;
    String latitude;
    String longitude;
    String userid;
    String experience;
    String applicationid;
    String gender;
    String appliedon;
    String applied ;
    String rejected ;
    String shortlisted ;
    String hired;
    String role;
    String usercode;
    String firebasekey;
    String title;

    public HireSearchItem(String fullname, String qualification, String location, String latitude, String longitude, String userid, String experience, String applicationid, String gender, String appliedon, String applied, String rejected, String shortlisted, String hired, String role, String usercode, String firebasekey, String title) {
        this.fullname = fullname;
        this.qualification = qualification;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userid = userid;
        this.experience = experience;
        this.applicationid = applicationid;
        this.gender = gender;
        this.appliedon = appliedon;
        this.applied = applied;
        this.rejected = rejected;
        this.shortlisted = shortlisted;
        this.hired = hired;
        this.role = role;
        this.usercode = usercode;
        this.firebasekey = firebasekey;
        this.title = title;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getApplicationid() {
        return applicationid;
    }

    public void setApplicationid(String applicationid) {
        this.applicationid = applicationid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
