package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Reminder
import com.example.ui.components.NonDiagnosticDisclaimerCard
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MeltdownRose
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun RemindersScreen(
    reminders: List<Reminder>,
    showNewReminderDialog: Boolean,
    onOpenNewReminderDialog: () -> Unit,
    onCloseNewReminderDialog: () -> Unit,
    onAddReminder: (String, String, String) -> Unit,
    onToggleReminder: (Reminder) -> Unit,
    onDeleteReminder: (Reminder) -> Unit
) {
    var reminderToDelete by remember { mutableStateOf<Reminder?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Routine Reminders",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Slate800,
                                letterSpacing = (-0.5).sp
                            )
                        )
                        Text(
                            text = "Visual warnings & transition prompts",
                            style = MaterialTheme.typography.bodySmall.copy(color = Slate500)
                        )
                    }

                    Button(
                        onClick = onOpenNewReminderDialog,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Slate900),
                        modifier = Modifier.testTag("reminder_add_button")
                    ) {
                        Text("+ New", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Quick Preset Suggestions
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 1.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "HELPFUL NEURO-AFFIRMING PRESETS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Slate400,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val presets = listOf(
                            Pair("5m Visual Timer Warning", "10 min before transition"),
                            Pair("Sensory Calm Break", "After school quiet time"),
                            Pair("Bedtime Sequence", "7:30 PM dim lights")
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            presets.forEach { preset ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Slate50)
                                        .clickable {
                                            onAddReminder(preset.first, "07:30 PM", "Daily")
                                        }
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(preset.first, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Slate800))
                                        Text(preset.second, style = MaterialTheme.typography.bodySmall.copy(color = Slate400, fontSize = 11.sp))
                                    }
                                    Text("+ Add", color = EmeraldPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "ACTIVE SCHEDULE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Slate400,
                        letterSpacing = 1.5.sp,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier.padding(start = 2.dp)
                )
            }

            if (reminders.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
                    ) {
                        Text(
                            text = "No custom reminders created yet. Tap '+ New' or choose a preset above.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Slate400),
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }
            } else {
                items(reminders, key = { it.id }) { reminder ->
                    ReminderItemCard(
                        reminder = reminder,
                        onToggle = { onToggleReminder(reminder) },
                        onDelete = { reminderToDelete = reminder }
                    )
                }
            }

            item {
                NonDiagnosticDisclaimerCard()
                Spacer(modifier = Modifier.height(72.dp))
            }
        }

        FloatingActionButton(
            onClick = onOpenNewReminderDialog,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("reminder_fab_button"),
            containerColor = Slate900,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Reminder")
        }
    }

    if (reminderToDelete != null) {
        AlertDialog(
            onDismissRequest = { reminderToDelete = null },
            title = { Text("Delete Reminder?", fontWeight = FontWeight.Bold) },
            text = { Text("Remove '${reminderToDelete?.title}' from your reminders list?") },
            confirmButton = {
                Button(
                    onClick = {
                        reminderToDelete?.let { onDeleteReminder(it) }
                        reminderToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MeltdownRose)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { reminderToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showNewReminderDialog) {
        NewReminderDialog(
            onDismiss = onCloseNewReminderDialog,
            onSave = onAddReminder
        )
    }
}

@Composable
private fun ReminderItemCard(
    reminder: Reminder,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val icon = when {
        reminder.title.contains("Bed", ignoreCase = true) || reminder.title.contains("Sleep", ignoreCase = true) -> "🌙"
        reminder.title.contains("Meal", ignoreCase = true) || reminder.title.contains("Snack", ignoreCase = true) -> "🍎"
        reminder.title.contains("Homework", ignoreCase = true) || reminder.title.contains("School", ignoreCase = true) -> "📚"
        reminder.title.contains("Sensory", ignoreCase = true) -> "🎧"
        else -> "⏰"
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("reminder_card_${reminder.id}"),
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
                        .size(42.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (reminder.isEnabled) IndigoContainer else Slate100),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = icon, fontSize = 18.sp)
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = reminder.isEnabled,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = EmeraldPrimary,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Slate200,
                        uncheckedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.testTag("switch_reminder_${reminder.id}")
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp).testTag("delete_reminder_${reminder.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = Slate400,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NewReminderDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("07:30 PM") }
    var repeat by remember { mutableStateOf("Daily") }

    val repeatOptions = listOf("Daily", "Weekdays", "Weekends", "Once")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Routine Reminder", fontWeight = FontWeight.Bold, color = Slate800) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Routine / Reminder Title") },
                    placeholder = { Text("e.g. 5-Min Playground Warning") },
                    modifier = Modifier.fillMaxWidth().testTag("new_reminder_title_input"),
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time") },
                    placeholder = { Text("07:30 PM") },
                    modifier = Modifier.fillMaxWidth().testTag("new_reminder_time_input"),
                    shape = RoundedCornerShape(14.dp)
                )

                Text("REPEAT CADENCE", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Slate400))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeatOptions.forEach { opt ->
                        val isSelected = repeat == opt
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { repeat = opt },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Slate900 else Slate100
                        ) {
                            Text(
                                text = opt,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Slate700,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(title, time.ifBlank { "07:30 PM" }, repeat)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Slate900),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.testTag("save_new_reminder_button")
            ) {
                Text("Add Reminder", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Slate500)
            }
        }
    )
}
