package kw.team.ai_gpt.mapper

import kw.team.ai.model.Message
import kw.team.network.gpt.GptRequestDto.GptMessageRequestDto

internal fun List<Message>.toRequestBody(): List<GptMessageRequestDto> =
    map {
        GptMessageRequestDto(
            role = it.role,
            content = it.message,
        )
    }
