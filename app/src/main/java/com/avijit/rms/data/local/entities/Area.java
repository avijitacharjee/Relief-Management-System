package com.avijit.rms.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Area {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String districtId;
    public String areaId;
    public String name;
    public String type;

    public Area(String districtId, String areaId, String name, String type) {
        this.districtId = districtId;
        this.areaId = areaId;
        this.name = name;
        this.type = type;
    }
}
