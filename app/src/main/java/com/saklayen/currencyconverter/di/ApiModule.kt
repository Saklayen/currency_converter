package com.saklayen.currencyconverter.di

import android.content.Context
import androidx.databinding.ktx.BuildConfig
import com.asainternational.ambsmobile.prefs.PreferenceStorage
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.saklayen.currencyconverter.api.ApiService
import com.saklayen.currencyconverter.api.adapter.FlowCallAdapterFactory
import com.saklayen.currencyconverter.api.interceptor.TokenInterceptor
import com.saklayen.currencyconverter.preference.RefreshTokenUseCase
import com.saklayen.currencyconverter.preference.TokenUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideBaseUrl() = API_URL

    @Singleton
    @Provides
    fun provideInterceptor(): Interceptor {
        return if (BuildConfig.DEBUG) StethoInterceptor()
        else HttpLoggingInterceptor { Timber.tag("OkHttp").d(it) }
    }

    @Singleton
    @Provides
    fun provideTokenInterceptor(
        @ApplicationContext context: Context,
        storage: PreferenceStorage,
        tokenUseCase: TokenUseCase,
        refreshTokenUseCase: RefreshTokenUseCase,
        interceptor: Interceptor
    ) = TokenInterceptor(context, storage, tokenUseCase, refreshTokenUseCase, interceptor)


    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: Interceptor, tokenInterceptor: TokenInterceptor) =
        OkHttpClient.Builder()
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .connectTimeout(300, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .addInterceptor(tokenInterceptor)
            .build()


    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideRetrofit(mBaseUrl: HttpUrl, mClient: OkHttpClient, mMoshi: Moshi): Retrofit =
        Retrofit.Builder()
            .client(mClient)
            .baseUrl(mBaseUrl)
            .addConverterFactory(MoshiConverterFactory.create(mMoshi))
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun provideApiService(mRetrofit: Retrofit): ApiService =
        mRetrofit.create(ApiService::class.java)

    companion object {
        val API_URL: HttpUrl = "https://api.exchangeratesapi.io/".toHttpUrl()
    }
}
