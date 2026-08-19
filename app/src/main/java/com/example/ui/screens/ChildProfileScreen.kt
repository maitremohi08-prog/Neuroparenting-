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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import com.example.data.model.ChildProfile
import com.example.ui.components.NonDiagnosticDisclaimerCard
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun ChildProfileScreen(
    profile: ChildProfile?,
    onSaveProfile: (String, String, String, String, String, String, String, String) -> Unit,
    onSkipOrBack: () -> Unit
) {
    var nickname by remember(profile) { mutableStateOf(profile?.nickname ?: "Leo") }
    var age by remember(profile) { mutableStateOf(profile?.age ?: "6") }
    var interests by remember(profile) { mutableStateOf(profile?.interests ?: "Trains, Minecraft, Dinosaurs, building blocks") }
    var challenges by remember(profile) { mutableStateOf(profile?.challenges ?: "Loud noises, unexpected transitions, starting homework") }
    var commPref by remember(profile) { mutableStateOf(profile?.commPref ?: "Verbal, but struggles when overwhelmed") }
    var sensoryPref by remember(profile) { mutableStateOf(profile?.sensoryPref ?: "Likes weighted blankets; sensitive to tags and siren sounds") }
    var strategies by remember(profile) { mutableStateOf(profile?.strategies ?: "5-minute visual timers, deep breathing, quiet space") }
    var routines by remember(profile) { mutableStateOf(profile?.routines ?: "Morning visual checklist, 7:30 PM bedtime sequence") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onSkipOrBack,
                            modifier = Modifier.testTag("profile_back_button")
                        ) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Slate800)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Child Profile",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Slate800,
                                letterSpacing = (-0.5).sp
                            )
                        )
                    }

                    TextButton(
                        onClick = onSkipOrBack,
                        modifier = Modifier.testTag("profile_skip_button")
                    ) {
                        Text(
                            text = "SKIP",
                            color = Slate400,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontSize = 11.sp
                        )
                    }
                }

                Text(
                    text = "Personalize AI recommendations. All information is stored securely on your device.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Slate500,
                        lineHeight = 18.sp
                    ),
                    modifier = Modifier.padding(start = 6.dp, top = 2.dp)
                )
            }

            // Basic Info Card
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
                            text = "BASIC INFORMATION",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Slate400,
                                letterSpacing = 1.sp
                            )
                        )

                        ProfileInputField(
                            label = "Child Nickname",
                            value = nickname,
                            onValueChange = { nickname = it },
                            icon = Icons.Default.Person,
                            placeholder = "e.g. Leo",
                            testTag = "profile_input_nickname"
                        )

                        ProfileInputField(
                            label = "Age",
                            value = age,
                            onValueChange = { age = it },
                            icon = Icons.Default.Schedule,
                            placeholder = "e.g. 6",
                            testTag = "profile_input_age"
                        )
                    }
                }
            }

            // Preferences & Traits Card
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
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "SUPPORT CONTEXT & TRAITS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Slate400,
                                letterSpacing = 1.sp
                            )
                        )

                        ProfileInputField(
                            label = "Communication Preferences",
                            value = commPref,
                            onValueChange = { commPref = it },
                            icon = Icons.Default.RecordVoiceOver,
                            placeholder = "e.g. Verbal, struggles when overwhelmed",
                            isMultiline = true,
                            testTag = "profile_input_comm_pref"
                        )

                        ProfileInputField(
                            label = "Likes & Special Interests",
                            value = interests,
                            onValueChange = { interests = it },
                            icon = Icons.Default.Favorite,
                            placeholder = "e.g. Trains, Minecraft, dinosaurs, space",
                            isMultiline = true,
                            testTag = "profile_input_interests"
                        )

                        ProfileInputField(
                            label = "Known Challenges",
                            value = challenges,
                            onValueChange = { challenges = it },
                            icon = Icons.Default.WarningAmber,
                            placeholder = "e.g. Leaving the park, loud sounds",
                            isMultiline = true,
                            testTag = "profile_input_challenges"
                        )

                        ProfileInputField(
                            label = "Sensory Preferences",
                            value = sensoryPref,
                            onValueChange = { sensoryPref = it },
                            icon = Icons.Default.Hearing,
                            placeholder = "e.g. Likes weighted blankets, sensitive to tags",
                            isMultiline = true,
                            testTag = "profile_input_sensory"
                        )

                        ProfileInputField(
                            label = "Helpful Strategies",
                            value = strategies,
                            onValueChange = { strategies = it },
                            icon = Icons.Default.Lightbulb,
                            placeholder = "e.g. Visual timers, counting, deep hugs",
                            isMultiline = true,
                            testTag = "profile_input_strategies"
                        )

                        ProfileInputField(
                            label = "Important Routines",
                            value = routines,
                            onValueChange = { routines = it },
                            icon = Icons.Default.Schedule,
                            placeholder = "e.g. Morning visual checklist, 7:30 PM bedtime",
                            isMultiline = true,
                            testTag = "profile_input_routines"
                        )
                    }
                }
            }

            item {
                NonDiagnosticDisclaimerCard()
            }

            // Save Buttons
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            onSaveProfile(nickname, age, interests, challenges, commPref, sensoryPref, strategies, routines)
                            onSkipOrBack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("profile_save_button"),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Slate900),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    ) {
                        Text("Save & Continue", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                    }

                    OutlinedButton(
                        onClick = onSkipOrBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("profile_cancel_button"),
                        shape = RoundedCornerShape(24.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                    ) {
                        Text("Close Profile", fontWeight = FontWeight.SemiBold, color = Slate700)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    placeholder: String,
    isMultiline: Boolean = false,
    testTag: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = EmeraldPrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Slate800,
                    fontSize = 13.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 13.sp, color = Slate400) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = Slate200,
                focusedContainerColor = Slate50,
                unfocusedContainerColor = Slate50
            ),
            minLines = if (isMultiline) 2 else 1,
            maxLines = if (isMultiline) 4 else 1
        )
    }
}
