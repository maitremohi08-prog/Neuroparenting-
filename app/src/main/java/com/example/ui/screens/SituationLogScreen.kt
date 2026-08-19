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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.data.model.SituationLog
import com.example.ui.components.NonDiagnosticDisclaimerCard
import com.example.ui.theme.CommCyan
import com.example.ui.theme.CommCyanBg
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.MealtimeOrange
import com.example.ui.theme.MealtimeOrangeBg
import com.example.ui.theme.MeltdownRose
import com.example.ui.theme.MeltdownRoseBg
import com.example.ui.theme.SensoryAmber
import com.example.ui.theme.SensoryAmberBg
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
import com.example.ui.theme.TransitionEmerald
import com.example.ui.theme.TransitionEmeraldBg

@Composable
fun SituationLogScreen(
    logs: List<SituationLog>,
    showNewLogDialog: Boolean,
    onOpenNewLogDialog: () -> Unit,
    onCloseNewLogDialog: () -> Unit,
    onAddLog: (String, String, String, String, Int) -> Unit,
    onDeleteLog: (SituationLog) -> Unit
) {
    var selectedCategoryFilter by remember { mutableStateOf("All") }
    var logToDelete by remember { mutableStateOf<SituationLog?>(null) }

    val categories = listOf("All", "Transition", "Sensory Difficulty", "Meltdown", "Communication", "Sleep", "Mealtime", "School")

    val filteredLogs = if (selectedCategoryFilter == "All") {
        logs
    } else {
        logs.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }
    }

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
                            text = "Situation Log",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Slate800,
                                letterSpacing = (-0.5).sp
                            )
                        )
                        Text(
                            text = "Antecedents, behaviors & interventions",
                            style = MaterialTheme.typography.bodySmall.copy(color = Slate500)
                        )
                    }

                    Button(
                        onClick = onOpenNewLogDialog,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Slate900),
                        modifier = Modifier.testTag("log_add_new_button")
                    ) {
                        Text("+ Log", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Category Filter Chips (Sleek pill style)
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategoryFilter == category
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { selectedCategoryFilter = category }
                                .testTag("log_filter_$category"),
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) Slate900 else Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Slate900 else Slate200)
                        ) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Slate700
                                ),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            if (filteredLogs.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "📝", fontSize = 32.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No logged situations in this category",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Slate800),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Logging moments helps identify triggers and what co-regulation tools work best.",
                                style = MaterialTheme.typography.bodySmall.copy(color = Slate400),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(filteredLogs, key = { it.id }) { log ->
                    SituationLogCard(
                        log = log,
                        onDeleteClick = { logToDelete = log }
                    )
                }
            }

            item {
                NonDiagnosticDisclaimerCard()
                Spacer(modifier = Modifier.height(72.dp))
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = onOpenNewLogDialog,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("log_fab_button"),
            containerColor = Slate900,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Log Situation")
        }
    }

    if (logToDelete != null) {
        AlertDialog(
            onDismissRequest = { logToDelete = null },
            title = { Text("Delete Log Entry?", fontWeight = FontWeight.Bold) },
            text = { Text("Remove this situation log entry from your local history?") },
            confirmButton = {
                Button(
                    onClick = {
                        logToDelete?.let { onDeleteLog(it) }
                        logToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MeltdownRose)
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { logToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showNewLogDialog) {
        NewSituationLogDialog(
            onDismiss = onCloseNewLogDialog,
            onSave = onAddLog
        )
    }
}

@Composable
private fun SituationLogCard(
    log: SituationLog,
    onDeleteClick: () -> Unit
) {
    val (categoryColor, categoryBg) = when (log.category) {
        "Meltdown" -> Pair(MeltdownRose, MeltdownRoseBg)
        "Sensory Difficulty" -> Pair(SensoryAmber, SensoryAmberBg)
        "Transition" -> Pair(TransitionEmerald, TransitionEmeraldBg)
        "Communication" -> Pair(CommCyan, CommCyanBg)
        "Sleep" -> Pair(SleepIndigo, SleepIndigoBg)
        "Mealtime" -> Pair(MealtimeOrange, MealtimeOrangeBg)
        else -> Pair(Slate800, Slate100)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("situation_log_card_${log.id}"),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Card Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = categoryBg
                    ) {
                        Text(
                            text = log.category.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = categoryColor,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    val intensityLabel = when (log.intensity) {
                        1 -> "Mild (1/5)"
                        2 -> "Moderate (2/5)"
                        3 -> "Elevated (3/5)"
                        4 -> "High (4/5)"
                        else -> "Peak (5/5)"
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Slate100
                    ) {
                        Text(
                            text = intensityLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate500,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${log.date}, ${log.time}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    )
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(28.dp).testTag("delete_log_${log.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Log",
                            tint = Slate400,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Antecedent
            LogSectionItem(
                label = "Trigger / Before:",
                text = log.before,
                bulletEmoji = "⚡"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Behavior
            LogSectionItem(
                label = "Child's Response:",
                text = log.what,
                bulletEmoji = "💭"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // What helped
            LogSectionItem(
                label = "What Helped:",
                text = log.helped,
                bulletEmoji = "🌱"
            )
        }
    }
}

@Composable
private fun LogSectionItem(
    label: String,
    text: String,
    bulletEmoji: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = bulletEmoji, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Slate400
                )
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.sp,
                    color = Slate800
                )
            )
        }
    }
}

@Composable
fun NewSituationLogDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, Int) -> Unit
) {
    var category by remember { mutableStateOf("Transition") }
    var before by remember { mutableStateOf("") }
    var what by remember { mutableStateOf("") }
    var helped by remember { mutableStateOf("") }
    var intensity by remember { mutableFloatStateOf(3f) }

    val categories = listOf("Transition", "Sensory Difficulty", "Meltdown", "Communication", "Sleep", "Mealtime", "School")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Log a Situation",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Slate800)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "CATEGORY",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Slate400)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        val isSelected = category == cat
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { category = cat },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Slate900 else Slate100
                        ) {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Slate700,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = before,
                    onValueChange = { before = it },
                    label = { Text("What happened before? (Trigger)") },
                    placeholder = { Text("e.g. Asked to leave the playground") },
                    modifier = Modifier.fillMaxWidth().testTag("new_log_before_input"),
                    shape = RoundedCornerShape(14.dp),
                    minLines = 2
                )

                OutlinedTextField(
                    value = what,
                    onValueChange = { what = it },
                    label = { Text("What did the child do?") },
                    placeholder = { Text("e.g. Cried, refused to walk to car") },
                    modifier = Modifier.fillMaxWidth().testTag("new_log_what_input"),
                    shape = RoundedCornerShape(14.dp),
                    minLines = 2
                )

                OutlinedTextField(
                    value = helped,
                    onValueChange = { helped = it },
                    label = { Text("What helped / Resolution?") },
                    placeholder = { Text("e.g. 5-min timer, holding toy") },
                    modifier = Modifier.fillMaxWidth().testTag("new_log_helped_input"),
                    shape = RoundedCornerShape(14.dp),
                    minLines = 2
                )

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Intensity Rating", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate800)
                        Text(
                            text = when (intensity.toInt()) {
                                1 -> "1 - Mild"
                                2 -> "2 - Moderate"
                                3 -> "3 - Elevated"
                                4 -> "4 - High"
                                else -> "5 - Peak"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }
                    Slider(
                        value = intensity,
                        onValueChange = { intensity = it },
                        valueRange = 1f..5f,
                        steps = 3,
                        colors = SliderDefaults.colors(
                            thumbColor = Slate900,
                            activeTrackColor = Slate900
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (before.isNotBlank() || what.isNotBlank()) {
                        onSave(category, before.ifBlank { "Unspecified trigger" }, what.ifBlank { "Friction moment" }, helped.ifBlank { "Gentle co-regulation" }, intensity.toInt())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Slate900),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.testTag("new_log_save_button")
            ) {
                Text("Save Log", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Slate500)
            }
        }
    )
}
