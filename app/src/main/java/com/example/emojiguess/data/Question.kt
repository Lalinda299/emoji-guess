package com.example.emojiguess.data

data class Question(
    val id: Int,
    val emojiCombination: String,
    val correctAnswer: String,
    val options: List<String>,
    val category: String,
    val level: Int = 1
)
