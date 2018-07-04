package com.pivotalsoft.user.hikestreet.Items;

/**
 * Created by Gangadhar on 11/8/2017.
 */

public class AddCardItem {
    String addid;
    String jobid;
    String image;

    public AddCardItem(String addid, String jobid, String image) {
        this.addid = addid;
        this.jobid = jobid;
        this.image = image;
    }

    public String getAddid() {
        return addid;
    }

    public void setAddid(String addid) {
        this.addid = addid;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
