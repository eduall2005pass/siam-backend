import os, re

BASE = "app/src/main/java/com/siam/ai"

# ── 1. ChatMessage.kt rewrite ──────────────────────────────────────────────
chat_message = '''package com.siam.ai.model

import java.time.LocalDateTime

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: LocalDateTime? = null,
    val isLoading: Boolean = false
)
'''
with open(f"{BASE}/model/ChatMessage.kt", "w") as f:
    f.write(chat_message)
print("✅ ChatMessage.kt fixed")

# ── 2. ChatSession.kt — add updatedAt field ────────────────────────────────
chat_session = '''package com.siam.ai.model

import java.time.LocalDateTime

data class ChatSession(
    val id: String,
    val title: String,
    val messages: List<ChatMessage> = emptyList(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
'''
with open(f"{BASE}/model/ChatSession.kt", "w") as f:
    f.write(chat_session)
print("✅ ChatSession.kt fixed")

# ── 3. MessageBubble.kt — fix FontSize ────────────────────────────────────
path = f"{BASE}/ui/components/MessageBubble.kt"
with open(path, "r") as f:
    content = f.read()

# FontSize is not a valid Compose reference — replace with sp
content = content.replace(
    "import androidx.compose.ui.text.style.FontSize",
    ""
).replace(
    "FontSize",
    "14.sp"
)
# Ensure sp import exists
if "import androidx.compose.ui.unit.sp" not in content:
    content = content.replace(
        "package com.siam.ai",
        "package com.siam.ai\n\nimport androidx.compose.ui.unit.sp",
        1
    )

with open(path, "w") as f:
    f.write(content)
print("✅ MessageBubble.kt fixed")

# ── 4. ChatScreen.kt — fix animateTo ──────────────────────────────────────
path = f"{BASE}/ui/screen/ChatScreen.kt"
with open(path, "r") as f:
    content = f.read()

content = re.sub(
    r'drawerState\.animateTo\(DrawerValue\.Open[^)]*\)',
    'drawerState.open()',
    content
)
content = re.sub(
    r'drawerState\.animateTo\(DrawerValue\.Closed[^)]*\)',
    'drawerState.close()',
    content
)
# Generic animateTo fallback
content = re.sub(
    r'drawerState\.animateTo\([^)]*Open[^)]*\)',
    'drawerState.open()',
    content
)
content = re.sub(
    r'drawerState\.animateTo\([^)]*\)',
    'drawerState.close()',
    content
)

with open(path, "w") as f:
    f.write(content)
print("✅ ChatScreen.kt fixed")

print("\n✅ সব fix! এখন push করো:")
print("git add . && git commit -m 'fix: final kotlin errors' && git push")
