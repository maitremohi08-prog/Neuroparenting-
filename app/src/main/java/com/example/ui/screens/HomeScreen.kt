package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildProfile
import com.example.data.model.Reminder
import com.example.data.model.SituationLog
import com.example.ui.components.NonDiagnosticDisclaimerCard
import com.example.ui.theme.CommCyan
import com.example.ui.theme.CommCyanBg
import com.example.ui.theme.CommCyanBorder
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MealtimeOrange
import com.example.ui.theme.MealtimeOrangeBg
import com.example.ui.theme.MealtimeOrangeBorder
import com.example.ui.theme.MeltdownRose
import com.example.ui.theme.MeltdownRoseBg
import com.example.ui.theme.MeltdownRoseBorder
import com.example.ui.theme.SensoryAmber
import com.example.ui.theme.SensoryAmberBg
import com.example.ui.theme.SensoryAmberBorder
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.SleepIndigo
import com.example.ui.theme.SleepIndigoBg
import com.example.ui.theme.SleepIndigoBorder
import com.example.ui.theme.TransitionEmerald
import com.example.ui.theme.TransitionEmeraldBg
import com.example.ui.theme.TransitionEmeraldBorder

@Composable
fun HomeScreen(
    profile: ChildProfile?,
    reminders: List<Reminder>,
    recentLogs: List<SituationLog>,
    onAskAiClick: () -> Unit,
    onQuickHelpClick: (String) -> Unit,
    onLogSituationClick: () -> Unit,
    onViewAllRemindersClick: () -> Unit,
    onToggleReminder: (Reminder) -> Unit,
    onAddNewReminderClick: () -> Unit,
    onViewAllLogsClick: () -> Unit
) {
    val childName = profile?.nickname?.ifBlank { "Leo" } ?: "Leo"

    // Subtle pulse animation for live AI status
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(2.dp))
        }

        // Primary AI Action Card (Sleek Interface hero component)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .clickable { onAskAiClick() }
                    .testTag("home_ask_ai_card"),
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFECFDF5))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF6366F1).copy(alpha = pulseAlpha))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Ask NeuroParent AI",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Slate800,
                                        fontSize = 17.sp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Get supportive strategies for the current moment.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Slate500,
                                    lineHeight = 18.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Indigo 600 action icon badge
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(IndigoPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChatBubble,
                                contentDescription = "Chat with AI",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Safety First micro banner inside card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Slate50
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SAFETY FIRST",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate400,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Non-diagnostic educational support",
                                fontSize = 11.sp,
                                color = Slate500,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Quick Help Section (Immediate Help in Sleek Interface 3-col grid)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "IMMEDIATE HELP",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Slate400,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier.padding(start = 2.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                val row1 = listOf(
                    SleekHelpCategory("Meltdown", "❤️", Icons.Default.Favorite, MeltdownRose, MeltdownRoseBg, MeltdownRoseBorder),
                    SleekHelpCategory("Sensory", "🎧", Icons.Default.Headphones, SensoryAmber, SensoryAmberBg, SensoryAmberBorder),
                    SleekHelpCategory("Transition", "⏰", Icons.Default.Schedule, TransitionEmerald, TransitionEmeraldBg, TransitionEmeraldBorder)
                )
                val row2 = listOf(
                    SleekHelpCategory("Comm", "💬", Icons.Default.RecordVoiceOver, CommCyan, CommCyanBg, CommCyanBorder),
                    SleekHelpCategory("Sleep", "🌙", Icons.Default.Bedtime, SleepIndigo, SleepIndigoBg, SleepIndigoBorder),
                    SleekHelpCategory("Mealtime", "🍎", Icons.Default.Restaurant, MealtimeOrange, MealtimeOrangeBg, MealtimeOrangeBorder)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row1.forEach { cat ->
                        SleekQuickHelpTile(
                            category = cat,
                            modifier = Modifier.weight(1f),
                            onClick = { onQuickHelpClick(cat.title) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row2.forEach { cat ->
                        SleekQuickHelpTile(
                            category = cat,
                            modifier = Modifier.weight(1f),
                            onClick = { onQuickHelpClick(cat.title) }
                        )
                    }
                }
            }
        }

        // Today's Schedule Section (Sleek Interface Reminders Card Stack)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TODAY'S SCHEDULE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Slate400,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            fontSize = 11.sp
                        )
                    )
                    Text(
                        text = "SEE ALL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = EmeraldPrimary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.clickable { onViewAllRemindersClick() }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                val scheduleItems = reminders.take(2)
                if (scheduleItems.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
                    ) {
                        Text(
                            text = "No scheduled routines for today. Tap 'See All' to configure transition warnings.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Slate400),
                            modifier = Modifier.padding(18.dp)
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        scheduleItems.forEachIndexed { index, reminder ->
                            val iconEmoji = if (index == 0) "🌙" else "🍎"
                            val iconBg = if (index == 0) Color(0xFFEEF2FF) else Color(0xFFF8FAFC)
                            val iconTint = if (index == 0) IndigoPrimary else Slate400

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("home_reminder_item_${reminder.id}"),
                                shape = RoundedCornerShape(24.dp),
                                color = Color.White,
                                shadowElevation = 1.dp,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(iconBg),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = iconEmoji, fontSize = 18.sp)
                                        }

                                        Spacer(modifier = Modifier.width(14.dp))

                                        Column {
                                            Text(
                                                text = reminder.title,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (reminder.isEnabled) Slate800 else Slate400
                                                )
                                            )
                                            Text(
                                                text = "${reminder.time} • ${reminder.repeat}",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Slate400,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 12.sp
                                                )
                                            )
                                        }
                                    }

                                    Switch(
                                        checked = reminder.isEnabled,
                                        onCheckedChange = { onToggleReminder(reminder) },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = EmeraldPrimary,
                                            uncheckedThumbColor = Color.White,
                                            uncheckedTrackColor = Slate200,
                                            uncheckedBorderColor = Color.Transparent
                                        ),
                                        modifier = Modifier.testTag("home_schedule_switch_${reminder.id}")
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Log Hero Button (Sleek Interface dark button styling)
        item {
            Button(
                onClick = onLogSituationClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("home_quick_log_btn"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Slate900),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "+",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Log a Situation",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }

        item {
            NonDiagnosticDisclaimerCard()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

data class SleekHelpCategory(
    val title: String,
    val emoji: String,
    val icon: ImageVector,
    val color: Color,
    val bgColor: Color,
    val borderColor: Color
)

@Composable
private fun SleekQuickHelpTile(
    category: SleekHelpCategory,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag("quick_help_${category.title.lowercase()}"),
        shape = RoundedCornerShape(20.dp),
        color = category.bgColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, category.borderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = category.emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = category.title.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = category.color,
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}
