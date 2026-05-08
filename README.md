# LaNiao 🐦

A personal bladder health tracking app for Android, built with Kotlin and Jetpack Compose.

Track urination patterns, hydration, pelvic floor exercises, and void schedules — all stored locally on your device.

## Download & Install

### Option 1: Build from Source (Recommended)

**Prerequisites:**
- [Android Studio](https://developer.android.com/studio) (Ladybug or newer)
- JDK 17+ (bundled with Android Studio)
- Android SDK with API 26+ (Android 8.0+)

**Steps:**

1. **Clone the repository**
   ```bash
   git clone https://github.com/studioghibli/la-niao.git
   cd la-niao
   ```

2. **Open in Android Studio**
   - Open Android Studio → **File → Open** → select the `app/` directory
   - Wait for Gradle sync to complete

3. **Build the APK**
   ```bash
   cd app
   ./gradlew assembleDebug
   ```
   The APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

4. **Install on your device**
   - **Via USB:** Connect your phone, enable USB debugging, then:
     ```bash
     adb install app/build/outputs/apk/debug/app-debug.apk
     ```
   - **Via file transfer:** Copy `app-debug.apk` to your phone and open it
     (you may need to enable "Install from unknown sources" in Settings)

### Option 2: Build from Command Line (no Android Studio)

1. Ensure `JAVA_HOME` points to a JDK 17+ installation
2. Ensure `ANDROID_HOME` points to your Android SDK
3. Run:
   ```bash
   cd la-niao/app
   ./gradlew assembleDebug    # Linux/macOS
   .\gradlew.bat assembleDebug # Windows
   ```
4. Install the APK from `app/build/outputs/apk/debug/app-debug.apk`

### Option 3: Run on Emulator

1. Open the project in Android Studio
2. Create an AVD (Android Virtual Device) with API 26+
3. Click **Run ▶** or use:
   ```bash
   ./gradlew installDebug
   ```

## Requirements

- **Android 8.0+** (API 26)
- ~50 MB storage
- No internet connection required — all data is stored locally

## Features

- 📋 **Void Tracking** — Log voids, urges, and leaks with volume, color, and urgency
- 🗓️ **Void Scheduling** — Set up timed voiding schedules for bladder training
- 💪 **Pelvic Floor Exercises** — Track Kegel and relaxation exercises with daily goals
- 🥤 **Drink Tracking** — Log water and other fluid intake
- 📊 **Statistics** — Charts for void frequency, schedule adherence, hydration, and exercise completion
- 📅 **Calendar Views** — Month view, day timeline, and chronological list
- 💾 **Backup & Restore** — JSON backup and CSV export for sharing with healthcare providers
- 🔒 **Privacy** — All data stored locally, no cloud sync, no analytics

## Tech Stack

- Kotlin 1.9+ / Jetpack Compose / Material 3
- MVVM + Clean Architecture
- Room (SQLite) / Hilt / Coroutines + Flow

## Privacy

LaNiao stores all data locally on your device. No data is sent to any server. You can export or delete your data at any time from the Settings screen.
