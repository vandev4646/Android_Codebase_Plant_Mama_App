package com.android.example.plantmamaapp_v3.data

import kotlinx.coroutines.flow.Flow

class OfflinePlantsRepository(private val plantDao: PlantDao) : PlantsRepository {
    override fun getAllPlantsStream(): Flow<List<Plant>> = plantDao.getAllItems()

    override fun getPlantStream(id: Int): Flow<Plant?> = plantDao.getItem(id)

    override suspend fun getPlant(id: Int):  Plant = plantDao.getPlantNonFlow(id)

    override fun getFirstPlant(id: Int): Plant = plantDao.getPlantNonFlow(id)

    override suspend fun insertPlant(plant: Plant) = plantDao.insert(plant)

    override suspend fun deletePlant(plant: Plant) = plantDao.delete(plant)

    override suspend fun updatePlant(plant: Plant) = plantDao.update(plant)
}