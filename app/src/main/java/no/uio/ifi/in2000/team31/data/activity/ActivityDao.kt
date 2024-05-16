package no.uio.ifi.in2000.team31.data.activity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(activity: Activity)

    @Update
    suspend fun update(activity: Activity)

    @Delete
    suspend fun delete(activity: Activity)

    @Query("SELECT * from activities")
    fun getAllActivities(): Flow<List<Activity>>

    @Query("SELECT * from activities WHERE id = :id")
    fun getActivity(id: Int?): Flow<Activity>
}