package com.avijit.rms.data.local.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.avijit.rms.data.local.entities.District;

import java.util.List;

@Dao
public interface DistrictDao {
    @Query("SELECT * FROM District")
    List<District> getAll();
    @Insert
    void insertAll(District... districts);
    @Query("DELETE FROM District")
    void deleteAll();
    @Query("SELECT * FROM District where districtId=(:id)")
    List<District> getDistrictByDivisionId(String id);
}
