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
        @Header("Authorization") header1: String,
        @Body gptRequestDto: GptRequestDto,
    ): ResponseBody
}
