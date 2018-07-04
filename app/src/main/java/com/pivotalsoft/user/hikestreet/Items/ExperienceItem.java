package com.pivotalsoft.user.hikestreet.Items;

/**
 * Created by Gangadhar on 11/10/2017.
 */

public class ExperienceItem {
    private String experienceid;
    private String workedas;
    private String company;
    private String startyear;
    private String endyear;
    private String stillworking;
    private String userid;

    public ExperienceItem(String experienceid, String workedas, String company, String startyear, String endyear, String stillworking, String userid) {
        this.experienceid = experienceid;
        this.workedas = workedas;
        this.company = company;
        this.startyear = startyear;
        this.endyear = endyear;
        this.stillworking = stillworking;
        this.userid = userid;
    }

    public String getExperienceid() {
        return experienceid;
    }

    public void setExperienceid(String experienceid) {
        this.experienceid = experienceid;
    }

    public String getWorkedas() {
        return workedas;
    }

    public void setWorkedas(String workedas) {
        this.workedas = workedas;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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
