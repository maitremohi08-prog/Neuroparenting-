package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child_profile")
data class ChildProfile(
    @PrimaryKey val id: Int = 1,
    val nickname: String = "Charlie",
    val age: String = "6",
    val interests: String = "Trains, Minecraft, Dinosaurs, building blocks",
    val challenges: String = "Loud noises, unexpected transitions, starting homework",
    val commPref: String = "Verbal, but struggles to express feelings when overwhelmed",
    val sensoryPref: String = "Prefers weighted blankets and dim lighting; sensitive to clothing tags and siren sounds",
    val strategies: String = "5-minute visual timer warnings, deep breathing 'flower & candle', quiet corner reset",
    val routines: String = "Morning visual routine chart; consistent 7:30 PM bedtime steps"
)

@Entity(tableName = "situation_logs")
data class SituationLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String, // Transition, Sensory Difficulty, Meltdown, Communication, Sleep, Mealtime, Social, School
    val before: String, // What happened before (Triggers/Antecedent)
    val what: String, // What the child did (Behavior)
    val helped: String, // What helped (Intervention)
    val intensity: Int, // 1 to 5
    val date: String,
    val time: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val time: String, // e.g. "07:30 PM"
    val date: String = "Daily",
    val repeat: String = "Daily", // None, Daily, Weekdays, Weekly
    val isEnabled: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val role: String, // "user" or "ai"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
