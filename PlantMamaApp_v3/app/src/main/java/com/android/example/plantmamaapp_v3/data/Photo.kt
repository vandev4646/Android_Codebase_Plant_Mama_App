package com.android.example.plantmamaapp_v3.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val plantId: Int,
    val uri: String,
    val lastUpdated: Long = System.currentTimeMillis(),
    val syncState: SyncState = SyncState.NOT_SYNCED
)

