package com.siam.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siam.ai.ui.theme.CardDark
import com.siam.ai.ui.theme.DarkBg
import com.siam.ai.ui.theme.DividerColor
import com.siam.ai.ui.theme.TextLight

@Composable
fun TopBar(
    onMenuClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = TextLight,
            modifier = Modifier
                .clickable(onClick = onMenuClick)
                .padding(8.dp)
        )

        Text(
            text = "AI Assistant",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextLight
        )

        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Settings",
            tint = TextLight,
            modifier = Modifier
                .clickable(onClick = onSettingsClick)
                .padding(8.dp)
        )
    }

    androidx.compose.material3.Divider(
        color = DividerColor,
        thickness = 1.dp
    )
}
