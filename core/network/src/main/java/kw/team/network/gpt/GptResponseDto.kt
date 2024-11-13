package kw.team.network.gpt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GptResponseDto(
    @SerialName("choices")
    val choices: List<ChoiceResponseDto>,
) {

    @Serializable
    data class ChoiceResponseDto(
        @SerialName("message")
        val messageResponseDto: MessageResponseDto,
    ) {

        @Serializable
        data class MessageResponseDto(
            @SerialName("role")
            val role: String,
            @SerialName("content")
            val content: String,
        )
    }
}

// logprobs: 단어별 신뢰도
// refusal: 윤리 등 GPT 자체적인 응답 거부
// finish_reason: GPT 통신 이슈

@Serializable
data class GptResponse(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    @SerialName("system_fingerprint") val systemFingerprint: String,
    val choices: List<Choice>,
)

@Serializable
data class Choice(
    val index: Int,
    val delta: Delta,
    @SerialName("finish_reason") val finishReason: String?,
)

@Serializable
data class Delta(
    val content: String,
)
