package com.android.example.plantmamaapp_v3.data

import android.content.Context

interface AppContainer {
    val waterRepository : WaterRepository
    val plantsRepository: PlantsRepository
    val reminderRepository: ReminderRepository

}

class DefaultAppContainer(context: Context) : AppContainer {
    override val waterRepository = WorkManagerWaterRepository(context)

    override val plantsRepository: PlantsRepository by lazy {
        OfflinePlantsRepository(PlantDatabase.getDatabase(context).plantDao())
    }

    override val reminderRepository: ReminderRepository by lazy {
        OfflineRemindersRepository(PlantDatabase.getDatabase(context).reminderDao())
    }

}