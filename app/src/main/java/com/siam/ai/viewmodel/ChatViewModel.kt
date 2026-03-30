package com.siam.ai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siam.ai.model.ChatMessage
import com.siam.ai.model.ChatSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _sessions = MutableStateFlow<List<ChatSession>>(emptyList())
    val sessions: StateFlow<List<ChatSession>> = _sessions.asStateFlow()

    private val _currentSession = MutableStateFlow<ChatSession?>(null)
    val currentSession: StateFlow<ChatSession?> = _currentSession.asStateFlow()

    private val _drawerOpen = MutableStateFlow(false)
    val drawerOpen: StateFlow<Boolean> = _drawerOpen.asStateFlow()

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        // Add user message
        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = text,
            isUser = true,
            timestamp = LocalDateTime.now()
        )

        _messages.value = _messages.value + userMessage
        _inputText.value = ""

        // Simulate AI response
        viewModelScope.launch {
            _isLoading.value = true

            // Add loading message
            val loadingMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                text = "",
                isUser = false,
                isLoading = true
            )
            _messages.value = _messages.value + loadingMessage

            // Simulate delay (replace with actual API call)
            kotlinx.coroutines.delay(2000)

            // Replace loading message with actual response
            val aiMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                text = generateResponse(text),
                isUser = false,
                timestamp = LocalDateTime.now()
            )

            _messages.value = _messages.value.dropLast(1) + aiMessage
            _isLoading.value = false
        }
    }

    fun deleteMessage(messageId: String) {
        _messages.value = _messages.value.filter { it.id != messageId }
    }

    fun copyMessage(text: String) {
        // Copy to clipboard logic here
    }

    fun newChat() {
        val session = ChatSession(
            id = UUID.randomUUID().toString(),
            title = "New Chat - ${System.currentTimeMillis()}",
            messages = emptyList()
        )
        _sessions.value = _sessions.value + session
        _currentSession.value = session
        _messages.value = emptyList()
    }

    fun selectSession(session: ChatSession) {
        _currentSession.value = session
        _messages.value = session.messages
        _drawerOpen.value = false
    }

    fun deleteSession(sessionId: String) {
        _sessions.value = _sessions.value.filter { it.id != sessionId }
    }

    fun toggleDrawer(open: Boolean) {
        _drawerOpen.value = open
    }

    private fun generateResponse(userMessage: String): String {
        val responses = listOf(
            "That's a great question! Let me help you with that.",
            "I understand what you're asking. Here's what I think...",
            "Interesting! Let me break that down for you.",
            "Good point! I have some insights on this topic.",
            "That's an excellent observation. Let me elaborate..."
        )
        return responses.random() + "\n\nThis is a simulated response. Replace with actual API integration."
    }
}
