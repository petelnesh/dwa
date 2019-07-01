package com.dwakenya.techsavanna.newdwa.holders;

/**
 * Created by Padie on 8/25/2017.
 */

public class OrgHolder {
    String id, org_name;

    public OrgHolder(String id, String org_name) {
        this.id = id;
        this.org_name = org_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }
}
