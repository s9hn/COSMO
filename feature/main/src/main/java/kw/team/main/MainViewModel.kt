package kw.team.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kw.team.ai.model.AiModel
import kw.team.ai.usecase.ChatAiUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val chatAiUseCase: ChatAiUseCase,
) : ViewModel() {
    val aiModels: List<AiModel> get() = AiModel.entries.toList()
    val logs = MutableStateFlow("")

    fun converseWith(message: String) {
        viewModelScope.launch {
            chatAiUseCase(message)
        }
    }
}

// 1. Stream 구현
// 2. 채팅 UI 정상 구현
// 3. 룸 구현
// 4. 캐싱 구현
// 5. 에러일 때 대응
// 6. Claude 구현
// 7. 스레드 관리
// 문제 생성 시 logprobs 값 비교하기
