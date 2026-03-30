package com.siam.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.siam.ai.ui.theme.AccentGreen
import com.siam.ai.ui.theme.CardDark
import com.siam.ai.ui.theme.DarkBg
import com.siam.ai.ui.theme.TextLight
import com.siam.ai.ui.theme.TextMuted

@Composable
fun InputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onMicClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBg)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                androidx.compose.material3.Text(
                    "Type a message...",
                    color = TextMuted
                )
            },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = CardDark,
                unfocusedContainerColor = CardDark,
                focusedTextColor = TextLight,
                unfocusedTextColor = TextLight,
                cursorColor = AccentGreen,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp),
            singleLine = false,
            maxLines = 3
        )

        IconButton(
            onClick = onMicClick,
            modifier = Modifier
                .height(48.dp)
                .background(CardDark, RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Voice",
                tint = TextMuted
            )
        }

        IconButton(
            onClick = onSendClick,
            enabled = text.isNotBlank(),
            modifier = Modifier
                .height(48.dp)
                .background(
                    if (text.isNotBlank()) AccentGreen else CardDark,
                    RoundedCornerShape(12.dp)
                )
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = if (text.isNotBlank()) Color.White else TextMuted
            )
        }
    }
}
