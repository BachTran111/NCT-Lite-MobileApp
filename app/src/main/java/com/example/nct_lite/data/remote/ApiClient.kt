package com.example.nct_lite.data.remote

import com.example.nct_lite.data.remote.api.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // ⚠️ Em dùng localhost → Android emulator cần dùng 10.0.2.2
    private const val BASE_URL = "http://10.0.2.2:5000/api/"

    // Token sẽ lưu ở đây (hoặc SharedPreferences)
    var authToken: String? = null

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Interceptor tự động thêm Authorization: Bearer <token>
    private val authInterceptor = Interceptor { chain ->
        val request = chain.request()
        val newRequest = if (authToken != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $authToken")
                .build()
        } else request
        chain.proceed(newRequest)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // region API Interface
    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val songApi: SongApi by lazy { retrofit.create(SongApi::class.java) }
    val albumApi: AlbumApi by lazy { retrofit.create(AlbumApi::class.java) }
    val genreApi: GenreApi by lazy { retrofit.create(GenreApi::class.java) }
    val historyApi: HistoryApi by lazy { retrofit.create(HistoryApi::class.java) }
    // endregion
}
