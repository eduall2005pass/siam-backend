import re

BASE = "app/src/main/java/com/siam/ai"

# ── 1. MessageBubble.kt — remove bad import ───────────────────────────────
path = f"{BASE}/ui/components/MessageBubble.kt"
with open(path, "r") as f:
    content = f.read()

content = content.replace(
    "import androidx.compose.ui.text.font.FontSize\n", ""
)

with open(path, "w") as f:
    f.write(content)
print("✅ MessageBubble.kt fixed")

# ── 2. ChatScreen.kt — fix animateTo ─────────────────────────────────────
path = f"{BASE}/ui/screen/ChatScreen.kt"
with open(path, "r") as f:
    content = f.read()

content = content.replace(
    "drawerState.animateTo(if (drawerOpen) DrawerValue.Open else DrawerValue.Closed)",
    "if (drawerOpen) drawerState.open() else drawerState.close()"
)

with open(path, "w") as f:
    f.write(content)
print("✅ ChatScreen.kt fixed")

print("\n✅ Done! Push করো:")
print("git add . && git commit -m 'fix: remove bad FontSize import, fix animateTo' && git push")
��─ 3. MessageBubble.kt — fix FontSize ────────────────────────────────────
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
