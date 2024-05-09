package no.uio.ifi.in2000.team31.data.activity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class Activity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val info: String,
    val imagePath: String? = null
)