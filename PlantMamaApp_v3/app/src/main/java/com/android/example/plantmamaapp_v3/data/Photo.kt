package com.android.example.plantmamaapp_v3.data

import androidx.annotation.DrawableRes
import com.android.example.plantmamaapp_v3.R

data class Photo(
    @DrawableRes val imageResourceId: Int
)

    val photos = listOf<Photo>(
    Photo(R.drawable.aloe), Photo(R.drawable.forever_plant), Photo( R.drawable.house_warming), Photo(R.drawable.money_tree), Photo(R.drawable.sophie), Photo(R.drawable.zoe)
)