package com.avijit.rms.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class District {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String districtId;
    public String divisionId;
    public String name;

    public District(String districtId, String divisionId, String name) {
        this.districtId = districtId;
        this.divisionId = divisionId;
        this.name = name;
    }
}
