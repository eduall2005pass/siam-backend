package com.siam.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siam.ai.model.ChatSession
import com.siam.ai.ui.theme.AccentGreen
import com.siam.ai.ui.theme.CardDark
import com.siam.ai.ui.theme.DarkBg
import com.siam.ai.ui.theme.SurfaceDark
import com.siam.ai.ui.theme.TextLight
import com.siam.ai.ui.theme.TextMuted

@Composable
fun AppDrawer(
    sessions: List<ChatSession>,
    onNewChat: () -> Unit,
    onSelectSession: (ChatSession) -> Unit,
    onDeleteSession: (String) -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(SurfaceDark)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkBg)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chat History",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextLight
            )
        }

        // New Chat Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onNewChat)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New Chat",
                tint = AccentGreen,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "New Chat",
                color = TextLight,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        androidx.compose.material3.Divider(
            color = CardDark,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Sessions List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(sessions) { session ->
                ChatSessionItem(
                    session = session,
                    onSelect = { onSelectSession(session) },
                    onDelete = { onDeleteSession(session.id) }
                )
            }
        }

        androidx.compose.material3.Divider(
            color = CardDark
        )

        // Settings
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onSettingsClick)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = TextMuted,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Settings",
                color = TextLight,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ChatSessionItem(
    session: ChatSession,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = session.title,
                color = TextLight,
                fontSize = 13.sp,
                maxLines = 1,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Last updated: ${session.updatedAt.hour}:${String.format("%02d", session.updatedAt.minute)}",
                color = TextMuted,
                fontSize = 11.sp
            )
        }

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = TextMuted,
            modifier = Modifier
                .clickable(onClick = onDelete)
                .padding(8.dp)
        )
    }
}
