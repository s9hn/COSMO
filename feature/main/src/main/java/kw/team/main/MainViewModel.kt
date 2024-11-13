package kw.team.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kw.team.ai.model.AiModel
import kw.team.ai.repository.AiRepository
import kw.team.ai.usecase.SendMessageUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    aiRepository: AiRepository,
    private val sendMessageUseCase: SendMessageUseCase,
) : ViewModel() {
    val aiModels: List<AiModel> get() = AiModel.entries.toList()

    private val _chatLog: MutableStateFlow<List<ChatModel>> = MutableStateFlow(listOf(ChatModel()))
    val chatLog: StateFlow<List<ChatModel>> get() = _chatLog.asStateFlow()

    val reply: StateFlow<String> = aiRepository.chatRepository.reply.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = "",
    )

    init {
        // reply의 변경 사항을 실시간으로 반영
        viewModelScope.launch {
            reply.collect { response ->
                if (response.isNotEmpty()) {
                    _chatLog.update { currentLog ->
                        // 마지막 메시지를 AI 응답으로 업데이트
                        val lastMessage = currentLog.lastOrNull()
                        if (lastMessage != null && !lastMessage.isMe) {
                            currentLog.dropLast(1) + lastMessage.copy(message = response)
                        } else {
                            currentLog + ChatModel(isMe = false, message = response)
                        }
                    }
                }
            }
        }
    }

    fun converseWith(message: String) {
        _chatLog.update { currentLog ->
            currentLog + ChatModel(
                isMe = true,
                message = message,
            )
        }

        viewModelScope.launch {
            sendMessageUseCase(message)
        }
    }
}

data class ChatModel(
    val isMe: Boolean = false,
    val message: String = "무엇을 도와드릴까요?",
)

// 1. Stream 구현
// 2. 채팅 UI 정상 구현
// 3. 룸 구현
// 4. 캐싱 구현
// 5. 에러일 때 대응
// 6. Claude 구현
// 7. 스레드 관리
// 문제 생성 시 logprobs 값 비교하기
