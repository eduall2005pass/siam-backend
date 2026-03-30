# Siam AI - ChatGPT Style Android App

একটি আধুনিক ChatGPT-স্টাইল Android অ্যাপ্লিকেশন Kotlin + Jetpack Compose দিয়ে তৈরি।

## ফিচার

✅ **ডার্ক মোড ডিজাইন** - ChatGPT এর মতো মিনিমালিস্ট UI
✅ **স্মুথ মেসেজ বাবল** - ইউজার এবং AI মেসেজ ডিফারেন্সিয়েশন
✅ **টাইপিং অ্যানিমেশন** - "Thinking..." ইন্ডিকেটর
✅ **সাইড ড্রয়ার** - চ্যাট হিস্টরি ম্যানেজমেন্ট
✅ **ইন্টারঅ্যাক্টিভ কম্পোনেন্টস** - কপি, ডিলিট মেসেজ
✅ **স্বয়ংক্রিয় স্ক্রল** - লেটেস্ট মেসেজে স্ক্রল

## প্রজেক্ট স্ট্রাকচার

```
app/src/main/
├── java/com/siam/ai/
│   ├── MainActivity.kt              # Main entry point
│   ├── model/
│   │   └── ChatMessage.kt           # Data models
│   ├── viewmodel/
│   │   └── ChatViewModel.kt         # State management
│   └── ui/
│       ├── theme/
│       │   ├── Color.kt             # Color palette
│       │   └── Theme.kt             # Theme configuration
│       ├── components/
│       │   ├── TopBar.kt            # Header
│       │   ├── MessageBubble.kt     # Chat bubbles
│       │   ├── InputBar.kt          # Message input
│       │   └── AppDrawer.kt         # Sidebar
│       └── screen/
│           └── ChatScreen.kt        # Main screen
├── res/
│   ├── drawable/
│   │   ├── ic_claude_noti.xml       # App icon
│   │   └── ...
│   ├── values/
│   │   ├── strings.xml
│   │   ├── styles.xml
│   │   └── colors.xml
│   └── layout/
│       └── activity_main.xml
└── AndroidManifest.xml
```

## ইনস্টলেশন নির্দেশনা

### Termux-এ সেটআপ

```bash
cd ~/Siam-AI

# ফাইল কপি করো
mkdir -p app/src/main/java/com/siam/ai/{model,viewmodel,ui/{theme,components,screen}}

# Color.kt
cp /home/claude/Color.kt app/src/main/java/com/siam/ai/ui/theme/

# ChatMessage.kt
cp /home/claude/ChatMessage.kt app/src/main/java/com/siam/ai/model/

# ChatViewModel.kt
cp /home/claude/ChatViewModel.kt app/src/main/java/com/siam/ai/viewmodel/

# Components
cp /home/claude/TopBar.kt app/src/main/java/com/siam/ai/ui/components/
cp /home/claude/MessageBubble.kt app/src/main/java/com/siam/ai/ui/components/
cp /home/claude/InputBar.kt app/src/main/java/com/siam/ai/ui/components/
cp /home/claude/AppDrawer.kt app/src/main/java/com/siam/ai/ui/components/

# Screens
cp /home/claude/ChatScreen.kt app/src/main/java/com/siam/ai/ui/screen/

# Theme
cp /home/claude/Theme.kt app/src/main/java/com/siam/ai/ui/theme/

# Main
cp /home/claude/MainActivity.kt app/src/main/java/com/siam/ai/

# Build files
cp /home/claude/build.gradle app/
cp /home/claude/AndroidManifest.xml app/src/main/

# বিল্ড করো
./gradlew clean
./gradlew assembleDebug
```

## কোর কম্পোনেন্টস

### 1. Color.kt - থিম কালার
```kotlin
val DarkBg = Color(0xFF0D0D0D)        // মেইন ব্যাকগ্রাউন্ড
val AccentGreen = Color(0xFF10A37F)   // প্রাইমারি অ্যাকসেন্ট
val UserBubbleBg = Color(0xFF1E3A5F)  // ইউজার মেসেজ
val AiBubbleBg = Color(0xFF2D2D2D)    // AI মেসেজ
```

### 2. MessageBubble.kt - চ্যাট ইউআই
- মেসেজ বাবল (রাউন্ডেড কর্নার)
- টাইপিং ইন্ডিকেটর অ্যানিমেশন
- কপি/ডিলিট অ্যাকশন

### 3. InputBar.kt - ইনপুট এরিয়া
- মাল্টিলাইন টেক্সট ফিল্ড
- মাইক্রোফোন বাটন
- সেন্ড বাটন (সশর্ত এনেবল)

### 4. ChatScreen.kt - মেইন স্ক্রিন
- মেসেজ লিস্ট (অটো স্ক্রল)
- নেভিগেশন ড্রয়ার ইন্টিগ্রেশন
- ভিউমডেল সাথে সংযোগ

### 5. ChatViewModel.kt - স্টেট ম্যানেজমেন্ট
```kotlin
fun sendMessage(text: String)        // মেসেজ পাঠানো
fun deleteMessage(messageId: String) // মেসেজ ডিলিট
fun newChat()                        // নতুন চ্যাট শুরু
fun selectSession(session: ChatSession) // পুরানো চ্যাট খোলা
```

## কাস্টমাইজেশন

### থিম পরিবর্তন
`Color.kt`-এ কালার ভ্যালু চেঞ্জ করো:

```kotlin
val AccentGreen = Color(0xFF0EA5E9) // নীল রঙ
val DarkBg = Color(0xFF1A1A1A)      // হালকা ডার্ক
```

### মেসেজ স্টাইল
`MessageBubble.kt`-এ বাবল আকার পরিবর্তন করো:

```kotlin
.clip(RoundedCornerShape(24.dp))  // আরও বেশি রাউন্ডেড
.width(320.dp)                     // বড় মেসেজ বাবল
```

### API ইন্টিগ্রেশন
`ChatViewModel.kt`-এ `generateResponse()` ফাংশন প্রতিস্থাপন করো:

```kotlin
private fun generateResponse(userMessage: String): String {
    // আপনার API কল এখানে
    return apiClient.chat(userMessage)
}
```

## এপিআই ইন্টিগ্রেশন গাইড

### OpenAI API সহ
```kotlin
// Retrofit dependency যোগ করো
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

// API কল করো
private suspend fun callApi(message: String): String {
    val response = apiClient.createChatCompletion(
        ChatRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(ChatMessage(role = "user", content = message))
        )
    )
    return response.choices.first().message.content
}
```

## ডিবাগিং

### Logcat চেক করো
```bash
adb logcat | grep "Siam\|ChatViewModel\|Compose"
```

### কমন ইস্যু

**মেসেজ স্ক্রল না হওয়া:**
- `LazyListState` সঠিকভাবে সেট করা আছে কিনা চেক করো

**টাইপিং অ্যানিমেশন কাজ না করা:**
- `infiniteRepeatable` সঠিকভাবে কনফিগার করা আছে কিনা চেক করো

**ড্রয়ার খোলা না হওয়া:**
- `rememberDrawerState` এবং `drawerOpen` state সিঙ্ক আছে কিনা চেক করো

## পারফরমেন্স টিপস

✅ `@Composable` ফাংশনে স্টেট ভেরিয়েবল মিনিমাল রাখো
✅ `LazyColumn` বড় লিস্টের জন্য ব্যবহার করো
✅ ইমেজ কম্প্রেস করো এবং কেশ করো
✅ নেটওয়ার্ক কল অফ-মেইন থ্রেডে করো

## লাইসেন্স

MIT License - ফ্রি ব্যবহারের জন্য

---

**তৈরি করেছেন:** Siam AI Team
**সংস্করণ:** 1.0.0
