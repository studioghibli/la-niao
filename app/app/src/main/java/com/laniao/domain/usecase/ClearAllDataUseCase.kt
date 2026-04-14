package com.laniao.domain.usecase

import com.laniao.data.local.LaNiaoDatabase
import com.laniao.data.local.dao.AppSettingsDao
import com.laniao.data.local.dao.DrinkEntryDao
import com.laniao.data.local.dao.ExerciseCompletionDao
import com.laniao.data.local.dao.ExerciseScheduleDao
import com.laniao.data.local.dao.PeeEntryDao
import com.laniao.data.local.dao.VoidScheduleDao
import com.laniao.data.local.dao.WaterIntakeDao
import javax.inject.Inject

class ClearAllDataUseCase @Inject constructor(
    private val database: LaNiaoDatabase,
    private val peeEntryDao: PeeEntryDao,
    private val voidScheduleDao: VoidScheduleDao,
    private val waterIntakeDao: WaterIntakeDao,
    private val drinkEntryDao: DrinkEntryDao,
    private val exerciseScheduleDao: ExerciseScheduleDao,
    private val exerciseCompletionDao: ExerciseCompletionDao,
    private val appSettingsDao: AppSettingsDao
) {
    suspend operator fun invoke() {
        database.runInTransaction {
            kotlinx.coroutines.runBlocking {
                exerciseCompletionDao.deleteAll()
                exerciseScheduleDao.deleteAllItems()
                exerciseScheduleDao.deleteAllSchedules()
                drinkEntryDao.deleteAll()
                waterIntakeDao.deleteAll()
                voidScheduleDao.deleteAll()
                peeEntryDao.deleteAll()
                appSettingsDao.deleteAll()
            }
        }
    }
}
