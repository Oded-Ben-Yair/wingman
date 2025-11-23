# Wingman Android

> AI-Powered Dating Assistant - Native Android App

## 🚀 Quick Start

```bash
# Clone the repository
git clone https://github.com/Oded-Ben-Yair/Flirrt-screens-shots-v1.git
cd Flirrt-screens-shots-v1
git checkout feature/android-native-app
cd android

# Build APK (requires Android Studio or Android SDK)
./build-apk.sh

# Install on connected device
adb install app/build/outputs/apk/debug/app-debug.apk
```

**📖 For detailed build instructions, see [BUILD_AND_INSTALL_GUIDE.md](BUILD_AND_INSTALL_GUIDE.md)**

---

## 📱 What is Wingman?

Wingman is an AI-powered dating assistant that helps you craft the perfect messages in dating apps like Tinder, Bumble, and Hinge.

**Key Features:**
- 📸 **Automatic Screenshot Detection** - Detects when you take a screenshot in dating apps
- 🤖 **AI-Powered Suggestions** - Generates flirty, playful, and engaging message suggestions
- ⌨️ **Custom Keyboard** - Access suggestions directly from your keyboard
- 🔥 **Multiple Tones** - Playful, flirty, curious, bold, and more
- 💬 **Conversation Context** - Maintains session for better suggestions

---

## 🎯 Development Status

**Current Phase:** 5 of 7 (71% Complete)

✅ **Phase 1:** Project Setup & Foundation  
✅ **Phase 2:** Backend Integration  
✅ **Phase 3:** Automatic Screenshot Detection  
✅ **Phase 4:** Custom Keyboard (IME)  
✅ **Phase 5:** Onboarding & Settings  
🔄 **Phase 6:** Testing & QA (Next)  
⏳ **Phase 7:** Deployment & Play Store Submission  

---

## 🏗️ Architecture

**Technology Stack:**
- **Language:** Kotlin 1.9.20
- **UI Framework:** Jetpack Compose (Material 3)
- **Architecture:** MVVM + Clean Architecture
- **Dependency Injection:** Hilt 2.48
- **Networking:** Retrofit 2.9.0 + Moshi 1.15.0
- **Async:** Kotlin Coroutines + Flow
- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 34 (Android 14)

---

## 📚 Documentation

- [BUILD_AND_INSTALL_GUIDE.md](BUILD_AND_INSTALL_GUIDE.md) - Comprehensive build instructions
- [PROJECT_PROGRESS_SUMMARY.md](PROJECT_PROGRESS_SUMMARY.md) - Overall project status
- [CHECKPOINT_1_PHASE_1_COMPLETE.md](CHECKPOINT_1_PHASE_1_COMPLETE.md) - Phase 1 report
- [CHECKPOINT_2_PHASE_2_COMPLETE.md](CHECKPOINT_2_PHASE_2_COMPLETE.md) - Phase 2 report
- [CHECKPOINT_3_PHASE_3_COMPLETE.md](CHECKPOINT_3_PHASE_3_COMPLETE.md) - Phase 3 report

---

**Built with ❤️ by Manus AI Agent**  
**Version:** 1.0.0 (Build 1)  
**Status:** ✅ Ready for Testing
