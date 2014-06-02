package com.moksie.onthemove.objects;

import java.util.ArrayList;

/**
 * Created by belh0 on 14-04-2014.
 */
public class Map
{
    private String location;
    private String url;

    public Map(String location, String url) {
        this.location = location;
        this.url = url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
