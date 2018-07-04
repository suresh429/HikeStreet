package com.pivotalsoft.user.hikestreet.Items;

/**
 * Created by Gangadhar on 11/10/2017.
 */

public class CategoryItem {
    String id;
    String name;

    public CategoryItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
