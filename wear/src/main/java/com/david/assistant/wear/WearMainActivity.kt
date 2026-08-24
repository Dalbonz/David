package com.david.assistant.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.google.android.gms.wearable.Wearable

private val Ink = Color(0xFF090A0C)
private val Orange = Color(0xFFFF7A18)
private val Cream = Color(0xFFFFF5EC)

/**
 * 워치 화면의 오렌지 원형 버튼을 누르면 페어링된 휴대폰에 "/david/open" 메시지를 보낸다.
 * 휴대폰 쪽 수신은 app 모듈의 WearRequestListenerService.kt가 담당한다.
 */
class WearMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var status by remember { mutableStateOf("눌러서 다비드 호출") }
                Box(
                    modifier = Modifier.fillMaxSize().background(Ink),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Orange)
                                .clickable {
                                    status = "휴대폰 호출 중..."
                                    sendOpenRequest { ok -> status = if (ok) "휴대폰을 열었어요" else "휴대폰 연결 실패" }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = "다비드 호출", tint = Ink)
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(status, color = Cream)
                    }
                }
            }
        }
    }

    private fun sendOpenRequest(onDone: (Boolean) -> Unit) {
        val messageClient = Wearable.getMessageClient(this)
        Wearable.getNodeClient(this).connectedNodes
            .addOnSuccessListener { nodes ->
                if (nodes.isEmpty()) {
                    onDone(false)
                    return@addOnSuccessListener
                }
                var remaining = nodes.size
                var anySuccess = false
                nodes.forEach { node ->
                    messageClient.sendMessage(node.id, "/david/open", ByteArray(0))
                        .addOnSuccessListener {
                            anySuccess = true
                            remaining--
                            if (remaining == 0) onDone(anySuccess)
                        }
                        .addOnFailureListener {
                            remaining--
                            if (remaining == 0) onDone(anySuccess)
                        }
                }
            }
            .addOnFailureListener { onDone(false) }
    }
}
