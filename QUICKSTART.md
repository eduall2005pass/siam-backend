# 🚀 Siam AI - দ্রুত শুরু গাইড

## প্রজেক্ট স্ট্রাকচার

```
SiamAI-Compose/
├── app/
│   ├── build.gradle                   # অ্যাপ dependencies
│   └── src/main/
│       ├── java/com/siam/ai/
│       │   ├── MainActivity.kt        # এন্ট্রি পয়েন্ট
│       │   ├── model/
│       │   │   └── ChatMessage.kt    # ডেটা মডেল
│       │   ├── viewmodel/
│       │   │   └── ChatViewModel.kt  # স্টেট ম্যানেজমেন্ট
│       │   └── ui/
│       │       ├── theme/
│       │       │   ├── Color.kt      # কালার প্যালেট
│       │       │   └── Theme.kt      # থিম কনফিগ
│       │       ├── components/
│       │       │   ├── TopBar.kt
│       │       │   ├── MessageBubble.kt
│       │       │   ├── InputBar.kt
│       │       │   └── AppDrawer.kt
│       │       └── screen/
│       │           └── ChatScreen.kt
│       └── res/
│           ├── values/
│           ├── drawable/
│           └── layout/
├── gradle/wrapper/
├── build.gradle                       # রুট কনফিগ
├── settings.gradle
├── gradle.properties
├── README.md
└── SETUP.sh
```

## Termux-এ ইনস্টলেশন

### 1. ZIP এক্সট্র্যাক্ট করো
```bash
cd ~/
unzip SiamAI-Compose.zip
cd SiamAI-Compose
```

### 2. সেটআপ চালাও
```bash
chmod +x SETUP.sh
./SETUP.sh
```

### 3. ম্যানুয়াল বিল্ড (যদি সেটআপ শেল কাজ না করে)
```bash
# Gradle clean
./gradlew clean

# Debug APK বিল্ড করো
./gradlew assembleDebug

# আউটপুট: app/build/outputs/apk/debug/app-debug.apk
```

### 4. ডিভাইসে ইনস্টল করো
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## কাস্টমাইজেশন

### থিম পরিবর্তন করো
**File:** `app/src/main/java/com/siam/ai/ui/theme/Color.kt`

```kotlin
// সবুজ কে নীলে পরিবর্তন করো
val AccentGreen = Color(0xFF0EA5E9)  // নীল
val AccentBlue = Color(0xFF10A37F)   // সবুজ (swap)
```

### API ইন্টিগ্রেশন
**File:** `app/src/main/java/com/siam/ai/viewmodel/ChatViewModel.kt`

`generateResponse()` ফাংশন খুঁজে এটা প্রতিস্থাপন করো:

```kotlin
private fun generateResponse(userMessage: String): String {
    // এখানে আপনার API কল করো
    return apiClient.sendMessage(userMessage)
    
    // OpenAI এর জন্য:
    // return openaiApi.createCompletion(userMessage).choices[0].text
}
```

### মেসেজ বাবল স্টাইল
**File:** `app/src/main/java/com/siam/ai/ui/components/MessageBubble.kt`

```kotlin
// মেসেজ চওড়া বৃদ্ধি করো
.width(280.dp)      // থেকে 300.dp বা 320.dp

// বেশি বা কম রাউন্ডেড করো
RoundedCornerShape(16.dp)  // 24.dp বা 8.dp
```

## ডিবাগিং

### Logcat দেখো
```bash
adb logcat | grep -i "siam\|compose\|chat"
```

### কমন ইস্যু

**1. Build failed - dependencies**
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

**2. মেসেজ স্ক্রল না হওয়া**
- ChatScreen.kt-এ LazyListState সঠিক? চেক করো

**3. ড্রয়ার খোলা না হওয়া**
- rememberDrawerState সিঙ্ক আছে? চেক করো

## API ইন্টিগ্রেশন উদাহরণ

### OpenAI API সহ
```kotlin
// build.gradle-এ add করো:
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

// ChatViewModel.kt-এ:
private suspend fun callOpenAiApi(message: String): String {
    val response = apiClient.createChatCompletion(
        ChatRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(Message(role = "user", content = message))
        )
    )
    return response.choices[0].message.content
}
```

## পারফরমেন্স টিপস

✅ `@Composable` এ স্টেট মিনিমাল রাখো
✅ `LazyColumn` বড় লিস্টে ব্যবহার করো
✅ ইমেজ কম্প্রেস করো
✅ নেটওয়ার্ক কল IO thread-এ করো

## গিটহাব পুশ করো

```bash
git init
git add .
git commit -m "init: SiamAI Jetpack Compose"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/SiamAI.git
git push -u origin main
```

## সাহায্য

প্রশ্ন থাকলে README.md পড়ো বা এখানে ফিরো!

---
**সংস্করণ:** 1.0.0
**ডেভেলপার:** Siam AI Team
