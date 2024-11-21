package com.example.coursefinderapp.di

import android.util.Log
import com.example.coursefinderapp.data.remote.api.NetworkClientConfig.API_KEY
import com.example.coursefinderapp.data.remote.api.NetworkClientConfig.BASE_URL_FIREBASE
import com.example.coursefinderapp.data.remote.api.service.AuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object FirebaseNetworkModule {

    @Provides
    @Named("FirebaseRetrofit")
    fun provideRetrofit(
        @Named("FirebaseOkHttp") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_FIREBASE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideHttpInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    fun provideApiKeyInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val originalUrl = original.url

            Log.d("API_KEY", "Original URL: ${originalUrl}")

            val urlWithApiKey = originalUrl.newBuilder()
                .addQueryParameter("key", API_KEY)
                .build()

            Log.d("API_KEY", "URL with API key: ${urlWithApiKey}")

            val requestWithApiKey = original.newBuilder()
                .url(urlWithApiKey)
                .build()

            chain.proceed(requestWithApiKey)
        }
    }


    @Provides
    @Named("FirebaseOkHttp")
    fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Provides
    fun provideAuthApiService(@Named("FirebaseRetrofit") retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)


}