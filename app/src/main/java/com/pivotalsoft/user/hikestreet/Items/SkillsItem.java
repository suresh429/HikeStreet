package com.pivotalsoft.user.hikestreet.Items;

/**
 * Created by Gangadhar on 11/26/2017.
 */

public class SkillsItem {
   private String skillid;
    private String institute;
    private String course;
    private String startyear;
    private String endyear;
    private String userid;

    public SkillsItem(String skillid, String institute, String course, String startyear, String endyear, String userid) {
        this.skillid = skillid;
        this.institute = institute;
        this.course = course;
        this.startyear = startyear;
        this.endyear = endyear;
        this.userid = userid;
    }

    public String getSkillid() {
        return skillid;
    }

    public void setSkillid(String skillid) {
        this.skillid = skillid;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}

