#!/bin/bash

# Wingman Android - Automated Build Script
# This script builds the debug APK for testing

set -e  # Exit on error

echo "🚀 Wingman Android Build Script"
echo "================================"
echo ""

# Check if we're in the android directory
if [ ! -f "build.gradle.kts" ]; then
    echo "❌ Error: Must be run from the android/ directory"
    echo "   cd android && ./build-apk.sh"
    exit 1
fi

# Check Java version
echo "📋 Checking Java version..."
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}' | awk -F '.' '{print $1}')
if [ "$JAVA_VERSION" != "17" ]; then
    echo "⚠️  Warning: Java 17 required, found Java $JAVA_VERSION"
    echo "   Install Java 17: sudo apt-get install openjdk-17-jdk"
fi

# Check if Android SDK is set
if [ -z "$ANDROID_HOME" ]; then
    echo "⚠️  Warning: ANDROID_HOME not set"
    echo "   Set it with: export ANDROID_HOME=/path/to/android-sdk"
    echo ""
    echo "   Attempting to use local.properties..."
fi

# Clean previous builds
echo "🧹 Cleaning previous builds..."
./gradlew clean

# Build debug APK
echo "🔨 Building debug APK..."
./gradlew assembleDebug

# Check if build succeeded
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo ""
    echo "✅ Build successful!"
    echo ""
    echo "📦 APK Location:"
    echo "   $(pwd)/app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "📱 To install on connected device:"
    echo "   adb install app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "🔍 APK Size:"
    ls -lh app/build/outputs/apk/debug/app-debug.apk | awk '{print "   " $5}'
    echo ""
else
    echo ""
    echo "❌ Build failed!"
    echo "   Check the error messages above"
    exit 1
fi
