package com.example.daftarmovie.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.daftarmovie.model.MovieTM

@Database(entities = [MovieTM::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        private var INSTANCE: MovieDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): MovieDatabase {
            if (INSTANCE == null) {
                synchronized(lock) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java, "favorite_movies.db"
                    ).build()

                    return INSTANCE as MovieDatabase
                }
            }
            return INSTANCE as MovieDatabase
        }
    }
}
