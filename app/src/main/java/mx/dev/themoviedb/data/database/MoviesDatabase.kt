package mx.dev.themoviedb.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mx.dev.themoviedb.data.database.dao.MovieDao
import mx.dev.themoviedb.data.database.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MoviesDatabase:RoomDatabase() {
    abstract fun getMovieDao():MovieDao
}

private lateinit var INSTANCE : MoviesDatabase

fun getDatabase(context: Context): MoviesDatabase {

    synchronized(MoviesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java,
                "database"
            ).build()
        }
    }

    return INSTANCE
}