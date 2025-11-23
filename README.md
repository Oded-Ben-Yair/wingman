# Wingman - AI Dating Assistant (Android)

**Automatic screenshot detection + AI-powered conversation suggestions**

---

## 🎯 What is Wingman?

Wingman is a native Android app that automatically detects when you take screenshots of dating app conversations, analyzes them using AI (GPT-4O + Grok-4), and provides smart reply suggestions directly in your keyboard.

**The feature that was impossible on iOS now works on Android!**

---

## ✨ Key Features

- ✅ **Automatic Screenshot Detection** - Monitors MediaStore for new screenshots
- ✅ **AI-Powered Suggestions** - GPT-4O analysis + Grok-4 generation  
- ✅ **Custom Keyboard** - Suggestions appear right in your keyboard
- ✅ **Multiple Tones** - Flirty, funny, thoughtful, direct
- ✅ **Privacy-First** - Images processed securely, not stored permanently

---

## 🚀 Quick Start

### Get the APK

**Option 1: Download from GitHub Actions (Recommended)**
1. Go to [Actions tab](https://github.com/Oded-Ben-Yair/wingman/actions)
2. Click latest workflow run
3. Download **Wingman-Android-Debug-APK** artifact
4. Extract ZIP and install `app-debug.apk`

**Option 2: Build Locally**
```bash
git clone https://github.com/Oded-Ben-Yair/wingman.git
cd wingman/android
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

### Install on Phone

```bash
adb install app-debug.apk
```

Or transfer APK to phone and install directly.

---

## 📱 How to Use

### 1. Complete Onboarding
- Grant all permissions (storage, notifications)
- Enable Wingman keyboard in system settings
- Start screenshot detection service

### 2. Take Screenshots
- Open any dating app (Tinder, Bumble, Hinge, etc.)
- Take screenshot of conversation
- Wait 10 seconds for AI processing

### 3. Use Suggestions
- Open messaging app
- Switch to Wingman keyboard (long-press spacebar)
- Tap suggestion to insert text

---

## 🛠️ Tech Stack

- **Language:** Kotlin 1.9.20
- **UI:** Jetpack Compose (Material 3)
- **Architecture:** MVVM + Clean Architecture
- **DI:** Hilt 2.48
- **Networking:** Retrofit 2.9.0 + Moshi 1.15.0
- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 34 (Android 14)

---

## 📚 Documentation

- **[COMPLETE_APK_BUILD_SOLUTION.md](COMPLETE_APK_BUILD_SOLUTION.md)** - Complete build and testing guide
- **[SETUP_GITHUB_ACTIONS_MANUALLY.md](SETUP_GITHUB_ACTIONS_MANUALLY.md)** - 2-minute workflow setup
- **[UX_UI_IMPROVEMENT_PLAN.md](UX_UI_IMPROVEMENT_PLAN.md)** - Testing and improvement framework
- **[GITHUB_ACTIONS_APK_BUILD_GUIDE.md](GITHUB_ACTIONS_APK_BUILD_GUIDE.md)** - Detailed build guide
- **[QUICK_START_TESTING.md](QUICK_START_TESTING.md)** - Fast-track testing

---

## 🏗️ Project Structure

```
android/
├── app/
│   ├── src/main/java/com/wingman/app/
│   │   ├── data/           # API, models, repositories
│   │   ├── domain/         # Business logic (future)
│   │   ├── presentation/   # UI (Compose screens)
│   │   ├── service/        # Screenshot detection service
│   │   ├── keyboard/       # Custom keyboard (IME)
│   │   ├── util/           # Helpers, managers
│   │   └── di/             # Dependency injection
│   ├── src/main/res/       # Resources (strings, themes, icons)
│   └── build.gradle.kts    # App dependencies
├── build.gradle.kts        # Project config
├── settings.gradle.kts     # Module config
└── gradle/                 # Gradle wrapper
```

---

## 🔧 Development

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17
- Android SDK 34

### Build Commands

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Install on connected device
./gradlew installDebug
```

---

## 🧪 Testing

See **[UX_UI_IMPROVEMENT_PLAN.md](UX_UI_IMPROVEMENT_PLAN.md)** for comprehensive testing checklist.

### Quick Test
1. Install APK
2. Complete onboarding
3. Take screenshot in any app
4. Open Wingman → See suggestions
5. Open keyboard → Tap suggestion

---

## 🎨 UX/UI Improvement

The app is functional but needs design polish. Follow the improvement workflow:

1. **Test** → Document issues
2. **Prioritize** → Critical, medium, low
3. **Design** → Sketch solutions
4. **Implement** → Update Compose UI
5. **Build** → GitHub Actions (automatic)
6. **Test** → Download new APK
7. **Repeat** → Until satisfied

---

## 📊 Status

**Completion:** 71% (5 of 7 phases)

✅ Phase 1: Project Setup  
✅ Phase 2: Backend Integration  
✅ Phase 3: Screenshot Detection  
✅ Phase 4: Custom Keyboard  
✅ Phase 5: Onboarding & Settings  
⏳ Phase 6: Testing & QA  
⏳ Phase 7: Play Store Deployment  

---

## 🚀 Roadmap

### Short-Term
- [ ] Polish UX/UI design
- [ ] Test on 5+ devices
- [ ] Optimize performance
- [ ] Add unit tests

### Medium-Term
- [ ] Improve detection rate (75%+ → 90%+)
- [ ] Add conversation context
- [ ] Add tone customization
- [ ] Add suggestion history

### Long-Term
- [ ] Play Store submission
- [ ] Beta testing program
- [ ] Analytics integration
- [ ] Multi-language support

---

## 🤝 Contributing

This is a private project. For access, contact the repository owner.

---

## 📄 License

Proprietary - All rights reserved

---

## 📞 Support

For issues or questions, create an issue in this repository.

---

**Built with ❤️ by Manus AI Agent**  
**Version:** 1.0.0 (Build 1)  
**Date:** November 23, 2025
