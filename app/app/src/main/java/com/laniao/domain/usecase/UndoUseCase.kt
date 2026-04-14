package com.laniao.domain.usecase

import com.laniao.domain.repository.DrinkEntryRepository
import com.laniao.domain.repository.ExerciseRepository
import com.laniao.domain.repository.ManuallyMissedTimeRepository
import com.laniao.domain.repository.PeeEntryRepository
import com.laniao.domain.repository.VoidScheduleRepository
import javax.inject.Inject

/**
 * Executes an undo operation by reversing the most recent action.
 */
class UndoUseCase @Inject constructor(
    private val undoRedoManager: UndoRedoManager,
    private val peeEntryRepository: PeeEntryRepository,
    private val drinkEntryRepository: DrinkEntryRepository,
    private val exerciseRepository: ExerciseRepository,
    private val voidScheduleRepository: VoidScheduleRepository,
    private val manuallyMissedTimeRepository: ManuallyMissedTimeRepository
) {
    /**
     * Undo the most recent action. Returns a description of what was undone, or null if nothing to undo.
     */
    suspend operator fun invoke(): String? {
        val action = undoRedoManager.popUndo() ?: return null

        when (action) {
            is UndoableAction.AddEntry -> {
                peeEntryRepository.deleteById(action.entry.id)
            }
            is UndoableAction.UpdateEntry -> {
                peeEntryRepository.update(action.before)
            }
            is UndoableAction.DeleteEntry -> {
                peeEntryRepository.insert(action.entry)
            }
            is UndoableAction.AddDrink -> {
                drinkEntryRepository.delete(action.drink)
            }
            is UndoableAction.UpdateDrink -> {
                drinkEntryRepository.update(action.before)
            }
            is UndoableAction.DeleteDrink -> {
                drinkEntryRepository.insert(action.drink)
            }
            is UndoableAction.AddExercise -> {
                exerciseRepository.deleteCompletion(action.exercise.id)
            }
            is UndoableAction.DeleteExercise -> {
                exerciseRepository.insertCompletion(action.exercise)
            }
            is UndoableAction.UpdateExercise -> {
                exerciseRepository.deleteCompletion(action.after.id)
                exerciseRepository.insertCompletion(action.before)
            }
            is UndoableAction.CreateSchedule -> {
                voidScheduleRepository.deleteById(action.schedule.id)
            }
            is UndoableAction.DeleteSchedule -> {
                voidScheduleRepository.insert(action.schedule)
            }
            is UndoableAction.CopySchedule -> {
                voidScheduleRepository.deleteById(action.schedule.id)
            }
            is UndoableAction.CreateExerciseSchedule -> {
                exerciseRepository.deleteSchedule(action.schedule.id)
            }
            is UndoableAction.DeleteExerciseSchedule -> {
                val newId = exerciseRepository.insertSchedule(action.schedule)
                val itemsWithId = action.items.map { it.copy(id = 0, scheduleId = newId) }
                exerciseRepository.insertItems(itemsWithId)
            }
            is UndoableAction.UpdateExerciseSchedule -> {
                exerciseRepository.deleteItemsForSchedule(action.afterSchedule.id)
                exerciseRepository.updateSchedule(action.beforeSchedule.copy(id = action.afterSchedule.id))
                val itemsWithId = action.beforeItems.map { it.copy(id = 0, scheduleId = action.afterSchedule.id) }
                exerciseRepository.insertItems(itemsWithId)
            }
            is UndoableAction.MarkMissed -> {
                manuallyMissedTimeRepository.unmarkMissed(action.date, action.scheduledTime)
            }
            is UndoableAction.UnmarkMissed -> {
                manuallyMissedTimeRepository.markMissed(action.date, action.scheduledTime)
            }
        }

        return "Undone: ${action.description}"
    }
}
