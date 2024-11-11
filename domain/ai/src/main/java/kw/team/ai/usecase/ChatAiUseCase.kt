package kw.team.ai.usecase

import kw.team.ai.repository.AiRepository
import javax.inject.Inject

class ChatAiUseCase @Inject constructor(
    private val aiRepository: AiRepository,
) {

    suspend operator fun invoke(message: String) {
        aiRepository.fetchMessage(message)
    }
}
