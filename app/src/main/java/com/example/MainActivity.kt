package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.ui.components.NeuroBottomBar
import com.example.ui.components.NeuroTopBar
import com.example.ui.screens.AiCoachScreen
import com.example.ui.screens.ChildProfileScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InsightsScreen
import com.example.ui.screens.RemindersScreen
import com.example.ui.screens.SettingsDialog
import com.example.ui.screens.SituationLogScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.NeuroParentViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: NeuroParentViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        NeuroParentApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun NeuroParentApp(viewModel: NeuroParentViewModel) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val profile by viewModel.profile.collectAsState()
  val logs by viewModel.logs.collectAsState()
  val reminders by viewModel.reminders.collectAsState()
  val chatMessages by viewModel.chatMessages.collectAsState()
  val isThinking by viewModel.isAiThinking.collectAsState()
  val chatInput by viewModel.chatInput.collectAsState()
  val showSettings by viewModel.showSettingsDialog.collectAsState()
  val showNewLogDialog by viewModel.showNewLogDialog.collectAsState()
  val showNewReminderDialog by viewModel.showNewReminderDialog.collectAsState()
  val infoBannerMessage by viewModel.infoBannerMessage.collectAsState()

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(infoBannerMessage) {
    infoBannerMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.dismissBanner()
    }
  }

  val childName = profile?.nickname?.ifBlank { "Child" } ?: "Child"

  Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
        if (currentScreen != AppScreen.WELCOME) {
          NeuroTopBar(
            title = currentScreen.title,
            childName = childName,
            onProfileClick = { viewModel.navigateTo(AppScreen.CHILD_PROFILE) },
            onSettingsClick = { viewModel.openSettings() }
          )
        }
      },
      bottomBar = {
        if (currentScreen != AppScreen.WELCOME && currentScreen != AppScreen.CHILD_PROFILE) {
          NeuroBottomBar(
            currentScreen = currentScreen,
            onTabSelected = { screen -> viewModel.navigateTo(screen) }
          )
        }
      },
      snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
      Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
        Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
          when (screen) {
            AppScreen.WELCOME -> {
              WelcomeScreen(
                onGetStarted = { viewModel.navigateTo(AppScreen.CHILD_PROFILE) },
                onSignIn = { viewModel.navigateTo(AppScreen.HOME) }
              )
            }

            AppScreen.HOME -> {
              HomeScreen(
                profile = profile,
                reminders = reminders,
                recentLogs = logs,
                onAskAiClick = { viewModel.navigateTo(AppScreen.AI_COACH) },
                onQuickHelpClick = { category -> viewModel.triggerQuickHelp(category) },
                onLogSituationClick = {
                  viewModel.navigateTo(AppScreen.SITUATION_LOG)
                  viewModel.openNewLogDialog()
                },
                onViewAllRemindersClick = { viewModel.navigateTo(AppScreen.REMINDERS) },
                onToggleReminder = { reminder -> viewModel.toggleReminder(reminder) },
                onAddNewReminderClick = {
                  viewModel.navigateTo(AppScreen.REMINDERS)
                  viewModel.openNewReminderDialog()
                },
                onViewAllLogsClick = { viewModel.navigateTo(AppScreen.SITUATION_LOG) }
              )
            }

            AppScreen.AI_COACH -> {
              AiCoachScreen(
                profile = profile,
                messages = chatMessages,
                isThinking = isThinking,
                inputText = chatInput,
                onInputChange = { viewModel.updateChatInput(it) },
                onSendMessage = { customPrompt -> viewModel.sendChatMessage(customPrompt) },
                onClearChat = { viewModel.clearChat() }
              )
            }

            AppScreen.CHILD_PROFILE -> {
              ChildProfileScreen(
                profile = profile,
                onSaveProfile = { nickname, age, interests, challenges, commPref, sensoryPref, strategies, routines ->
                  viewModel.saveChildProfile(nickname, age, interests, challenges, commPref, sensoryPref, strategies, routines)
                },
                onSkipOrBack = { viewModel.navigateTo(AppScreen.HOME) }
              )
            }

            AppScreen.SITUATION_LOG -> {
              SituationLogScreen(
                logs = logs,
                showNewLogDialog = showNewLogDialog,
                onOpenNewLogDialog = { viewModel.openNewLogDialog() },
                onCloseNewLogDialog = { viewModel.closeNewLogDialog() },
                onAddLog = { cat, before, what, helped, intensity ->
                  viewModel.addSituationLog(cat, before, what, helped, intensity)
                },
                onDeleteLog = { log -> viewModel.deleteSituationLog(log) }
              )
            }

            AppScreen.REMINDERS -> {
              RemindersScreen(
                reminders = reminders,
                showNewReminderDialog = showNewReminderDialog,
                onOpenNewReminderDialog = { viewModel.openNewReminderDialog() },
                onCloseNewReminderDialog = { viewModel.closeNewReminderDialog() },
                onAddReminder = { title, time, repeat ->
                  viewModel.addReminder(title, time, repeat)
                },
                onToggleReminder = { reminder -> viewModel.toggleReminder(reminder) },
                onDeleteReminder = { reminder -> viewModel.deleteReminder(reminder) }
              )
            }

            AppScreen.INSIGHTS -> {
              InsightsScreen(
                profile = profile,
                logs = logs
              )
            }
          }
        }
      }
    }

    if (showSettings) {
      SettingsDialog(
        onDismiss = { viewModel.closeSettings() },
        onDeleteChildData = { viewModel.deleteChildData() }
      )
    }
  }
}

