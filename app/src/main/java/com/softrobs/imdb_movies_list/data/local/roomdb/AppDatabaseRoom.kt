package com.softrobs.imdb_movies_list.data.local.roomdb
import androidx.room.Database
import androidx.room.RoomDatabase
import com.softrobs.imdb_movies_list.data.remote.response.Search

@Database(entities = [Search::class], version = 3, exportSchema = true)
abstract class AppDatabaseRoom : RoomDatabase() {

    abstract fun getAllMoviesDao(): Dao

//    companion object {
//        @Volatile
//        private var instance: com.softrobs.imdb_movies_list.data.local.roomdb.AppDatabaseRoom? = null
//        private val Lock = Any()
//
//        operator fun invoke(context: Context) = instance ?: synchronized(Lock) {
//            instance ?: buildDatabase(context).also {
//                instance = it
//            }
//        }
//        private fun buildDatabase(context: Context) = Room.databaseBuilder(
//            context.applicationContext, com.softrobs.imdb_movies_list.data.local.roomdb.AppDatabaseRoom::class.java, "AppDataBase"
//        ).build()
//    }
}
