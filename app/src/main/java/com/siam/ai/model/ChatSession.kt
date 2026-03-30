package com.siam.ai.model

data class ChatSession(
    val id: String,
    val title: String,
    val messages: List<ChatMessage> = emptyList()
)
