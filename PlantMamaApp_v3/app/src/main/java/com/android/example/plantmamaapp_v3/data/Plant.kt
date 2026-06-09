package com.android.example.plantmamaapp_v3.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val profilePic: String,
    val name: String,
    val datePurchased: Date,
    val type: String,
    val description: String,
)