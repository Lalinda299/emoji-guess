package com.lalinda.emojiguess.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lalinda.emojiguess.data.GamePreferences
import com.lalinda.emojiguess.data.ProceduralQuestionGenerator
import com.lalinda.emojiguess.data.Question
import com.lalinda.emojiguess.data.QuestionBank
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

sealed class GameScreenState {
    object Home : GameScreenState()
    object StageSelect : GameScreenState()
    object Playing : GameScreenState()
    object GameOver : GameScreenState()
}

data class CurrentQuestionState(
    val question: Question,
    val questionNumber: Int,
    val totalQuestions: Int,
    val shuffledOptions: List<String>,
    val disabledOptions: Set<String> = emptySet(),
    val selectedOption: String? = null,
    val isCorrect: Boolean? = null,
    val isDailyChallenge: Boolean = false,
    val level: Int = 1,
    val stage: Int = 1
)

data class GameUiState(
    val currentScreen: GameScreenState = GameScreenState.Home,
    val selectedStage: Int = 1,
    val selectedLevel: Int = 1,
    val currentLevel: Int = 1,
    val score: Int = 0,
    val bestScore: Int = 0,
    val coins: Int = 500,
    val lives: Int = 6,
    val maxLives: Int = 6,
    val nextLifeTimerSeconds: Long = 0L,
    val playerXp: Int = 0,
    val playerLevel: Int = 1,
    val playerTitle: String = "Emoji Novice",
    val unlockedStage: Int = 1,
    val levelStarsMap: Map<String, Int> = emptyMap(),
    val streak: Int = 0,
    val highestStreakInSession: Int = 0,
    val questionsAnsweredInSession: Int = 0,
    val correctAnswersInSession: Int = 0,
    val coinsEarnedInSession: Int = 0,
    val xpEarnedInSession: Int = 0,
    val starsEarnedInLevel: Int = 0,
    val currentQuestionState: CurrentQuestionState? = null,
    val isFlashBonusActive: Boolean = false,
    val flashBonusTimeRemainingSeconds: Int = 5,
    val isHardcoreMode: Boolean = false,
    val hardcoreTimeRemainingSeconds: Int = 10,
    val showRefillDialog: Boolean = false,
    val showLevelUpDialog: Boolean = false,
    val levelUpRewardCoins: Int = 100,
    val soundEnabled: Boolean = true,
    val bgmEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val isDailyChallengeAvailableToday: Boolean = true,
    val isDailyChallengeMode: Boolean = false,
    val snackbarMessage: String? = null,
    val triggerParticleBurst: Boolean = false
)

sealed class GameEvent {
    object PlayGameStartSound : GameEvent()
    object PlayStageUnlockedSound : GameEvent()
    object PlayHintSound : GameEvent()
    object PlayCorrectSound : GameEvent()
    object PlayWrongSound : GameEvent()
    object PlayClickSound : GameEvent()
    object PlayGameOverSound : GameEvent()
    object PlayLevelUpSound : GameEvent()
    object PlayFlashBonusSound : GameEvent()
    object PlaySadSound : GameEvent()
    object PlayRefillSound : GameEvent()
    object PlayVictorySound : GameEvent()
    object PerformCorrectHaptic : GameEvent()
    object PerformWrongHaptic : GameEvent()
    object PerformClickHaptic : GameEvent()
    object PerformLevelUpHaptic : GameEvent()
    data class ShowSnackbar(val message: String) : GameEvent()
}

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = GamePreferences(application.applicationContext)

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<GameEvent>()
    val eventFlow: SharedFlow<GameEvent> = _eventFlow.asSharedFlow()

    private var activeQuestionList: List<Question> = emptyList()
    private var lastLifeRegenTimestamp: Long = 0L
    private val usedQuestionIdsInSession = mutableSetOf<Int>()

    private var flashBonusTimerJob: Job? = null
    private var hardcoreTimerJob: Job? = null
    private var lifeRegenJob: Job? = null

    init {
        viewModelScope.launch {
            preferences.userPreferencesFlow.collect { userPrefs ->
                val todayStr = getTodayDateString()
                val isDailyAvailable = userPrefs.dailyChallengeCompletedDate != todayStr

                val starsMap = userPrefs.levelStarsEncoded.split(",")
                    .mapNotNull {
                        val parts = it.split(":")
                        if (parts.size == 2) parts[0] to (parts[1].toIntOrNull() ?: 0) else null
                    }.toMap()

                val pLevel = calculatePlayerLevel(userPrefs.playerXp)
                val pTitle = getPlayerTitle(pLevel)

                lastLifeRegenTimestamp = userPrefs.lastLifeTimestamp

                _uiState.value = _uiState.value.copy(
                    bestScore = userPrefs.bestScore,
                    coins = userPrefs.coins,
                    lives = userPrefs.lives,
                    playerXp = userPrefs.playerXp,
                    playerLevel = pLevel,
                    playerTitle = pTitle,
                    unlockedStage = userPrefs.unlockedStage,
                    levelStarsMap = starsMap,
                    soundEnabled = userPrefs.soundEnabled,
                    bgmEnabled = userPrefs.bgmEnabled,
                    vibrationEnabled = userPrefs.vibrationEnabled,
                    isDailyChallengeAvailableToday = isDailyAvailable
                )

                checkAndProcessLifeRegen()
            }
        }

        startLifeRegenTicker()
    }

    private fun calculatePlayerLevel(xp: Int): Int {
        return (xp / 500) + 1
    }

    private fun getPlayerTitle(level: Int): String {
        return when {
            level >= 25 -> "Emoji Legend 👑"
            level >= 20 -> "Emoji Master ⚡"
            level >= 15 -> "Emoji Grandmaster 🔥"
            level >= 10 -> "Emoji Specialist 🌟"
            level >= 5 -> "Emoji Apprentice ✨"
            else -> "Emoji Novice 🌱"
        }
    }

    private fun getTodayDateString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun startLifeRegenTicker() {
        lifeRegenJob?.cancel()
        lifeRegenJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                checkAndProcessLifeRegen()
            }
        }
    }

    private fun checkAndProcessLifeRegen() {
        val currentLives = _uiState.value.lives
        if (currentLives >= 6) {
            _uiState.value = _uiState.value.copy(nextLifeTimerSeconds = 0L)
            return
        }

        val now = System.currentTimeMillis()
        if (lastLifeRegenTimestamp <= 0L) {
            lastLifeRegenTimestamp = now
            viewModelScope.launch { preferences.updateLivesAndTimestamp(currentLives, now) }
        }

        val regenIntervalMs = 30 * 60 * 1000L // 30 mins per life
        val elapsedMs = now - lastLifeRegenTimestamp

        if (elapsedMs >= regenIntervalMs) {
            val livesToGrant = (elapsedMs / regenIntervalMs).toInt()
            val newLives = (currentLives + livesToGrant).coerceAtMost(6)
            val newTimestamp = if (newLives >= 6) now else lastLifeRegenTimestamp + (livesToGrant * regenIntervalMs)
            lastLifeRegenTimestamp = newTimestamp

            _uiState.value = _uiState.value.copy(lives = newLives)
            viewModelScope.launch { preferences.updateLivesAndTimestamp(newLives, newTimestamp) }
        } else {
            val remainingMs = regenIntervalMs - elapsedMs
            _uiState.value = _uiState.value.copy(nextLifeTimerSeconds = (remainingMs / 1000L).coerceAtLeast(0L))
        }
    }

    fun openStageSelect() {
        emitEvent(GameEvent.PerformClickHaptic)
        emitEvent(GameEvent.PlayClickSound)
        _uiState.value = _uiState.value.copy(currentScreen = GameScreenState.StageSelect)
    }

    fun selectStage(stage: Int) {
        if (stage == 5 && _uiState.value.unlockedStage < 5) {
            emitEvent(GameEvent.ShowSnackbar("🔒 Stage 5 unlocks after completing all 200 levels (Stages 1-4)!"))
            return
        }
        if (stage > _uiState.value.unlockedStage) {
            emitEvent(GameEvent.ShowSnackbar("Stage $stage is locked! Complete Stage ${stage - 1} first."))
            return
        }
        emitEvent(GameEvent.PerformClickHaptic)
        emitEvent(GameEvent.PlayClickSound)
        _uiState.value = _uiState.value.copy(selectedStage = stage)
    }

    fun openRefillDialog() {
        emitEvent(GameEvent.PerformClickHaptic)
        emitEvent(GameEvent.PlayClickSound)
        _uiState.value = _uiState.value.copy(showRefillDialog = true)
    }

    fun startStageLevel(stage: Int, level: Int) {
        if (stage == 5 && _uiState.value.unlockedStage < 5) {
            emitEvent(GameEvent.ShowSnackbar("🔒 Stage 5 unlocks after completing all 200 campaign levels!"))
            return
        }
        if (_uiState.value.lives <= 0) {
            openRefillDialog()
            return
        }

        emitEvent(GameEvent.PerformClickHaptic)
        emitEvent(GameEvent.PlayGameStartSound)

        usedQuestionIdsInSession.clear()

        _uiState.value = _uiState.value.copy(
            currentScreen = GameScreenState.Playing,
            selectedStage = stage,
            selectedLevel = level,
            currentLevel = level,
            score = 0,
            streak = 0,
            highestStreakInSession = 0,
            questionsAnsweredInSession = 0,
            correctAnswersInSession = 0,
            coinsEarnedInSession = 0,
            xpEarnedInSession = 0,
            isHardcoreMode = (stage == 5),
            isDailyChallengeMode = false,
            triggerParticleBurst = false
        )

        loadQuestionForStageLevel(stage, level)
    }

    fun startDailyChallenge() {
        if (_uiState.value.lives <= 0) {
            openRefillDialog()
            return
        }

        emitEvent(GameEvent.PerformClickHaptic)
        emitEvent(GameEvent.PlayGameStartSound)

        val todayStr = getTodayDateString()
        val dayHashCode = kotlin.math.abs(todayStr.hashCode())
        val challengeQuestionIndex = dayHashCode % QuestionBank.questions.size
        val challengeQuestion = QuestionBank.questions[challengeQuestionIndex]

        activeQuestionList = listOf(challengeQuestion)

        _uiState.value = _uiState.value.copy(
            currentScreen = GameScreenState.Playing,
            selectedStage = 1,
            selectedLevel = 1,
            currentLevel = 1,
            score = 0,
            streak = 0,
            highestStreakInSession = 0,
            questionsAnsweredInSession = 0,
            correctAnswersInSession = 0,
            coinsEarnedInSession = 0,
            xpEarnedInSession = 0,
            isHardcoreMode = false,
            isDailyChallengeMode = true,
            triggerParticleBurst = false
        )

        loadCurrentQuestionState(challengeQuestion, 1, 1, true)
    }

    private fun loadQuestionForStageLevel(stage: Int, level: Int) {
        var question = ProceduralQuestionGenerator.generateQuestionForStageLevel(stage, level)
        var attempts = 0
        while (usedQuestionIdsInSession.contains(question.id) && attempts < 10) {
            question = ProceduralQuestionGenerator.generateQuestionForStageLevel(stage, level + attempts + 1)
            attempts++
        }
        usedQuestionIdsInSession.add(question.id)
        loadCurrentQuestionState(question, stage, level, false)
    }

    private fun loadCurrentQuestionState(question: Question, stage: Int, level: Int, isDaily: Boolean) {
        cancelTimers()

        val shuffledOptions = question.options.shuffled()

        // 25% chance of triggering Speed Bonanza on regular non-daily levels
        val triggerFlashBonus = !isDaily && Random.nextFloat() < 0.25f

        _uiState.value = _uiState.value.copy(
            currentQuestionState = CurrentQuestionState(
                question = question,
                questionNumber = level,
                totalQuestions = if (isDaily) 1 else 50,
                shuffledOptions = shuffledOptions,
                disabledOptions = emptySet(),
                selectedOption = null,
                isCorrect = null,
                isDailyChallenge = isDaily,
                level = level,
                stage = stage
            ),
            isFlashBonusActive = triggerFlashBonus,
            flashBonusTimeRemainingSeconds = 5,
            hardcoreTimeRemainingSeconds = 10,
            triggerParticleBurst = false
        )

        if (triggerFlashBonus) {
            emitEvent(GameEvent.PlayFlashBonusSound)
            startFlashBonusTimer()
        }

        if (_uiState.value.isHardcoreMode) {
            startHardcoreTimer()
        }
    }

    private fun startFlashBonusTimer() {
        flashBonusTimerJob?.cancel()
        flashBonusTimerJob = viewModelScope.launch {
            for (sec in 5 downTo 1) {
                _uiState.value = _uiState.value.copy(flashBonusTimeRemainingSeconds = sec)
                delay(1000)
            }
            _uiState.value = _uiState.value.copy(isFlashBonusActive = false)
        }
    }

    private fun startHardcoreTimer() {
        hardcoreTimerJob?.cancel()
        hardcoreTimerJob = viewModelScope.launch {
            for (sec in 10 downTo 1) {
                _uiState.value = _uiState.value.copy(hardcoreTimeRemainingSeconds = sec)
                delay(1000)
            }
            submitAnswer("")
        }
    }

    private fun cancelTimers() {
        flashBonusTimerJob?.cancel()
        hardcoreTimerJob?.cancel()
    }

    fun submitAnswer(selectedOption: String) {
        val currentQState = _uiState.value.currentQuestionState ?: return
        if (currentQState.selectedOption != null) return

        cancelTimers()

        val isCorrect = selectedOption == currentQState.question.correctAnswer
        val wasFlashBonusEarned = isCorrect && _uiState.value.isFlashBonusActive && _uiState.value.flashBonusTimeRemainingSeconds > 0

        val newQuestionsAnswered = _uiState.value.questionsAnsweredInSession + 1
        var newScore = _uiState.value.score
        var newCoins = _uiState.value.coins
        var newCoinsEarned = _uiState.value.coinsEarnedInSession
        var newXpEarned = _uiState.value.xpEarnedInSession
        var newLives = _uiState.value.lives
        var newStreak = _uiState.value.streak
        var newCorrectAnswers = _uiState.value.correctAnswersInSession
        var newHighestStreak = _uiState.value.highestStreakInSession
        val currentStage = _uiState.value.selectedStage
        val currentLevel = _uiState.value.selectedLevel

        if (isCorrect) {
            emitEvent(GameEvent.PlayVictorySound)
            emitEvent(GameEvent.PerformCorrectHaptic)

            newCorrectAnswers++
            newStreak++
            if (newStreak > newHighestStreak) {
                newHighestStreak = newStreak
            }

            val pointsEarned = 100 + (newStreak - 1) * 25 + (currentLevel * 10)
            newScore += pointsEarned

            var addCoins = 10
            var addXp = 50

            if (wasFlashBonusEarned) {
                addCoins += 50
                addXp += 50
                emitEvent(GameEvent.ShowSnackbar("⚡ SPEED BONANZA! +50 COINS & +50 XP!"))
            }

            if (newStreak % 3 == 0) {
                addCoins += 25
            }

            newCoins += addCoins
            newCoinsEarned += addCoins
            newXpEarned += addXp

            val totalXp = _uiState.value.playerXp + addXp
            val oldPlayerLevel = _uiState.value.playerLevel
            val newPlayerLevel = calculatePlayerLevel(totalXp)
            val isLeveledUp = newPlayerLevel > oldPlayerLevel

            val starsEarned = when (_uiState.value.maxLives - newLives) {
                0 -> 3
                1 -> 2
                else -> 1
            }

            val isNewStageUnlocked = currentLevel >= 50 && currentStage < 5 && (currentStage + 1 > _uiState.value.unlockedStage)
            val nextUnlockedStage = if (currentLevel >= 50 && currentStage < 5) currentStage + 1 else _uiState.value.unlockedStage

            if (isNewStageUnlocked) {
                emitEvent(GameEvent.PlayStageUnlockedSound)
                emitEvent(GameEvent.ShowSnackbar("🎉 STAGE $nextUnlockedStage UNLOCKED!"))
            }

            _uiState.value = _uiState.value.copy(
                score = newScore,
                coins = newCoins,
                coinsEarnedInSession = newCoinsEarned,
                xpEarnedInSession = newXpEarned,
                starsEarnedInLevel = starsEarned,
                playerXp = totalXp,
                playerLevel = newPlayerLevel,
                playerTitle = getPlayerTitle(newPlayerLevel),
                unlockedStage = nextUnlockedStage,
                streak = newStreak,
                highestStreakInSession = newHighestStreak,
                correctAnswersInSession = newCorrectAnswers,
                questionsAnsweredInSession = newQuestionsAnswered,
                currentQuestionState = currentQState.copy(
                    selectedOption = selectedOption,
                    isCorrect = true
                ),
                showLevelUpDialog = isLeveledUp,
                levelUpRewardCoins = 100,
                triggerParticleBurst = true
            )

            viewModelScope.launch {
                preferences.updateCoins(newCoins)
                preferences.addPlayerXp(addXp)
                preferences.saveLevelProgress(currentStage, currentLevel, starsEarned, nextUnlockedStage)

                if (isLeveledUp) {
                    val bonusCoins = newCoins + 100
                    val fullLives = 6
                    preferences.updateCoins(bonusCoins)
                    preferences.updateLivesAndTimestamp(fullLives, System.currentTimeMillis())
                    _uiState.value = _uiState.value.copy(coins = bonusCoins, lives = fullLives)
                    emitEvent(GameEvent.PlayLevelUpSound)
                    emitEvent(GameEvent.PerformLevelUpHaptic)
                }
            }
        } else {
            emitEvent(GameEvent.PlayWrongSound)
            emitEvent(GameEvent.PerformWrongHaptic)

            newLives = (newLives - 1).coerceAtLeast(0)
            val now = System.currentTimeMillis()
            if (lastLifeRegenTimestamp <= 0L) {
                lastLifeRegenTimestamp = now
            }

            if (newLives == 0) {
                emitEvent(GameEvent.PlaySadSound)
            }

            _uiState.value = _uiState.value.copy(
                lives = newLives,
                streak = 0,
                questionsAnsweredInSession = newQuestionsAnswered,
                currentQuestionState = currentQState.copy(
                    selectedOption = selectedOption,
                    isCorrect = false
                ),
                triggerParticleBurst = false
            )

            viewModelScope.launch {
                preferences.updateLivesAndTimestamp(newLives, lastLifeRegenTimestamp)
            }
        }

        viewModelScope.launch {
            delay(1200)

            if (_uiState.value.isDailyChallengeMode && isCorrect) {
                val todayStr = getTodayDateString()
                val bonusCoins = 100
                val totalCoins = _uiState.value.coins + bonusCoins
                preferences.setDailyChallengeCompleted(todayStr)
                preferences.updateCoins(totalCoins)
                _uiState.value = _uiState.value.copy(
                    coins = totalCoins,
                    coinsEarnedInSession = _uiState.value.coinsEarnedInSession + bonusCoins,
                    isDailyChallengeAvailableToday = false
                )
                onGameOver()
            } else if (newLives <= 0) {
                onGameOver()
            } else {
                if (isCorrect) {
                    if (_uiState.value.isHardcoreMode) {
                        val nextLvl = _uiState.value.selectedLevel + 1
                        _uiState.value = _uiState.value.copy(selectedLevel = nextLvl, currentLevel = nextLvl)
                        loadQuestionForStageLevel(5, nextLvl)
                    } else if (currentLevel < 50) {
                        val nextLvl = currentLevel + 1
                        _uiState.value = _uiState.value.copy(selectedLevel = nextLvl, currentLevel = nextLvl)
                        loadQuestionForStageLevel(currentStage, nextLvl)
                    } else {
                        onGameOver()
                    }
                } else {
                    loadQuestionForStageLevel(currentStage, currentLevel)
                }
            }
        }
    }

    fun useHint() {
        val currentQState = _uiState.value.currentQuestionState ?: return
        if (currentQState.selectedOption != null) return

        val cost = 100
        if (_uiState.value.coins < cost) {
            emitEvent(GameEvent.ShowSnackbar("Not enough coins! Need 100 🪙"))
            return
        }

        val incorrectOptions = currentQState.shuffledOptions.filter {
            it != currentQState.question.correctAnswer && !currentQState.disabledOptions.contains(it)
        }

        if (incorrectOptions.isEmpty()) return

        val optionToRemove = incorrectOptions.random()
        val newDisabled = currentQState.disabledOptions + optionToRemove
        val updatedCoins = _uiState.value.coins - cost

        emitEvent(GameEvent.PerformClickHaptic)
        emitEvent(GameEvent.PlayHintSound)

        _uiState.value = _uiState.value.copy(
            coins = updatedCoins,
            currentQuestionState = currentQState.copy(disabledOptions = newDisabled)
        )

        viewModelScope.launch {
            preferences.updateCoins(updatedCoins)
        }
    }

    private fun onGameOver() {
        cancelTimers()
        emitEvent(GameEvent.PlayGameOverSound)

        val state = _uiState.value
        val isNewBest = state.score > state.bestScore
        val newBest = if (isNewBest) state.score else state.bestScore

        _uiState.value = state.copy(
            currentScreen = GameScreenState.GameOver,
            bestScore = newBest
        )

        viewModelScope.launch {
            preferences.recordGameFinished(
                score = state.score,
                correctCount = state.correctAnswersInSession,
                questionsCount = state.questionsAnsweredInSession,
                maxStreak = state.highestStreakInSession,
                coinsEarned = 0
            )
        }
    }

    fun refillLivesWithAd() {
        val currentLives = _uiState.value.lives
        val newLives = (currentLives + 2).coerceAtMost(6)
        val now = System.currentTimeMillis()

        _uiState.value = _uiState.value.copy(
            lives = newLives,
            showRefillDialog = false
        )

        emitEvent(GameEvent.PlayRefillSound)
        emitEvent(GameEvent.ShowSnackbar("❤️ Refilled +2 Lives! ($newLives/6)"))

        viewModelScope.launch {
            preferences.updateLivesAndTimestamp(newLives, now)
        }
    }

    fun refillLivesWithCoins() {
        val cost = 500
        if (_uiState.value.coins < cost) {
            emitEvent(GameEvent.ShowSnackbar("Not enough coins! Need 500 🪙"))
            return
        }

        val newCoins = _uiState.value.coins - cost
        val now = System.currentTimeMillis()

        _uiState.value = _uiState.value.copy(
            coins = newCoins,
            lives = 6,
            showRefillDialog = false
        )

        emitEvent(GameEvent.PlayRefillSound)
        emitEvent(GameEvent.ShowSnackbar("❤️ Lives fully refilled for 500 coins!"))

        viewModelScope.launch {
            preferences.updateCoins(newCoins)
            preferences.updateLivesAndTimestamp(6, now)
        }
    }

    fun closeRefillDialog() {
        _uiState.value = _uiState.value.copy(showRefillDialog = false)
    }

    fun closeLevelUpDialog() {
        _uiState.value = _uiState.value.copy(showLevelUpDialog = false)
    }

    fun onRewardedAdCompleted() {
        refillLivesWithAd()
        if (_uiState.value.currentScreen == GameScreenState.GameOver) {
            _uiState.value = _uiState.value.copy(currentScreen = GameScreenState.Playing)
            loadQuestionForStageLevel(_uiState.value.selectedStage, _uiState.value.selectedLevel)
        }
    }

    fun returnToHome() {
        cancelTimers()
        emitEvent(GameEvent.PerformClickHaptic)
        emitEvent(GameEvent.PlayClickSound)
        _uiState.value = _uiState.value.copy(
            currentScreen = GameScreenState.Home,
            currentQuestionState = null
        )
    }

    fun toggleSound(enabled: Boolean) {
        viewModelScope.launch {
            preferences.setSoundEnabled(enabled)
        }
    }

    fun toggleBgm(enabled: Boolean) {
        viewModelScope.launch {
            preferences.setBgmEnabled(enabled)
        }
    }

    fun toggleVibration(enabled: Boolean) {
        viewModelScope.launch {
            preferences.setVibrationEnabled(enabled)
        }
    }

    fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }

    private fun emitEvent(event: GameEvent) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }
}
