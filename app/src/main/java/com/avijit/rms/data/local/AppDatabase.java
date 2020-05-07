package com.avijit.rms.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.avijit.rms.data.local.daos.AreaDao;
import com.avijit.rms.data.local.daos.DistrictDao;
import com.avijit.rms.data.local.daos.DivisionDao;
import com.avijit.rms.data.local.daos.UserDao;
import com.avijit.rms.data.local.entities.Area;
import com.avijit.rms.data.local.entities.District;
import com.avijit.rms.data.local.entities.Division;
import com.avijit.rms.data.local.entities.User;

@Database(entities = {User.class, Division.class, District.class, Area.class},version = 3 ,exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "app_db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context){
        if(instance==null){
            instance = Room.databaseBuilder(context,AppDatabase.class,DB_NAME).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
    public abstract UserDao userDao();
    public abstract DivisionDao divisionDao();
    public abstract DistrictDao districtDao();
    public abstract AreaDao areaDao();
}
