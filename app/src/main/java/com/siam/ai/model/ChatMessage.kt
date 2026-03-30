package com.siam.ai.model

import java.time.LocalDateTime

data class ChatMessage(
    val id: String = "",
    val text: String = "",
    val isUser: Boolean = true,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val isLoading: Boolean = false
)

data class ChatSession(
    val id: String = "",
    val title: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
