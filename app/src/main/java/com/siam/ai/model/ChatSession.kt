package com.siam.ai.model

import java.time.LocalDateTime

data class ChatSession(
    val id: String,
    val title: String,
    val messages: List<ChatMessage> = emptyList(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
