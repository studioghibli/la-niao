# Implementation Plan: LaNiao v1.1 — Bug Fixes & Enhancements

## Overview

This document covers bug fixes and feature enhancements identified after the v1.0 release. Issues are grouped into phases by area of concern, with bug fixes prioritized first.

**Reference:** See [AGENTS.md](../AGENTS.md) for conventions, [PRODUCT-REQUIREMENTS.md](./PRODUCT-REQUIREMENTS.md) for requirements, [IMPLEMENTATION-PLAN.md](./IMPLEMENTATION-PLAN.md) for v1.0 phases

---

## Issue Tracker

| # | Type | Summary | Phase |
|---|------|---------|-------|
| 1 | Bug | Pop-up message says "drink updated" when editing an exercise | 1 |
| 2 | Bug | Unable to update early entry in list view, only in timeline view | 1 |
| 3 | Bug | Balloon emoji shows on calendar for all urges, should only show for full leaks (red balloon/BURST urgency) | 1 |
| 4 | Bug | Timeline times on left do not switch to 24-hour view | 1 |
| 5 | Bug | When swiping between days in timeline view, the visible time window resets instead of maintaining position | 1 |
| 6 | Bug | Current time red line does not move in real-time, requires page reload | 1 |
| ~~7~~ | ~~Bug~~ | ~~Don't set scheduled time until first void of day is inputted~~ | ~~Removed~~ |
| 8 | Feature | Implement undo and redo functionality | 2 |
| 9 | Feature | Add ability to copy existing schedule for future use | 3 |
| 10 | Feature | Filter entries by type in calendar view (timeline and list) | 3 |
| 11 | Feature | Add ability to show a void time as missed | 3 |
| 12 | UI | Move titles of Calendar, Statistics, and Settings higher up | 4 |
| 13 | Feature | Add average gap between voids per day (Statistics) | 4 |
| 14 | Bug | Calendar month view dots/icons slow to appear, sometimes need to navigate away and back | 1 |

---

## Phase 1: Bug Fixes

**Goal:** Fix all user-reported bugs in existing functionality.

### Deliverables
- [x] Correct snackbar message when editing exercises
- [x] Enable entry editing from list view
- [x] Balloon emoji only on BURST urgency entries in calendar
- [x] 24-hour format support for timeline hour labels
- [x] Maintain scroll position when swiping between days in timeline
- [x] Real-time current time indicator (red line)
- [x] Calendar month view dots load reliably without needing to navigate away and back

### Tasks

#### 1.1 — Fix "drink updated" snackbar when editing exercise

**Problem:** When editing an exercise entry from the calendar, the snackbar incorrectly displays "Drink updated" instead of "Exercise updated".

**Root Cause:** Likely a shared or copy-pasted snackbar message string that references the drink type instead of branching on entry type.

**Fix:**
- Locate the save/update handler in `CalendarViewModel` (or whichever ViewModel handles exercise edit completion)
- Ensure the snackbar message string is determined by the entry type being saved:
  - Void → "Entry updated"
  - Drink → "Drink updated"
  - Exercise → "Exercise updated"
- Check all add/update/delete paths for correct message assignment

**Tests:**
- [ ] `updateExercise_showsCorrectSnackbarMessage`
- [ ] `updateDrink_showsCorrectSnackbarMessage`
- [ ] `updateVoidEntry_showsCorrectSnackbarMessage`

---

#### 1.2 — Enable entry editing from list view

**Problem:** Tapping an entry in the list view (chronological feed) does not open the edit dialog. Editing only works from the timeline view.

**Root Cause:** List view items likely missing `onClick`/tap handler that opens the edit dialog, or the handler is not wired to `loadPeeEntryForEdit` / drink edit flow.

**Fix:**
- In `DayEntryList` or the list-mode section of `CalendarScreen`, add tap handlers to each `FeedItem`:
  - Pee entries → call `loadPeeEntryForEdit(entryId)` on `CalendarViewModel`
  - Drink entries → open `AddDrinkDialog` with existing entry
  - Exercise entries → open exercise detail dialog
- Ensure the same dialog-opening logic used in timeline view is reused in list view

**Tests:**
- [ ] `tapPeeEntryInListView_opensEditDialog`
- [ ] `tapDrinkEntryInListView_opensEditDialog`
- [ ] `editEntryFromListView_savesChanges`

---

#### 1.3 — Balloon emoji only on full leaks (BURST urgency)

**Problem:** The balloon (🎈) emoji appears on the calendar for all urge entries. It should only appear when the urgency is BURST (red balloon), indicating a full leak.

**Root Cause:** The calendar day cell or schedule progress indicator checks for any urgency value rather than specifically `Urgency.BURST`.

**Fix:**
- In the calendar day cell indicator logic and `ScheduleProgressList`, update the condition:
  ```kotlin
  // Before (incorrect):
  if (entry.urgency != Urgency.UNKNOWN && entry.urgency != Urgency.NONE) → show 🎈
  
  // After (correct):
  if (entry.urgency == Urgency.BURST) → show 🎈
  ```
- Review all places where 🎈 is rendered and ensure the BURST-only condition

**Tests:**
- [ ] `calendarDayCell_withBurstUrgency_showsBalloon`
- [ ] `calendarDayCell_withHighUrgency_doesNotShowBalloon`
- [ ] `calendarDayCell_withMediumUrgency_doesNotShowBalloon`
- [ ] `scheduleProgress_withBurstUrgency_showsBalloon`

---

#### 1.4 — Timeline hour labels do not respect 24-hour format

**Problem:** The hour labels on the left side of the timeline view always display 12-hour format (e.g., "10:00 PM") even when the device is set to 24-hour time.

**Fix:**
- Read the system 24-hour preference using `android.text.format.DateFormat.is24HourFormat(context)`
- Pass the preference into the timeline composable (or read it within the composable via `LocalContext`)
- Format hour labels conditionally:
  ```kotlin
  val is24Hour = DateFormat.is24HourFormat(LocalContext.current)
  val formatter = if (is24Hour) {
      DateTimeFormatter.ofPattern("HH:00")
  } else {
      DateTimeFormatter.ofPattern("h:00 a")
  }
  ```
- Apply to all hour label rendering in `DayTimelineCard`

**Tests:**
- [ ] `timelineHourLabels_24HourDevice_showsHH00Format`
- [ ] `timelineHourLabels_12HourDevice_showsHAmPmFormat`

---

#### 1.5 — Maintain scroll position when swiping between days

**Problem:** When viewing the timeline at a specific time range (e.g., 10 AM – 7 PM) and swiping to the next or previous day, the view resets to show 12 AM – 10 AM instead of keeping the same time window.

**Root Cause:** The scroll offset and/or zoom level are being reset when `selectedDate` changes, likely because the scroll state is keyed to the date or recreated on recomposition.

**Fix:**
- Store the current scroll offset and zoom level in `CalendarViewModel` (or a remembered state that survives date changes) rather than in a composable state that resets per-date
- When navigating between days, restore the previous scroll offset and zoom level:
  ```kotlin
  // In CalendarUiState or CalendarViewModel:
  var timelineScrollOffset: Float  // persisted across day changes
  var timelineZoomLevel: Float     // persisted across day changes
  ```
- On day change, apply the stored offset after the new day's timeline content is composed
- Clamp the offset if the new day has different content height

**Tests:**
- [ ] `swipeToNextDay_maintainsTimelineScrollPosition`
- [ ] `swipeToPreviousDay_maintainsTimelineScrollPosition`
- [ ] `swipeToNextDay_maintainsZoomLevel`

---

#### 1.6 — Current time red line should move in real-time

**Problem:** The red "current time" indicator line on the timeline view is static. It only updates when the page is reloaded/recomposed, not as time passes.

**Fix:**
- Add a periodic timer in the timeline composable to update the current time position:
  ```kotlin
  var currentTime by remember { mutableStateOf(LocalTime.now()) }
  
  LaunchedEffect(Unit) {
      while (isActive) {
          delay(60_000) // update every minute
          currentTime = LocalTime.now()
      }
  }
  ```
- Use `currentTime` to calculate the red line's Y position
- Only show the red line when viewing today's timeline

**Tests:**
- [ ] `timelineRedLine_afterOneMinute_updatesPosition`
- [ ] `timelineRedLine_onNonTodayDate_isHidden`

---

#### 1.7 — Calendar month view dots slow to appear

**Problem:** The schedule dots and entry indicator icons on the calendar month view are slow to appear. Users sometimes need to navigate away from the calendar page and back before the dots show up.

**Root Cause:** Likely the `Flow` collecting entries-per-month data is not being observed when the calendar month view first composes, or the data loads lazily and the UI doesn't recompose when it arrives. Possible causes:
- `GetEntriesForMonthUseCase` result not collected until after initial composition
- Month data `Flow` uses `stateIn(SharingStarted.Lazily)` instead of `SharingStarted.WhileSubscribed` or `Eagerly`
- Calendar composable not subscribed to the state on first frame (e.g., conditional collection inside `LaunchedEffect`)

**Fix:**
- Ensure `CalendarViewModel` starts collecting month entry data eagerly when the screen is active
- Verify the `daysWithEntries` / `daysWithSchedule` state flows are collected with `SharingStarted.WhileSubscribed(5000)` (not `Lazily`)
- If data is loaded via `LaunchedEffect(selectedMonth)`, ensure it triggers immediately on first composition — not deferred behind another state change
- Check that the calendar month grid composable observes the state directly (via `collectAsState`), not through an intermediate that delays emission
- Add a loading indicator (subtle shimmer or dots placeholder) while data loads

**Tests:**
- [ ] `calendarMonthView_onFirstLoad_showsDotsWithoutNavigation`
- [ ] `calendarMonthView_afterAddingEntry_dotsUpdateImmediately`
- [ ] `calendarMonthView_switchMonth_dotsLoadForNewMonth`

### Implementation Notes (Phase 1)
- **#1**: `CalendarViewModel.updateExerciseCompletionTime()` was emitting `CalendarEvent.DrinkUpdated` → added `ExerciseUpdated` event and handler in CalendarScreen
- **#2**: Edit dialog delete handler referenced `uiState.allEntries` (current day only) → changed to use the already-loaded `editingPeeEntry` directly; added `onClick` to `FeedExerciseRow`, `DayExerciseRow`, and wired `onExerciseClick` through `ChronologicalFeed` and `DayEntryList`
- **#3**: Renamed `CalendarDayStats.hadLeak` → `hadBurstUrgency`; condition now checks `Urgency.BURST` instead of `hasLeak || isLeakOnly` in `GetMonthDayStatsUseCase` and `CalendarView`
- **#4**: `DayTimelineCard.HourLine` now accepts a `DateTimeFormatter` parameter; format chosen based on `DateFormat.is24HourFormat(context)` — `"HH:00"` for 24h, `"h:00 a"` for 12h
- **#5**: Added `timelineScrollOffsetPx` and `timelineHourHeightDp` to `CalendarUiState`; `DayTimelineCard` accepts initial values and reports changes via `onScrollStateChanged`; auto-scroll to now uses `scrollToNowPending` boolean + `scrollToNowAnimated` flag — pending set by `selectDate()` (instant) and `goToToday()` (animated 500ms via `Animatable`); cleared by `clearScrollToNow()` callback; swiping between days never sets pending → scroll preserved
- **#6**: Replaced static `val now = LocalDateTime.now()` with `mutableStateOf` + `LaunchedEffect` timer updating every 60 seconds
- **#14**: Added `onStart { emit(emptyList()) }` to all three repository `Flow`s in `GetMonthDayStatsUseCase` so `combine` emits immediately
- **UI polish**: Removed `KeyboardArrowRight` icons from `FeedPeeRow` and `FeedDrinkRow` in chronological feed; added `QuickAddFabBackground`/`QuickAddFabContent` to `LaNiaoColors` using `isSystemInDarkTheme()` pattern (dark purple / light purple); reverted Calendar main FAB to Material 3 defaults

---

## Phase 2: Undo & Redo

**Goal:** Implement undo/redo for entry operations across the app.

### Deliverables
- [x] Undo support for add, edit, and delete operations on all entry types (void, drink, exercise), schedule operations, and mark-as-missed
- [x] Redo support to re-apply undone operations
- [x] Undo and Redo buttons in Settings screen
- [x] Snackbar message showing what was undone/redone

### Design Decisions
- **Scope:** Undo/redo applies to add/edit/delete operations for all entry types (void, urge, leak, drink, exercise), schedule operations (create, delete, copy), and mark-as-missed actions
- **Stack depth:** Keep last 10 operations in the undo stack
- **Redo:** Redo stack clears when a new operation is performed (standard undo/redo behavior)
- **Persistence:** Undo/redo stack is in-memory only (lost on app restart)
- **UI location:** Undo and Redo buttons appear ONLY in Settings screen (not in snackbars or top bars)
- **Feedback:** After undo/redo, show snackbar describing what was done (e.g., "Undone: Entry deleted" / "Redone: Drink added") — no confirmation dialog
- **Schedule associations:** By the time a schedule creation is the most recent undoable action, no entries should have associations with it (entries are created after schedules, so they'd be undone first)

### Tasks

1. **Create UndoRedoManager** (`/domain/usecase/` or `/util/`)
   ```kotlin
   sealed class UndoableAction {
       // Void / Urge / Leak entries
       data class AddEntry(val entry: PeeEntry) : UndoableAction()
       data class UpdateEntry(val before: PeeEntry, val after: PeeEntry) : UndoableAction()
       data class DeleteEntry(val entry: PeeEntry) : UndoableAction()
       // Drink entries
       data class AddDrink(val drink: DrinkEntry) : UndoableAction()
       data class UpdateDrink(val before: DrinkEntry, val after: DrinkEntry) : UndoableAction()
       data class DeleteDrink(val drink: DrinkEntry) : UndoableAction()
       // Exercise entries
       data class AddExercise(val exercise: ExerciseCompletion) : UndoableAction()
       data class DeleteExercise(val exercise: ExerciseCompletion) : UndoableAction()
       // Schedule operations
       data class CreateSchedule(val schedule: VoidSchedule) : UndoableAction()
       data class DeleteSchedule(val schedule: VoidSchedule) : UndoableAction()
       data class CopySchedule(val schedule: VoidSchedule) : UndoableAction()
       // Mark-as-missed
       data class MarkMissed(val date: LocalDate, val scheduledTime: LocalTime) : UndoableAction()
       data class UnmarkMissed(val date: LocalDate, val scheduledTime: LocalTime) : UndoableAction()
   }
   
   class UndoRedoManager @Inject constructor() {
       private val undoStack = ArrayDeque<UndoableAction>(10)
       private val redoStack = ArrayDeque<UndoableAction>(10)
       
       fun push(action: UndoableAction)
       fun undo(): UndoableAction?
       fun redo(): UndoableAction?
       val canUndo: StateFlow<Boolean>
       val canRedo: StateFlow<Boolean>
   }
   ```

2. **Create UndoUseCase and RedoUseCase** (`/domain/usecase/`)
   - `UndoUseCase`: pops from undo stack, reverses the operation (delete → re-insert, add → delete, update → restore previous), pushes to redo stack
   - `RedoUseCase`: pops from redo stack, re-applies the operation, pushes to undo stack

3. **Integrate into ViewModels**
   - `HomeViewModel`, `CalendarViewModel`, `ScheduleViewModel`: after each mutating operation, push `UndoableAction` to `UndoRedoManager`
   - `SettingsViewModel`: expose `canUndo`/`canRedo` state and `undo()`/`redo()` actions

4. **Add Undo/Redo to Settings screen**
   - Add "Undo" and "Redo" buttons in Settings (below Data section, above Danger Zone)
   - Buttons disabled when stack is empty (`canUndo`/`canRedo` = false)
   - Show description of last undoable/redoable action on button (e.g., "Undo: Entry added at 2:05 PM")
   - Tapping executes undo/redo and shows snackbar: "Undone: [description]" / "Redone: [description]"

### Tests
- [ ] `undoAddEntry_deletesEntry`
- [ ] `undoDeleteEntry_reInsertsEntry`
- [ ] `undoUpdateEntry_restoresPreviousValues`
- [ ] `redo_afterUndo_reAppliesAction`
- [ ] `newOperation_clearsRedoStack`
- [ ] `undoStack_limitedTo10Operations`
- [ ] `undoRedoManager_afterAppRestart_stacksEmpty`
- [ ] `undoCreateSchedule_deletesSchedule`
- [ ] `undoDeleteSchedule_reInsertsSchedule`
- [ ] `undoCopySchedule_deletescopiedSchedule`
- [ ] `undoMarkMissed_removesManuallyMissedRow`
- [ ] `undoUnmarkMissed_reInsertsManuallyMissedRow`
- [ ] `settingsScreen_undoButton_disabledWhenStackEmpty`
- [ ] `settingsScreen_undoButton_showsActionDescription`

### Dependencies
- Phase 1 (bug fixes) ✅
- Phase 3 (copy schedule + mark-as-missed actions must exist before undo can cover them) ✅

### Implementation Notes (Phase 2)
- **UndoRedoManager**: `@Singleton` in-memory manager with `ArrayDeque<UndoableAction>` for undo/redo stacks (max 10). Exposes `canUndo`/`canRedo`/`lastUndoDescription`/`lastRedoDescription` as `StateFlow`
- **UndoableAction**: 15 action types — AddEntry, UpdateEntry, DeleteEntry, AddDrink, UpdateDrink, DeleteDrink, AddExercise, DeleteExercise, UpdateExercise, CreateSchedule, DeleteSchedule, CopySchedule, MarkMissed, UnmarkMissed
- **UndoUseCase / RedoUseCase**: reverse or re-apply each action via direct repository calls (delete/insert/update)
- **HomeViewModel**: all mutations (quickAdd, savePeeEntry, deletePeeEntry, addDrink, updateDrink, deleteDrink, completeExercise, completeExerciseAt) push to undo stack
- **CalendarViewModel**: all mutations (deleteEntry, updateDrink, deleteDrink, deleteExercise, updateExerciseCompletion, savePeeEntry, addDrink, completeExercise, markMissed, unmarkMissed) push to undo stack
- **ScheduleViewModel**: create, confirmReplace, deleteSchedule, deleteScheduleById push to undo stack
- **SettingsViewModel**: observes undo/redo state flows; `undo()`/`redo()` methods; clears stack on `clearAllData()`
- **SettingsScreen**: Undo & Redo card placed at top of page (above Hydration Goal), with description-labeled buttons
- **Bug fix**: upcoming void schedule delete was calling `deleteSchedule()` (active only) → added `deleteScheduleById(schedule)` to fix
- **Exercise edit enhancement**: exercise edit dialog now allows changing exercise type via grouped dropdown (Kegel / Relaxation), tracked by `UpdateExercise` undo action
- **CompleteExerciseUseCase**: changed return type from `Unit` to `ExerciseCompletion` (with ID) to support undo snapshots

---

## Phase 3: Calendar & Schedule Enhancements

**Goal:** Add schedule copy, entry type filtering, and void-as-missed marking.

### Deliverables
- [x] Copy existing schedule to create a new future schedule
- [x] Filter calendar entries by type (void, drink, urge, leak, exercise) in both timeline and list views
- [x] Mark a scheduled void time as manually missed

### Tasks

#### 3.1 — Copy schedule for future use

**Problem:** Users want to reuse schedule parameters (start time, end time, interval) without re-entering them.

**Implementation:**
- Add a "Copy Schedule" button/action on the `ScheduleScreen` for each active or expired schedule
- Tapping opens the `CreateScheduleDialog` pre-populated with the copied schedule's parameters:
  - Typical start time
  - End time
  - Interval
  - Duration (default to same length)
- Start date defaults to tomorrow (or day after current schedule expires)
- User can modify any field before saving
- Overlap detection applies as usual

**Tasks:**
1. Add "Copy" icon button (📋) next to existing edit/delete icons on `ScheduleCard`
2. Add `copySchedule(scheduleId: Long)` method to `ScheduleViewModel` that loads existing schedule parameters
3. Open `CreateScheduleDialog` with pre-populated fields
4. Overlap detection and confirmation flow unchanged

**Tests:**
- [ ] `copySchedule_populatesDialogWithExistingValues`
- [ ] `copySchedule_defaultsStartDateToTomorrow`
- [ ] `copySchedule_withOverlap_showsConfirmation`
- [ ] `copySchedule_savedAsNewSchedule_doesNotModifyOriginal`

---

#### 3.2 — Filter entries by type in calendar view

**Problem:** Users want to see only specific types of entries (e.g., only voids, only leaks) in calendar timeline and list views.

**Implementation:**
- Add a filter chip row that is visible in ALL calendar modes (month view, timeline, and list)
- Place filter chips below the month navigation / date header area so they persist across view switches
- Filter chip row:
- Default: "All" selected
- Multiple filters can be active simultaneously (e.g., Voids + Leaks)
- Filter applies to both timeline view and list view
- Calendar month dot indicators are NOT affected by filters (always show all)
- Filter state persists within the session (resets on app restart)

**Tasks:**
1. Add `activeFilters: Set<EntryTypeFilter>` to `CalendarUiState`
   ```kotlin
   enum class EntryTypeFilter { VOID, URGE, LEAK, DRINK, EXERCISE }
   ```
2. Add filter chip row composable below view mode toggle
3. In `CalendarViewModel`, filter the displayed entries/feed items based on `activeFilters`
4. Apply filter to both `DayTimelineCard` items and `DayEntryList` items
5. Show "No matching entries" empty state when filter hides all entries

**Tests:**
- [ ] `filterVoidsOnly_showsOnlyVoidEntries`
- [ ] `filterUrgesOnly_showsOnlyUrgeEntries`
- [ ] `filterMultipleTypes_showsMatchingEntries`
- [ ] `filterAll_showsAllEntries`
- [ ] `calendarDots_unaffectedByFilter`
- [ ] `filterApplies_toBothTimelineAndListViews`

---

#### 3.3 — Mark void time as missed

**Problem:** Users want to explicitly mark a scheduled void time as "missed" rather than leaving it as unclaimed/upcoming.

**Terminology (updated):**
- **Overdue** = time has passed (grace period expired), no entry logged, NOT manually marked — auto state
- **Missed** = user explicitly tapped "Mark as missed" — intentional acknowledgment

**Implementation:**
- Added `OVERDUE` status to `ScheduledTimeStatus` enum alongside existing `MISSED`
- Tapping a schedule marker (on timeline or list view) opens a dialog showing the scheduled time details
  - If an entry is already associated: shows the entry details (existing behavior)
  - If no entry is associated (upcoming/unclaimed): shows a dialog with "Mark as missed" checkbox option
- Manually missed times appear as distinct entries in the list view (ChronologicalFeed) with ❌ and "Missed scheduled void" label
- Timeline view: overdue times show amber/orange background with "⏰ Overdue" label; manually missed times show red background with "⏰ ❌ Missed" label
- Home screen SummaryCard: skips manually missed times when determining "next" schedule time — moves to the next overdue or upcoming time
- MissedScheduleAlert (home screen banner): only shows overdue times, not manually missed (those are intentional)
- ScheduleProgressList: shows separate counts for Done/Overdue/Missed/Upcoming
- The user can un-miss by tapping the missed time again (unchecks "Mark as missed", reverts to upcoming) or by logging an entry for that time
- Persisted via `manually_missed_times` Room table (schema v2)

**Schema Change: v1 → v2**
- New table: `manually_missed_times`
  ```kotlin
  @Entity(
      tableName = "manually_missed_times",
      indices = [Index(value = ["date", "scheduledTime"], unique = true)]
  )
  data class ManuallyMissedTimeEntity(
      @PrimaryKey(autoGenerate = true)
      val id: Long = 0,
      val date: LocalDate,
      val scheduledTime: LocalTime,
      val createdAt: Instant
  )
  ```
- Room migration: `MIGRATION_7_8` — creates `manually_missed_times` table
- Backup schema version bumped from 1 → 2
- `BackupDataUseCase`: includes `manuallyMissedTimes` list in backup JSON (empty list for days with none)
- `RestoreDataUseCase`: when restoring a v1 backup (no `manuallyMissedTimes` field), treat as empty list — table starts empty, which is the correct default

**Backward Compatibility:**
- v1 backups restore on v1.1: ✅ (new table left empty, all other data restored)
- v1.1 backups restore on v1.0: ❌ (rejected — v1.0 `RestoreDataUseCase` rejects schema version 2, which is the existing intended behavior)

**Tasks:**
1. Create `ManuallyMissedTimeEntity` and `ManuallyMissedTimeDao`
2. Add `MIGRATION_7_8` to `LaNiaoDatabase` (creates `manually_missed_times` table)
3. Bump backup schema version to 2
4. Update `BackupDataUseCase` to include `manuallyMissedTimes`
5. Update `RestoreDataUseCase` to handle missing `manuallyMissedTimes` field from v1 backups (default to empty list)
6. Create `ManuallyMissedTimeRepository` interface and implementation (including `getByDateRange`)
7. Add tap handler to schedule markers on timeline and list views → opens schedule time dialog
8. Schedule time dialog: shows time, status (Completed/Upcoming/Missed), and "Mark as missed" toggle for unclaimed times
9. Add `OVERDUE` status to `ScheduledTimeStatus` enum; auto-expired (past grace period, no entry, not manually marked) → `OVERDUE`, manually marked → `MISSED`
10. Timeline: overdue markers show amber/orange background with "⏰ Overdue"; manually missed show red background with "⏰ ❌ Missed"
11. List view (ChronologicalFeed): add `missedScheduleTime` field to `FeedItem`; include manually missed times as feed entries with ❌ "Missed scheduled void"
12. Home screen SummaryCard: `firstUnclaimed` skips `MISSED` (manually marked) — shows next `OVERDUE` or `UPCOMING` time
13. MissedScheduleAlert: only shows overdue times (excludes manually missed); label changed from "Missed" to "Overdue"
14. ScheduleProgressList: separate counts for Done/Overdue/Missed/Upcoming with distinct icons (✅/⏰/❌/⏳)
15. If user logs an entry and assigns to a manually-missed time, delete the row from `manually_missed_times`
16. Tapping a manually-missed time again removes the row (reverts to upcoming)

**Tests:**
- [ ] `markAsMissed_insertsRowInDatabase`
- [ ] `markAsMissed_changesStatusToMissed`
- [ ] `tapMissedTimeAgain_revertsToUpcoming`
- [ ] `logEntryForMissedTime_deletesManuallyMissedRow`
- [ ] `manuallyMissedTime_showsRedMissedOnTimeline`
- [ ] `overdueTime_showsAmberOverdueOnTimeline`
- [ ] `manuallyMissedTime_appearsInListViewFeed`
- [ ] `homeScreen_skipsManuallyMissedForNextTime`
- [ ] `overdueTimes_shownInMissedScheduleAlert`
- [ ] `manuallyMissedTimes_excludedFromMissedScheduleAlert`
- [ ] `migration7to8_createsManuallyMissedTimesTable`
- [ ] `restoreV1Backup_onV2Schema_leavesManuallyMissedTimesEmpty`
- [ ] `backupV2_includesManuallyMissedTimes`

### Implementation Notes (Phase 3)
- **#9 (Copy schedule)**: Added copy icon button on `ScheduleCard`; `ScheduleViewModel.copySchedule()` loads parameters and opens `CreateScheduleDialog` pre-populated; start date defaults to day after original expires
- **#10 (Filter)**: Added `EntryTypeFilter` enum and `activeFilters` to `CalendarUiState`; `EntryFilterChips` composable placed below view mode toggle; `filterTimelineItems()` and `filterFeedItems()` applied to both views; schedule markers always pass through
- **#11 (Mark missed)**: Split `ScheduledTimeStatus.MISSED` into `OVERDUE` (auto-expired, amber) and `MISSED` (manual, red ❌); `ManuallyMissedTimeEntity` + `MIGRATION_7_8`; tap dialog with checkbox; missed entries appear in ChronologicalFeed via `FeedItem.missedScheduleTime`; home SummaryCard skips MISSED for next time; MissedScheduleAlert shows only overdue; ScheduleProgressList shows 4 categories (Done/Overdue/Missed/Upcoming)

### Dependencies
- Phase 1 (bug fixes) ✅

---

## Phase 4: UI & Statistics Enhancements

**Goal:** Polish UI elements and add new statistics metrics.

### Deliverables
- [x] Move screen titles higher (Calendar, Statistics, Settings)
- [x] Add average gap between voids per day to Statistics

### Tasks

#### 4.1 — Move screen titles higher

**Problem:** The titles "Calendar", "Statistics", and "Settings" in the top app bars are positioned too low, wasting vertical space.

**Fix:**
- Review the `TopAppBar` configuration on Calendar, Statistics, and Settings screens
- Reduce top padding or switch from `LargeTopAppBar`/`MediumTopAppBar` to `TopAppBar` (small) if currently using a larger variant
- Ensure consistent title positioning across all three screens
- Verify the title remains visible and does not overlap with the status bar (respect `WindowInsets`)

**Tests:**
- [ ] Visual verification: titles positioned consistently across Calendar, Statistics, Settings

---

#### 4.2 — Average gap between voids per day

**Problem:** Users want to see the average time between consecutive voids as a daily statistic to track bladder capacity improvement.

**Implementation:**
- Create `GetAverageVoidGapUseCase` (`/domain/usecase/`):
  ```kotlin
  /**
   * Calculates the average time gap (in minutes) between consecutive void entries
   * for the given date range. Urge-only and leak-only entries are excluded.
   * Returns average gap per day.
   */
  class GetAverageVoidGapUseCase @Inject constructor(
      private val repository: PeeEntryRepository
  ) {
      suspend operator fun invoke(dateRange: ClosedRange<LocalDate>): List<DailyVoidGap>
  }
  
  data class DailyVoidGap(
      val date: LocalDate,
      val averageGapMinutes: Double,
      val voidCount: Int
  )
  ```
- Add to Statistics screen as a summary stat card:
  ```
  Average Gap Between Voids
  Today: 2h 15min
  7-day avg: 1h 52min
  ```
- For days with 0 or 1 void, show "—" (insufficient data)

**Tasks:**
1. Create `GetAverageVoidGapUseCase`
2. Create `DailyVoidGap` data class
3. Add `averageVoidGap` field to `StatisticsUiState`
4. Add summary card to `StatisticsScreen`
5. Wire up in `StatisticsViewModel`

**Tests:**
- [ ] `averageGap_threeVoidsAt9_11_15_returns3Hours`
- [ ] `averageGap_singleVoid_returnsNull`
- [ ] `averageGap_noVoids_returnsNull`
- [ ] `averageGap_excludesUrgeOnlyEntries`
- [ ] `averageGap_excludesLeakOnlyEntries`
- [ ] `averageGap_multiDayRange_returnsPerDayAverages`

### Dependencies
- Phase 1 (bug fixes) ✅

### Implementation Notes (Phase 4)
- **#12 (Titles higher)**: Added `windowInsets = WindowInsets(0)` to `TopAppBar` in CalendarScreen, StatisticsScreen, and SettingsScreen — eliminates double status bar inset padding that was pushing titles down
- **#13 (Average void gap)**: Created `GetAverageVoidGapUseCase` — collects void entries via `PeeEntryRepository.getByDateRange().first()`, groups by date, filters days with <2 voids, calculates average gap in minutes between consecutive voids. `DailyVoidGap` data class with `date`, `averageGapMinutes`, `voidCount`, `scheduleGoalMinutes`. `VoidGapChart` bar chart with per-day dotted goal line from active schedule. Bars turn from light purple (dim) to dark purple when meeting goal. Card shows Latest day / N-day avg / Best (longest gap) with `formatGap()` helper. Days without 2+ voids filled with 0.

---

## Post-Phase Polish & Enhancements

Changes made after all four phases were complete:

### Undo/Redo Enhancements
- **Exercise schedule undo**: Added `CreateExerciseSchedule`, `DeleteExerciseSchedule`, `UpdateExerciseSchedule` action types to `UndoableAction`; captures schedule + items for full restore
- **Exercise edit undo**: Added `UpdateExercise(before, after)` for exercise completion edits (type + time changes)
- **Upcoming schedule delete fix**: `deleteSchedule()` only deleted active schedule — added `deleteScheduleById(schedule)` for upcoming/past schedule deletion
- **Undo/Redo UI moved** to top of Settings page (above Hydration Goal)

### Exercise Edit Enhancement
- Exercise edit dialog now allows changing exercise type via grouped dropdown (Kegel / Relaxation categories with headers and divider)

### Schedule Management
- **Past schedules**: "See past schedules" link at bottom of Schedule screen opens scrollable dialog with All/Void/Exercise filter chips; merged and sorted by end date descending
- **Past date creation unblocked**: Removed `startDate >= today` check from void schedule `CreateScheduleDialog`; exercise schedule date pickers fixed (wrapped in `Box` for reliable click handling)
- **Exercise schedule overlap**: Confirmed intentional — creating overlapping exercise schedule replaces existing

### Spacing & Layout
- Consistent spacing between title bar and first content across Calendar, Statistics, Settings screens (removed extra `padding(top = 8.dp)` from Settings section headers)
- Statistics screen date range chips tighter to title

### Timeline View
- **Date header**: Wrapped date text + `DayTimelineCard` in Column with `weight(1f)` so date header (day of week + date) stays visible above timeline; shows "• Today" suffix in primary color for current day
- **Bottom padding**: `TOTAL_HOURS = 24.5f` constant used in content height and all 3 `maxScroll` calculations (was `24f` — entries near midnight were inaccessible)
- **Swipe threshold**: Increased from 150px to 300px for day navigation
- **Slide direction**: "Today" button now slides left from past dates, right from future dates (was always sliding right)

### List View (ChronologicalFeed)
- **Sticky date headers**: Converted to `stickyHeader` with `Surface` background so date floats while scrolling
- **Smooth scroll on load more**: Tracks item count before loading; adjusts scroll position after new items inserted at top to prevent jumping

### Chart & Visual
- **Void gap chart**: `VoidGapChart` composable with animated bars, per-day dotted goal line from schedule `intervalMinutes`, light/dark purple color transition on goal met; `ChartPurple`/`ChartPurpleDim` added to `LaNiaoColors` (dark mode aware)
- **Void frequency chart**: `LIGHT_YELLOW` color changed from green (`0xFFC5E1A5`) to pale yellow (`0xFFFFF9C4`) to match actual pee color meaning

### Emoji & Code Quality
- **Urge emoji**: Changed from ⚡/💪 to 😣 (persevering face) across 6 files
- **All emojis standardized to unicode escapes** — 22 literal emojis converted across 10 files; prevents encoding corruption
- **Dead code removed**: Unused `DayEntryList` import, `UpcomingSchedulesCard` import, no-op `loadCalendarDots()` method + 4 call sites
- **Compiler warning fixed**: `item.isScheduled && item.entry == null` → `item.isScheduled` (redundant null check)

---

## Phase Summary

| Phase | Name | Issues | Status |
|-------|------|--------|--------|
| 1 | Bug Fixes | #1–#6, #14 (7 bugs) | ✅ Complete |
| 3 | Calendar & Schedule Enhancements | #9, #10, #11 | ✅ Complete |
| 2 | Undo & Redo | #8 | ✅ Complete |
| 4 | UI & Statistics Enhancements | #12, #13 | ✅ Complete |
