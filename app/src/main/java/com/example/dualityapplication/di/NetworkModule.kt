package com.example.dualityapplication.di

import android.provider.SyncStateContract
import com.example.dualityapplication.api.AuthApi
import com.example.dualityapplication.api.AuthInterceptor
import com.example.dualityapplication.api.UserApi
import com.example.dualityapplication.utils.Util
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    private var client = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(Util.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }
    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun providesAuthAPI(retrofitBuilder: Retrofit.Builder): AuthApi {
        return retrofitBuilder.build().create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): UserApi {
        return retrofitBuilder.client(okHttpClient).build().create(UserApi::class.java)
    }


}