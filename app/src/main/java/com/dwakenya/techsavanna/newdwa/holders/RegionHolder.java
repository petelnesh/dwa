package com.dwakenya.techsavanna.newdwa.holders;

/**
 * Created by Padie on 8/14/2017.
 */

public class RegionHolder {
    private String id, region_name;

    public RegionHolder(String id, String region_name) {
        this.id = id;
        this.region_name = region_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }
}
