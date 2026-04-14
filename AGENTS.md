# AGENTS.md

## Project Overview

**What:** LaNiao is an Android app for tracking daily urination patterns and habits.

**Why:** Helps users monitor their bladder health, identify patterns, stay hydrated, and share data with healthcare providers if needed. Tracking frequency, volume, color, and timing can help identify potential health issues early.

**Users:** 
- Individuals monitoring their hydration habits
- People with bladder conditions (overactive bladder, incontinence)
- Patients tracking for medical consultations
- Health-conscious users wanting to establish healthy patterns

---

## Tech Stack

- **Language:** Kotlin 1.9+
- **Framework:** Android SDK (minSdk 26 / Android 8.0+, targetSdk 34)
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM with Clean Architecture
- **Dependency Injection:** Hilt
- **Database:** Room (SQLite)
- **Navigation:** Jetpack Compose Navigation
- **Charts:** Custom Canvas drawing (no external library)
- **Notifications:** AlarmManager (exact timing), WorkManager (flexible)
- **Async:** Kotlin Coroutines + Flow
- **Testing:** JUnit 5, Mockk, Compose UI Testing
- **Build:** Gradle with Kotlin DSL

---

## Project Structure

```
/LaNiao
  /app
    /src
      /main
        /java/com/laniao
          /data
            /local           - Room database, DAOs, entities
            /repository      - Repository implementations
          /domain
            /model           - Domain models (PeeEntry, DailyStats, etc.)
            /repository      - Repository interfaces
            /usecase         - Business logic use cases
          /presentation
            /screens         - Composable screens
            /components      - Reusable UI components
            /viewmodel       - ViewModels for each screen
            /navigation      - NavHost, routes, navigation helpers
            /theme           - Colors, typography, shapes
          /di               - Hilt dependency injection modules
          /util             - Extensions, helpers, constants
          LaNiaoApp.kt      - Application class
        /res
          /drawable         - Icons, images
          /values           - Strings, colors, dimensions
      /test                 - Unit tests
      /androidTest          - Instrumented/UI tests
  /docs                     - Documentation
  AGENTS.md
  README.md
  build.gradle.kts
```

---

## Coding Conventions

> **Reference:** Follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html) and [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide) as baseline. Project-specific conventions below extend these guidelines.

### Language Features
- Use Kotlin idioms (data classes, sealed classes, extension functions)
- Prefer `val` over `var` (immutability)
- Use nullable types explicitly with `?`
- Leverage `when` expressions for exhaustive matching
- Use `Flow` for reactive data streams
- Use `suspend` functions for coroutines

### Code Organization
- One class/interface per file (small related classes can be grouped)
- File name matches primary class name
- Order members: companion object, properties, init block, functions
- Group related functions together

### Jetpack Compose
- Use `@Composable` functions for UI
- Keep composables small and focused
- Extract reusable components to `/components`
- Use `remember` and `derivedStateOf` appropriately
- Preview functions with `@Preview` annotation

### Dependency Injection (Hilt)
- Use constructor injection
- Define modules in `/di` package
- Use `@Singleton` for app-wide dependencies
- Use `@ViewModelScoped` for ViewModel dependencies

---

## Naming Conventions

- **Classes, Interfaces, Objects:** PascalCase
  - ViewModels: suffix with `ViewModel` (e.g., `HomeViewModel`)
  - Use cases: verb + noun (e.g., `AddPeeEntryUseCase`)
  - Repositories: suffix with `Repository` (e.g., `PeeEntryRepository`)
  - DAOs: suffix with `Dao` (e.g., `PeeEntryDao`)

- **Functions, Properties, Variables:** camelCase
  - Composables: PascalCase (e.g., `PeeEntryCard()`)
  - Boolean properties: prefix with `is`, `has`, `should` (e.g., `isLoading`)

- **Constants:** UPPER_SNAKE_CASE in companion objects

- **Packages:** lowercase, no underscores

- **Resource IDs:** snake_case (e.g., `btn_add_entry`, `txt_volume`)

---

## Namespace Conventions

```kotlin
package com.laniao.data.local
package com.laniao.domain.model
package com.laniao.presentation.screens.home
```

---

## Domain Models

### PeeEntry
Core data model representing a single urination event:
```kotlin
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

data class PeeEntry(
    val id: Long = 0,
    val timestamp: Instant,
    val didVoid: Boolean = true,                     // false = urge only, no void
    val volumeSize: VolumeSize = VolumeSize.UNKNOWN, // ignored if didVoid=false
    val color: PeeColor = PeeColor.UNKNOWN,          // ignored if didVoid=false
    val urgency: Urgency = Urgency.UNKNOWN,  // enum: UNKNOWN, NONE, LOW, MEDIUM, HIGH, BURST 🎈
    val notes: String? = null,
    val scheduledTime: LocalTime? = null     // null = explicitly unscheduled
) {
    // Calculate minutes early/late compared to schedule
    fun minutesFromSchedule(entryDate: LocalDate): Int? {
        scheduledTime ?: return null
        val scheduled = scheduledTime.atDate(entryDate).toInstant(ZoneOffset.systemDefault())
        return Duration.between(scheduled, timestamp).toMinutes().toInt()
    }
}
```

### VolumeSize
Simplified volume tracking:
- `UNKNOWN` - Not recorded (for retroactive entries)
- `SMALL` - Brief, minimal output
- `MEDIUM` - Normal, typical void
- `LARGE` - Full bladder, extended void

### Color Scale
Use a standardized color scale for urine color:
- `UNKNOWN` - Not recorded (for retroactive entries) — displayed as gray in UI
- `CLEAR` - Well hydrated
- `LIGHT_YELLOW` - Normal, healthy
- `YELLOW` - Normal
- `DARK_YELLOW` - Drink more water
- `AMBER` - Dehydrated
- `BROWN` - Severely dehydrated / possible issue

### WaterIntake
Daily water consumption tracking:
```kotlin
data class WaterIntake(
    val id: Long = 0,
    val date: LocalDate,
    val amount: WaterAmount
)

enum class WaterAmount(val description: String, val litersApprox: Double) {
    LITTLE("A little", 1.8),      // < ~1.8 liters
    NORMAL("Normal", 2.7),        // ~2.7 liters (recommended daily)
    A_LOT("A lot", 3.6)           // > ~3.6 liters
}
```
- One entry per day (update if already exists)
- Displayed on Home screen with quick selector
- Used in Statistics to correlate hydration with urine color

### UrineSchedule
Custom scheduled voiding times for bladder training:
```kotlin
data class VoidSchedule(
    val id: Long = 0,
    val startTime: LocalTime,       // e.g., 09:00
    val endTime: LocalTime,         // e.g., 21:00
    val intervalMinutes: Int,       // e.g., 120 for every 2 hours
    val enabled: Boolean = true,
    val createdAt: LocalDate,       // when schedule was created
    val expiresAt: LocalDate        // schedule active until this date (inclusive)
) {
    // Days remaining until schedule expires
    fun daysRemaining(today: LocalDate = LocalDate.now()): Int =
        maxOf(0, (expiresAt.toEpochDay() - today.toEpochDay()).toInt())
    
    fun isActive(today: LocalDate = LocalDate.now()): Boolean =
        enabled && today <= expiresAt
    // Generate all scheduled times for the day
    fun getScheduledTimes(): List<LocalTime> {
        val times = mutableListOf<LocalTime>()
        var current = startTime
        while (current <= endTime) {
            times.add(current)
            current = current.plusMinutes(intervalMinutes.toLong())
        }
        return times
    }
}

// Example: 2-hour interval from 9am to 9pm
// VoidSchedule(startTime = LocalTime.of(9, 0), endTime = LocalTime.of(21, 0), intervalMinutes = 120)
// Results in: 9:00, 11:00, 13:00, 15:00, 17:00, 19:00, 21:00
```

**Schedule Configuration UI:**
- Start time picker (e.g., "First void at: 9:00 AM")
- End time picker (e.g., "Last void at: 9:00 PM")
- Interval selector: any value from 1 minute or more; preset buttons show 1.5h, 1.75h, 2h, 2.25h, 2.5h, 2.75h, 3h, 3.25h, 3.5h, 3.75h, 4h as quick options
- Duration picker: "Schedule active for X days" (e.g., 7, 14, 30 days, or custom)
- Preview of generated schedule times
- Shows expiration date and days remaining
- Notifications at exact scheduled void times
- Track adherence: actual time vs scheduled time (early/late minutes)
- Support for bladder training programs (create new schedule with longer intervals)

**Schedule Association Logic (Add Entry):**
When user logs an entry:
1. Auto-suggest absolute nearest scheduled time that has not been claimed by another entry
2. Show dropdown with all unclaimed scheduled times for user to choose different one
3. Provide explicit "Unscheduled" option for entries outside the schedule
4. Example: Entry at 11:05 with schedule [9:00, 11:00, 13:00] where 9:00 is already claimed
   - Default selection: 11:00 (nearest unclaimed)
   - Options shown: "11:00 AM ✓", "1:00 PM", "Unscheduled"
   - 9:00 AM not shown (already has an entry)

**Schedule Rules:**
- Multiple schedules allowed, but time ranges must not overlap
- If new schedule overlaps with existing active schedule, prompt user to confirm replacement
- When schedule expires (`today > expiresAt`), automatically disable it
- No prompt to create new schedule on expiry — user creates when ready
- User can manually disable or create new schedules anytime

### Exercise (Pelvic Floor)
Daily exercise tracking for bladder health:
```kotlin
data class ExerciseConfig(
    val id: Long = 0,
    val type: KegelType,
    val enabled: Boolean = true,
    val sessionsPerDay: Int = 3,      // default: 3 sessions per day
    val sets: Int = 1,                // default: 1 set
    val reps: Int = 12,               // default: 12 reps
    val holdSeconds: Int = 5          // default: 5 second hold
)

data class ExerciseCompletion(
    val id: Long = 0,
    val configId: Long,
    val type: KegelType,
    val completedAt: Instant,
    val scheduledDate: LocalDate
)

enum class KegelType {
    STANDARD,   // Basic squeeze and hold
    QUICK,      // Rapid contractions
    HOLD        // Extended hold (10+ seconds)
}
```
- User configures which Kegel types to do and how many sessions daily
- Daily checklist shows uncompleted exercises only
- Records exact timestamp when each exercise is completed (`completedAt`)
- Visual progress tracking (checkmarks, 🎈 for burst urgency, streaks)
- Customizable sets, reps, and hold duration
- Reminder notifications for exercises

### Statistics
Track daily/weekly/monthly aggregates:
- Total void count
- Scheduled void count
- Unscheduled void count
- Urge-only count (urges resisted without voiding)
- Urge-to-void ratio (bladder control indicator)
- Volume size distribution (small/medium/large)
- Daily hydration volume score (Small=1, Medium=2, Large=3, summed per day)
- Hydration goal tracking (default 15/day, adjustable 5-30 in Settings)
- Color distribution (shown via stacked bars in void frequency chart)
- Urgency distribution
- Water intake distribution (little/normal/a lot)
- Water intake vs urine color correlation
- Peak void hours
- Frequency patterns
- Schedule adherence rate (% of scheduled voids completed)
- Average minutes early/late from scheduled time
- Exercise completion rate
- Kegel streak (consecutive days completed)

---

## UI/UX Conventions

### Screens
1. **Home** - Today's summary, water intake selector, quick add button, last 3 entries, missed scheduled times alert, uncompleted exercises
2. **Add Entry** - Form to log a new pee event (volume size, color, urgency); entries are editable
3. **History** - Calendar view, day timeline view (visualize gaps), and list of past entries; tap to edit
4. **Schedule** - Custom void schedule setup, bladder training programs
5. **Exercises** - Daily Kegel checklist, instructions, progress tracking
6. **Statistics** - Charts, insights, schedule adherence, exercise streaks
7. **Settings** - Preferences, reminders, notifications, export data

### Visual Design
- Use Material 3 Design guidelines
- Color coding for hydration levels (green → yellow → orange → red)
- Simple, quick-entry focused UI
- Support dark mode
- Accessible color contrast ratios

### Charts (Statistics Screen)
- Use custom Compose Canvas drawing — no external chart libraries
- Keep charts simple: bar charts, line graphs, pie/donut charts
- Match Material 3 color scheme
- Support dark mode colors
- Animate transitions smoothly

### Quick Entry
- One-tap logging: saves timestamp only, all other fields default to UNKNOWN, scheduledTime = null (unscheduled)
- Toggle for "Urge only" (felt urge but didn't void)
- Expandable form for detailed logging
- Default to current time, allow adjustment

---

## Data Persistence

### Room Database
- Database name: `laniao_database`
- Current schema version: 6
- Export schema for debugging: `exportSchema = true`
- **No destructive migration fallback** — the app will crash on schema mismatch rather than silently wiping user data

> ⚠️ **IMPORTANT: Database Migrations**
> When changing the database schema (adding/removing/renaming tables or columns, changing types),
> you **MUST** add an explicit `Migration(oldVersion, newVersion)` in `DatabaseModule.kt` with
> the appropriate SQL `ALTER TABLE` / `CREATE TABLE` statements and register it via `.addMigrations()`.
> Never use `fallbackToDestructiveMigration()` — it deletes all user data.

### Data Export
- CSV export for sharing with doctors (from Statistics screen)
- Date range selection for CSV exports

### Backup & Restore
- JSON backup of all data (from Settings)
- JSON restore to recover data
- Full fidelity: preserves all relationships, settings, and configurations

### Privacy
- All data stored locally only
- No cloud sync (future consideration)
- Data retention: Keep all data forever (no auto-deletion)

### Timezone & DST
- All times stored as UTC internally
- Display times converted to device local time
- Schedule times recalculate based on current device local time when timezone/DST changes

---

## Notifications & Scheduling

### Notification Channels
```kotlin
// Create in Application.onCreate()
object NotificationChannels {
    const val VOID_REMINDERS = "void_reminders"      // High importance, sound
    const val EXERCISE_REMINDERS = "exercise_reminders"  // Default importance
}
```

### Void Schedule Reminders (AlarmManager)
Use AlarmManager for exact-time notifications at scheduled void times:
```kotlin
// Exact timing - fires at precisely 11:00 AM
alarmManager.setExactAndAllowWhileIdle(
    AlarmManager.RTC_WAKEUP,
    scheduledTimeMillis,
    pendingIntent
)
```
- Reschedule alarms on device reboot (`BOOT_COMPLETED` receiver)
- Cancel and reschedule when schedule is modified
- Use `BroadcastReceiver` to handle alarm and show notification

### Kegel Exercise Reminders (WorkManager)
Use WorkManager for flexible daily reminders:
```kotlin
// Daily reminder - flexible timing OK
val workRequest = PeriodicWorkRequestBuilder<ExerciseReminderWorker>(
    1, TimeUnit.DAYS
).setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
 .build()
```
- Battery-efficient for non-time-critical reminders
- Survives app restarts automatically
- Use `@HiltWorker` for dependency injection

### Required Permissions
```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

### Permission Handling
- Request `POST_NOTIFICATIONS` at runtime (Android 13+)
- Check `canScheduleExactAlarms()` before using AlarmManager (Android 12+)
- Guide user to settings if exact alarm permission denied

---

## Error Handling Pattern

### Coroutines
- Use `try-catch` in ViewModel/UseCase layer
- Expose UI state with `sealed class` (Loading, Success, Error)
- Show user-friendly error messages

### Validation
- Validate volumeSize is a valid enum value
- Validate timestamp (not in future)
- Validate schedule times don't overlap
- Validate exercise sets/reps are positive integers
- Handle empty/null states gracefully

---

## Testing Standards

### Test Classification

**Fast Tests:**
- Unit tests for UseCases, ViewModels
- Repository tests with fake/mock DAOs
- Utility/extension function tests
- Run without Android framework

**Slow Tests:**
- Room database tests (instrumented)
- Compose UI tests
- End-to-end flow tests

### Test Organization
- Mirror source structure in test folders
- Test class name: `[ClassUnderTest]Test`

### Test Naming
Pattern: `functionName_scenario_expectedResult`

Examples:
- `addEntry_withValidData_savesToDatabase`
- `calculateDailyStats_withMultipleEntries_returnsCorrectAverage`
- `getPeeColor_whenAmber_showsDehydrationWarning`

### Mocking
- Use MockK for Kotlin mocking
- Use fake repositories for ViewModel tests
- Use `TestDispatcher` for coroutine tests

---

## Localization

- English only
- All user-facing strings in `strings.xml`
- Use `stringResource()` in Compose
- Date/time formatting respects device locale

---

## Instructions for AI Agents

When working in this codebase:

1. **Always read AGENTS.md first** to understand conventions

2. **Follow Clean Architecture layers:**
   - Presentation → Domain → Data
   - Dependencies point inward only

3. **Create tests alongside implementation** - ViewModels and UseCases must have tests

4. **Use Hilt for dependency injection** - No manual instantiation of dependencies

5. **Keep Composables stateless** - Hoist state to ViewModel

6. **Use Kotlin idioms** - Data classes, sealed classes, extension functions

7. **Handle nullable types explicitly** - No `!!` operator without justification

8. **Consider accessibility** - Content descriptions, proper contrast

9. **Respect user privacy** - All data local, clear data handling

10. **Document non-obvious decisions** - Add comments explaining "why"

---

## Example Code Style

### Domain Model
```kotlin
package com.laniao.domain.model

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

data class PeeEntry(
    val id: Long = 0,
    val timestamp: Instant,
    val didVoid: Boolean = true,
    val volumeSize: VolumeSize = VolumeSize.UNKNOWN,
    val color: PeeColor = PeeColor.UNKNOWN,
    val urgency: Urgency = Urgency.UNKNOWN,
    val notes: String? = null,
    val scheduledTime: LocalTime? = null
) {
    val isScheduled: Boolean get() = scheduledTime != null
    val isUrgeOnly: Boolean get() = !didVoid
}

enum class VolumeSize {
    UNKNOWN, SMALL, MEDIUM, LARGE
}

enum class PeeColor(val hydrationLevel: Int?) {
    UNKNOWN(null),
    CLEAR(5),
    LIGHT_YELLOW(4),
    YELLOW(3),
    DARK_YELLOW(2),
    AMBER(1),
    BROWN(0)
}

enum class Urgency {
    UNKNOWN, NONE, LOW, MEDIUM, HIGH, BURST  // 🎈 = bursting urgency on void entry
}

data class VoidSchedule(
    val id: Long = 0,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val intervalMinutes: Int,
    val enabled: Boolean = true,
    val createdAt: LocalDate = LocalDate.now(),
    val expiresAt: LocalDate
) {
    fun getScheduledTimes(): List<LocalTime> = buildList {
        var current = startTime
        while (current <= endTime) {
            add(current)
            current = current.plusMinutes(intervalMinutes.toLong())
        }
    }
    
    fun daysRemaining(today: LocalDate = LocalDate.now()): Int =
        maxOf(0, (expiresAt.toEpochDay() - today.toEpochDay()).toInt())
    
    fun isActive(today: LocalDate = LocalDate.now()): Boolean =
        enabled && today <= expiresAt
}

data class DailyExercise(
    val id: Long = 0,
    val type: KegelType,
    val scheduledDate: LocalDate,
    val completedAt: Instant? = null,
    val sets: Int = 3,
    val reps: Int = 10,
    val holdSeconds: Int = 5
) {
    val isCompleted: Boolean get() = completedAt != null
}

enum class KegelType {
    STANDARD,
    QUICK,
    HOLD
}
```

### UseCase
```kotlin
package com.laniao.domain.usecase

import com.laniao.domain.model.PeeEntry
import com.laniao.domain.repository.PeeEntryRepository
import java.time.Instant
import javax.inject.Inject

class AddPeeEntryUseCase @Inject constructor(
    private val repository: PeeEntryRepository
) {
    suspend operator fun invoke(entry: PeeEntry): Result<Long> {
        return runCatching {
            require(entry.timestamp <= Instant.now()) {
                "Timestamp cannot be in the future"
            }
            repository.insert(entry)
        }
    }
}

class CompleteExerciseUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(exerciseId: Long): Result<Unit> {
        return runCatching {
            repository.markCompleted(exerciseId, Instant.now())
        }
    }
}
```

### Composable
```kotlin
package com.laniao.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.laniao.domain.model.PeeEntry
import com.laniao.R

@Composable
fun PeeEntryCard(
    entry: PeeEntry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = entry.timestamp.formatTime(),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            HydrationIndicator(color = entry.color)
        }
    }
}
```

---

## Checklist

Before finalizing your AGENTS.md, verify:

- [x] Project overview clearly describes purpose
- [x] Tech stack is accurate and complete
- [x] Project structure reflects Android/Compose architecture
- [x] Coding conventions are specific and actionable
- [x] Domain models define data structure
- [x] Testing standards include Fast/Slow classification
- [x] Instructions for agents are clear and prioritized
- [x] Privacy considerations documented
