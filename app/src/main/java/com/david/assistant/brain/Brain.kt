package com.david.assistant.brain

/** 하나의 사용자 발화를 두뇌(Brain)에게 넘길 때 담는 요청. */
data class AgentRequest(val text: String)

/**
 * 두뇌의 응답. actions는 아직 구체적인 종류가 정해지지 않았으므로(검색/알림 등),
 * type 문자열 기반의 범용 구조로 열어두고 나중에 실제 종류가 정해지면 구체화한다.
 */
data class AgentAction(val type: String, val payload: Map<String, String> = emptyMap())

data class AgentResponse(val text: String, val actions: List<AgentAction> = emptyList())

/** 다비드의 '두뇌' 인터페이스. 구현체를 갈아끼워도(데모 → Ollama 등) 호출부는 바뀌지 않는다. */
interface Brain {
    suspend fun reply(request: AgentRequest): AgentResponse
}

/** 실제 LLM 연결 전까지 쓰는 임시 두뇌 — 기존 DavidApp.kt의 localReply 로직을 그대로 옮김. */
class DemoBrain : Brain {
    override suspend fun reply(request: AgentRequest): AgentResponse =
        AgentResponse(text = localReply(request.text))
}

private fun localReply(input: String): String = when {
    input.contains("시간") -> "현재 시간 확인 기능은 다음 단계에서 기기 도구로 연결할 수 있어요."
    input.contains("안녕") -> "반가워요. 다비드는 현재 무료 로컬 데모 모드로 동작하고 있어요."
    else -> "'$input'이라고 말씀하셨네요. 지금은 기본 대화 화면입니다. README의 다음 단계에 따라 무료 로컬 AI를 연결해 보세요."
}
