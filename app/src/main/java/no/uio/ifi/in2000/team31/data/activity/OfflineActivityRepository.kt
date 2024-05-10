package no.uio.ifi.in2000.team31.data.activity

import kotlinx.coroutines.flow.Flow

class OfflineActivityRepository(private val activityDao: ActivityDao) : ActivityRepository {
    override fun getAllActivitiesStream(): Flow<List<Activity>> = activityDao.getAllActivities()
    override fun getItemStream(id: Int): Flow<Activity> = activityDao.getActivity(id)
    override suspend fun insertActivity(activity: Activity) = activityDao.insert(activity)
    override suspend fun updateActivity(activity: Activity) = activityDao.update(activity)
    override suspend fun deleteActivity(activity: Activity) = activityDao.delete(activity)
}