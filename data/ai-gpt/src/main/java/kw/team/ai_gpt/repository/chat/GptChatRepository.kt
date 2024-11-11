package kw.team.ai_gpt.repository.chat

import kw.team.ai.repository.ChatRepository
import kw.team.network.source.GptRemoteDataSource
import javax.inject.Inject

class GptChatRepository @Inject constructor(
    private val gptRemoteDataSource: GptRemoteDataSource,
    // private val gptLocalDataSource: GptLocalDataSource,
) : ChatRepository {

    override suspend fun fetchMessage(message: String) {
        gptRemoteDataSource.fetchMessage(message)
    }
}
