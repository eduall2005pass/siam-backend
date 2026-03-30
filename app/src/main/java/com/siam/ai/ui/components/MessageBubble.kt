package com.siam.ai.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.14.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siam.ai.model.ChatMessage
import com.siam.ai.ui.theme.AiBubbleBg
import com.siam.ai.ui.theme.BubbleText
import com.siam.ai.ui.theme.LoadingBg
import com.siam.ai.ui.theme.TextMuted
import com.siam.ai.ui.theme.TypingDot
import com.siam.ai.ui.theme.UserBubbleBg

@Composable
fun MessageBubble(
    message: ChatMessage,
    onDelete: (String) -> Unit = {},
    onCopy: (String) -> Unit = {}
) {
    if (message.isLoading) {
        TypingIndicator()
        return
    }

    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (message.isUser) UserBubbleBg else AiBubbleBg
                )
                .padding(12.dp)
                .clickable {
                    // Show actions on click
                }
        ) {
            Column(
                modifier = Modifier.width(280.dp)
            ) {
                Text(
                    text = message.text,
                    color = BubbleText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                if (!message.isUser) {
                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = TextMuted,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { onCopy(message.text) }
                        )

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = TextMuted,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { onDelete(message.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")

    val dot1Opacity by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(
                durationMillis = 600,
                easing = LinearEasing
            )
        ),
        label = "dot1"
    )

    val dot2Opacity by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(
                durationMillis = 600,
                delayMillis = 200,
                easing = LinearEasing
            )
        ),
        label = "dot2"
    )

    val dot3Opacity by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(
                durationMillis = 600,
                delayMillis = 400,
                easing = LinearEasing
            )
        ),
        label = "dot3"
    )

    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(AiBubbleBg)
                .padding(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(TypingDot.copy(alpha = dot1Opacity))
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(TypingDot.copy(alpha = dot2Opacity))
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(TypingDot.copy(alpha = dot3Opacity))
                )
            }
        }
    }
}
