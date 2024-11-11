package kw.team.ai_gpt.repository

import kw.team.ai.repository.AiRepository
import kw.team.ai_gpt.repository.chat.GptChatRepository
import javax.inject.Inject

class DefaultGptAiRepository @Inject constructor(
    gptChatRepository: GptChatRepository,
) : AiRepository(gptChatRepository)
