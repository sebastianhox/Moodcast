package no.uio.ifi.in2000.team31.data.activity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Activity::class], version = 1, exportSchema = false)
abstract class ActivityDatabase : RoomDatabase(){
    abstract fun activityDao(): ActivityDao
    companion object {
        @Volatile
        private var Instance: ActivityDatabase? = null
        fun detDatabase(context: Context): ActivityDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ActivityDatabase::class.java,
                    "activity_database"
                ).fallbackToDestructiveMigration().build().also { Instance = it }
            }
        }
    }
}