package com.android.example.plantmamaapp_v3.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.example.plantmamaapp_v3.R

@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    @DrawableRes val imageResourceId: Int,
    val name: String,
    val age: Int,
    val type: String,
    val description: String,
)


val plants = listOf(
    Plant(0, R.drawable.aloe, "Aloe", 1, "Aloe", "None"),
    Plant(1, R.drawable.forever_plant, "Forever", 4, "Pothos", "None"),
    Plant(2, R.drawable.house_warming, "House Warming", 3, "Croton", "None"),
    Plant(3, R.drawable.money_tree, "Jerry", 1, "Money Tree", "None"),
    Plant(4, R.drawable.sophie, "Sophie", 4, "Fiddle Leaf Fig", "None"),
    Plant(5, R.drawable.zoe, "Zoe", 3, "Fiddle Leaf Fig", "None"),
    Plant(6, R.drawable.zoe, "Zoe", 3, "Fiddle Leaf Fig", "None"),

)