package com.pivotalsoft.user.hikestreet.Items;

/**
 * Created by Gangadhar on 12/6/2017.
 */

public class JobCountItem {
    String count;
    String status;



    public JobCountItem(String count, String status) {
        this.count = count;
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
