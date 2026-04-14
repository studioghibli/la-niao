package com.laniao.domain.usecase

import com.laniao.domain.repository.DrinkEntryRepository
import com.laniao.domain.repository.ExerciseRepository
import com.laniao.domain.repository.ManuallyMissedTimeRepository
import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import javax.inject.Inject

/**
 * Executes a redo operation by re-applying the most recently undone action.
 */
class RedoUseCase @Inject constructor(
    private val undoRedoManager: UndoRedoManager,
    private val peeEntryRepository: PeeEntryRepository,
    private val drinkEntryRepository: DrinkEntryRepository,
    private val exerciseRepository: ExerciseRepository,
    private val voidScheduleRepository: VoidScheduleRepository,
    private val manuallyMissedTimeRepository: ManuallyMissedTimeRepository
) {
    /**
     * Redo the most recently undone action. Returns a description of what was redone, or null if nothing to redo.
     */
    suspend operator fun invoke(): String? {
        val action = undoRedoManager.popRedo() ?: return null

        when (action) {
            is UndoableAction.AddEntry -> {
                peeEntryRepository.insert(action.entry)
            }
            is UndoableAction.UpdateEntry -> {
                peeEntryRepository.update(action.after)
            }
            is UndoableAction.DeleteEntry -> {
                peeEntryRepository.deleteById(action.entry.id)
            }
            is UndoableAction.AddDrink -> {
                drinkEntryRepository.insert(action.drink)
            }
            is UndoableAction.UpdateDrink -> {
                drinkEntryRepository.update(action.after)
            }
            is UndoableAction.DeleteDrink -> {
                drinkEntryRepository.delete(action.drink)
            }
            is UndoableAction.AddExercise -> {
                exerciseRepository.insertCompletion(action.exercise)
            }
            is UndoableAction.DeleteExercise -> {
                exerciseRepository.deleteCompletion(action.exercise.id)
            }
            is UndoableAction.UpdateExercise -> {
                exerciseRepository.deleteCompletion(action.before.id)
                exerciseRepository.insertCompletion(action.after)
            }
            is UndoableAction.CreateSchedule -> {
                voidScheduleRepository.insert(action.schedule)
            }
            is UndoableAction.DeleteSchedule -> {
                voidScheduleRepository.deleteById(action.schedule.id)
            }
            is UndoableAction.CopySchedule -> {
                voidScheduleRepository.insert(action.schedule)
            }
            is UndoableAction.CreateExerciseSchedule -> {
                val newId = exerciseRepository.insertSchedule(action.schedule)
                val itemsWithId = action.items.map { it.copy(id = 0, scheduleId = newId) }
                exerciseRepository.insertItems(itemsWithId)
            }
            is UndoableAction.DeleteExerciseSchedule -> {
                exerciseRepository.deleteSchedule(action.schedule.id)
            }
            is UndoableAction.UpdateExerciseSchedule -> {
                exerciseRepository.deleteItemsForSchedule(action.afterSchedule.id)
                exerciseRepository.updateSchedule(action.afterSchedule)
                val itemsWithId = action.afterItems.map { it.copy(id = 0, scheduleId = action.afterSchedule.id) }
                exerciseRepository.insertItems(itemsWithId)
            }
            is UndoableAction.MarkMissed -> {
                manuallyMissedTimeRepository.markMissed(action.date, action.scheduledTime)
            }
            is UndoableAction.UnmarkMissed -> {
                manuallyMissedTimeRepository.unmarkMissed(action.date, action.scheduledTime)
            }
        }

        return "Redone: ${action.description}"
    }
}
