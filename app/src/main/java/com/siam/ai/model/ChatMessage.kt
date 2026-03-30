package com.siam.ai.model

import java.time.LocalDateTime

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: LocalDateTime? = null,
    val isLoading: Boolean = false
)
