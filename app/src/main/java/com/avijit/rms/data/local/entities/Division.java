package com.avijit.rms.data.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Division {
    @PrimaryKey(autoGenerate = true)
    public int id ;
    public String divisionId;
    public String name;

    public Division(String divisionId, String name) {
        this.divisionId = divisionId;
        this.name = name;
    }
}
