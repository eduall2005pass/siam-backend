package com.siam.ai.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.siam.ai.ui.components.AppDrawer
import com.siam.ai.ui.components.InputBar
import com.siam.ai.ui.components.MessageBubble
import com.siam.ai.ui.components.TopBar
import com.siam.ai.ui.theme.DarkBg
import com.siam.ai.viewmodel.ChatViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val messages by viewModel.messages.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val sessions by viewModel.sessions.collectAsState()
    val drawerOpen by viewModel.drawerOpen.collectAsState()

    val lazyListState = rememberLazyListState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(drawerOpen) {
        if (drawerOpen) drawerState.open() else drawerState.close()
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(messages.lastIndex)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                sessions = sessions,
                onNewChat = { viewModel.newChat() },
                onSelectSession = { viewModel.selectSession(it) },
                onDeleteSession = { viewModel.deleteSession(it) },
                onSettingsClick = {}
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBg)
        ) {
            TopBar(
                onMenuClick = { viewModel.toggleDrawer(true) },
                onSettingsClick = {}
            )
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxSize(),
                state = lazyListState
            ) {
                items(items = messages, key = { it.id }) { message ->
                    MessageBubble(
                        message = message,
                        onDelete = { viewModel.deleteMessage(it) },
                        onCopy = { viewModel.copyMessage(it) }
                    )
                }
            }
            InputBar(
                text = inputText,
                onTextChange = { viewModel.updateInputText(it) },
                onSendClick = { viewModel.sendMessage(inputText) },
                onMicClick = {}
            )
        }
    }
}
