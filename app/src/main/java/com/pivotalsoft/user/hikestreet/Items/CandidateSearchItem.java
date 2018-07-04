package com.pivotalsoft.user.hikestreet.Items;

/**
 * Created by Gangadhar on 12/1/2017.
 */

public class CandidateSearchItem {

    String personalid;
    String fullname;
    String about;
    String location;
    String latitude;
    String longitude;
    String emailid;
    String dob;
    String experience;
    String qualification;
    String gender;
    String languages;
    String profilepic;
    String userid;
    String mobileno;
    String role;
    String status ;
    String lastlogintime;
    String usercode;

    public CandidateSearchItem(String personalid, String fullname, String about, String location, String latitude, String longitude, String emailid, String dob, String experience, String qualification, String gender, String languages, String profilepic, String userid, String mobileno, String role, String status, String lastlogintime, String usercode) {
        this.personalid = personalid;
        this.fullname = fullname;
        this.about = about;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.emailid = emailid;
        this.dob = dob;
        this.experience = experience;
        this.qualification = qualification;
        this.gender = gender;
        this.languages = languages;
        this.profilepic = profilepic;
        this.userid = userid;
        this.mobileno = mobileno;
        this.role = role;
        this.status = status;
        this.lastlogintime = lastlogintime;
        this.usercode = usercode;
    }

    public String getPersonalid() {
        return personalid;
    }

    public void setPersonalid(String personalid) {
        this.personalid = personalid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
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
}
