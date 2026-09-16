package com.example.emojiguess.ui

import android.app.Activity
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.emojiguess.ads.AdManager
import com.example.emojiguess.audio.BgmContext
import com.example.emojiguess.audio.SoundManager
import com.example.emojiguess.game.GameEvent
import com.example.emojiguess.game.GameScreenState
import com.example.emojiguess.game.GameViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun EmojiGuessApp(
    viewModel: GameViewModel,
    adManager: AdManager
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val soundManager = remember { SoundManager(context) }
    soundManager.isSoundEnabled = uiState.soundEnabled
    soundManager.isBgmEnabled = uiState.bgmEnabled

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> {
                    soundManager.pauseBgm()
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (uiState.bgmEnabled) {
                        soundManager.resumeBgm()
                    }
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            soundManager.release()
        }
    }

    var showSettingsDialog by remember { mutableStateOf(false) }
    var showDailyChallengeDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    // Dynamic BGM switching based on active screen and mode
    LaunchedEffect(uiState.currentScreen, uiState.isHardcoreMode) {
        when (uiState.currentScreen) {
            is GameScreenState.Home -> soundManager.setBgmContext(BgmContext.Home)
            is GameScreenState.StageSelect -> soundManager.setBgmContext(BgmContext.StageSelect)
            is GameScreenState.Playing -> {
                if (uiState.isHardcoreMode) {
                    soundManager.setBgmContext(BgmContext.Hardcore)
                } else {
                    soundManager.setBgmContext(BgmContext.Gameplay)
                }
            }
            is GameScreenState.GameOver -> soundManager.setBgmContext(BgmContext.Home)
        }
    }

    // Hardware Back Button Handler
    BackHandler {
        when (uiState.currentScreen) {
            is GameScreenState.Home -> showExitDialog = true
            is GameScreenState.StageSelect -> viewModel.returnToHome()
            is GameScreenState.Playing -> viewModel.openStageSelect()
            is GameScreenState.GameOver -> viewModel.returnToHome()
        }
    }

    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(VibratorManager::class.java)
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    fun performHaptic(effectType: String) {
        if (!uiState.vibrationEnabled || vibrator == null || !vibrator.hasVibrator()) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            when (effectType) {
                "click" -> vibrator.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE))
                "correct" -> vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 40, 40, 60), -1))
                "wrong" -> vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 80, 50, 80, 50, 80), -1))
                "level_up" -> vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 60, 40, 60, 40, 100), -1))
            }
        } else {
            @Suppress("DEPRECATION")
            when (effectType) {
                "click" -> vibrator.vibrate(25)
                "correct" -> vibrator.vibrate(100)
                "wrong" -> vibrator.vibrate(200)
                "level_up" -> vibrator.vibrate(250)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is GameEvent.PlayGameStartSound -> soundManager.playGameStartSound()
                is GameEvent.PlayStageUnlockedSound -> soundManager.playStageUnlockedSound()
                is GameEvent.PlayHintSound -> soundManager.playHintSound()
                is GameEvent.PlayCorrectSound -> soundManager.playCorrectSound()
                is GameEvent.PlayWrongSound -> soundManager.playWrongSound()
                is GameEvent.PlayClickSound -> soundManager.playClickSound()
                is GameEvent.PlayGameOverSound -> soundManager.playGameOverSound()
                is GameEvent.PlayLevelUpSound -> soundManager.playLevelUpSound()
                is GameEvent.PlayFlashBonusSound -> soundManager.playLevelUpSound()
                is GameEvent.PlaySadSound -> soundManager.playSadSound()
                is GameEvent.PlayRefillSound -> soundManager.playRefillSound()
                is GameEvent.PlayVictorySound -> soundManager.playVictorySound()
                is GameEvent.PerformCorrectHaptic -> performHaptic("correct")
                is GameEvent.PerformWrongHaptic -> performHaptic("wrong")
                is GameEvent.PerformClickHaptic -> performHaptic("click")
                is GameEvent.PerformLevelUpHaptic -> performHaptic("level_up")
                is GameEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = DarkNavyBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState.currentScreen) {
                is GameScreenState.Home -> {
                    HomeScreen(
                        uiState = uiState,
                        onSelectStageClick = { viewModel.openStageSelect() },
                        onHardcoreClick = { viewModel.startStageLevel(5, 1) },
                        onDailyChallengeClick = { showDailyChallengeDialog = true },
                        onRefillLivesClick = { viewModel.openRefillDialog() },
                        onSettingsClick = { showSettingsDialog = true }
                    )
                }

                is GameScreenState.StageSelect -> {
                    StageSelectScreen(
                        uiState = uiState,
                        onStageSelect = { stage -> viewModel.selectStage(stage) },
                        onLevelClick = { stage, level -> viewModel.startStageLevel(stage, level) },
                        onRefillLivesClick = { viewModel.openRefillDialog() },
                        onBackClick = { viewModel.returnToHome() }
                    )
                }

                is GameScreenState.Playing -> {
                    GameScreen(
                        uiState = uiState,
                        onAnswerSelected = { option -> viewModel.submitAnswer(option) },
                        onHintClick = { viewModel.useHint() },
                        onRefillLivesClick = { viewModel.openRefillDialog() },
                        onBackClick = { viewModel.openStageSelect() }
                    )
                }

                is GameScreenState.GameOver -> {
                    ResultScreen(
                        uiState = uiState,
                        onPlayAgainClick = {
                            if (activity != null) {
                                adManager.showInterstitialAd(activity) {
                                    viewModel.startStageLevel(uiState.selectedStage, uiState.selectedLevel)
                                }
                            } else {
                                viewModel.startStageLevel(uiState.selectedStage, uiState.selectedLevel)
                            }
                        },
                        onHomeClick = {
                            if (activity != null) {
                                adManager.showInterstitialAd(activity) {
                                    viewModel.returnToHome()
                                }
                            } else {
                                viewModel.returnToHome()
                            }
                        },
                        onWatchAdContinueClick = {
                            if (activity != null) {
                                adManager.showRewardedAd(
                                    activity = activity,
                                    onRewardEarned = {
                                        viewModel.onRewardedAdCompleted()
                                    },
                                    onAdFailed = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Ad currently unavailable. Refilling lives with local backup...")
                                            viewModel.refillLivesWithAd()
                                        }
                                    }
                                )
                            } else {
                                viewModel.refillLivesWithAd()
                            }
                        }
                    )
                }
            }

            // Dialogs
            if (uiState.showRefillDialog) {
                RefillLivesDialog(
                    coins = uiState.coins,
                    onWatchAdRefill = {
                        if (activity != null) {
                            adManager.showRewardedAd(
                                activity = activity,
                                onRewardEarned = { viewModel.refillLivesWithAd() },
                                onAdFailed = {
                                    viewModel.refillLivesWithAd()
                                }
                            )
                        } else {
                            viewModel.refillLivesWithAd()
                        }
                    },
                    onCoinsRefill = { viewModel.refillLivesWithCoins() },
                    onDismiss = { viewModel.closeRefillDialog() }
                )
            }

            if (uiState.showLevelUpDialog) {
                LevelUpDialog(
                    newLevel = uiState.playerLevel,
                    rewardCoins = uiState.levelUpRewardCoins,
                    onClaimReward = { viewModel.closeLevelUpDialog() }
                )
            }

            if (showSettingsDialog) {
                SettingsDialog(
                    soundEnabled = uiState.soundEnabled,
                    bgmEnabled = uiState.bgmEnabled,
                    vibrationEnabled = uiState.vibrationEnabled,
                    onSoundToggled = { viewModel.toggleSound(it) },
                    onBgmToggled = { viewModel.toggleBgm(it) },
                    onVibrationToggled = { viewModel.toggleVibration(it) },
                    onDismiss = { showSettingsDialog = false }
                )
            }

            if (showDailyChallengeDialog) {
                DailyChallengeDialog(
                    isAvailable = uiState.isDailyChallengeAvailableToday,
                    onStartChallenge = { viewModel.startDailyChallenge() },
                    onDismiss = { showDailyChallengeDialog = false }
                )
            }

            if (showExitDialog) {
                ExitAppDialog(
                    onConfirmExit = { activity?.finish() },
                    onDismiss = { showExitDialog = false }
                )
            }
        }
    }
}
