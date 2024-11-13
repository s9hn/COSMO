package kw.team.ai.repository

abstract class AiRepository(
    private val chatRepository: ChatRepository,
    private val generateQuizRepository: GenerateQuizRepository? = null,
) {

    suspend fun fetchMessage(message: String) {
        chatRepository.fetchMessage(message)
    }

    suspend fun fetchQuiz() {
        if (generateQuizRepository == null) return
        generateQuizRepository.fetchQuiz()
    }
}

interface ChatRepository {
    suspend fun fetchMessage(message: String)
}

interface GenerateQuizRepository {
    suspend fun fetchQuiz()
}
