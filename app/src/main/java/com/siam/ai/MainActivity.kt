package com.siam.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.siam.ai.ui.screen.ChatScreen
import com.siam.ai.ui.theme.DarkBg
import com.siam.ai.ui.theme.SiamAiTheme
import com.siam.ai.viewmodel.ChatViewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        // Initialize with a new chat
        if (viewModel.sessions.value.isEmpty()) {
            viewModel.newChat()
        }

        setContent {
            SiamAiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBg
                ) {
                    ChatScreen(viewModel = viewModel)
                }
            }
        }
    }
}
