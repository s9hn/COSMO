package kw.team.network.source

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kw.team.common.di.Dispatcher
import kw.team.common.di.DispatcherType
import kw.team.network.gpt.GptApi
import kw.team.network.gpt.GptRequestDto
import kw.team.network.gpt.GptRequestDto.GptMessageRequestDto
import kw.team.network.gpt.GptResponseDto
import kw.team.network.source.GptRemoteDataSource.Role.ASSISTANT
import kw.team.network.source.GptRemoteDataSource.Role.SYSTEM
import kw.team.network.source.GptRemoteDataSource.Role.USER
import java.util.Locale
import javax.inject.Inject

class GptRemoteDataSource @Inject constructor(
    private val gptApi: GptApi,
    @Dispatcher(DispatcherType.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    @Inject
    internal lateinit var json: Json

    private val _reply: MutableStateFlow<String> = MutableStateFlow("")
    val reply: StateFlow<String> get() = _reply.asStateFlow()

    private val messageLog: MutableList<GptMessageRequestDto> = mutableListOf(SYSTEM_INIT_MESSAGE)

    private val defaultGptRequestDto by lazy {
        GptRequestDto(
            model = GPT_MODEL,
            messages = messageLog,
            stream = IS_STREAMING,
        )
    }

    suspend fun fetchMessage(message: String) {
        updateMessageLog(role = USER, message = message)

        gptApi.postMessage(
            gptRequestDto = defaultGptRequestDto.copy(messages = messageLog)
        ).byteStream().apply {
            withContext(ioDispatcher) {
                bufferedReader().let { reader ->
                    while (currentCoroutineContext().isActive) {
                        val response = reader.readLine().orEmpty()

                        when {
                            response.contains(STREAM_END_SIGN) -> break
                            response.startsWith(STREAM_START_SIGN) -> {
                                response.decode().also { decodedContent ->
                                    _reply.update { reply.value + decodedContent }
                                }
                            }

                            else -> continue
                        }
                        delay(100)
                    }
                }
            }
        }

        updateMessageLog(role = ASSISTANT, message = reply.value)
    }

    private fun updateMessageLog(role: Role, message: String) {
        messageLog.add(
            GptMessageRequestDto(
                role = role.toString(),
                content = message,
            )
        )
    }

    private fun String.decode(): String = json.decodeFromString<GptResponseDto>(
        substring(STREAM_START_SIGN.length).trim()
    ).choices.first().messageResponseDto.content.orEmpty()

    private enum class Role {
        USER, SYSTEM, ASSISTANT,
        ;

        override fun toString(): String = name.lowercase(Locale.ROOT)
    }

    companion object {
        private const val STREAM_START_SIGN = "data:"
        private const val STREAM_END_SIGN = "[DONE]"
        private const val GPT_MODEL = "gpt-4o-mini"
        private const val IS_STREAMING = true
        private val SYSTEM_INIT_MESSAGE = GptMessageRequestDto(
            role = SYSTEM.toString(),
            content = "한글로 해줘",
        )
    }
}
