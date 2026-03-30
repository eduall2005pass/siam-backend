#!/bin/bash
# Siam AI - Jetpack Compose Setup Guide for Termux

echo "🚀 Siam AI Jetpack Compose Setup"
echo "================================"

# পথ চেক করো
if [ ! -d "app" ]; then
    echo "❌ Error: app directory not found!"
    echo "Please extract the ZIP in your project root"
    exit 1
fi

echo "✅ Step 1: প্রজেক্ট স্ট্রাকচার যাচাই"
ls -la app/src/main/java/com/siam/ai/

echo ""
echo "✅ Step 2: Gradle clean"
./gradlew clean

echo ""
echo "✅ Step 3: Dependencies download"
./gradlew build --dry-run

echo ""
echo "✅ Step 4: আপনার API key সেট করুন (optional)"
echo "ChatViewModel.kt-এ generateResponse() ফাংশন edit করো"

echo ""
echo "✅ Step 5: বিল্ড করো"
./gradlew assembleDebug

echo ""
echo "✅ Step 6: ইনস্টল করো"
adb install -r app/build/outputs/apk/debug/app-debug.apk

echo ""
echo "🎉 সম্পন্ন! অ্যাপ চালু হবে"
