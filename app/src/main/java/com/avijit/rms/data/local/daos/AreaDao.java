package com.avijit.rms.data.local.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.avijit.rms.data.local.entities.Area;

import java.util.List;

@Dao
public interface AreaDao {
    @Query("SELECT * FROM Area")
    List<Area> getAll();
    @Insert
    void insert(Area... areas);
    @Query("DELETE from Area")
    void deleteAll();
    @Query("SELECT * FROM Area WHERE Area.districtId =(:districtId)")
    List<Area> getAreasByDistrictId(String districtId);
}
