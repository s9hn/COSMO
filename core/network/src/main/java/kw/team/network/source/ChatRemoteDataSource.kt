package kw.team.network.source

import android.util.Log
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import kw.team.network.gpt.GptApi
import kw.team.network.gpt.GptRequestDto
import kw.team.network.gpt.GptRequestDto.GptMessageRequestDto
import kw.team.network.gpt.GptResponse
import javax.inject.Inject

class GptRemoteDataSource @Inject constructor(
    private val gptApi: GptApi,
) {
    private val _messageLog: MutableList<GptMessageRequestDto> =
        mutableListOf(SYSTEM_SETTING_MESSAGE)
    val messageLog: List<GptMessageRequestDto> get() = _messageLog.toList()

    private val _streamedMessages = MutableStateFlow<String>("")

    suspend fun fetchMessage(message: String) = flow {
        val gptRequestDto = GptRequestDto(
            model = GPT_MODEL,
            messages = messageLog,
            stream = IS_STREAMING,
        )

        _messageLog.add(
            GptMessageRequestDto(
                role = "user",
                content = message,
            )
        )

        // API 호출
        val byteStream = gptApi.postMessage(
            gptRequestDto = gptRequestDto.copy(messages = messageLog)
        ).byteStream().bufferedReader()
        val line = byteStream.readLine()
//        if (line != null && line.startsWith("data:")) {
//            val temp = Json {
//                ignoreUnknownKeys = true
//            }.decodeFromString<GptResponse>(
//                line.substring(5).trim()
//            )
//            Log.d("123123", temp.choices.first().delta.content)
//        }
        while (currentCoroutineContext().isActive) {
            val line = byteStream.readLine()
            if (line != null && line.startsWith("data:")) {
                val temp = Json {
                    ignoreUnknownKeys = true
                }.decodeFromString<GptResponse>(
                    line.substring(5).trim()
                )
                Log.d("123123", temp.choices.first().delta.content)
            }
            delay(100)
        }
    }

    companion object {
        private const val GPT_MODEL = "gpt-4o-mini"
        private const val IS_STREAMING = true
        private val SYSTEM_SETTING_MESSAGE = GptMessageRequestDto(
            role = "system",
            content = "한글로 해줘",
        )
    }
}
