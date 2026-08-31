package com.david.assistant.brain

import com.anthropic.client.AnthropicClient
import com.anthropic.client.okhttp.AnthropicOkHttpClient
import com.anthropic.models.messages.MessageCreateParams
import com.david.assistant.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Claude(Anthropic API)를 실제 두뇌로 쓰는 구현체. 네트워크 호출이라 IO 디스패처에서 실행한다. */
class ClaudeBrain(
    apiKey: String = BuildConfig.CLAUDE_API_KEY,
) : Brain {
    private val client: AnthropicClient = AnthropicOkHttpClient.builder()
        .apiKey(apiKey)
        .build()

    override suspend fun reply(request: AgentRequest): AgentResponse = withContext(Dispatchers.IO) {
        val params = MessageCreateParams.builder()
            .model("claude-opus-5")
            .maxTokens(1024L)
            .system("너는 '다비드'라는 개인 AI 비서다. 한국어로 짧고 자연스럽게 답한다.")
            .addUserMessage(request.text)
            .build()

        val text = try {
            client.messages().create(params).content()
                .mapNotNull { it.text().orElse(null)?.text() }
                .joinToString("")
        } catch (e: Exception) {
            "다비드가 지금 답을 못 가져왔어요: ${e.message}"
        }

        AgentResponse(text = text.ifBlank { "죄송해요, 답을 받지 못했어요." })
    }
}
