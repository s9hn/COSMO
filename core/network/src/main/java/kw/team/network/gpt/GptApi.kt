package kw.team.network.gpt

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Streaming

interface GptApi {

    @Streaming
    @POST("v1/chat/completions")
    suspend fun postMessage(
        @Header("Authorization") header1: String = "Bearer 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",
        @Body gptRequestDto: GptRequestDto,
    ): ResponseBody
}
