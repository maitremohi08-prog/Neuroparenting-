package com.example.data.repository

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.api.AiCoachEngine
import com.example.data.db.AppDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.ChildProfile
import com.example.data.model.Reminder
import com.example.data.model.SituationLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class NeuroParentRepository private constructor(context: Context) {

    private val db: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "neuroparent.db"
    ).addCallback(object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Prepopulate default sample data in background
            CoroutineScope(Dispatchers.IO).launch {
                prepopulateDefaults()
            }
        }
    }).build()

    private val profileDao = db.childProfileDao()
    private val logDao = db.situationLogDao()
    private val reminderDao = db.reminderDao()
    private val chatDao = db.chatMessageDao()

    private val aiCoachEngine = AiCoachEngine()

    val profileFlow: Flow<ChildProfile?> = profileDao.getProfile()
    val logsFlow: Flow<List<SituationLog>> = logDao.getAllLogs()
    val remindersFlow: Flow<List<Reminder>> = reminderDao.getAllReminders()
    val chatMessagesFlow: Flow<List<ChatMessage>> = chatDao.getAllMessages()

    suspend fun ensureInitialized() {
        val currentProfile = profileDao.getProfile().firstOrNull()
        if (currentProfile == null) {
            prepopulateDefaults()
        }
    }

    private suspend fun prepopulateDefaults() {
        profileDao.saveProfile(
            ChildProfile(
                id = 1,
                nickname = "Charlie",
                age = "6",
                interests = "Trains, Minecraft, Dinosaurs, building blocks",
                challenges = "Loud noises, unexpected transitions, starting homework",
                commPref = "Verbal, but struggles to express feelings when overwhelmed",
                sensoryPref = "Prefers weighted blankets and dim lighting; sensitive to clothing tags and siren sounds",
                strategies = "5-minute visual timer warnings, deep breathing 'flower & candle', quiet corner reset",
                routines = "Morning visual routine chart; consistent 7:30 PM bedtime steps"
            )
        )

        // Seed realistic situation logs
        val sampleLogs = listOf(
            SituationLog(
                category = "Transition",
                before = "Time to leave the playground to go home for lunch",
                what = "Cried, sat down on the ground, and refused to walk to the car",
                helped = "Used 5-minute visual countdown timer on phone and let him hold his toy dinosaur",
                intensity = 3,
                date = "Today",
                time = "11:45 AM",
                timestamp = System.currentTimeMillis() - 3600000 * 4
            ),
            SituationLog(
                category = "Sensory Difficulty",
                before = "Vacuum cleaner was turned on in the living room",
                what = "Covered ears, ran to bedroom, and hid under bed",
                helped = "Offered noise-cancelling headphones and turned off vacuum immediately",
                intensity = 4,
                date = "Yesterday",
                time = "02:15 PM",
                timestamp = System.currentTimeMillis() - 3600000 * 24
            ),
            SituationLog(
                category = "Transition",
                before = "Told to shut off tablet for dinner",
                what = "Threw tablet cushion, yelled 'I am not finished!'",
                helped = "Gave a 2-minute bridge warning and gave him a role: carry the napkins to table",
                intensity = 3,
                date = "2 days ago",
                time = "06:20 PM",
                timestamp = System.currentTimeMillis() - 3600000 * 48
            ),
            SituationLog(
                category = "Mealtime",
                before = "Broccoli was placed touching the mac and cheese",
                what = "Pushed plate away and refused to sit at table",
                helped = "Used a divided plate with separate compartments, served safe crackers",
                intensity = 2,
                date = "3 days ago",
                time = "05:45 PM",
                timestamp = System.currentTimeMillis() - 3600000 * 72
            )
        )
        sampleLogs.forEach { logDao.insertLog(it) }

        // Seed reminders
        val sampleReminders = listOf(
            Reminder(
                title = "Start bedtime routine",
                time = "07:30 PM",
                date = "Daily",
                repeat = "Daily",
                isEnabled = true
            ),
            Reminder(
                title = "10-minute transition warning before dinner",
                time = "05:50 PM",
                date = "Daily",
                repeat = "Daily",
                isEnabled = true
            ),
            Reminder(
                title = "Homework micro-step check-in",
                time = "04:00 PM",
                date = "Weekdays",
                repeat = "Weekdays",
                isEnabled = true
            ),
            Reminder(
                title = "Sensory reset & hydration break",
                time = "02:30 PM",
                date = "Daily",
                repeat = "Daily",
                isEnabled = false
            )
        )
        sampleReminders.forEach { reminderDao.insertReminder(it) }

        // Seed welcome chat message
        chatDao.insertMessage(
            ChatMessage(
                role = "ai",
                content = "Hello! I'm your NeuroParent AI Coach. How can I support you and Charlie today? Feel free to describe any situation or pick a Quick Help topic."
            )
        )
    }

    suspend fun saveProfile(profile: ChildProfile) {
        profileDao.saveProfile(profile)
    }

    suspend fun insertLog(log: SituationLog) {
        logDao.insertLog(log)
    }

    suspend fun deleteLog(log: SituationLog) {
        logDao.deleteLog(log)
    }

    suspend fun insertReminder(reminder: Reminder) {
        reminderDao.insertReminder(reminder)
    }

    suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder)
    }

    suspend fun sendMessageToCoach(userText: String, profile: ChildProfile, history: List<ChatMessage>): String {
        // Save user message
        chatDao.insertMessage(ChatMessage(role = "user", content = userText))

        // Get AI response
        val aiResponse = aiCoachEngine.getCoachResponse(userText, profile, history)

        // Save AI response
        chatDao.insertMessage(ChatMessage(role = "ai", content = aiResponse))

        return aiResponse
    }

    suspend fun clearChatHistory() {
        chatDao.clearAllMessages()
        val currentProfile = profileDao.getProfile().firstOrNull()
        val childName = currentProfile?.nickname ?: "your child"
        chatDao.insertMessage(
            ChatMessage(
                role = "ai",
                content = "Chat refreshed. How can I assist you with $childName right now?"
            )
        )
    }

    suspend fun deleteAllChildData() {
        logDao.clearAllLogs()
        reminderDao.clearAllReminders()
        chatDao.clearAllMessages()
        profileDao.saveProfile(
            ChildProfile(
                id = 1,
                nickname = "",
                age = "",
                interests = "",
                challenges = "",
                commPref = "",
                sensoryPref = "",
                strategies = "",
                routines = ""
            )
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: NeuroParentRepository? = null

        fun getInstance(context: Context): NeuroParentRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NeuroParentRepository(context).also { INSTANCE = it }
            }
        }
    }
}
