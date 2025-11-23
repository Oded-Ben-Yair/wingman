# Wingman Android - Quick Start Testing Guide

## ⚡ 5-Minute Setup

### Step 1: Get the Code
```bash
git clone https://github.com/Oded-Ben-Yair/Flirrt-screens-shots-v1.git
cd Flirrt-screens-shots-v1
git checkout feature/android-native-app
cd android
```

### Step 2: Build APK

**Option A: Android Studio (Easiest)**
1. Open Android Studio
2. Open the `android` folder
3. Click **Build** → **Build APK(s)**
4. APK: `app/build/outputs/apk/debug/app-debug.apk`

**Option B: Command Line**
```bash
export ANDROID_HOME=/path/to/android-sdk
./build-apk.sh
```

### Step 3: Install on Phone
```bash
# Enable USB Debugging on phone first
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Step 4: Test (2 minutes)
1. Open Wingman app
2. Complete onboarding (grant all permissions)
3. Take a screenshot in any app
4. Wait 10 seconds
5. Open Wingman app → See suggestions
6. Open messaging app → Switch to Wingman keyboard
7. Tap a suggestion → Text inserts ✅

---

## 🐛 Troubleshooting

**Build fails?**
- Ensure Java 17 is installed
- Set ANDROID_HOME environment variable

**Screenshots not detected?**
- Check notification is visible
- Verify storage permission granted
- Wait 10 seconds after screenshot

**Keyboard not showing?**
- Go to Settings → Keyboard
- Enable "Wingman"
- Long-press spacebar to switch

---

## 📁 Important Files

- **BUILD_AND_INSTALL_GUIDE.md** - Detailed instructions
- **FINAL_DELIVERY_REPORT.md** - Complete project summary
- **README.md** - Project overview

---

## ✅ What's Working

✅ Automatic screenshot detection  
✅ Backend API integration  
✅ Custom keyboard with suggestions  
✅ Onboarding flow  
✅ Settings management  

---

## 🎯 Test These Features

1. **Screenshot Detection**
   - Take 5 screenshots in different apps
   - Verify all are detected (check Wingman app)

2. **Keyboard**
   - Switch to Wingman keyboard
   - Tap suggestions to insert text
   - Type manually with QWERTY layout

3. **Settings**
   - Stop/start screenshot detection service
   - Clear suggestions
   - Start new conversation

---

## 📞 Need Help?

Check `android/BUILD_AND_INSTALL_GUIDE.md` for detailed troubleshooting.

---

**Status:** ✅ Ready for Testing  
**Version:** 1.0.0 (Build 1)  
**Last Updated:** November 23, 2025
