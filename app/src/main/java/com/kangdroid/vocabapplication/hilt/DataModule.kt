package com.kangdroid.vocabapplication.hilt

import android.content.Context
import androidx.room.Room
import com.kangdroid.vocabapplication.data.entity.user.UserDao
import com.kangdroid.vocabapplication.data.entity.user.UserDatabase
import com.kangdroid.vocabapplication.data.entity.word.WordDao
import com.kangdroid.vocabapplication.data.entity.word.WordDatabase
import com.kangdroid.vocabapplication.data.repository.UserRepository
import com.kangdroid.vocabapplication.data.repository.WordRepository
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
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user.db"
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

    @Provides
    fun provideWordDatabase(@ApplicationContext context: Context): WordDatabase {
        return Room.databaseBuilder(
            context,
            WordDatabase::class.java,
            "word.db"
        ).createFromAsset("database/word.db")
            .build()
    }

    @Provides
    fun provideWordDao(database: WordDatabase): WordDao {
        return database.getWordDao()
    }

    @Provides
    fun provideWordRepository(wordDao: WordDao): WordRepository {
        return WordRepository(wordDao)
    }
}