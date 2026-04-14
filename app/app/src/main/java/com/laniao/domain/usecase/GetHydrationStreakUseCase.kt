package com.laniao.domain.usecase

import com.laniao.data.local.dao.AppSettingsDao
import com.laniao.domain.repository.DrinkEntryRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

/**
 * Calculates hydration streak — consecutive days where total drink liters >= hydration goal.
 */
class GetHydrationStreakUseCase @Inject constructor(
    private val drinkRepository: DrinkEntryRepository,
    private val appSettingsDao: AppSettingsDao
) {
    suspend operator fun invoke(today: LocalDate): Int {
        val goal = appSettingsDao.getOnce()?.hydrationGoalLiters ?: 2.7
        var streak = 0
        var day = today
        val maxLookback = 365

        for (i in 0 until maxLookback) {
            val drinks = drinkRepository.getByDate(day).first()
            val totalLiters = drinks.sumOf { it.liters }

            if (totalLiters >= goal) {
                streak++
                day = day.minusDays(1)
            } else {
                break
            }
        }

        return streak
    }
}
