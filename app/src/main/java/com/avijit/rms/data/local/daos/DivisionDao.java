package com.avijit.rms.data.local.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.avijit.rms.data.local.entities.Division;

import java.util.List;

@Dao
public interface DivisionDao {
    @Query("Select * from Division")
    List<Division> getAll();
    @Insert
    void insertAll(Division... divisions);
    @Delete
    void delete(Division division);
    @Query("Delete from Division")
    void deleteAll();
}
