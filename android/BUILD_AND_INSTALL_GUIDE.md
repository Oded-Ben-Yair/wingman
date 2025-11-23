# Wingman Android - Build & Installation Guide

## Quick Start (Recommended)

### Option 1: Build with Android Studio (Easiest)

1. **Install Android Studio:**
   - Download from: https://developer.android.com/studio
   - Install and complete the setup wizard
   - Ensure Android SDK 34 is installed

2. **Clone the Repository:**
   ```bash
   git clone https://github.com/Oded-Ben-Yair/Flirrt-screens-shots-v1.git
   cd Flirrt-screens-shots-v1
   git checkout feature/android-native-app
   ```

3. **Open Project:**
   - Launch Android Studio
   - Click "Open" and select the `android` folder
   - Wait for Gradle sync to complete

4. **Build APK:**
   - Click **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
   - Wait for build to complete (~2-5 minutes first time)
   - APK location: `android/app/build/outputs/apk/debug/app-debug.apk`

5. **Install on Phone:**
   - Connect your Android phone via USB
   - Enable "USB Debugging" in Developer Options
   - Click the green "Run" button in Android Studio
   - OR manually install: `adb install app-debug.apk`

---

### Option 2: Build with Command Line

**Prerequisites:**
- Java 17 (OpenJDK)
- Android SDK Command Line Tools

**Steps:**

1. **Install Java 17:**
   ```bash
   # Ubuntu/Debian
   sudo apt-get update
   sudo apt-get install openjdk-17-jdk
   
   # macOS
   brew install openjdk@17
   
   # Windows
   # Download from: https://adoptium.net/
   ```

2. **Install Android SDK:**
   ```bash
   # Download command-line tools
   wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
   unzip commandlinetools-linux-11076708_latest.zip
   mkdir -p ~/android-sdk/cmdline-tools/latest
   mv cmdline-tools/* ~/android-sdk/cmdline-tools/latest/
   
   # Set environment variables
   export ANDROID_HOME=~/android-sdk
   export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
   export PATH=$PATH:$ANDROID_HOME/platform-tools
   
   # Accept licenses
   yes | sdkmanager --licenses
   
   # Install required SDK components
   sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
   ```

3. **Clone and Build:**
   ```bash
   git clone https://github.com/Oded-Ben-Yair/Flirrt-screens-shots-v1.git
   cd Flirrt-screens-shots-v1
   git checkout feature/android-native-app
   cd android
   
   # Build debug APK
   ./gradlew assembleDebug
   
   # APK location: app/build/outputs/apk/debug/app-debug.apk
   ```

4. **Install on Phone:**
   ```bash
   # Connect phone via USB with USB Debugging enabled
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

---

## After Installation

### 1. Grant Permissions

When you first open Wingman, you'll go through onboarding:

1. **Welcome Screen** - Tap "Get Started"
2. **Permissions Screen** - Tap "Grant Permissions"
   - Allow "Storage Access" (for screenshot detection)
   - Allow "Notifications" (for background service)
   - Disable "Battery Optimization" (for reliable service)
3. **Keyboard Setup** - Tap "Open Keyboard Settings"
   - Find "Wingman" in the list
   - Toggle it ON
   - Tap back and then "I've enabled it"
4. **Complete** - Tap "Start Using Wingman"

### 2. Test Screenshot Detection

1. **Open Wingman app** - Service should start automatically
2. **Open any app** (e.g., Chrome, Gallery)
3. **Take a screenshot** - Press Power + Volume Down
4. **Wait 7-10 seconds** - Wingman detects and uploads
5. **Check Wingman app** - Suggestions should appear on home screen

### 3. Use the Keyboard

1. **Open a messaging app** (e.g., WhatsApp, Tinder)
2. **Tap a text field** to bring up keyboard
3. **Long-press the spacebar** or tap the keyboard switcher icon
4. **Select "Wingman"** from the list
5. **Tap a suggestion chip** to insert it into the text field

---

## Troubleshooting

### Build Errors

**Error: "SDK location not found"**
```bash
# Create local.properties file
echo "sdk.dir=/path/to/your/android-sdk" > android/local.properties
```

**Error: "Java version mismatch"**
```bash
# Check Java version
java -version  # Should be 17.x.x

# Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
```

**Error: "Gradle sync failed"**
```bash
# Clean and rebuild
cd android
./gradlew clean
./gradlew assembleDebug
```

### Runtime Errors

**Screenshots not detected:**
1. Check if service is running (notification should be visible)
2. Go to Settings → Enable "Screenshot Detection"
3. Verify storage permission is granted
4. Check Logcat for errors: `adb logcat | grep Wingman`

**Keyboard not showing suggestions:**
1. Take a screenshot first (service needs to process it)
2. Wait 7-10 seconds for upload to complete
3. Open Wingman app to verify suggestions are saved
4. Switch to Wingman keyboard

**Service stops after phone sleep:**
1. Go to Settings → Battery Optimization
2. Find Wingman and disable optimization
3. Some manufacturers (Xiaomi, Huawei) have additional battery restrictions - disable those too

---

## Development Build vs Release Build

### Debug Build (Current)
- **File:** `app-debug.apk`
- **Signing:** Debug keystore (auto-generated)
- **Installable:** Yes, on any device with USB debugging
- **Play Store:** No, cannot be uploaded
- **Size:** ~15-20 MB

### Release Build (For Play Store)
- **File:** `app-release.aab` (Android App Bundle)
- **Signing:** Requires release keystore
- **Installable:** Only after signing
- **Play Store:** Yes, required for submission
- **Size:** ~10-12 MB (optimized)

**To create a release build:**
```bash
# Generate release keystore (one-time)
keytool -genkey -v -keystore wingman-release.keystore \
  -alias wingman -keyalg RSA -keysize 2048 -validity 10000

# Build signed release AAB
./gradlew bundleRelease

# Sign the AAB
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore wingman-release.keystore \
  app/build/outputs/bundle/release/app-release.aab wingman
```

---

## Testing Checklist

### Functional Testing

- [ ] App installs successfully
- [ ] Onboarding flow completes without errors
- [ ] All permissions granted
- [ ] Keyboard appears in keyboard list
- [ ] Service starts and shows notification
- [ ] Screenshot detection works (test 5 screenshots)
- [ ] Suggestions appear in Wingman app
- [ ] Keyboard shows suggestions
- [ ] Tapping suggestion inserts text
- [ ] Manual keyboard typing works
- [ ] Settings screen opens
- [ ] Service can be stopped/started from settings

### Compatibility Testing

Test on multiple devices:
- [ ] Samsung Galaxy (OneUI)
- [ ] Google Pixel (Stock Android)
- [ ] Xiaomi (MIUI)
- [ ] OnePlus (OxygenOS)
- [ ] Budget device (< $200)

Test on multiple Android versions:
- [ ] Android 8.0 (API 26) - Minimum
- [ ] Android 10 (API 29)
- [ ] Android 12 (API 31)
- [ ] Android 13 (API 33)
- [ ] Android 14 (API 34) - Target

### Performance Testing

- [ ] Battery drain < 5% per day
- [ ] Memory usage < 100 MB
- [ ] Screenshot detection latency < 2 seconds
- [ ] API response time < 10 seconds
- [ ] No crashes after 24 hours

---

## Next Steps

### Phase 6: Testing & QA
1. Fix any critical bugs found during testing
2. Write unit tests for business logic
3. Profile memory and battery usage
4. Test on 5+ physical devices
5. Achieve 99%+ crash-free rate

### Phase 7: Deployment
1. Create Play Store listing
2. Generate signed release AAB
3. Submit to Google Play Console
4. Configure Firebase Crashlytics
5. Monitor analytics and crashes

---

## Support

If you encounter issues:

1. **Check Logcat:**
   ```bash
   adb logcat | grep -E "Wingman|Screenshot|Keyboard"
   ```

2. **Clear App Data:**
   ```bash
   adb shell pm clear com.wingman.app
   ```

3. **Reinstall:**
   ```bash
   adb uninstall com.wingman.app
   adb install app-debug.apk
   ```

4. **Report Issues:**
   - Create a GitHub issue with:
     - Device model and Android version
     - Steps to reproduce
     - Logcat output
     - Expected vs actual behavior

---

## File Locations

- **Source Code:** `android/app/src/main/java/com/wingman/app/`
- **Resources:** `android/app/src/main/res/`
- **Gradle Config:** `android/app/build.gradle.kts`
- **Manifest:** `android/app/src/main/AndroidManifest.xml`
- **Debug APK:** `android/app/build/outputs/apk/debug/app-debug.apk`
- **Release AAB:** `android/app/build/outputs/bundle/release/app-release.aab`

---

**Built with ❤️ by Manus AI Agent**  
**Project:** Wingman - AI-Powered Dating Assistant  
**Platform:** Native Android (Kotlin + Jetpack Compose)  
**Status:** Phase 5 Complete (71% Done)
