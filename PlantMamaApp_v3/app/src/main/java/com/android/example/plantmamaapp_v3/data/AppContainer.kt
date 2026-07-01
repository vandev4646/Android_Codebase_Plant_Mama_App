package com.android.example.plantmamaapp_v3.data

import android.content.Context

interface AppContainer {
    val waterRepository: WaterRepository
    val plantsRepository: PlantsRepository
    val reminderRepository: ReminderRepository
    val photosRepository: PhotosRepository
    val notesRepository: NotesRepository
    val syncRepository: SyncRepository

}

class DefaultAppContainer(context: Context) : AppContainer {
    override val waterRepository = WorkManagerWaterRepository(context)

    override val plantsRepository: PlantsRepository by lazy {
        OfflinePlantsRepository(PlantDatabase.getDatabase(context).plantDao(), context)
    }

    override val reminderRepository: ReminderRepository by lazy {
        OfflineRemindersRepository(PlantDatabase.getDatabase(context).reminderDao(), context)
    }

    override val photosRepository: PhotosRepository by lazy {
        OfflinePhotoRepository(PlantDatabase.getDatabase(context).photoDao(), context)
    }

    override val notesRepository: NotesRepository by lazy {
        OfflineNoteRepository(PlantDatabase.getDatabase(context).noteDao(), context)
    }

    override val syncRepository: SyncRepository by lazy {
        SyncRepository(context)
    }

}