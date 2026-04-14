package com.laniao.domain.usecase

import com.laniao.domain.model.PeeEntry
import com.laniao.domain.repository.PeeEntryRepository
import javax.inject.Inject

/**
 * Use case for retrieving a single entry by ID.
 */
class GetPeeEntryByIdUseCase @Inject constructor(
    private val repository: PeeEntryRepository
) {
    /**
     * Get an entry by ID.
     * Returns null if not found.
     */
    suspend operator fun invoke(entryId: Long): PeeEntry? {
        return repository.getById(entryId)
    }
}
