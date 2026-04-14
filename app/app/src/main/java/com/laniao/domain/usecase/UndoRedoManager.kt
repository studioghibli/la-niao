package com.laniao.domain.usecase

import com.laniao.domain.model.DrinkEntry
import com.laniao.domain.model.ExerciseCompletion
import com.laniao.domain.model.ExerciseSchedule
import com.laniao.domain.model.ExerciseScheduleItem
import com.laniao.domain.model.PeeEntry
import com.laniao.domain.model.VoidSchedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Represents an undoable user action.
 */
sealed class UndoableAction {
    abstract val description: String

    // Pee entries (void / urge / leak)
    data class AddEntry(val entry: PeeEntry) : UndoableAction() {
        override val description = "Entry added"
    }
    data class UpdateEntry(val before: PeeEntry, val after: PeeEntry) : UndoableAction() {
        override val description = "Entry updated"
    }
    data class DeleteEntry(val entry: PeeEntry) : UndoableAction() {
        override val description = "Entry deleted"
    }

    // Drink entries
    data class AddDrink(val drink: DrinkEntry) : UndoableAction() {
        override val description = "Drink added"
    }
    data class UpdateDrink(val before: DrinkEntry, val after: DrinkEntry) : UndoableAction() {
        override val description = "Drink updated"
    }
    data class DeleteDrink(val drink: DrinkEntry) : UndoableAction() {
        override val description = "Drink deleted"
    }

    // Exercise entries
    data class AddExercise(val exercise: ExerciseCompletion) : UndoableAction() {
        override val description = "Exercise completed"
    }
    data class DeleteExercise(val exercise: ExerciseCompletion) : UndoableAction() {
        override val description = "Exercise deleted"
    }
    data class UpdateExercise(val before: ExerciseCompletion, val after: ExerciseCompletion) : UndoableAction() {
        override val description = "Exercise updated"
    }

    // Schedule operations
    data class CreateSchedule(val schedule: VoidSchedule) : UndoableAction() {
        override val description = "Schedule created"
    }
    data class DeleteSchedule(val schedule: VoidSchedule) : UndoableAction() {
        override val description = "Schedule deleted"
    }
    data class CopySchedule(val schedule: VoidSchedule) : UndoableAction() {
        override val description = "Schedule copied"
    }

    // Exercise schedule operations
    data class CreateExerciseSchedule(val schedule: ExerciseSchedule, val items: List<ExerciseScheduleItem>) : UndoableAction() {
        override val description = "Exercise schedule created"
    }
    data class DeleteExerciseSchedule(val schedule: ExerciseSchedule, val items: List<ExerciseScheduleItem>) : UndoableAction() {
        override val description = "Exercise schedule deleted"
    }
    data class UpdateExerciseSchedule(
        val beforeSchedule: ExerciseSchedule, val beforeItems: List<ExerciseScheduleItem>,
        val afterSchedule: ExerciseSchedule, val afterItems: List<ExerciseScheduleItem>
    ) : UndoableAction() {
        override val description = "Exercise schedule updated"
    }

    // Mark-as-missed
    data class MarkMissed(val date: LocalDate, val scheduledTime: LocalTime) : UndoableAction() {
        override val description = "Marked as missed"
    }
    data class UnmarkMissed(val date: LocalDate, val scheduledTime: LocalTime) : UndoableAction() {
        override val description = "Unmarked missed"
    }
}

/**
 * In-memory undo/redo stack manager.
 * Shared singleton across all ViewModels.
 * Stacks are lost on app restart (by design).
 */
@Singleton
class UndoRedoManager @Inject constructor() {

    companion object {
        private const val MAX_STACK_SIZE = 10
    }

    private val undoStack = ArrayDeque<UndoableAction>()
    private val redoStack = ArrayDeque<UndoableAction>()

    private val _canUndo = MutableStateFlow(false)
    val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    private val _canRedo = MutableStateFlow(false)
    val canRedo: StateFlow<Boolean> = _canRedo.asStateFlow()

    private val _lastUndoDescription = MutableStateFlow<String?>(null)
    val lastUndoDescription: StateFlow<String?> = _lastUndoDescription.asStateFlow()

    private val _lastRedoDescription = MutableStateFlow<String?>(null)
    val lastRedoDescription: StateFlow<String?> = _lastRedoDescription.asStateFlow()

    /**
     * Push a new undoable action. Clears the redo stack (standard behavior).
     */
    fun push(action: UndoableAction) {
        undoStack.addLast(action)
        if (undoStack.size > MAX_STACK_SIZE) {
            undoStack.removeFirst()
        }
        redoStack.clear()
        updateState()
    }

    /**
     * Pop the most recent action from the undo stack.
     * Returns the action to reverse, or null if stack is empty.
     */
    fun popUndo(): UndoableAction? {
        val action = undoStack.removeLastOrNull() ?: return null
        redoStack.addLast(action)
        if (redoStack.size > MAX_STACK_SIZE) {
            redoStack.removeFirst()
        }
        updateState()
        return action
    }

    /**
     * Pop the most recent action from the redo stack.
     * Returns the action to re-apply, or null if stack is empty.
     */
    fun popRedo(): UndoableAction? {
        val action = redoStack.removeLastOrNull() ?: return null
        undoStack.addLast(action)
        if (undoStack.size > MAX_STACK_SIZE) {
            undoStack.removeFirst()
        }
        updateState()
        return action
    }

    /**
     * Clear both stacks (e.g. after data restore or clear all).
     */
    fun clear() {
        undoStack.clear()
        redoStack.clear()
        updateState()
    }

    private fun updateState() {
        _canUndo.value = undoStack.isNotEmpty()
        _canRedo.value = redoStack.isNotEmpty()
        _lastUndoDescription.value = undoStack.lastOrNull()?.description
        _lastRedoDescription.value = redoStack.lastOrNull()?.description
    }
}
