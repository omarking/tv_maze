package com.ellerbach.tvmazeapp.model

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface ShowDAO {
    @Insert
    suspend fun save(show: Show?): Long

    @get:Query("SELECT * FROM Show")
    val searchAll: Flow<List<Show?>?>

    @Query("SELECT * FROM Show WHERE id = :id")
    fun searchShow(id: Long): Show?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(shows: List<Show?>?)
}


@Database(entities = [Show::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ShowDatabase : RoomDatabase() {
    abstract val showDAO: ShowDAO
}

private lateinit var INSTANCE: ShowDatabase

fun getDatabase(context: Context): ShowDatabase {
    synchronized(ShowDatabase::class) {
        if (isInstanceNotInitialized()) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    ShowDatabase::class.java,
                    "titles_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}

private fun isInstanceNotInitialized() = !::INSTANCE.isInitialized

