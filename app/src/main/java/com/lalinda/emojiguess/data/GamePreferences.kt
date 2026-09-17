package com.lalinda.emojiguess.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "emoji_guess_prefs")

data class UserPreferences(
    val bestScore: Int = 0,
    val coins: Int = 500,
    val gamesPlayed: Int = 0,
    val totalCorrectAnswers: Int = 0,
    val totalQuestions: Int = 0,
    val highestStreak: Int = 0,
    val soundEnabled: Boolean = true,
    val bgmEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val dailyChallengeCompletedDate: String = "",
    val lives: Int = 6,
    val lastLifeTimestamp: Long = 0L,
    val playerXp: Int = 0,
    val unlockedStage: Int = 1,
    val levelStarsEncoded: String = "s1_l1:0" // format: s1_l1:3,s1_l2:2
)

class GamePreferences(private val context: Context) {

    private object PreferencesKeys {
        val BEST_SCORE = intPreferencesKey("best_score")
        val COINS = intPreferencesKey("coins")
        val GAMES_PLAYED = intPreferencesKey("games_played")
        val TOTAL_CORRECT_ANSWERS = intPreferencesKey("total_correct_answers")
        val TOTAL_QUESTIONS = intPreferencesKey("total_questions")
        val HIGHEST_STREAK = intPreferencesKey("highest_streak")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val BGM_ENABLED = booleanPreferencesKey("bgm_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val DAILY_CHALLENGE_DATE = stringPreferencesKey("daily_challenge_completed_date")
        val LIVES = intPreferencesKey("lives")
        val LAST_LIFE_TIMESTAMP = longPreferencesKey("last_life_timestamp")
        val PLAYER_XP = intPreferencesKey("player_xp")
        val UNLOCKED_STAGE = intPreferencesKey("unlocked_stage")
        val LEVEL_STARS_ENCODED = stringPreferencesKey("level_stars_encoded")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(androidx.datastore.preferences.core.emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserPreferences(
                bestScore = preferences[PreferencesKeys.BEST_SCORE] ?: 0,
                coins = preferences[PreferencesKeys.COINS] ?: 500,
                gamesPlayed = preferences[PreferencesKeys.GAMES_PLAYED] ?: 0,
                totalCorrectAnswers = preferences[PreferencesKeys.TOTAL_CORRECT_ANSWERS] ?: 0,
                totalQuestions = preferences[PreferencesKeys.TOTAL_QUESTIONS] ?: 0,
                highestStreak = preferences[PreferencesKeys.HIGHEST_STREAK] ?: 0,
                soundEnabled = preferences[PreferencesKeys.SOUND_ENABLED] ?: true,
                bgmEnabled = preferences[PreferencesKeys.BGM_ENABLED] ?: true,
                vibrationEnabled = preferences[PreferencesKeys.VIBRATION_ENABLED] ?: true,
                dailyChallengeCompletedDate = preferences[PreferencesKeys.DAILY_CHALLENGE_DATE] ?: "",
                lives = preferences[PreferencesKeys.LIVES] ?: 6,
                lastLifeTimestamp = preferences[PreferencesKeys.LAST_LIFE_TIMESTAMP] ?: 0L,
                playerXp = preferences[PreferencesKeys.PLAYER_XP] ?: 0,
                unlockedStage = preferences[PreferencesKeys.UNLOCKED_STAGE] ?: 1,
                levelStarsEncoded = preferences[PreferencesKeys.LEVEL_STARS_ENCODED] ?: "s1_l1:0"
            )
        }

    suspend fun updateBestScore(newScore: Int) {
        context.dataStore.edit { preferences ->
            val currentBest = preferences[PreferencesKeys.BEST_SCORE] ?: 0
            if (newScore > currentBest) {
                preferences[PreferencesKeys.BEST_SCORE] = newScore
            }
        }
    }

    suspend fun updateCoins(newCoins: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.COINS] = newCoins.coerceAtLeast(0)
        }
    }

    suspend fun updateLivesAndTimestamp(lives: Int, timestamp: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LIVES] = lives.coerceIn(0, 6)
            preferences[PreferencesKeys.LAST_LIFE_TIMESTAMP] = timestamp
        }
    }

    suspend fun addPlayerXp(additionalXp: Int) {
        context.dataStore.edit { preferences ->
            val currentXp = preferences[PreferencesKeys.PLAYER_XP] ?: 0
            preferences[PreferencesKeys.PLAYER_XP] = currentXp + additionalXp
        }
    }

    suspend fun saveLevelProgress(stage: Int, level: Int, stars: Int, newUnlockedStage: Int) {
        context.dataStore.edit { preferences ->
            val currentEncoded = preferences[PreferencesKeys.LEVEL_STARS_ENCODED] ?: "s1_l1:0"
            val map = currentEncoded.split(",")
                .mapNotNull {
                    val parts = it.split(":")
                    if (parts.size == 2) parts[0] to (parts[1].toIntOrNull() ?: 0) else null
                }.toMap().toMutableMap()

            val key = "s${stage}_l${level}"
            val existingStars = map[key] ?: 0
            if (stars > existingStars) {
                map[key] = stars
            }

            val nextKey = if (level < 50) "s${stage}_l${level + 1}" else "s${stage + 1}_l1"
            if (!map.containsKey(nextKey)) {
                map[nextKey] = 0
            }

            val updatedEncoded = map.entries.joinToString(",") { "${it.key}:${it.value}" }
            preferences[PreferencesKeys.LEVEL_STARS_ENCODED] = updatedEncoded

            val currentUnlocked = preferences[PreferencesKeys.UNLOCKED_STAGE] ?: 1
            if (newUnlockedStage > currentUnlocked) {
                preferences[PreferencesKeys.UNLOCKED_STAGE] = newUnlockedStage.coerceAtMost(5)
            }
        }
    }

    suspend fun recordGameFinished(
        score: Int,
        correctCount: Int,
        questionsCount: Int,
        maxStreak: Int,
        coinsEarned: Int
    ) {
        context.dataStore.edit { preferences ->
            val currentBest = preferences[PreferencesKeys.BEST_SCORE] ?: 0
            if (score > currentBest) {
                preferences[PreferencesKeys.BEST_SCORE] = score
            }

            val currentCoins = preferences[PreferencesKeys.COINS] ?: 500
            preferences[PreferencesKeys.COINS] = (currentCoins + coinsEarned).coerceAtLeast(0)

            val currentGames = preferences[PreferencesKeys.GAMES_PLAYED] ?: 0
            preferences[PreferencesKeys.GAMES_PLAYED] = currentGames + 1

            val currentCorrect = preferences[PreferencesKeys.TOTAL_CORRECT_ANSWERS] ?: 0
            preferences[PreferencesKeys.TOTAL_CORRECT_ANSWERS] = currentCorrect + correctCount

            val currentTotalQuestions = preferences[PreferencesKeys.TOTAL_QUESTIONS] ?: 0
            preferences[PreferencesKeys.TOTAL_QUESTIONS] = currentTotalQuestions + questionsCount

            val currentHighestStreak = preferences[PreferencesKeys.HIGHEST_STREAK] ?: 0
            if (maxStreak > currentHighestStreak) {
                preferences[PreferencesKeys.HIGHEST_STREAK] = maxStreak
            }
        }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_ENABLED] = enabled
        }
    }

    suspend fun setBgmEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.BGM_ENABLED] = enabled
        }
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.VIBRATION_ENABLED] = enabled
        }
    }

    suspend fun setDailyChallengeCompleted(dateStr: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DAILY_CHALLENGE_DATE] = dateStr
        }
    }
}

