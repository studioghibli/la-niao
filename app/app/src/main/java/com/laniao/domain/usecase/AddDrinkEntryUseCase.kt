package com.laniao.domain.usecase

import com.laniao.domain.model.DrinkEntry
import com.laniao.domain.repository.DrinkEntryRepository
import javax.inject.Inject

/**
 * Use case for adding a drink entry.
 */
class AddDrinkEntryUseCase @Inject constructor(
    private val repository: DrinkEntryRepository
) {
    suspend operator fun invoke(entry: DrinkEntry): Long {
        require(entry.amount > 0) { "Drink amount must be positive" }
        return repository.insert(entry)
    }
}
