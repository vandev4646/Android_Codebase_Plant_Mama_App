package com.android.example.plantmamaapp_v3.data

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.example.plantmamaapp_v3.R

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val plantId: Int,
    val uri: String,
)


data class Photo2(
    @DrawableRes val imageResourceId: Int
)

    val photo2s = listOf<Photo2>(
    Photo2(R.drawable.aloe), Photo2(R.drawable.forever_plant), Photo2( R.drawable.house_warming), Photo2(R.drawable.money_tree), Photo2(R.drawable.sophie), Photo2(R.drawable.zoe)
)