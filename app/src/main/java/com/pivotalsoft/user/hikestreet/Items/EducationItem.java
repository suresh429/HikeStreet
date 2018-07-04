package com.pivotalsoft.user.hikestreet.Items;

/**
 * Created by Gangadhar on 11/26/2017.
 */

public class EducationItem {
    private String educationid;
    private String degree;
    private String schoolcollege;
    private String startyear;
    private String endyear;
    private String stillworking;
    private String userid;

    public EducationItem(String educationid, String degree, String schoolcollege, String startyear, String endyear, String stillworking, String userid) {
        this.educationid = educationid;
        this.degree = degree;
        this.schoolcollege = schoolcollege;
        this.startyear = startyear;
        this.endyear = endyear;
        this.stillworking = stillworking;
        this.userid = userid;
    }

    public String getEducationid() {
        return educationid;
    }

    public void setEducationid(String educationid) {
        this.educationid = educationid;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSchoolcollege() {
        return schoolcollege;
    }

    public void setSchoolcollege(String schoolcollege) {
        this.schoolcollege = schoolcollege;
    }

    public String getStartyear() {
        return startyear;
    }

    public void setStartyear(String startyear) {
        this.startyear = startyear;
    }

    public String getEndyear() {
        return endyear;
    }

    public void setEndyear(String endyear) {
        this.endyear = endyear;
    }

    public String getStillworking() {
        return stillworking;
    }

    public void setStillworking(String stillworking) {
        this.stillworking = stillworking;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}

