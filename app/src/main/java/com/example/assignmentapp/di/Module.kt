package com.example.assignmentapp.di

import android.content.Context
import androidx.room.Room
import com.example.assignmentapp.BuildConfig
import com.example.assignmentapp.data.repository.UserRepositoryImpl
import com.example.assignmentapp.data.source.local.AssignmentAppDatabase
import com.example.assignmentapp.data.source.local.PrefDataStore
import com.example.assignmentapp.data.source.local.PrefDataStoreImpl
import com.example.assignmentapp.data.source.local.UserDataSource
import com.example.assignmentapp.data.source.local.UserDataSourceImpl
import com.example.assignmentapp.data.source.remote.NewsApi
import com.example.assignmentapp.data.source.remote.network.ApiKeyInterceptor
import com.example.assignmentapp.data.source.remote.network.NetworkConfig.TIMEOUT
import com.example.assignmentapp.data.source.remote.network.createAppApiClient
import com.example.assignmentapp.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val apiKeyInterceptor = ApiKeyInterceptor(BuildConfig.NEWS_API_KEY)

        return OkHttpClient.Builder().connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideUserApi(okHttpClient: OkHttpClient): NewsApi {
        val baseUrl = BuildConfig.BASE_API_URL
        val wMSRoleApiClient = createAppApiClient(baseUrl, okHttpClient)
        return wMSRoleApiClient.create(NewsApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(
        userDataSource: UserDataSource,
        prefDataStore: PrefDataStore
    ): UserRepository {
        return UserRepositoryImpl(
            userDataSource,
            prefDataStore
        )
    }
}


@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideUserDataSource(
        assignmentAppDatabase: AssignmentAppDatabase
    ): UserDataSource {
        return UserDataSourceImpl(assignmentAppDatabase.userDao())
    }

    @Singleton
    @Provides
    fun providePrefDataStore(
        @ApplicationContext appContext: Context
    ): PrefDataStore {
        return PrefDataStoreImpl(appContext)
    }

}


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AssignmentAppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AssignmentAppDatabase::class.java,
            "AssignmentAppDatabase.db"
        ).build()
    }
}