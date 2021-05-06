package com.kangdroid.vocabapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kangdroid.vocabapplication.data.entity.word.Word
import com.kangdroid.vocabapplication.data.entity.word.WordDao
import com.kangdroid.vocabapplication.data.repository.WordRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class HomeViewModelTest {
    // Rule that every android-thread should launched in single thread
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    /* Copyright 2019 Google LLC.
SPDX-License-Identifier: Apache-2.0 */
    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }

    @Test
    fun is_setRandomWordList_notifies_liveData() {
        class FakeWordRepository: WordDao {
            override suspend fun getAllWordList(): List<Word> {
                return listOf(
                    Word(null, "Category", "test", "Exam")
                )
            }
        }
        val homeViewModel: HomeViewModel = HomeViewModel(
            WordRepository(FakeWordRepository())
        )

        homeViewModel.setRandomWordList()
        val gotValue: MutableList<Word> = homeViewModel.randomWordList.getOrAwaitValue()

        assertThat(gotValue.size).isEqualTo(1)
        assertThat(gotValue[0].category).isEqualTo("Category")
        assertThat(gotValue[0].word).isEqualTo("test")
        assertThat(gotValue[0].meaning).isEqualTo("Exam")
    }
}