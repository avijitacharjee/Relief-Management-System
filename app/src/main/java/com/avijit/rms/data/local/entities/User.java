package com.avijit.rms.data.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    public int id;
    @ColumnInfo(name = "name")
    public String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
