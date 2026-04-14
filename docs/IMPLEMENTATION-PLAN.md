# Implementation Plan: LaNiao

## Overview

This document outlines the phased implementation approach for the LaNiao Android app. Each phase builds on the previous, with clear deliverables and testable outcomes.

**Total Estimated Phases:** 9 (+ 8.5)  
**Architecture:** MVVM + Clean Architecture  
**Reference:** See [AGENTS.md](../AGENTS.md) for conventions, [PRODUCT-REQUIREMENTS.md](./PRODUCT-REQUIREMENTS.md) for requirements

---

## Architectural Decisions

### Versions
- Use latest stable versions for all dependencies
- Kotlin, Compose BOM, Hilt, Room, Gradle: latest stable at time of implementation

### App Configuration
- App name: "LaNiao"
- App icon: Placeholder with 拉尿 text (Chinese for "pee")
- Language: English only for v1
- Orientation: Portrait only (lock orientation)
- Tablet: No special tablet layout

### Error Handling
- Use cases throw exceptions for validation errors
- ViewModel catches exceptions and shows Toast messages
- Custom exception types: `MaxEntriesException`, `DuplicateMinuteException`, `ValidationException`

### State Management
- ViewModels expose `StateFlow<UiState>` for UI state
- UiState is a data class per screen
- Loading shown as circular spinner (centered)

### Navigation
- Use Jetpack Compose Navigation with type-safe arguments
- Pass entry IDs as Long for edit screens

### Testability
- Inject `Clock` interface for all time operations (mockable in tests)
- Inject `DispatcherProvider` for coroutine contexts

### DAO Return Types
- `Flow<List<T>>` for lists displayed in UI (auto-updates on data change)
- `suspend fun` for single item lookups and write operations

### Data Policies
- Delete entry: Hard delete (gone forever, no soft delete)
- Delete schedule: Entries remain but lose schedule association (scheduledTime becomes null)
- Backup filename: Auto-generated `laniao_backup_YYYY-MM-DD_HHmmss.json`

### Crash Reporting
- Include crash reporting (Firebase Crashlytics or similar)
- Add to dependencies in Phase 1

### Accessibility
- Content descriptions on all icons and interactive elements
- Test with TalkBack before release

### UI Decisions
- Loading: Circular progress indicator, centered
- Errors: Toast messages
- Empty states: Text only (no illustrations)
- Color picker: Horizontal row of 7 circle chips (40dp each) — UNKNOWN shown as gray
- Interval selector: Horizontal scroll of chips
- No onboarding: User lands directly on empty Home screen

### Confirmation Dialogs

**Delete Entry:**
```
Title: "Delete entry?"
Message: "This entry from [TIME] will be permanently removed."
Buttons: [Cancel] [Delete]
```

**Clear All Data (Step 1):**
```
Title: "Clear all data?"
Message: "This will permanently delete all your entries, schedules, and settings."
Buttons: [Cancel] [Continue]
```

**Clear All Data (Step 2):**
```
Title: "Are you sure?"
Message: "This cannot be undone. All data will be lost."
Buttons: [Cancel] [Clear All Data]
```

**Restore Data:**
```
Title: "Replace all data?"
Message: "This will replace your current [X] entries with [Y] entries from the backup."
Buttons: [Cancel] [Replace]
```

**Replace Schedule (overlap):**
```
Title: "Replace schedule?"
Message: "Your new schedule overlaps with the existing one. The existing schedule will be removed."
Buttons: [Cancel] [Replace]
```

---

## Phase 1: Project Setup & Core Infrastructure

**Goal:** Establish project structure, dependencies, and foundational components.

### Deliverables
- [x] Android project with Kotlin DSL build files
- [x] Hilt dependency injection setup
- [x] Room database configuration
- [x] Base theme (Material 3, dark mode support)
- [x] Navigation scaffold (empty screens)

### Tasks

1. **Create Android project**
   - Package: `com.laniao`
   - minSdk: 26, targetSdk: 34
   - Jetpack Compose enabled

2. **Configure dependencies** (build.gradle.kts)
   ```
   - Jetpack Compose BOM (latest stable)
   - Hilt (hilt-android, hilt-navigation-compose)
   - Room (room-runtime, room-ktx, room-compiler)
   - Navigation Compose (with type-safe args)
   - Kotlin Coroutines
   - Firebase Crashlytics (crash reporting)
   - JUnit 5, MockK, Turbine, Compose UI Testing
   ```

3. **Set up project structure**
   ```
   /app/src/main/java/com/laniao/
     /data/local/          - Database, DAOs, entities
     /data/repository/     - Repository implementations
     /domain/model/        - Domain models
     /domain/repository/   - Repository interfaces
     /domain/usecase/      - Use cases
     /presentation/screens/
     /presentation/components/
     /presentation/viewmodel/
     /presentation/navigation/
     /presentation/theme/
     /di/                  - Hilt modules
     /util/                - Extensions, constants
   ```

4. **Create Room database**
   - `LaNiaoDatabase` with entities placeholder
   - Database module for Hilt

5. **Create navigation scaffold**
   - `NavRoutes` sealed class: Home, History, Statistics, Settings
   - `LaNiaoNavHost` composable with empty screens
   - Bottom navigation bar

6. **Configure theme**
   - Material 3 color scheme
   - Light/dark mode from system
   - Typography scale

7. **Create testability utilities** (`/util/`)
   - `Clock` interface with `now(): Instant` method
   - `SystemClock` implementation (production)
   - `DispatcherProvider` interface (Main, IO, Default)
   - `StandardDispatcherProvider` implementation (production)

8. **Configure app settings**
   - Lock to portrait orientation in AndroidManifest.xml
   - Set app name to "LaNiao"
   - Add placeholder app icon with 拉尿 text

9. **Create Hilt modules** (`/di/`)
   - `DatabaseModule`: provides Room database and DAOs
   - `RepositoryModule`: binds repository implementations to interfaces
   - `UtilModule`: provides Clock, DispatcherProvider

10. **Define navigation routes** (`/presentation/navigation/`)
    ```kotlin
    sealed class NavRoutes {
        object Home : NavRoutes()
        object History : NavRoutes()
        object Statistics : NavRoutes()
        object Settings : NavRoutes()
        object Schedule : NavRoutes()
        data class AddEntry(val entryId: Long? = null) : NavRoutes() // null = new entry
    }
    ```

### Tests
- [x] App launches without crash
- [x] Navigation between empty screens works
- [x] Database initializes correctly

### Dependencies
None (foundation phase)

---

## Phase 2: Domain Models & Database Layer

**Goal:** Implement all domain models and Room entities with DAOs.

### Deliverables
- [x] All domain models (PeeEntry, VoidSchedule, WaterIntake, ExerciseConfig, ExerciseCompletion)
- [x] All enums (VolumeSize, PeeColor, Urgency, WaterAmount, KegelType)
- [x] Room entities and DAOs
- [x] Repository interfaces and implementations

### Tasks

1. **Create enums** (`/domain/model/`)
   - `VolumeSize`: UNKNOWN, SMALL, MEDIUM, LARGE
   - `PeeColor`: UNKNOWN, CLEAR, LIGHT_YELLOW, YELLOW, DARK_YELLOW, AMBER
   - `Urgency`: UNKNOWN, NONE, LOW, MEDIUM, HIGH, BURST 🎈
   - `WaterAmount`: LITTLE, NORMAL, A_LOT
   - `KegelType`: STANDARD, QUICK, HOLD
   - `LeakAmount`: NONE, SMALL, MEDIUM, LARGE
   - `DrinkType`: WATER, SPARKLING_WATER, TEA, COFFEE, MILK, JUICE, CUSTOM
   - `DrinkUnit`: OZ, ML, CUPS (with `litersPerUnit` conversion property)

2. **Create domain models** (`/domain/model/`)
   - `PeeEntry` with all fields per AGENTS.md
   - `LeakAmount` enum (NONE, SMALL, MEDIUM, LARGE)
   - `PeeEntry` includes `leakAmount`, `activityContext`, derived: `isLeakOnly`, `isUrgeOnly`, `hasLeak`
   - `VoidSchedule` with dynamic anchor logic support
   - `DrinkEntry` (timestamp, type, amount, unit, customName, `liters` computed property)
   - `ExerciseConfig` (type, sessions, sets, reps, hold)
   - `ExerciseCompletion` (configId, type, completedAt, scheduledDate)

3. **Create Room entities** (`/data/local/entity/`)
   - `PeeEntryEntity` - maps to PeeEntry (includes `leakAmount`, `activityContext` columns)
   - `VoidScheduleEntity` - maps to VoidSchedule
   - `DrinkEntryEntity` - maps to DrinkEntry (type, amount, unit, customName)
   - `ExerciseConfigEntity` - maps to ExerciseConfig
   - `ExerciseCompletionEntity` - maps to ExerciseCompletion
   - Type converters for Instant, LocalDate, LocalTime, enums

4. **Create DAOs** (`/data/local/dao/`)
   - `PeeEntryDao`: insert, update, delete, getByDate, getByDateRange, getAll, leaks-only, urges-only queries
   - `VoidScheduleDao`: insert, update, delete, getActive, getByDateRange
   - `DrinkEntryDao`: insert, update, delete, getByDate, getByDateRange
   - `ExerciseConfigDao`: insert, update, getAll, getEnabled
   - `ExerciseCompletionDao`: insert, getByDate, getByDateRange

5. **Create repository interfaces** (`/domain/repository/`)
   - `PeeEntryRepository`
   - `VoidScheduleRepository`
   - `DrinkEntryRepository`
   - `ExerciseRepository`

6. **Create repository implementations** (`/data/repository/`)
   - Implement all repositories with DAO injection
   - Map entities ↔ domain models

7. **Update database**
   - Add all entities to `LaNiaoDatabase` (version 7, explicit migrations)
   - Using explicit `MIGRATION_X_Y` objects (no fallbackToDestructiveMigration)
   - Includes: PeeEntryEntity, DrinkEntryEntity, VoidScheduleEntity, WaterIntakeEntity, ExerciseConfigEntity, ExerciseCompletionEntity, ExerciseScheduleItemEntity, AppSettingsEntity

8. **Create custom exceptions** (`/domain/exception/`)
   - `MaxEntriesException` - thrown when 50 entries exist for day
   - `DuplicateMinuteException` - thrown when entry exists in same minute
   - `ValidationException` - generic validation failure (e.g., notes > 500 chars)
   - `ScheduleOverlapException` - thrown when date ranges overlap

### Tests
- [x] All DAOs: insert, query, update, delete operations
- [x] Type converters: round-trip for all types
- [x] Repository: mapping entity ↔ domain model

### Dependencies
- Phase 1 (project structure, Room setup)

---

## Phase 3: Entry Logging (Core Feature) ✅

**Goal:** Implement Quick Add and detailed entry form with full CRUD.

### Deliverables
- [x] AddEntryScreen with all fields
- [x] Quick Add functionality (timestamp only, UNKNOWN defaults)
- [x] Edit entry functionality
- [x] Delete entry with confirmation
- [x] Entry validation (one per minute, max 50/day)

### Tasks

1. **Create use cases** (`/domain/usecase/`)
   - `AddPeeEntryUseCase`: validates and saves entry
   - `UpdatePeeEntryUseCase`: validates and updates
   - `DeletePeeEntryUseCase`: deletes and releases scheduled time
   - `GetEntriesForDateUseCase`: returns entries for a date
   - `GetUnclaimedScheduledTimesUseCase`: returns available times
   - `ValidateEntryUseCase`: checks constraints (one/min, max 50/day)

2. **Create AddEntryViewModel** (`/presentation/viewmodel/`)
   - State: timestamp, didVoid, volumeSize, color, urgency, notes, scheduledTime
   - Actions: save, validate, loadUnclaimedTimes
   - Handle Quick Add mode vs detailed mode

3. **Create AddEntryScreen** (`/presentation/screens/`)
   - Time picker (defaults to now, allows any past time)
   - Urge only toggle (hides volume/color when true)
   - Volume size selector (Small/Medium/Large)
   - Color picker (visual color chips)
   - Urgency selector (segmented buttons)
   - Scheduled time dropdown (unclaimed times + Unscheduled)
   - Notes field (max 500 chars)
   - Save button

4. **Create reusable components** (`/presentation/components/`)
   - `TimePicker` composable
   - `VolumeSelector` composable
   - `ColorPicker` composable
   - `UrgencySelector` composable
   - `ScheduledTimeDropdown` composable

5. **Implement Quick Add**
   - FAB on Home screen
   - Saves with timestamp only
   - All fields default to UNKNOWN
   - No scheduled time association

6. **Implement Edit Entry**
   - Navigate to AddEntryScreen with existing entry
   - Pre-populate all fields
   - Allow changing scheduled time association

7. **Implement Delete Entry**
   - Confirmation dialog
   - Release scheduled time for reassignment
   - Note: Anchor recalculation on delete is implemented in Phase 5

### Notes
- `GetUnclaimedScheduledTimesUseCase` returns empty list if no active schedule exists
- Scheduled time dropdown shows only "Unscheduled" option until Phase 5

### UiState Pattern Example
```kotlin
data class AddEntryUiState(
    val timestamp: Instant = Clock.System.now(),
    val didVoid: Boolean = true,
   val leakAmount: LeakAmount = LeakAmount.NONE,
    val volumeSize: VolumeSize = VolumeSize.UNKNOWN,
    val color: PeeColor = PeeColor.UNKNOWN,
    val urgency: Urgency = Urgency.UNKNOWN,
   val activityContext: String = "",
    val notes: String = "",
    val scheduledTime: LocalTime? = null,
    val unclaimedTimes: List<LocalTime> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null  // shown as Toast when non-null
)
```

### Tests
- [ ] Quick Add creates entry with correct defaults
- [ ] Validation rejects second entry in same minute
- [ ] Validation rejects when 50 entries exist for day
- [ ] `addEntry_at50thEntry_succeeds`
- [ ] `addEntry_at51stEntry_throwsMaxEntriesException`
- [ ] Edit preserves all fields
- [ ] Delete triggers confirmation dialog
- [ ] Scheduled time dropdown hides claimed times
- [ ] `addEntry_withTimestampFromYesterday_savesCorrectly`
- [ ] `validateEntry_withNotes501Chars_throwsValidationException`

### Dependencies
- Phase 2 (models, database, repositories)

---

## Phase 4: Home Screen ✅

**Goal:** Build the main dashboard with summary, entries, and recent logs.

### Deliverables
- [x] Today's summary (void count, urges, total output volume)
- [x] Recent entries list (last 3 from today)
- [x] Missed scheduled times alert section (placeholder ready)
- [ ] Exercise checklist (or "All done! ✓") - deferred to Phase 7
- [ ] Midnight auto-refresh - deferred

### Design Decision: Water Intake
Liquid intake is tracked with explicit entries (type + amount + unit), converted to liters, and displayed as daily total liters.

### Tasks

1. **Create use cases** (`/domain/usecase/`) ✅
   - `GetTodaySummaryUseCase`: calculates void/urge counts + total output score
   - `GetRecentEntriesUseCase`: returns last 3 entries for today
   - `AddLiquidIntakeUseCase`: adds one liquid intake entry
   - `GetTodayLiquidTotalUseCase`: returns today's total liters
   - `GetTodayLiquidEntriesUseCase`: returns today's intake entries
   - `GetMissedScheduledTimesUseCase`: returns unclaimed past times (after 15min grace)
   - `GetUncompletedExercisesUseCase`: returns exercises not yet done today

2. **Create HomeViewModel** (`/presentation/viewmodel/`) ✅
   - State: summary (includes totalOutputScore), recentEntries, liquid total liters, liquid entries
   - Observe database changes with Flow
   - Handle midnight refresh

3. **Create Home screen components** (`/presentation/components/`) ✅
   - `SummaryCard`: displays void count, urges, and total output
   - `LiquidIntakeCard`: type picker, amount + unit input, add action, today's liters total
   - `MissedScheduleAlert`: list of missed times (placeholder)
   - `RecentEntriesList`: tappable entry rows

4. **Create HomeScreen** (`/presentation/screens/`) ✅
   - Compose all components
   - FAB for Quick Add
   - Navigate to Settings via header icon
   - Handle entry tap → navigate to edit

5. **Implement midnight refresh**
   - Observe system time changes
   - Refresh ViewModel state at 00:00

### Notes
- Missed scheduled times section shows empty until Phase 5 (schedule management)
- Exercise checklist section shows empty until Phase 7 (exercise tracking)
- UI structure is built in this phase; data populates in later phases

### Tests
- [ ] Summary shows correct counts including total output
- [ ] Missed times section shows empty when no schedule exists
- [ ] Recent entries limited to 3 from today
- [ ] Exercise section shows empty when no exercises configured

### Dependencies
- Phase 3 (entry logging for Quick Add navigation)

---

## Phase 5: Schedule Management ✅

**Goal:** Implement void schedule creation, dynamic anchor, and progress tracking.

### Deliverables
- [x] Create schedule screen with preview
- [x] Dynamic anchor from first void of day
- [x] Schedule overlap detection and replacement
- [x] Days remaining display
- [x] Today's progress (completed, missed, upcoming)
- [ ] Edit expiry date - UI deferred

### Tasks

1. **Create use cases** (`/domain/usecase/`) ✅
   - `CreateVoidScheduleUseCase`: validates and saves schedule
   - `UpdateVoidScheduleExpiryUseCase`: modifies expiry date
   - `DeleteVoidScheduleUseCase`: removes schedule
   - `GetActiveSchedulesUseCase`: returns non-expired schedules
   - `CheckScheduleOverlapUseCase`: detects date range overlaps
   - `GetDailyScheduledTimesUseCase`: calculates times based on anchor
   - `GetScheduleProgressUseCase`: returns completed/missed/upcoming

2. **Create schedule anchor logic** ✅
   - Find first void entry of day
   - If no void, use typical start time
   - Calculate intervals from anchor to end time
   - Only include intervals that fit before end time
   - Handle edge: first void after end time → no scheduled times
   - Recalculate anchor when first void of day is deleted (find next void or revert to typical)
   - Recalculate anchor when entry timestamp is edited (if it affects first void status)

3. **Create ScheduleViewModel** (`/presentation/viewmodel/`) ✅
   - State: activeSchedules, todayProgress, previewTimes
   - Actions: create, updateExpiry, delete
   - Handle overlap confirmation

4. **Create schedule components** (`/presentation/components/`) ✅
   - `ScheduleCard`: displays active schedule details
   - `ScheduleProgressList`: completed ✅ (or 🎈 if burst urgency), missed ⚠️, upcoming ○
   - `CreateScheduleDialog`: time pickers, interval selector, duration

5. **Create ScheduleScreen** (`/presentation/screens/`) ✅
   - Empty state: "Create Schedule" button only
   - Active schedule card with days remaining
   - Today's progress list
   - Edit expiry button
   - Create new schedule button (with overlap check)

6. **Implement overlap handling** ✅
   - Check date ranges (not time-of-day)
   - Show confirmation dialog: "Replace existing schedule?"

### Tests
- [ ] Schedule generates correct times
- [ ] Partial intervals excluded (before end time only)
- [ ] First void anchors schedule correctly
- [ ] Late first void marks earlier times as missed
- [ ] Early first void extends schedule
- [ ] Delete first void recalculates anchor
- [ ] Overlap detection works for date ranges
- [ ] Days remaining updates correctly
- [ ] `getScheduledTimes_firstVoidAfterEndTime_returnsEmptyList`
- [ ] `getScheduledTimes_intervalDoesNotFit_excludesPartialInterval`
- [ ] `deleteFirstVoid_withSecondVoidAt11am_recalculatesAnchorTo11am`
- [ ] `deleteFirstVoid_withNoOtherVoids_revertsToTypicalStartTime`
- [ ] `checkOverlap_adjacentDateRanges_noOverlap`
- [ ] `checkOverlap_singleDayOverlap_detectsOverlap`

### Dependencies
- Phase 3 (entries for anchor calculation)
- Phase 4 (missed times on Home screen)

---

## Phase 6: History & Statistics ✅

**Goal:** Implement calendar view, day timeline, and statistics with charts.

### Deliverables
- [x] Calendar view (month, dots on days with entries)
- [x] Day entry list view (shows all entries for selected day)
- [x] Day timeline view with pinch-to-zoom (custom pointer input for simultaneous scroll + zoom)
- [x] Timeline shows scheduled time markers (⏰ Upcoming/Missed/✅), void entries (colored, with delta), drink entries, urge-only, and leak-only entries
- [x] Schedule markers distinguish claimed (✅) vs missed vs upcoming
- [x] Chronological feed: list view starts at today, scroll-up-to-load-more (7 days at a time)
- [x] Drink entries integrated into timeline and list views with type emojis
- [x] "See all" button on Home navigates to Calendar
- [x] Statistics charts (void frequency per day — stacked by pee color)
- [x] Schedule adherence rate chart (% per day, green/amber/red bars)
- [x] Liquid intake chart (liters/day with hydration goal line, conditional bar colors)
- [x] Exercise completion rate chart (% per day, green/amber/red bars)
- [x] Calendar day cells show schedule indicator dots (yellow=void, green=exercise)
- [x] CSV export (share via FileProvider + ACTION_SEND)

### Tasks

1. **Create use cases** (`/domain/usecase/`)
   - [x] `GetEntriesForMonthUseCase`: returns entry dates for calendar dots
   - [x] `GetDayTimelineUseCase`: returns entries + schedule times + drinks for timeline
   - [x] `GetVoidFrequencyUseCase`: void count per day with color breakdown (stacked bars)
   - [x] `GetScheduleAdherenceUseCase`: % of scheduled voids completed per day
   - [x] `GetLiquidIntakeUseCase`: total liters per day
   - [x] `GetExerciseCompletionUseCase`: % of scheduled exercises completed per day
   - [x] `ExportCsvUseCase`: generates CSV string

2. **Create CalendarViewModel** (`/presentation/viewmodel/`) ✅
   - State: selectedMonth, daysWithEntries, selectedDay, entries, timelineMode
   - FeedItem data class for merged pee+drink chronological feed
   - loadFeed() starts with today, loadMoreHistory() loads 7 more days
   - Drink CRUD (add, update, delete) integrated

3. **Create history components** (`/presentation/components/`) ✅
   - `CalendarView`: month grid with dot indicators
   - `DayTimelineCard`: vertical timeline with pinch-to-zoom (custom pointerInput, awaitEachGesture)
     - MIN_HOUR_HEIGHT=40dp, MAX=160dp, DEFAULT=80dp
     - 1-finger scroll + 2-finger zoom, scroll offset clamped on zoom change
     - Schedule markers (light gray, ⏰), void entries (colored), drinks (thin 18dp bars), urges (purple), leaks (pink)
   - `DayEntryList`: merged pee+drink list with chronological ordering
   - `EntryListItem`: tappable row with time, color, volume

4. **Create CalendarScreen** (`/presentation/screens/`) ✅
   - Calendar month view with day selection
   - Day timeline (pinch-to-zoom)
   - Chronological feed (LazyColumn, scroll-up pagination, 7 days per batch)
   - Edit drink dialog integrated
   - Empty state when no entries
   - [ ] Filter: all, voids only, urges only, leaks only

5. **Create StatisticsViewModel** (`/presentation/viewmodel/`) ✅
   - State: dateRange, voidFrequency, scheduleAdherence, liquidIntake, exerciseCompletion, hydrationGoal
   - Date range selector: 7d, 30d
   - Loads all chart data in parallel

6. **Create chart components** (`/presentation/components/`) ✅
   - `VoidFrequencyChart`: stacked bar chart by pee color, consistent integer Y-axis steps
   - `ScheduleAdherenceChart`: 0-100% bars, green/amber/red coloring
   - `LiquidIntakeChart`: liters/day bars with dashed hydration goal line, lighter blue below goal
   - `ExerciseCompletionChart`: 0-100% bars, green/amber/red coloring
   - ~~`ColorDistributionChart`~~: **Removed** — pee color info is shown in the stacked void frequency chart
   - All charts: custom Canvas drawing, 600ms grow animation, consistent 24dp padding, M/d date labels with smart label-stepping

7. **Create StatisticsScreen** (`/presentation/screens/`) ✅
   - Date range chips (7d, 30d)
   - Date range display
   - 4 chart cards with summary stats below each
   - Scrollable layout

8. **Implement CSV export**
   - Format: Date, Time, EntryType, Volume, LeakAmount, Color, Urgency, ScheduledTime, MinutesFromSchedule, ActivityContext, LiquidType, LiquidAmount, LiquidUnit, LiquidLiters, Notes
   - Block during database writes
   - Save to app cache directory
   - Share via FileProvider + ACTION_SEND intent:
     ```kotlin
     val file = File(context.cacheDir, "laniao_export_YYYY-MM-DD.csv")
     file.writeText(csvContent)
     
     val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
     
     val intent = Intent(Intent.ACTION_SEND).apply {
         type = "text/csv"
         putExtra(Intent.EXTRA_STREAM, uri)
         addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
     }
     context.startActivity(Intent.createChooser(intent, "Share CSV"))
     ```
   - Requires: FileProvider in AndroidManifest.xml, file_paths.xml resource

### Tests
- [x] Calendar shows dots only on days with entries
- [x] Day timeline displays entries and scheduled times correctly
- [x] Charts render without crash
- [x] CSV export produces valid format
- [ ] Export blocked during DB write

### Dependencies
- Phase 4 (drink intake data)
- Phase 5 (schedule data)

### Implementation Notes (Phase 6)
- History screen was renamed to Calendar screen; old HistoryScreen/HistoryViewModel were deleted
- Timeline uses custom `pointerInput` with `awaitEachGesture` instead of `verticalScroll` + `transformable` (which conflicted)
- Schedule markers and entry items are separate `TimelineItem` objects in the timeline
- `TimelineItem.isClaimed` tracks whether a schedule marker has a corresponding entry logged
- Drink entries show in both timeline (thin bars with type emoji) and list views
- Feed pagination: `LazyColumn` with `derivedStateOf` detecting scroll-to-top + `isScrollInProgress && !canScrollBackward`
- Room DB is at version 7 with explicit migrations (MIGRATION_6_7 drops old exercise tables)

---

## Phase 7: Exercise Tracking

**Goal:** Implement Kegel exercise configuration, tracking, and streaks.

### Deliverables
- [x] Exercise configuration (types, sessions, sets, reps, hold)
- [x] Daily checklist completion
- [x] Streak tracking (consecutive days, device local time)
- [ ] Exercise reminder notification — deferred to V2

### Tasks

1. **Create use cases** (`/domain/usecase/`)
   - `GetExerciseConfigsUseCase`: returns all configs
   - `UpdateExerciseConfigUseCase`: saves config changes
   - `CompleteExerciseUseCase`: marks exercise done
   - `GetExerciseStreakUseCase`: calculates consecutive days
   - `GetTodayExerciseStatusUseCase`: returns completed/remaining

2. **Create ExerciseViewModel** (`/presentation/viewmodel/`)
   - State: configs, todayStatus, streak
   - Actions: complete, updateConfig

3. **Create exercise components** (`/presentation/components/`)
   - `ExerciseConfigCard`: editable config for each type
   - `ExerciseChecklistItem`: tappable to complete
   - `StreakBadge`: shows streak count with 🔥

4. **Create exercise section in Settings**
   - Enable/disable each type
   - Configure sessions per day
   - Configure sets, reps, hold duration (defaults: 3 sessions/day, 1 set, 12 reps, 5s hold)

5. **Implement streak logic**
   - Check completions for yesterday (device local time midnight)
   - Any missing exercise breaks streak
   - Store streak count or calculate dynamically

6. **Implement exercise reminder**
   - Single daily notification at user-configured time
   - WorkManager for flexible timing
   - Configurable in Settings (on/off, time)

### Tests
- [ ] Config saves and loads correctly
- [ ] Completion marks exercise done
- [ ] Streak increments on consecutive days
- [ ] Missing any exercise resets streak
- [ ] Reminder schedules at correct time
- [ ] `getStreak_missingOneExerciseType_resetsToZero`
- [ ] `getStreak_completedYesterdayAndToday_returnsTwo`
- [ ] `getStreak_skippedYesterday_returnsOne`
- [ ] `getStreak_crossingMidnight_usesDeviceLocalTime`

### Dependencies
- Phase 4 (Home screen exercise checklist)

### Implementation Notes (Phase 7)
- 7 exercise types across 2 categories: Kegel (Standard, Quick Flick, Endurance Hold) and Relaxation (Deep Breathing, Chair Relaxation, Child's Pose, Reverse Kegel)
- Exercise schedule uses `ExerciseScheduleItemEntity` (Room) with per-type sessions/day
- Streak logic: calculates consecutive completed days using device local time midnight boundary
- Hydration streak also tracked (days meeting hydration goal)
- ExerciseChecklist on Home shows emoji bubbles per exercise type with completion counter
- DB migration 6→7 drops old exercise config/completion tables, creates new exercise_schedule_items + app_settings tables

---

## Phase 8: Retroactive Entry

**Goal:** Allow adding any entry type (pee, drink, exercise) for past dates from the calendar view.

### Deliverables
- [x] Single FAB (+) on calendar timeline view (bottom-right)
- [x] FAB opens entry type picker dialog: Void, Drink, Exercise
- [x] Each type opens its respective dialog (AddEntryDialog, AddDrinkDialog, AddExerciseDialog)
- [x] Date defaults to currently selected calendar date (today if viewing future date)
- [x] User can change date to any past date; future dates blocked
- [x] All entry types use dialog overlays (same UI as Home screen)
- [x] Created entries appear in timeline and list views immediately (via Room Flow)
- [x] Tapping existing entries on calendar opens edit dialog

### Design Decisions
- **Single FAB** with entry type picker (not multiple FABs like Home)
- **Reuse existing dialogs**: `AddEntryDialog`, `AddDrinkDialog`, `AddExerciseDialog` with `defaultDate` parameter
- **Date handling**: If calendar is viewing a future date, default to today; otherwise use selected date
- **No future entries**: All entry dates must be today or earlier

### Tasks

1. **Add FAB to CalendarScreen**
   - Single "+" FAB at bottom-right
   - Opens entry type picker dialog (Void/Drink/Exercise chips)

2. **Add entry type picker dialog**
   - Three options: 🚽 Void, 🥤 Drink, 💪 Exercise
   - Selecting opens the corresponding add dialog with `defaultDate`

3. **Wire save/delete for pee entries in CalendarViewModel**
   - Add `savePeeEntry(PeeEntry)` and `deletePeeEntry(Long)` methods
   - Reuse `AddPeeEntryUseCase`, `UpdatePeeEntryUseCase`, `DeletePeeEntryUseCase`

4. **Handle entry tap → edit dialog**
   - Tapping a pee entry on calendar opens `AddEntryDialog` with `existingEntry`
   - Tapping a drink entry opens `AddDrinkDialog` with `existingEntry`
   - Tapping an exercise entry opens exercise detail (delete only)

### Dependencies
- Phase 6 (Calendar/timeline views)
- AddEntryDialog, AddDrinkDialog, AddExerciseDialog (already created)

### Implementation Notes (Phase 8)
- Calendar uses speed-dial FAB pattern: single "+" FAB expands to show 🚽/🥤/💪 bubble FABs with AnimatedVisibility
- Entry type picker uses animated vertical expansion above main FAB
- All three add dialogs accept `defaultDate` parameter for retroactive entries
- All dialogs converted from screen navigation to dialog overlays
- CalendarViewModel has `savePeeEntry`, `deletePeeEntry`, `addDrink`, `completeExercise` methods
- Tapping existing pee entries loads entry for edit via `loadPeeEntryForEdit`
- Date pickers in all dialogs block future dates

---

## Phase 8.5: Calendar Day Swipe Transition

**Goal:** Add a slide transition when swiping between days in the calendar view.

### Deliverables
- [x] Slide-left transition when swiping/navigating to the next day
- [x] Slide-right transition when swiping/navigating to the previous day

### Tasks

1. **Add horizontal slide animation to calendar day transitions**
   - When navigating to a later date, slide content left (new day enters from right)
   - When navigating to an earlier date, slide content right (new day enters from left)
   - Keep transition snappy (< 300ms)

### Dependencies
- Phase 6 (Calendar view)

### Implementation Notes (Phase 8.5)
- Added `slideDirection` field to `CalendarUiState` (1 = forward, -1 = backward, 0 = initial)
- `nextDay()`, `previousDay()`, and `selectDate()` in CalendarViewModel set `slideDirection`
- CalendarScreen wraps DAY_LIST content in `AnimatedContent` with `slideInHorizontally`/`slideOutHorizontally`
- Transition direction determined by `slideDirection`: forward slides left, backward slides right

---

## Phase 9: Settings, Backup & Polish

**Goal:** Complete settings, backup/restore, and polish.

### Deliverables
- [x] Settings screen (backup/restore, clear data, about)
- [x] Backup to JSON (atomic write, schema version)
- [x] Restore from JSON (with warning)
- [x] Clear all data (confirmation dialog)
- [x] Navigation transitions: fade (250ms)
- [x] UI polish: consistent accent bar headers across all Home screen sections
- [x] UI polish: pastel card backgrounds on Home screen
- [x] UI polish: emojis on all Home section titles
- [x] UI polish: uniform TopAppBar titles on Calendar/Statistics/Settings
- [x] UI polish: all entry dialogs scrollable with date-first, time-second ordering
- [x] UI polish: TimePicker (full clock) in all dialogs
- [x] UI polish: timeline hour labels no-wrap (64dp width)
- [x] UI polish: calendar date header spacing and size

### Tasks

1. **Create SettingsViewModel** (`/presentation/viewmodel/`)
   - State: backupStatus
   - Actions: backup, restore, clearData

2. **Create SettingsScreen** (`/presentation/screens/`)
   - Backup button
   - Restore button
   - Clear all data button
   - About section (app name + version number)

3. **Create backup/restore use cases** (`/domain/usecase/`)
   - `BackupDataUseCase`: exports all data to JSON
   - `RestoreDataUseCase`: imports and replaces all data
   - Include schema version in backup

4. **Implement backup**
   - Write to temp file, rename atomically
   - Save to app-specific storage
   - Include: entries, schedules, water intake, exercise configs/completions
   - Show confirmation with file location

5. **Implement restore**
   - Show warning: "This will replace X entries from last Y days"
   - Parse JSON, validate schema version
   - Replace all data in transaction
   - Show confirmation with restored counts

6. **Implement clear data**
   - Tap "Clear all data" → "Are you sure?" dialog with Yes / No
   - Yes → delete all tables, show confirmation
   - No → dismiss dialog, no action

7. **Polish**
   - Visual feedback on all taps (ripple < 100ms)
   - Navigation transitions: fade (< 300ms)
   - Accessibility (color contrast WCAG AA)
   - Error states and empty states

### Tests
- [ ] Backup creates valid JSON
- [ ] Restore replaces data correctly
- [ ] Schema migration handles version mismatch
- [ ] Clear data removes all records
- [ ] Clear data confirmation dialog works (Yes clears, No dismisses)
- [ ] `backup_duringDbWrite_blocksUntilComplete`
- [ ] `restore_withOlderSchemaVersion_migratesSuccessfully`
- [ ] `restore_withNewerSchemaVersion_showsError`
- [ ] `restore_withCorruptedJson_showsError`

### Dependencies
- All previous phases

### Implementation Notes (Phase 9 — Settings & Polish)
- Home screen cards use distinct pastel backgrounds: Summary=#E3F2FD (blue), Drinks=#E0F7FA (cyan), Exercises=#E8F5E9 (green), Streaks=#FFF8E1 (amber)
- All Home section headers use accent bar pattern: 4dp wide colored bar + Spacer(8dp) + titleLarge Bold + emoji
- Section title emojis: 📋 Today's Summary, 🥤 Drinks, 💪 Exercises, 🔥 Streaks
- Settings screen converted from manual Column+headlineMedium to Scaffold+TopAppBar (matching Calendar/Statistics)
- Bottom nav labels use explicit `labelSmall` + `maxLines = 1` for uniform sizing
- All entry dialogs (AddEntryDialog, AddDrinkDialog, AddExerciseDialog) are scrollable with `verticalScroll`
- All entry dialogs use `TimePicker` (full clock dial), not `TimeInput`
- Field order in all entry dialogs: Date selector → Time picker → type-specific fields
- CreateScheduleDialog reordered: Start/End Date → Start/End Time → Interval → Preview; now scrollable
- Timeline hour labels widened from 48dp to 64dp with `maxLines = 1, softWrap = false` to prevent "10:00 PM" wrapping
- Calendar date header upgraded from `titleSmall`/4dp padding to `titleMedium`/12dp padding
- Schedule screen uses accent bar headers with icon buttons (edit ✏️ / delete 🗑️)
- BackupDataUseCase serializes all 7 entity tables + AppSettings to JSON using kotlinx.serialization
- Backup schema version = 1; BackupData includes peeEntries, voidSchedules, waterIntakes, drinkEntries, exerciseSchedules, exerciseScheduleItems, exerciseCompletions, hydrationGoalLiters
- Backup DTOs use primitive types (Long for timestamps, String for dates/enums) to avoid java.time serialization issues
- RestoreDataUseCase validates schema version; newer versions show error and refuse
- Restore clears all tables in FK-safe order then inserts backup data inside a Room transaction
- ClearAllDataUseCase deletes all tables in FK-safe order (completions → items → schedules → drinks → water → void → pee → settings)
- Backup writes to temp file then renames atomically (app-internal storage)
- Restore uses in-app backup picker dialog (scans filesDir for laniao_backup_*.json files)
- Settings screen sections: Hydration Goal, Data (CSV export + Backup + Restore), Danger Zone (Clear All Data), About (LaNiao v1.0.0)
- Clear data dialog: "Are you sure?" with Yes (red) / No buttons
- NavHost configured with fadeIn/fadeOut transitions (250ms tween) for all routes
- **Code polish**: Centralized date/time formatters in `util/DateFormatters.kt` (36 usages consolidated)
- **Code polish**: Centralized color constants in `theme/LaNiaoColors.kt` (card backgrounds, accents, chart colors, FAB colors)
- **Code polish**: All feedback messages use Snackbar (replaced Toast in HomeScreen, ScheduleScreen, SettingsScreen)
- **Code polish**: Unified card padding to 16.dp across all components (ExerciseChecklist was 12.dp)
- **Code polish**: Normalized all empty state messages to "No entries yet today"
- **Code polish**: All TopAppBar titles use `titleLarge` + `FontWeight.Bold` (Calendar, Statistics, Settings)

---

## Phase Summary

| Phase | Name | Key Deliverables | Status |
|-------|------|------------------|--------|
| 1 | Project Setup | Structure, DI, DB, Navigation | ✅ Complete |
| 2 | Domain & Database | Models, Entities, DAOs, Repos | ✅ Complete |
| 3 | Entry Logging | Add/Edit/Delete entries, Quick Add | ✅ Complete |
| 4 | Home Screen | Dashboard, drink intake, exercises | ✅ Complete |
| 5 | Schedule Management | Void + exercise schedules, dynamic anchor | ✅ Complete |
| 6 | History & Statistics | Calendar, timeline, charts, export | ✅ Complete |
| 7 | Exercise Tracking | Schedule-based, 7 types, streaks, checklist | ✅ Complete |
| 8 | Retroactive Entries | Add any entry type from calendar for past dates | ✅ Complete |
| 8.5 | Calendar Swipe Transition | Slide animation between days | ✅ Complete |
| 9 | Settings & Polish | Backup/restore, settings, polish | ✅ Complete |

---

## Testing Strategy

### Test File Structure
```
/app/src/test/java/com/laniao/           - Unit tests
  /domain/usecase/                        - Use case tests
  /data/repository/                       - Repository tests (with fake DAOs)
  /presentation/viewmodel/                - ViewModel tests

/app/src/androidTest/java/com/laniao/    - Instrumented tests
  /data/local/dao/                        - DAO tests (with in-memory DB)
  /presentation/screens/                  - Compose UI tests
```

### Test Naming Convention
Pattern: `methodName_scenario_expectedResult`

Examples:
- `addEntry_withDuplicateMinute_throwsValidationException`
- `getUnclaimedTimes_withNoSchedule_returnsEmptyList`
- `calculateAnchor_firstVoidAt10am_returnsScheduleFrom10am`

### Mocking Strategy
- **MockK** for mocking dependencies in unit tests
- **In-memory Room database** for DAO/integration tests
- **Fake repositories** for ViewModel tests (implement interface with in-memory data)
- **Turbine** for testing Kotlin Flows in ViewModels
- **Compose testing rules** for UI tests

### Example Test Patterns

**Use Case Test:**
```kotlin
class AddPeeEntryUseCaseTest {
    private val repository: PeeEntryRepository = mockk()
    private val useCase = AddPeeEntryUseCase(repository)

    @Test
    fun `addEntry_when50EntriesExist_throwsMaxEntriesException`() = runTest {
        // Arrange
        coEvery { repository.getEntriesForDate(any()) } returns List(50) { mockEntry }
        
        // Act & Assert
        assertThrows<MaxEntriesException> {
            useCase(newEntry)
        }
    }

    @Test
    fun `addEntry_withDuplicateMinute_throwsValidationException`() = runTest {
        // Arrange
        val existingEntry = PeeEntry(timestamp = Instant.parse("2026-02-11T09:05:00Z"))
        coEvery { repository.getEntriesForDate(any()) } returns listOf(existingEntry)
        val newEntry = PeeEntry(timestamp = Instant.parse("2026-02-11T09:05:30Z")) // same minute
        
        // Act & Assert
        assertThrows<DuplicateMinuteException> {
            useCase(newEntry)
        }
    }
}
```

**ViewModel Test (with Turbine):**
```kotlin
class HomeViewModelTest {
    private val getWaterIntakeUseCase: GetTodayWaterIntakeUseCase = mockk()
    private val setWaterIntakeUseCase: SetWaterIntakeUseCase = mockk()
    
    @Test
    fun `setWaterIntake_whenSelected_updatesState`() = runTest {
        // Arrange
        coEvery { setWaterIntakeUseCase(any()) } just Runs
        val viewModel = HomeViewModel(/* dependencies */)
        
        // Act & Assert
        viewModel.state.test {
            assertEquals(null, awaitItem().waterIntake)
            viewModel.setWaterIntake(WaterAmount.NORMAL)
            assertEquals(WaterAmount.NORMAL, awaitItem().waterIntake)
        }
    }

    @Test
    fun `setWaterIntake_sameTappedTwice_removesSelection`() = runTest {
        // Arrange
        val viewModel = HomeViewModel(/* with NORMAL already set */)
        
        // Act & Assert
        viewModel.state.test {
            assertEquals(WaterAmount.NORMAL, awaitItem().waterIntake)
            viewModel.setWaterIntake(WaterAmount.NORMAL) // tap same
            assertEquals(null, awaitItem().waterIntake)
        }
    }
}
```

**DAO Test (Instrumented):**
```kotlin
@RunWith(AndroidJUnit4::class)
class PeeEntryDaoTest {
    private lateinit var database: LaNiaoDatabase
    private lateinit var dao: PeeEntryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LaNiaoDatabase::class.java
        ).build()
        dao = database.peeEntryDao()
    }

    @Test
    fun getByDate_returnsOnlyEntriesForThatDate() = runTest {
        // Arrange
        val today = LocalDate.of(2026, 2, 11)
        val todayEntry = createEntity(today)
        val yesterdayEntry = createEntity(today.minusDays(1))
        dao.insert(todayEntry)
        dao.insert(yesterdayEntry)
        
        // Act
        val result = dao.getByDate(today)
        
        // Assert
        assertEquals(1, result.size)
        assertEquals(todayEntry.id, result[0].id)
    }
}
```

### Unit Tests
- All use cases (business logic)
- Repository mappings
- ViewModel state management
- Schedule anchor calculations

### Integration Tests
- Database operations (DAOs)
- Repository → DAO flow
- Navigation flows

### UI Tests
- Screen renders correctly
- User interactions work
- Form validation displays errors
- Charts render without crash

### Manual Testing
- Notification timing accuracy
- Midnight auto-refresh
- Dark mode appearance
- Edge cases (50 entries, first void late, etc.)

---

## V2 Queue

Items deferred to a future version.

### Notifications
- [ ] Void schedule notifications (AlarmManager with `setExactAndAllowWhileIdle()`)
  - Schedule for upcoming times only
  - Cancel and reschedule when schedule modified
  - Open app on tap (no quick actions)
  - Accept battery optimization may skip
- [ ] Missed schedule follow-up notification (optional, default OFF)
  - Configurable delay (e.g., 30 min after missed)
  - Only if no entry associated with time
- [ ] Exercise reminder notification
- [ ] Notification toggles in Settings (void reminders, exercise reminder)
- [ ] Missed schedule follow-up toggle + delay config in Settings

### Notification Tests
- [ ] Notifications fire at correct times
- [ ] Notification tap opens app
- [ ] `scheduleNotification_forPastTime_doesNotSchedule`

### Dark Mode
- [ ] Dark mode testing and polish

---

## Definition of Done

Each phase is complete when:
- [ ] All tasks implemented
- [ ] Unit tests passing
- [ ] UI tested on device/emulator
- [ ] Code follows AGENTS.md conventions
- [ ] No compiler warnings
- [ ] Reviewed against PRODUCT-REQUIREMENTS.md
