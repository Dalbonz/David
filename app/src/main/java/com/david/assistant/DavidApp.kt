package com.david.assistant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.david.assistant.brain.AgentRequest
import com.david.assistant.brain.Brain
import com.david.assistant.brain.DemoBrain
import kotlinx.coroutines.launch

private val Ink = Color(0xFF090A0C); private val Panel = Color(0xFF17191D)
private val Orange = Color(0xFFFF7A18); private val Cream = Color(0xFFFFF5EC)
data class ChatMessage(val text: String, val mine: Boolean)

@Composable fun DavidApp(onVoiceTap: ((String) -> Unit) -> Unit, speak: (String) -> Unit, brain: Brain = DemoBrain()) {
    var draft by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf(ChatMessage("안녕하세요. 저는 다비드입니다. 무엇을 도와드릴까요?", false))) }
    val scope = rememberCoroutineScope()
    fun send(text: String) {
        if (text.isBlank()) return
        messages = messages + ChatMessage(text, true); draft = ""
        scope.launch {
            val response = brain.reply(AgentRequest(text))
            messages = messages + ChatMessage(response.text, false)
            speak(response.text)
        }
    }
    MaterialTheme(colorScheme = darkColorScheme(primary = Orange, surface = Panel, background = Ink)) {
        Surface(Modifier.fillMaxSize(), color = Ink) {
            Column(Modifier.fillMaxSize().statusBarsPadding().padding(horizontal = 20.dp)) {
                Row(Modifier.fillMaxWidth().padding(vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
                    DavidMark(); Spacer(Modifier.width(12.dp)); Column { Text("다비드", color = Cream, fontSize = 23.sp, fontWeight = FontWeight.Bold); Text("LOCAL FIRST · 0원 시작", color = Orange, fontSize = 11.sp) }
                    Spacer(Modifier.weight(1f)); Text("준비됨", color = Color(0xFF9BFFB1), fontSize = 12.sp)
                }
                LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(vertical = 8.dp)) {
                    items(messages) { message -> Row(Modifier.fillMaxWidth(), horizontalArrangement = if (message.mine) Arrangement.End else Arrangement.Start) { Surface(color = if (message.mine) Orange else Panel, shape = RoundedCornerShape(18.dp)) { Text(message.text, Modifier.padding(14.dp), color = if (message.mine) Ink else Cream) } } }
                }
                Row(Modifier.navigationBarsPadding().padding(vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onVoiceTap(::send) }, modifier = Modifier.size(52.dp).clip(CircleShape).background(Orange)) { Icon(Icons.Default.Mic, "음성으로 말하기", tint = Ink) }
                    Spacer(Modifier.width(10.dp)); OutlinedTextField(draft, { draft = it }, Modifier.weight(1f), placeholder = { Text("다비드에게 물어보세요") }, singleLine = true, shape = RoundedCornerShape(24.dp))
                    IconButton(onClick = { send(draft) }) { Icon(Icons.Default.Send, "보내기", tint = Orange) }
                }
            }
        }
    }
}
@Composable private fun DavidMark() { Box(Modifier.size(38.dp).clip(CircleShape).background(Orange), contentAlignment = Alignment.Center) { Box(Modifier.size(21.dp).clip(CircleShape).background(Ink)); Box(Modifier.size(8.dp).clip(CircleShape).background(Cream).align(Alignment.CenterEnd)) } }
