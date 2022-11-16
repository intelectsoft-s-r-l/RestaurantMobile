package com.example.igor.restaurantmobile.di

import com.example.igor.restaurantmobile.data.repo.RemoteApiInterface
import com.example.igor.restaurantmobile.BuildConfig
import com.example.igor.restaurantmobile.controllers.App
import com.example.igor.restaurantmobile.controllers.AssortmentController
import com.example.igor.restaurantmobile.data.datastore.SettingsRepository
import com.example.igor.restaurantmobile.di.annotations.OkHttpNoAuthInterceptor
import com.example.igor.restaurantmobile.di.annotations.RetrofitNoAuthInterceptor
import com.example.igor.restaurantmobile.utils.ContextManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    fun providesAssortmentController() : AssortmentController = AssortmentController

    @Provides
    @Singleton
    fun settingsRepo(): SettingsRepository {
        return SettingsRepository(ContextManager.retrieveApplicationContext())
    }


    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )

    @Provides
    @Singleton
    @OkHttpNoAuthInterceptor
    fun provideOkHttpClientNoLoginInterceptor(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(loggingInterceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    @RetrofitNoAuthInterceptor
    fun provideRetrofitNoLoginInterceptor(
        @OkHttpNoAuthInterceptor okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
//            .baseUrl(BuildConfig.auth_api_url)
            .baseUrl("http://edi.md:4444/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAddressApiInterface(@RetrofitNoAuthInterceptor retrofit: Retrofit): RemoteApiInterface =
        retrofit.create(RemoteApiInterface::class.java)

    @Provides
    fun providesApplication(): App = App.instance
}