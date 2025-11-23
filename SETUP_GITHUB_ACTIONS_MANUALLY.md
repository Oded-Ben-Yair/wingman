# Setup GitHub Actions Manually (2 Minutes)

## ⚠️ Why Manual Setup?

GitHub security prevents automated workflow creation. You need to add the workflow file manually through the GitHub web interface.

---

## 🚀 Quick Setup (2 Minutes)

### Step 1: Go to Your Repository

Open: https://github.com/Oded-Ben-Yair/Flirrt-screens-shots-v1

### Step 2: Switch to Your Branch

1. Click the branch dropdown (currently shows "main")
2. Select **feature/android-native-app**

### Step 3: Create Workflow Directory

1. Click **Add file** → **Create new file**
2. In the filename box, type: `.github/workflows/build-apk.yml`
3. GitHub will automatically create the directories

### Step 4: Paste Workflow Content

Copy and paste this entire content into the file:

```yaml
name: Build Wingman Android APK

env:
  main_project_module: app
  playstore_name: Wingman

on:
  push:
    branches: [ feature/android-native-app, main, master ]
  pull_request:
    branches: [ feature/android-native-app, main, master ]
  
  # Allows manual trigger from Actions tab
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set current date
        run: echo "date_today=$(date +'%Y-%m-%d')" >> $GITHUB_ENV

      - name: Set repository name
        run: echo "repository_name=$(echo '${{ github.repository }}' | awk -F '/' '{print $2}')" >> $GITHUB_ENV

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'zulu'
          java-version: '17'
          cache: 'gradle'

      - name: Grant execute permission for gradlew
        run: chmod +x ./android/gradlew

      - name: Build debug APK
        run: cd android && ./gradlew assembleDebug

      - name: Build release APK
        run: cd android && ./gradlew assembleRelease

      - name: Upload Debug APK
        uses: actions/upload-artifact@v4
        with:
          name: ${{ env.date_today }} - Wingman-Android-Debug-APK
          path: android/app/build/outputs/apk/debug/*.apk

      - name: Upload Release APK
        uses: actions/upload-artifact@v4
        with:
          name: ${{ env.date_today }} - Wingman-Android-Release-APK
          path: android/app/build/outputs/apk/release/*.apk
```

### Step 5: Commit the File

1. Scroll down to **Commit new file**
2. Commit message: `ci: Add GitHub Actions workflow for APK build`
3. Select **Commit directly to the feature/android-native-app branch**
4. Click **Commit new file**

---

## ✅ Verify It's Working

### Check Workflow Runs

1. Go to **Actions** tab in your repository
2. You should see **Build Wingman Android APK** workflow
3. It should start running automatically (triggered by the commit)
4. Wait 5-10 minutes for completion

### Download APK

1. Click on the completed workflow run
2. Scroll down to **Artifacts** section
3. Download **Wingman-Android-Debug-APK**
4. Extract the ZIP file
5. You'll find `app-debug.apk` inside

---

## 📱 Install on Phone

```bash
# Enable USB Debugging on your phone
# Connect via USB

adb install app-debug.apk
```

Or transfer the APK to your phone and install directly.

---

## 🔄 Future Builds

After this one-time setup, every time you push code to `feature/android-native-app`, GitHub Actions will automatically build a new APK!

**Manual trigger:**
1. Go to **Actions** tab
2. Select **Build Wingman Android APK**
3. Click **Run workflow**
4. Select branch: **feature/android-native-app**
5. Click **Run workflow**

---

## 🎯 What Happens Next

1. ✅ Workflow builds APK automatically
2. ✅ APK available in 5-10 minutes
3. ✅ Download from Actions → Artifacts
4. ✅ Install on phone and test
5. ✅ Make UX/UI improvements
6. ✅ Push changes → New APK builds automatically
7. ✅ Iterate quickly!

---

**This is the fastest way to get a working APK for testing!** 🚀
