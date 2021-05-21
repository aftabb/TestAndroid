package com.example.winvestatest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.winvestatest.models.Result

@Database(version = 1, entities = [Result::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao

    companion object {
        var INSTANCE: AppDatabase? = null

        /**
         * Creating singleton object for Appdatabase
         *
         * @param context
         * @return
         */
        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "myRecords"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}