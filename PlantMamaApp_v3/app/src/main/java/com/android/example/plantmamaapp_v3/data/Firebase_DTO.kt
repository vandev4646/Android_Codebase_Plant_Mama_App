package com.android.example.plantmamaapp_v3.data
/*
This file contains the data mapping class to represent Firestore data
 */

data class PlantDocument(
    val roomId: Int = 0,
    val profilePic: String = "",
    val name: String = "",
    val datePurchased: Long = 0L,
    val type: String = "",
    val description: String = "",
    val lastUpdated: Long = 0L,
)

data class NoteDocument(
    val roomId: Int = 0,
    val title: String = "",
    val date: Long = 0L,
    val photoRoomIds: List<Int> = emptyList(),
    val lastUpdated: Long = 0L
)

data class PhotoDocument(
    val roomId: Int = 0,
    val uri: String = "",
    val lastUpdated: Long = 0L
)

data class ReminderDocument(
    val roomId: Int = 0,
    val wmIdentifier: String = "",
    val title: String = "",
    val date: String = "",
    val time: String = "",
    val lastUpdated: Long = 0L
)



