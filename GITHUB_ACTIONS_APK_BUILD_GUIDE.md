# GitHub Actions APK Build Guide

## 🎯 Solution: Automatic APK Building with GitHub Actions

This guide shows you how to automatically build the Wingman Android APK using GitHub Actions - **no local Android SDK installation required!**

---

## ✅ What's Been Set Up

I've created a GitHub Actions workflow that will:
1. ✅ Automatically build APK when you push code
2. ✅ Build both Debug and Release APKs
3. ✅ Upload APKs as downloadable artifacts
4. ✅ Run on free GitHub infrastructure
5. ✅ Complete in 5-10 minutes

**Workflow file location:** `.github/workflows/build-apk.yml`

---

## 🚀 How to Use (3 Steps)

### Step 1: Push the Workflow to GitHub

The workflow file is already created. Just commit and push it:

```bash
cd /path/to/wingman-android
git add .github/workflows/build-apk.yml
git commit -m "ci: Add GitHub Actions workflow for APK build"
git push origin feature/android-native-app
```

### Step 2: Trigger the Build

**Option A: Automatic (Recommended)**
- Just push any code change to the `feature/android-native-app` branch
- GitHub Actions will automatically start building

**Option B: Manual Trigger**
1. Go to your GitHub repository
2. Click the **Actions** tab
3. Select **Build Wingman Android APK** workflow
4. Click **Run workflow** button
5. Select branch: `feature/android-native-app`
6. Click **Run workflow**

### Step 3: Download the APK

1. Wait 5-10 minutes for build to complete
2. Go to **Actions** tab on GitHub
3. Click on the latest workflow run
4. Scroll down to **Artifacts** section
5. Download **Wingman-Android-Debug-APK** or **Wingman-Android-Release-APK**
6. Extract the ZIP file
7. Install the APK on your phone: `adb install app-debug.apk`

---

## 📱 Install APK on Your Phone

### Method 1: Via ADB (Recommended)

```bash
# Enable USB Debugging on your phone
# Connect phone via USB

# Install the APK
adb install app-debug.apk
```

### Method 2: Direct Install on Phone

1. Download the APK artifact ZIP from GitHub Actions
2. Extract the APK file
3. Transfer APK to your phone (via USB, Google Drive, email, etc.)
4. On your phone, go to **Settings → Security**
5. Enable **Install from Unknown Sources**
6. Open the APK file on your phone
7. Tap **Install**

---

## 🔍 Monitoring Build Progress

### View Build Logs

1. Go to **Actions** tab on GitHub
2. Click on the running workflow
3. Click on the **build** job
4. Expand each step to see detailed logs

### Build Status

- ✅ **Green checkmark** = Build succeeded
- ❌ **Red X** = Build failed (check logs)
- 🟡 **Yellow dot** = Build in progress

---

## 🐛 Troubleshooting

### Build Fails with "Gradle Error"

**Problem:** Gradle version mismatch or dependency issue

**Solution:**
1. Check the build logs in GitHub Actions
2. Look for the specific error message
3. Update `build.gradle.kts` if needed
4. Push the fix and trigger build again

### Build Fails with "Java Version Error"

**Problem:** Wrong Java version

**Solution:**
- The workflow is configured for Java 17
- Check that `build.gradle.kts` specifies Java 17
- This should already be correct

### Workflow Doesn't Trigger

**Problem:** Workflow file not in correct location

**Solution:**
```bash
# Ensure workflow is at repository root
ls .github/workflows/build-apk.yml

# If not, move it:
mkdir -p .github/workflows
mv android/.github/workflows/build-apk.yml .github/workflows/
git add .github/workflows/build-apk.yml
git commit -m "fix: Move workflow to correct location"
git push
```

### APK Download is Empty

**Problem:** Build succeeded but no APK generated

**Solution:**
1. Check build logs for "Upload Debug APK" step
2. Verify path: `android/app/build/outputs/apk/debug/*.apk`
3. Ensure `assembleDebug` task completed successfully

---

## 📊 What Gets Built

### Debug APK
- **File:** `app-debug.apk`
- **Location:** `android/app/build/outputs/apk/debug/`
- **Signing:** Debug keystore (auto-generated)
- **Installable:** Yes, on any device with USB debugging
- **Size:** ~15-20 MB
- **Use:** Testing and development

### Release APK
- **File:** `app-release-unsigned.apk`
- **Location:** `android/app/build/outputs/apk/release/`
- **Signing:** Unsigned (needs manual signing)
- **Installable:** No, requires signing first
- **Size:** ~12-15 MB (optimized)
- **Use:** Play Store submission (after signing)

---

## 🎨 Testing UX/UI/Design

Once you have the APK installed on your phone:

### 1. Test Onboarding Flow
- [ ] Welcome screen displays correctly
- [ ] Permission screens are clear
- [ ] Keyboard setup instructions work
- [ ] Completion screen shows next steps

### 2. Test Screenshot Detection
- [ ] Service starts after onboarding
- [ ] Notification appears
- [ ] Take 5 screenshots in different apps
- [ ] Verify detection in Wingman app

### 3. Test Keyboard
- [ ] Keyboard appears in keyboard list
- [ ] Suggestions display correctly
- [ ] Tapping suggestion inserts text
- [ ] QWERTY layout works

### 4. Test Settings
- [ ] Settings screen opens
- [ ] Service can be stopped/started
- [ ] Clear suggestions works
- [ ] All buttons functional

### 5. Evaluate Design
- [ ] Colors and theme consistent
- [ ] Text readable
- [ ] Buttons properly sized
- [ ] Spacing and padding appropriate
- [ ] Icons clear and meaningful

---

## 🔄 Iteration Workflow

**To improve UX/UI/design:**

1. **Test on phone** → Identify issues
2. **Update code** → Fix issues locally or in GitHub
3. **Push changes** → `git push origin feature/android-native-app`
4. **GitHub Actions builds** → Automatic (5-10 min)
5. **Download new APK** → From Actions artifacts
6. **Install and test** → `adb install -r app-debug.apk` (replace existing)
7. **Repeat** → Until satisfied

**Fast iteration tip:** Use `adb install -r` to replace the existing app without uninstalling (preserves data).

---

## 📈 Advanced: Automatic Releases

### Create GitHub Release with APK

Add this to the workflow (optional):

```yaml
- name: Create Release
  if: startsWith(github.ref, 'refs/tags/v')
  uses: softprops/action-gh-release@v1
  with:
    files: |
      android/app/build/outputs/apk/debug/*.apk
      android/app/build/outputs/apk/release/*.apk
  env:
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

Then create a tag and push:
```bash
git tag v1.0.0
git push origin v1.0.0
```

The APK will be attached to the GitHub Release automatically.

---

## 🎯 Next Steps

### Immediate Actions
1. ✅ Push workflow to GitHub
2. ✅ Trigger first build (manual or automatic)
3. ✅ Download APK from Actions artifacts
4. ✅ Install on your phone
5. ✅ Test all features
6. ✅ Identify UX/UI improvements

### UX/UI Improvement Plan
1. **Document issues** - Screenshot problems, note them
2. **Prioritize fixes** - Critical bugs first, then UX polish
3. **Update code** - Make changes to Compose UI
4. **Test iteration** - Push → Build → Download → Install → Test
5. **Refine design** - Colors, spacing, typography, icons
6. **Get feedback** - Show to friends/beta testers

### Design Tools (Optional)
- **Figma** - Create mockups before coding
- **Material Theme Builder** - Generate color schemes
- **Android Studio Layout Inspector** - Debug UI issues
- **Accessibility Scanner** - Test accessibility

---

## 📞 Support

### GitHub Actions Documentation
- https://docs.github.com/en/actions
- https://docs.github.com/en/actions/guides/building-and-testing-java-with-gradle

### Troubleshooting
1. Check workflow logs in Actions tab
2. Verify file paths in workflow
3. Ensure Java 17 is specified
4. Check Gradle version compatibility

---

## ✅ Summary

**What you now have:**
- ✅ Automatic APK building on GitHub
- ✅ No local Android SDK needed
- ✅ APK downloadable in 5-10 minutes
- ✅ Easy iteration workflow
- ✅ Ready for UX/UI testing and improvement

**Next action:** Push the workflow and trigger your first build!

```bash
git add .github/workflows/build-apk.yml
git commit -m "ci: Add GitHub Actions APK build workflow"
git push origin feature/android-native-app
```

Then go to GitHub → Actions tab → Watch the build → Download APK → Test on phone! 🚀

---

**Built with ❤️ by Manus AI Agent**  
**Date:** November 23, 2025
