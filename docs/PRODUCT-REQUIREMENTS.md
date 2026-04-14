# Product Requirements: LaNiao

## Overview

LaNiao is an Android app for tracking daily urination patterns, scheduled voiding for bladder training, and pelvic floor exercises. It helps users monitor bladder health, identify patterns, and share data with healthcare providers.

---

## Requirements Checklist

### Core Entry Logging
- [x] Quick Add button saves entry with timestamp only (all other fields default to UNKNOWN, unscheduled)
- [x] Detailed entry form captures: volume size, color, urgency, notes, scheduled time association
- [x] "Urge only" toggle to log urges resisted without voiding (cannot be associated with scheduled times)
- [x] "Leak only" entry type to log leakage events without a full void (cannot be associated with scheduled times)
- [x] Leak amount (Small, Medium, Large) can be recorded on any entry type (void, urge-only, or leak-only)
- [x] Void entries may optionally include a leak amount (e.g., voided but also leaked)
- [x] Entry type options: Void, Urge-only, Leak-only
- [x] Entries can be edited after creation (can reassign to any unclaimed time or unscheduled)
- [x] Timestamp editable to any past time (including previous days)
- [x] Entries can be deleted with confirmation (releases scheduled time for reassignment)
- [x] Timestamp defaults to current time, adjustable by user
- [x] Auto-suggest nearest unclaimed scheduled time (absolute time difference, past or future)
- [x] Dropdown shows only unclaimed scheduled times plus "Unscheduled"
- [x] Already-claimed scheduled times are hidden from selection
- [x] One entry per minute maximum (UI debounce for rapid taps)
- [x] Maximum 50 entries per day
- [x] Notes field: max 500 characters
- [x] Activity/context field for all entry types (void/urge/leak) to capture what user was doing (e.g., running, arriving home, sitting)

### Home Screen
- [x] Display today's summary (void count, scheduled vs unscheduled, urge-only count)
- [x] Display today's total liquid consumed in liters (calculated from all logged intake entries)
- [x] Liquid intake logging supports: Water, Sparkling water, Tea, Coffee, Milk, Juice, Custom
- [x] Liquid intake amount supports units: oz, ml, cups
- [x] Liquid intake entries are converted and aggregated to liters for daily total
- [ ] Home screen auto-refreshes at midnight to show new day
- [x] Quick Add floating action button
- [x] Show last 3 entries from today with time, volume, color indicators
- [ ] Alert section for missed scheduled times (15-minute grace period before showing as missed)
- [ ] Uncompleted Kegel exercises checklist (show "All done! ✓" when complete)
- [x] Tap entry to view/edit details

### Schedule Management
- [x] Create schedule with: typical start time, fixed end time, interval (presets + custom, 1 min to 6 hours)
- [x] Daily schedule times calculated from first void entry of the day (urge-only entries do not anchor schedule)
- [x] If no void entry yet, use typical start time for upcoming scheduled times
- [x] First void entry automatically associated with calculated start time
- [x] If first void is late, earlier scheduled times are automatically missed
- [x] If first void is early, schedule starts from that time (more scheduled times that day)
- [x] If first void is after end time, day has no scheduled times
- [x] Deleting the first void of the day recalculates anchor from next void (or reverts to typical start)
- [x] Editing entry timestamp that affects first void status also recalculates anchor
- [x] End time is fixed; only include intervals that fit completely before end time
- [x] Set schedule duration in days (7, 14, 30, or custom)
- [x] Schedule active through end of expiration day (11:59 PM)
- [ ] User can modify expiry date (end early, shorten, or extend)
- [x] Preview generated schedule times before saving
- [x] Multiple schedules allowed if date ranges don't overlap (dates, not time-of-day)
- [x] If new schedule overlaps existing dates, prompt to replace entire existing schedule
- [x] Schedule auto-disables when expired (entries retain historical associations)
- [x] Editing schedule keeps existing entry associations unchanged
- [x] Show days remaining on active schedule
- [x] Empty state: "Create Schedule" button only

### Exercise (Kegel) Tracking
- [ ] User configures which Kegel types to do (STANDARD, QUICK, HOLD)
- [ ] User sets sessions per day for each enabled type
- [ ] User configures sets, reps, hold duration per type (defaults: 3 sessions/day, 1 set, 12 reps, 5s hold)
- [ ] Home screen shows uncompleted exercises only
- [ ] Tap to mark exercise complete
- [ ] Track completion streaks (consecutive days based on device local time, missing any exercise breaks streak)
- [ ] Single daily exercise reminder notification at user-configured time (via WorkManager)

### History Screen
- [x] Calendar view showing days with entries (month view, dot indicator on days with entries)
- [x] Day timeline view: vertical timeline showing entries throughout a single day
- [x] Timeline shows scheduled times vs actual entry times
- [x] List of entries for selected day (empty state if no entries)
- [x] Tap entry to view/edit
- [x] Visual indicators for hydration levels (light/medium/dark yellow dots)
- [ ] Filter by: all, voids only, urges only, leaks only

### Statistics Screen
- [ ] Default view: Today for all stats except void frequency chart (defaults to 7 days)
- [ ] Time range picker: Today, 7 days, 30 days, custom range (max 1 year)
- [ ] Charts (custom Canvas): void frequency (per day, default 7d, stacked by pee color), volume distribution
- [ ] Hydration volume score chart (small=1, medium=2, large=3, summed per day; default Today)
- [ ] Daily hydration goal indicator (default 15, adjustable in Settings)
- [ ] Liquid intake chart (total liters over time; default Today)
- [ ] Liquid type distribution chart (water/sparkling/tea/coffee/milk/juice/custom)
- [x] Urine color distribution — shown via stacked bars in void frequency chart
- [ ] Schedule adherence rate (% of scheduled times completed)
- [ ] Average minutes early/late from scheduled times
- [ ] Urge-only count and frequency
- [ ] Kegel completion rate and streak
- [ ] Export CSV: Date, Time, EntryType, Volume, LeakAmount, Color, Urgency, ScheduledTime, MinutesFromSchedule, ActivityContext, LiquidType, LiquidAmount, LiquidUnit, LiquidLiters, Notes
- [ ] Export blocked during database write operations

> **Status:** Statistics screen is NOT YET IMPLEMENTED. All items above are pending.

### Settings Screen
- [ ] Notification preferences (void reminders on/off, exercise reminders on/off, system default sound)
- [ ] Missed schedule follow-up notification (optional, default OFF)
- [ ] Daily hydration volume goal (default 15, adjustable 5-30)
- [ ] Exercise configuration (types, sessions, sets, reps, hold time)
- [ ] Backup data (exports JSON to app-specific storage, writes to temp file then renames atomically)
- [ ] Backup includes schema version for future migration compatibility
- [ ] Restore data (imports JSON, replaces all existing data)
- [ ] Restore shows warning: "This will replace X entries from the last Y days"
- [ ] Clear all data option (two-step dialog confirmation)
- [ ] About/version info

### Notifications
- [ ] Void schedule reminders at exact scheduled times (AlarmManager, upcoming times only)
- [ ] No notifications sent for already-passed scheduled times
- [ ] Notification opens app (no quick actions)
- [ ] Kegel exercise daily reminder at user-configured time (WorkManager)
- [ ] Missed schedule follow-up: optional, default OFF, configurable delay
- [ ] Cancel and reschedule all pending alarms when schedule is modified
- [ ] Notifications may be skipped during battery optimization (acceptable)

---

## Screen Flows

### Home Screen

```
┌─────────────────────────────────────┐
│  LaNiao                    ⚙️       │
├─────────────────────────────────────┤
│                                     │
│  Today's Water Intake               │
│  ┌─────────┐┌─────────┐┌─────────┐ │
│  │ A Little││ Normal ✓││  A Lot  │ │
│  │ <1.8L   ││  ~2.7L  ││  >3.6L  │ │
│  └─────────┘└─────────┘└─────────┘ │
│                                     │
│  Today's Summary                    │
│  ┌─────────────────────────────────┐│
│  │ 🚽 5 voids  │ 📅 4/6 scheduled ││
│  │ 💪 2 urges  │ ⏰ 2 missed      ││
│  └─────────────────────────────────┘│
│                                     │
│  ⚠️ Missed Scheduled Times          │
│  ┌─────────────────────────────────┐│
│  │ 11:00 AM - No entry recorded    ││
│  │ 1:00 PM - No entry recorded     ││
│  └─────────────────────────────────┘│
│                                     │
│  Recent Entries                     │
│  ┌─────────────────────────────────┐│
│  │ 3:45 PM  🟡 Medium  @ 3:00 PM  ││
│  │ 12:10 PM 🟢 Small   Unscheduled││
│  │ 9:05 AM  🟡 Large   @ 9:00 AM  ││
│  └─────────────────────────────────┘│
│                                     │
│  Exercises Today                    │
│  ┌─────────────────────────────────┐│
│  │ ☐ Standard Kegel (1 of 2)      ││
│  │ ☐ Quick Kegel (1 of 1)         ││
│  └─────────────────────────────────┘│
│                                     │
│                          [ ➕ ]     │
├─────────────────────────────────────┤
│  🏠    📅    📊    ⚙️              │
└─────────────────────────────────────┘
```

### Add Entry Screen

```
┌─────────────────────────────────────┐
│  ← Add Entry                        │
├─────────────────────────────────────┤
│                                     │
│  Time                               │
│  ┌─────────────────────────────────┐│
│  │ 3:45 PM                    📅  ││
│  └─────────────────────────────────┘│
│                                     │
│  ☐ Urge only (didn't void)         │
│                                     │
│  Volume Size                        │
│  ┌───────┐ ┌───────┐ ┌───────┐     │
│  │ Small │ │Medium │ │ Large │     │
│  └───────┘ └───────┘ └───────┘     │
│                                     │
│  Color                              │
│  🔵 ⚪ 🟡 🟠 🟤 (color picker)     │
│                                     │
│  Urgency                            │
│  None | Low | Medium | High | Burst 🎈│
│                                     │
│  Scheduled Time                     │
│  ┌─────────────────────────────────┐│
│  │ 3:00 PM ✓              ▼      ││
│  └─────────────────────────────────┘│
│  Options: 1:00 PM, 3:00 PM ✓,      │
│           5:00 PM, Unscheduled      │
│                                     │
│  Notes (optional)                   │
│  ┌─────────────────────────────────┐│
│  │                                 ││
│  └─────────────────────────────────┘│
│                                     │
│  ┌─────────────────────────────────┐│
│  │           Save Entry            ││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
```

### Schedule Screen (Empty State)

```
┌─────────────────────────────────────┐
│  ← Schedule                         │
├─────────────────────────────────────┤
│                                     │
│                                     │
│         📅                          │
│                                     │
│    No active schedule               │
│                                     │
│    Create a voiding schedule to     │
│    help with bladder training.      │
│                                     │
│  ┌─────────────────────────────────┐│
│  │       Create Schedule           ││
│  └─────────────────────────────────┘│
│                                     │
└─────────────────────────────────────┘
```

### Schedule Screen (With Schedule)

```
┌─────────────────────────────────────┐
│  ← Schedule                         │
├─────────────────────────────────────┤
│                                     │
│  Active Schedule                    │
│  ┌─────────────────────────────────┐│
│  │ 9:00 AM - 9:00 PM               ││
│  │ Every 2 hours                   ││
│  │ Expires: Feb 14 (4 days left)   ││
│  │                                 ││
│  │ Times: 9:00, 11:00, 1:00,       ││
│  │        3:00, 5:00, 7:00, 9:00   ││
│  │                          [Edit] ││
│  └─────────────────────────────────┘│
│                                     │
│  Today's Progress                   │
│  ✅ 9:00 AM  - Completed 9:05 AM   │
│  🎈 11:00 AM - Completed 10:58 AM  │  <- burst urgency
│  ⚠️ 1:00 PM  - Missed              │
│  ⏳ 3:00 PM  - Upcoming            │
│  ○ 5:00 PM                         │
│  ○ 7:00 PM                         │
│  ○ 9:00 PM                         │
│                                     │
│  ┌─────────────────────────────────┐│
│  │      Create New Schedule        ││
│  └─────────────────────────────────┘│
│                                     │
└─────────────────────────────────────┘
```

### Create Schedule Dialog

```
┌─────────────────────────────────────┐
│  Create Schedule                    │
├─────────────────────────────────────┤
│                                     │
│  First void at                      │
│  ┌─────────────────────────────────┐│
│  │ 9:00 AM                    🕐  ││
│  └─────────────────────────────────┘│
│                                     │
│  Last void at                       │
│  ┌─────────────────────────────────┐│
│  │ 9:00 PM                    🕐  ││
│  └─────────────────────────────────┘│
│                                     │
│  Interval (starting at 1.5h, +15min)  │
│  ┌─────┐┌─────┐┌────┐┌─────┐┌────┐   │
│  │1.5h ││1.75h││ 2h ││2.25h││2.5h│...│
│  └─────┘└─────┘└────┘└─────┘└────┘   │
│                                     │
│  Active for                         │
│  ┌────┐ ┌────┐ ┌────┐ ┌──────┐    │
│  │ 7d │ │14d │ │30d │ │Custom│    │
│  └────┘ └────┘ └────┘ └──────┘    │
│                                     │
│  Preview                            │
│  9:00, 11:00, 1:00, 3:00, 5:00,    │
│  7:00, 9:00 (7 times/day)          │
│  Expires: Feb 17, 2026              │
│                                     │
│  ┌───────────┐ ┌───────────────┐   │
│  │  Cancel   │ │     Save      │   │
│  └───────────┘ └───────────────┘   │
└─────────────────────────────────────┘
```

### Statistics Screen

```
┌─────────────────────────────────────┐
│  ← Statistics              [Export] │
├─────────────────────────────────────┤
│                                     │
│  ┌───────┐┌───────┐┌───────┐┌─────┐│
│  │ Today ││ 7 Day ││ 30 Day││Custom│
│  └───────┘└───────┘└───────┘└─────┘│
│                                     │
│  Void Frequency (7 Day Default)     │
│  ┌─────────────────────────────────┐│
│  │     📊 Bar chart               ││
│  │     (voids per day)           ││
│  └─────────────────────────────────┘│
│                                     │
│  Summary                            │
│  ┌─────────────────────────────────┐│
│  │ Total voids: 6                  ││
│  │ Scheduled: 4 (67%)              ││
│  │ Unscheduled: 2                  ││
│  │ Urge only: 1                    ││
│  │ Avg early/late: +3 min          ││
│  └─────────────────────────────────┘│
│                                     │
│  (Color distribution shown in void  │
│   frequency chart stacked bars)      │
│                                     │
│  Daily Hydration Score              │
│  ┌─────────────────────────────────┐│
│  │  📊 Bar chart (per day)        ││
│  │  Score: S=1, M=2, L=3          ││
│  │  Goal line at 15 (adjustable)  ││
│  └─────────────────────────────────┘│
│                                     │
│  Kegel Streak: 12 days 🔥          │
│                                     │
└─────────────────────────────────────┘
```

### History Screen - Day Timeline View

```
┌─────────────────────────────────────┐
│  ← History        Feb 10, 2026      │
├─────────────────────────────────────┤
│  ┌─────────┐ ┌──────────┐          │
│  │ Calendar│ │ Timeline │          │
│  └─────────┘ └──────────┘          │
├─────────────────────────────────────┤
│                                     │
│  6 AM ─────────────────────────     │
│        │                            │
│  7 AM ─┤                            │
│        │                            │
│  8 AM ─┤                            │
│        │                            │
│  9 AM ─●──── Scheduled              │
│        │  ◉ 9:05 AM  🟡 Medium     │
│        │     (+5 min)               │
│        │                            │
│ 10 AM ─┤                            │
│        │                            │
│ 11 AM ─●──── Scheduled              │
│        │  ○ MISSED                  │
│        │                            │
│ 12 PM ─┤                            │
│        │  ◉ 12:10 PM 🟢 Small      │
│        │     (Unscheduled)          │
│        │                            │
│        │  💪 12:45 PM Urge resisted │
│        │                            │
│  1 PM ─●──── Scheduled              │
│        │  ○ MISSED                  │
│        │                            │
│  2 PM ─┤     ┌─────────────┐       │
│        │     │  2hr 35min  │       │
│        │     │    gap      │       │
│  3 PM ─●─────┴─────────────┴────   │
│        │  ◉ 3:45 PM  🟡 Large      │
│        │     (+45 min)              │
│        │                            │
│  4 PM ─┤                            │
│        │                            │
│  5 PM ─●──── Scheduled (upcoming)   │
│        │                            │
└─────────────────────────────────────┘

Legend:
● Scheduled time    ◉ Entry logged
○ Missed           � Urge resisted
�🟢🟡🟠 Color indicator
```

---

## Acceptance Criteria

### AC1: Quick Add Entry
- **Given** the user is on the Home screen
- **When** they tap the Quick Add button
- **Then** an entry is saved with current timestamp
- **And** all fields default to UNKNOWN
- **And** scheduledTime is null (unscheduled)

### AC1.5: Leak-Only Entry
- **Given** the user is adding an entry
- **When** they select "Leak-only"
- **Then** they can choose leak amount (Small/Medium/Large)
- **And** scheduled time selection is disabled
- **And** entry is saved as a leak-only event

### AC1.5b: Leak on Void Entry
- **Given** the user is adding or editing a void entry
- **When** they toggle "Had a leak" and select a leak amount
- **Then** the entry is saved as a void with the leak amount recorded
- **And** volume, color, and scheduled time remain available

### AC1.6: Activity Context
- **Given** the user is adding or editing a void, urge-only, or leak-only entry
- **When** they enter activity context text (e.g., "running", "arriving home")
- **Then** the text is saved and shown with the entry details

### AC1.7: Liquid Intake Logging and Daily Liters
- **Given** the user is on the Home screen
- **When** they add liquid intake entries using type + amount + unit (oz/ml/cups)
- **Then** each entry is converted to liters
- **And** today's total liters is displayed as the sum of converted entries
- **And** supported types include Water, Sparkling water, Tea, Coffee, Milk, Juice, and Custom

### AC2: Edit Past Entry
- **Given** the user has a saved entry
- **When** they tap on it from Home or History
- **Then** they can modify any field
- **And** save changes to the database

### AC3: Missed Schedule Alert
- **Given** the user has an active schedule with time 11:00 AM
- **When** it's past 11:00 AM and no entry is associated with that time
- **Then** Home screen shows "11:00 AM - No entry recorded" in missed section

### AC4: Schedule Association
- **Given** the user logs an entry at 11:05 AM
- **And** schedule times are [9:00, 11:00, 1:00]
- **And** 9:00 AM already has an associated entry
- **When** the Add Entry screen loads
- **Then** 11:00 AM is pre-selected (nearest unclaimed)
- **And** dropdown shows only unclaimed times plus "Unscheduled"
- **And** 9:00 AM is not shown (already claimed)

### AC5: Non-Overlapping Schedules
- **Given** an active schedule from 9 AM - 5 PM
- **When** user creates new schedule from 3 PM - 9 PM
- **Then** system detects overlap
- **And** prompts "This overlaps with existing schedule. Replace it?"
- **And** user can confirm or cancel

### AC6: Exercise Configuration
- **Given** the user is in Settings > Exercise Configuration
- **When** they enable STANDARD Kegel with 2 sessions/day
- **Then** Home screen shows 2 uncompleted STANDARD exercises
- **And** completing one shows 1 remaining

### AC7: Statistics Export
- **Given** the user is on Statistics screen
- **When** they tap Export button
- **Then** they can export data as CSV
- **And** select date range for export
- **And** CSV opens in spreadsheet apps for sharing with doctors

### AC8: Notification Opens App
- **Given** a void reminder notification appears
- **When** user taps it
- **Then** app opens to Home screen
- **And** no quick actions are shown on notification

### AC9: Missed Schedule Follow-up (Optional)
- **Given** user has enabled "Follow-up for missed schedules" in Settings
- **And** scheduled time 11:00 AM passes with no entry
- **When** configured delay (e.g., 30 min) passes
- **Then** follow-up notification is sent
- **But** if setting is OFF (default), no follow-up is sent

### AC10: Backup & Restore
- **Given** user taps "Backup data" in Settings
- **When** backup completes
- **Then** a JSON file is saved to device storage
- **And** file contains all entries, schedules, exercise configs, and water intake records
- **And** user sees confirmation with file location
---
- **Given** user taps "Restore data" and selects a valid backup file
- **When** restore completes
- **Then** all data is replaced with backup contents
- **And** user sees confirmation of restored record counts

---

## Non-Functional Requirements

- Visual feedback (ripple/highlight) within 100ms of tap; navigation complete within 300ms
- All database operations run on background thread; UI frame rate stays ≥30fps during DB writes
- All times stored as UTC internally, converted to device local time for display
- Timezone/DST changes: Schedule times recalculate based on current device local time
- Data retention: Keep all data forever (no auto-deletion)
- Midnight (00:00) belongs to the new day
- Support Android 8.0+ (API 26+)
- Work offline (all data local)
- Respect system dark mode
- Accessible color contrast (WCAG AA)
- Chart animations maintain ≥55fps average on supported devices

---

## Out of Scope (v1.0)

- Cloud sync / backup to server
- Multi-device sync
- Sharing entries with other users
- Integration with health apps (Google Fit, etc.)
- Wearable (WearOS) companion app
- Widgets
- Multiple user profiles
- App password / PIN lock / biometric authentication
