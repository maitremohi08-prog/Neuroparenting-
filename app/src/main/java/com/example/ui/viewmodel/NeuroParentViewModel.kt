package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChatMessage
import com.example.data.model.ChildProfile
import com.example.data.model.Reminder
import com.example.data.model.SituationLog
import com.example.data.repository.NeuroParentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppScreen(val title: String) {
    WELCOME("Welcome"),
    HOME("Home"),
    AI_COACH("AI Coach"),
    CHILD_PROFILE("Child Profile"),
    SITUATION_LOG("Situation Log"),
    REMINDERS("Reminders"),
    INSIGHTS("Insights")
}

class NeuroParentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NeuroParentRepository.getInstance(application)

    private val _currentScreen = MutableStateFlow(AppScreen.WELCOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    val profile: StateFlow<ChildProfile?> = repository.profileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val logs: StateFlow<List<SituationLog>> = repository.logsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reminders: StateFlow<List<Reminder>> = repository.remindersFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessagesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _chatInput = MutableStateFlow("")
    val chatInput: StateFlow<String> = _chatInput.asStateFlow()

    private val _showSettingsDialog = MutableStateFlow(false)
    val showSettingsDialog: StateFlow<Boolean> = _showSettingsDialog.asStateFlow()

    private val _showNewLogDialog = MutableStateFlow(false)
    val showNewLogDialog: StateFlow<Boolean> = _showNewLogDialog.asStateFlow()

    private val _showNewReminderDialog = MutableStateFlow(false)
    val showNewReminderDialog: StateFlow<Boolean> = _showNewReminderDialog.asStateFlow()

    private val _infoBannerMessage = MutableStateFlow<String?>(null)
    val infoBannerMessage: StateFlow<String?> = _infoBannerMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureInitialized()
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun updateChatInput(text: String) {
        _chatInput.value = text
    }

    fun openSettings() {
        _showSettingsDialog.value = true
    }

    fun closeSettings() {
        _showSettingsDialog.value = false
    }

    fun openNewLogDialog() {
        _showNewLogDialog.value = true
    }

    fun closeNewLogDialog() {
        _showNewLogDialog.value = false
    }

    fun openNewReminderDialog() {
        _showNewReminderDialog.value = true
    }

    fun closeNewReminderDialog() {
        _showNewReminderDialog.value = false
    }

    fun dismissBanner() {
        _infoBannerMessage.value = null
    }

    fun sendChatMessage(customText: String? = null) {
        val textToSend = customText ?: _chatInput.value.trim()
        if (textToSend.isBlank()) return

        _chatInput.value = ""
        _isAiThinking.value = true

        val currentProfile = profile.value ?: ChildProfile()
        val history = chatMessages.value

        viewModelScope.launch {
            try {
                repository.sendMessageToCoach(textToSend, currentProfile, history)
            } catch (e: Exception) {
                _infoBannerMessage.value = "Unable to connect: ${e.message}"
            } finally {
                _isAiThinking.value = false
            }
        }
    }

    fun triggerQuickHelp(categoryName: String) {
        val childName = profile.value?.nickname?.ifBlank { "my child" } ?: "my child"
        val prompt = when (categoryName) {
            "Meltdown" -> "$childName is having a meltdown right now. What should I do immediately to help co-regulate safely?"
            "Sensory difficulty" -> "$childName is feeling overwhelmed by sensory input (loud sounds/lights). How can I support them right now?"
            "Transition difficulty" -> "$childName is struggling with transitioning away from an activity. What immediate strategies can help?"
            "Communication" -> "$childName is having trouble communicating what they need and getting frustrated. How can I bridge the communication gap?"
            "Sleep" -> "$childName is resisting bedtime and struggling to settle down. What calming routine or step should I try?"
            "Mealtime" -> "$childName is overwhelmed during mealtime and refusing to sit or eat. How can I make this less stressful?"
            else -> "I need immediate parenting support for $childName regarding $categoryName."
        }

        navigateTo(AppScreen.AI_COACH)
        sendChatMessage(prompt)
    }

    fun saveChildProfile(
        nickname: String,
        age: String,
        interests: String,
        challenges: String,
        commPref: String,
        sensoryPref: String,
        strategies: String,
        routines: String
    ) {
        viewModelScope.launch {
            repository.saveProfile(
                ChildProfile(
                    id = 1,
                    nickname = nickname.trim(),
                    age = age.trim(),
                    interests = interests.trim(),
                    challenges = challenges.trim(),
                    commPref = commPref.trim(),
                    sensoryPref = sensoryPref.trim(),
                    strategies = strategies.trim(),
                    routines = routines.trim()
                )
            )
            _infoBannerMessage.value = "Child Profile saved successfully"
        }
    }

    fun addSituationLog(
        category: String,
        before: String,
        what: String,
        helped: String,
        intensity: Int
    ) {
        val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val now = Date()

        val log = SituationLog(
            category = category,
            before = before.trim(),
            what = what.trim(),
            helped = helped.trim(),
            intensity = intensity,
            date = dateFormat.format(now),
            time = timeFormat.format(now),
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.insertLog(log)
            _showNewLogDialog.value = false
            _infoBannerMessage.value = "Situation logged successfully"
        }
    }

    fun deleteSituationLog(log: SituationLog) {
        viewModelScope.launch {
            repository.deleteLog(log)
            _infoBannerMessage.value = "Log entry removed"
        }
    }

    fun addReminder(
        title: String,
        time: String,
        repeat: String,
        date: String = "Daily"
    ) {
        val reminder = Reminder(
            title = title.trim(),
            time = time.trim(),
            date = date.trim(),
            repeat = repeat.trim(),
            isEnabled = true,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.insertReminder(reminder)
            _showNewReminderDialog.value = false
            _infoBannerMessage.value = "Reminder created"
        }
    }

    fun toggleReminder(reminder: Reminder) {
        viewModelScope.launch {
            repository.updateReminder(reminder.copy(isEnabled = !reminder.isEnabled))
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
            _infoBannerMessage.value = "Reminder removed"
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChatHistory()
            _infoBannerMessage.value = "Chat reset"
        }
    }

    fun deleteChildData() {
        viewModelScope.launch {
            repository.deleteAllChildData()
            _showSettingsDialog.value = false
            _infoBannerMessage.value = "All child data deleted securely"
        }
    }
}
