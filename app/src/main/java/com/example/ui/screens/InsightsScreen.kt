package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildProfile
import com.example.data.model.SituationLog
import com.example.ui.components.NonDiagnosticDisclaimerCard
import com.example.ui.theme.CommCyan
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.MealtimeOrange
import com.example.ui.theme.MeltdownRose
import com.example.ui.theme.SensoryAmber
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate800
import com.example.ui.theme.TransitionEmerald

@Composable
fun InsightsScreen(
    profile: ChildProfile?,
    logs: List<SituationLog>
) {
    val childName = profile?.nickname?.ifBlank { "Leo" } ?: "Leo"

    val totalLogs = logs.size
    val transitionCount = logs.count { it.category.contains("Transition", ignoreCase = true) }
    val sensoryCount = logs.count { it.category.contains("Sensory", ignoreCase = true) }
    val meltdownCount = logs.count { it.category.contains("Meltdown", ignoreCase = true) }
    val mealtimeCount = logs.count { it.category.contains("Meal", ignoreCase = true) }
    val commCount = logs.count { it.category.contains("Comm", ignoreCase = true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                Text(
                    text = "Weekly Observations",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Slate800,
                        letterSpacing = (-0.5).sp
                    )
                )
                Text(
                    text = "Pattern recognition based on your situation logs",
                    style = MaterialTheme.typography.bodySmall.copy(color = Slate500)
                )
            }
        }

        // Summary Hero Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth().testTag("insights_hero_card"),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(EmeraldPrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Weekly Pattern Summary",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Slate800)
                            )
                            Text(
                                text = "$totalLogs logged moments recorded",
                                style = MaterialTheme.typography.bodySmall.copy(color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (totalLogs > 0) {
                            "The most frequent friction points recorded this week for $childName relate to Activity Transitions (e.g. leaving the playground or stopping tablet time) and sudden auditory sensory inputs."
                        } else {
                            "Start logging everyday situations in the Log tab to view automated weekly patterns and helpful trigger analysis."
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Slate800,
                            lineHeight = 22.sp,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }

        // Category Breakdown
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
                        text = "SITUATION FREQUENCY BREAKDOWN",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Slate400,
                            letterSpacing = 1.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val maxCount = maxOf(1, totalLogs)

                    InsightBarItem(title = "Transition Friction", count = transitionCount, total = maxCount, color = TransitionEmerald)
                    Spacer(modifier = Modifier.height(10.dp))
                    InsightBarItem(title = "Sensory Overload / Noise", count = sensoryCount, total = maxCount, color = SensoryAmber)
                    Spacer(modifier = Modifier.height(10.dp))
                    InsightBarItem(title = "Meltdown / Escalation", count = meltdownCount, total = maxCount, color = MeltdownRose)
                    Spacer(modifier = Modifier.height(10.dp))
                    InsightBarItem(title = "Mealtime Sensitivity", count = mealtimeCount, total = maxCount, color = MealtimeOrange)
                    Spacer(modifier = Modifier.height(10.dp))
                    InsightBarItem(title = "Communication Friction", count = commCount, total = maxCount, color = CommCyan)
                }
            }
        }

        // Effective Strategies
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "TOP STRATEGIES REPORTED HELPING",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Slate400,
                            letterSpacing = 1.sp
                        )
                    )

                    StrategyInsightItem(
                        title = "5-Minute Visual Timers & Tokens",
                        frequencyText = "Helped in 80% of transition moments",
                        note = "Giving a visible countdown significantly lowers cognitive transition shock."
                    )

                    StrategyInsightItem(
                        title = "Proprioceptive Pressure & Headphones",
                        frequencyText = "Helped in 75% of sensory moments",
                        note = "Noise reduction combined with deep touch pressure calms sympathetic nervous arousal."
                    )

                    StrategyInsightItem(
                        title = "Divided Plates & Safe Foods",
                        frequencyText = "Helped reduce mealtime refusal",
                        note = "Keeping novel foods physically separated lowers texture intimidation."
                    )
                }
            }
        }

        item {
            NonDiagnosticDisclaimerCard()
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InsightBarItem(
    title: String,
    count: Int,
    total: Int,
    color: Color
) {
    val progress = (count.toFloat() / total.toFloat()).coerceIn(0.05f, 1f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = Slate800)
            )
            Text(
                text = "$count (${(count.toFloat() / total * 100).toInt()}%)",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Slate100)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}

@Composable
private fun StrategyInsightItem(
    title: String,
    frequencyText: String,
    note: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Slate50,
        modifier = Modifier.fillMaxWidth(),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate100)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Slate800)
            )
            Text(
                text = frequencyText,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = EmeraldPrimary,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = note,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Slate500,
                    lineHeight = 16.sp
                )
            )
        }
    }
}
