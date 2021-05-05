package com.kangdroid.vocabapplication.hilt

import android.content.Context
import androidx.room.Room
import com.kangdroid.vocabapplication.data.entity.user.UserDao
import com.kangdroid.vocabapplication.data.entity.user.UserDatabase
import com.kangdroid.vocabapplication.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            UserDatabase::class.java
        ).build()
    }

    @Provides
    fun provideUserDao(database: UserDatabase): UserDao {
        return database.getUserDao()
    }

    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }
}