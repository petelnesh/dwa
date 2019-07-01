package com.dwakenya.techsavanna.newdwa.holders;

/**
 * Created by Padie on 8/25/2017.
 */

public class SpecialityHolder {
    String id, speciality_name, where_learnt;

    public SpecialityHolder(String id, String speciality_name) {
        this.id = id;
        this.speciality_name = speciality_name;
    }

    public SpecialityHolder(String id, String speciality_name, String where_learnt) {
        this.id = id;
        this.speciality_name = speciality_name;
        this.where_learnt = where_learnt;
    }

    public String getWhere_learnt() {
        return where_learnt;
    }

    public void setWhere_learnt(String where_learnt) {
        this.where_learnt = where_learnt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpeciality_name() {
        return speciality_name;
    }

    public void setSpeciality_name(String speciality_name) {
        this.speciality_name = speciality_name;
    }
}
