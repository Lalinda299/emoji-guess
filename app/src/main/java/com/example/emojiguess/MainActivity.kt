package com.example.emojiguess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.emojiguess.ads.AdManager
import com.example.emojiguess.game.GameViewModel
import com.example.emojiguess.ui.DarkNavyBackground
import com.example.emojiguess.ui.EmojiGuessApp
import com.example.emojiguess.ui.EmojiGuessTheme

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()
    private lateinit var adManager: AdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Google Mobile Ads SDK
        adManager = AdManager.getInstance(applicationContext)
        adManager.initialize()

        setContent {
            EmojiGuessTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkNavyBackground
                ) {
                    EmojiGuessApp(
                        viewModel = viewModel,
                        adManager = adManager
                    )
                }
            }
        }
    }
}
