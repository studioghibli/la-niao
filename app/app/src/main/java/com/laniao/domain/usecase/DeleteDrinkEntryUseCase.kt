package com.laniao.domain.usecase

import com.laniao.domain.model.DrinkEntry
import com.laniao.domain.repository.DrinkEntryRepository
import javax.inject.Inject

/**
 * Use case for deleting a drink entry.
 */
class DeleteDrinkEntryUseCase @Inject constructor(
    private val repository: DrinkEntryRepository
) {
    suspend operator fun invoke(entry: DrinkEntry) {
        repository.delete(entry)
    }
}
