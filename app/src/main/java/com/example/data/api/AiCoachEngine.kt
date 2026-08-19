package com.example.data.api

import com.example.BuildConfig
import com.example.data.model.ChatMessage
import com.example.data.model.ChildProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AiCoachEngine {

    suspend fun getCoachResponse(
        userInput: String,
        profile: ChildProfile,
        chatHistory: List<ChatMessage>
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        val childName = if (profile.nickname.isNotBlank()) profile.nickname else "your child"
        val systemPrompt = """
            You are "NeuroParent AI", a supportive, compassionate, and educational parenting coach for parents of neurodivergent or neurotypical children experiencing daily behavioral, sensory, and routine friction.
            
            MANDATORY COMPLIANCE & SAFETY RULES:
            1. DO NOT DIAGNOSE autism, ADHD, sensory processing disorders, or any medical or developmental condition.
            2. Always use calm, validating, non-judgmental, and reassuring language for the parent.
            3. Structure your response with:
               - Empathy & Validation: Acknowledge that this is hard and normal.
               - Non-Diagnostic Explanation: Explain what might be happening under the surface (e.g. nervous system overload, difficulty with cognitive flexibility, executive function demands, sensory sensitivity).
               - Immediate Actionable Strategies (in the moment): 2-3 concrete steps to de-escalate or co-regulate right now.
               - Future Prevention / Routine Ideas: 1-2 proactive adjustments for next time.
               - Clarifying questions: If helpful, ask 1 to 2 gentle clarifying questions.
            4. Personalize using the child's profile when relevant:
               - Child's Name: $childName (Age: ${profile.age})
               - Interests: ${profile.interests}
               - Known Challenges: ${profile.challenges}
               - Communication Preferences: ${profile.commPref}
               - Sensory Profile: ${profile.sensoryPref}
               - Known Helpful Strategies: ${profile.strategies}
            5. Reassure the parent: "You're doing great, and this is a learning process for both of you."
            6. Clearly recommend pediatric, occupational therapy, or developmental specialist consultation if the parent expresses deep safety concerns or distress.
        """.trimIndent()

        if (!apiKey.isNullOrBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val contentsList = mutableListOf<GeminiContent>()

                // Add recent history for context
                chatHistory.takeLast(6).forEach { msg ->
                    contentsList.add(
                        GeminiContent(
                            parts = listOf(GeminiPart(text = msg.content)),
                            role = if (msg.role == "user") "user" else "model"
                        )
                    )
                }

                // Add current user input
                contentsList.add(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = userInput)),
                        role = "user"
                    )
                )

                val request = GeminiRequest(
                    contents = contentsList,
                    systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemPrompt))),
                    generationConfig = GeminiGenerationConfig(temperature = 0.7f, maxOutputTokens = 900)
                )

                val response = GeminiClient.api.generateContent(apiKey, request)
                val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!responseText.isNullOrBlank()) {
                    return@withContext responseText
                }
            } catch (e: Exception) {
                // If API fails or is unavailable, use intelligent heuristic fallback
            }
        }

        // High quality offline heuristic fallback
        return@withContext generateHeuristicResponse(userInput, profile)
    }

    private fun generateHeuristicResponse(input: String, profile: ChildProfile): String {
        val lower = input.lowercase()
        val name = if (profile.nickname.isNotBlank()) profile.nickname else "your child"
        val strategies = if (profile.strategies.isNotBlank()) profile.strategies else "gentle co-regulation and visual cues"
        val sensory = if (profile.sensoryPref.isNotBlank()) profile.sensoryPref else "sensory accommodations"
        val interests = if (profile.interests.isNotBlank()) profile.interests else "their favorite hobbies"

        return when {
            lower.contains("meltdown") || lower.contains("upset") || lower.contains("tantrum") || lower.contains("screaming") || lower.contains("crying") -> {
                """
                Take a deep breath—you are not alone, and $name is not giving you a hard time; they are having a hard time.
                
                🔍 Possible Non-Diagnostic Reason:
                During a meltdown, the nervous system is overwhelmed and in 'fight-or-flight'. Reasoning or talking during this peak state usually adds more sensory load.
                
                ⚡ Immediate Strategies (Right Now):
                1. Ensure physical safety and lower all demands immediately.
                2. Reduce environmental stimulation: soften lights, lower voices, and offer space or a quiet nook.
                3. Co-regulate in silence: sit nearby quietly. If $name welcomes touch, offer firm, grounding pressure or their favorite calming item ($sensory).
                
                🌱 Future Prevention:
                • Practice the strategy you noted ($strategies) during calm baseline moments so it becomes an instinctive safe harbor.
                
                ❓ Gentle Questions for Later:
                • Did you notice any subtle warning cues (e.g. fidgeting, vocal changes) 10 minutes before?
                """.trimIndent()
            }

            lower.contains("playground") || lower.contains("leave") || lower.contains("transition") || lower.contains("stop playing") || lower.contains("switch") -> {
                """
                Leaving high-interest activities like the playground or a screen is one of the most common friction points for children.
                
                🔍 Possible Non-Diagnostic Reason:
                Switching tasks requires 'executive shifting'—moving brain focus from high dopamine stimulation to a lower stimulation transition can feel physically jarring.
                
                ⚡ Immediate Strategies:
                1. Give a "Now, Next, Later" bridge: "Now we say goodbye to the swings, Next we buckle into the car, Later we play with $interests at home."
                2. Use a visual timer or a physical countdown token rather than just verbal warnings.
                3. Bring a 'Transition Object': let $name carry a small favorite toy from the playground to the car to hold continuity.
                
                🌱 Future Prevention:
                • Set a preset "10-minute warning" reminder before leaving to establish predictable rhythm.
                
                ❓ Clarifying Question:
                • Would a visual countdown photo on your phone help $name anticipate the departure next time?
                """.trimIndent()
            }

            lower.contains("loud") || lower.contains("noise") || lower.contains("sound") || lower.contains("crowd") || lower.contains("sensory") || lower.contains("ears") -> {
                """
                Sensory overload from sound or busy environments is very real and exhausting for a sensitive sensory system.
                
                🔍 Possible Non-Diagnostic Reason:
                Auditory filtering can be taxing. When background sounds aren't automatically tuned out by the brain, every noise hits at equal, intense volume.
                
                ⚡ Immediate Strategies:
                1. Provide auditory shielding: offer over-ear headphones or quiet music immediately.
                2. Step into a 'sensory decompression zone' (a car, bathroom, or quiet hallway for 3-5 minutes).
                3. Offer proprioceptive heavy work (e.g., carrying a backpack, squeezing a stress ball, or weighted blanket: $sensory).
                
                🌱 Future Prevention:
                • Keep a small "Sensory Go-Bag" in your car or purse with earplugs, chewables, and comforting textures.
                """.trimIndent()
            }

            lower.contains("homework") || lower.contains("school") || lower.contains("start") || lower.contains("refuse") || lower.contains("task") -> {
                """
                Task initiation resistance often looks like defiance, but is almost always rooted in executive overwhelm or anxiety about mistakes.
                
                🔍 Possible Non-Diagnostic Reason:
                A blank page or multi-step assignment can feel like a massive mountain. Working memory and initiation require substantial cognitive energy.
                
                ⚡ Immediate Strategies:
                1. The "Micro-Step" rule: don't ask to do the worksheet; just ask to "write name and date" or "pick which colored pencil to use".
                2. Body doubling: sit alongside $name and quietly do your own paper task without hovering.
                3. Gamify with their interests: integrate $interests (e.g. "Let's solve 2 math problems like unlocking a Minecraft checkpoint").
                
                🌱 Future Prevention:
                • Schedule homework right after a 15-minute physical movement break and a protein snack to regulate blood sugar and focus.
                """.trimIndent()
            }

            lower.contains("sleep") || lower.contains("bedtime") || lower.contains("night") || lower.contains("wake") -> {
                """
                Bedtime requires downshifting the nervous system from active alertness to deep safety and surrender.
                
                🔍 Possible Non-Diagnostic Reason:
                If the body hasn't discharged daytime sensory energy or feels anxious about separation in the dark, settling down feels difficult.
                
                ⚡ Immediate Strategies:
                1. Proprioceptive wind-down: try gentle pillow squishes, wall pushes, or a weighted blanket.
                2. Dim all blue screens 60 minutes prior and use warm, low-level amber lamp lighting.
                3. Use a predictable, step-by-step visual bedtime sequence so $name knows exactly what comes next.
                
                🌱 Future Prevention:
                • Set an automated "Start bedtime routine" reminder at 7:00 PM to protect the wind-down buffer.
                """.trimIndent()
            }

            lower.contains("eat") || lower.contains("meal") || lower.contains("food") || lower.contains("dinner") || lower.contains("picky") -> {
                """
                Mealtimes involve intense sensory stimulation: smell, texture, temperature, oral motor control, and social expectations all combined!
                
                🔍 Possible Non-Diagnostic Reason:
                Texture aversion or fear of novel foods is a protective reflex of a sensitive palate. Pressure to eat usually increases anxiety and decreases appetite.
                
                ⚡ Immediate Strategies:
                1. Always ensure at least one reliable "safe food" is on the plate that $name readily eats.
                2. Secure posture: check that $name's feet are flat on a footrest or floor, which stabilizes the core and promotes oral motor comfort.
                3. Remove pressure: allow smelling, touching, or licking without requiring swallowing.
                
                🌱 Future Prevention:
                • Involve $name in low-stress food prep (like washing vegetables or stirring pasta) away from mealtime.
                """.trimIndent()
            }

            else -> {
                """
                Thank you for sharing this situation with $name. Navigating these everyday hurdles takes immense dedication and patience.
                
                🔍 Non-Diagnostic Understanding:
                When behaviors escalate or friction happens, it is often communication of an unmet physiological, sensory, or emotional need rather than willful misbehavior.
                
                ⚡ Practical Immediate Actions:
                1. Prioritize connection before correction: validate their feeling ("I see this feels really hard right now").
                2. Keep verbal language minimal and concrete—use 3-word sentences.
                3. Refer to what has worked before: consider trying $strategies or offering a brief quiet reset.
                
                🌱 Looking Forward:
                • Log this occurrence in your Situation Log so we can track triggers and patterns over the week.
                
                *Please note: NeuroParent AI provides supportive educational parenting ideas, not clinical or medical diagnoses. Always consult a licensed pediatrician or specialist for comprehensive guidance.*
                """.trimIndent()
            }
        }
    }
}
