package com.android.example.plantmamaapp_v3.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.android.example.plantmamaapp_v3.worker.PlantDeleteWorker
import com.android.example.plantmamaapp_v3.worker.PlantSyncWorker
import kotlinx.coroutines.flow.Flow

class OfflinePlantsRepository(
    private val plantDao: PlantDao,
    context: Context
) : PlantsRepository {
    val appContext = context.applicationContext

    override fun getAllPlantsStream(): Flow<List<Plant>> = plantDao.getAllItems()

    override fun getPlantStream(id: Int): Flow<Plant?> = plantDao.getItem(id)

    override suspend fun getPlant(id: Int):  Plant = plantDao.getPlantNonFlow(id)

    override fun getFirstPlant(id: Int): Plant = plantDao.getPlantNonFlow(id)

    override suspend fun insertPlant(plant: Plant){
        val generatedId = plantDao.insert(plant).toInt()
        triggerPlantSync(generatedId)
    }

    override suspend fun deletePlant(plant: Plant){
        val tombstone = plant.copy(
            syncState = SyncState.TO_DELETE,
            lastUpdated = System.currentTimeMillis()
        )
        plantDao.update(tombstone)
        triggerPlantDeleteSync(tombstone.id)
    }

    override suspend fun updatePlant(plant: Plant) {
        val localPlant = plant.copy(
            syncState = SyncState.NOT_SYNCED,
            lastUpdated = System.currentTimeMillis()
        )
        plantDao.update(localPlant)
        triggerPlantSync(localPlant.id)
    }
    override suspend fun getUnsyncedPlants(): List<Plant>  = plantDao.getUnsyncedPlants()

    override suspend fun updateSyncState(id: Int, state: String) = plantDao.updateSyncState(id, state)

    override suspend fun upsertPlant(plant: Plant) = plantDao.upsertPlant(plant)

    override suspend fun getLastUpdatedTime(id: Int): Long? = plantDao.getLastUpdatedTime(id)

    private fun triggerPlantSync(plantId: Int){
        val inputData = Data.Builder()
            .putInt("PLANT_ID", plantId)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<PlantSyncWorker>()
            .setInputData(inputData)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(appContext).enqueueUniqueWork(
            "sync_plant_$plantId",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }

    private fun triggerPlantDeleteSync(plantId: Int){
        val inputData = Data.Builder()
            .putInt("PLANT_ID", plantId)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<PlantDeleteWorker>()
            .setInputData(inputData)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(appContext).enqueueUniqueWork(
            "delete_plant_$plantId",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )

    }
}

