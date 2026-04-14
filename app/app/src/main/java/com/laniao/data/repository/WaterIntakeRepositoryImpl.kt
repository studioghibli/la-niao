package com.laniao.data.repository

import com.laniao.data.local.dao.WaterIntakeDao
import com.laniao.data.local.entity.WaterIntakeEntity
import com.laniao.domain.model.WaterAmount
import com.laniao.domain.model.WaterIntake
import com.laniao.domain.repository.WaterIntakeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [WaterIntakeRepository].
 */
@Singleton
class WaterIntakeRepositoryImpl @Inject constructor(
    private val dao: WaterIntakeDao
) : WaterIntakeRepository {

    override suspend fun upsert(waterIntake: WaterIntake) {
        dao.upsert(waterIntake.toEntity())
    }

    override suspend fun delete(waterIntake: WaterIntake) {
        dao.delete(waterIntake.toEntity())
    }

    override suspend fun getById(id: Long): WaterIntake? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun getByDate(date: LocalDate): WaterIntake? {
        return dao.getByDate(date)?.toDomain()
    }

    override fun observeByDate(date: LocalDate): Flow<WaterIntake?> {
        return dao.observeByDate(date).map { it?.toDomain() }
    }

    override fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<WaterIntake>> {
        return dao.getByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAll(): Flow<List<WaterIntake>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    private fun WaterIntake.toEntity() = WaterIntakeEntity(
        id = id,
        date = date,
        amount = amount
    )

    private fun WaterIntakeEntity.toDomain() = WaterIntake(
        id = id,
        date = date,
        amount = amount
    )
}
