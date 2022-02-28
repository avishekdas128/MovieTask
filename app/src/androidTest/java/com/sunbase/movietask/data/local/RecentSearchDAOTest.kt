package com.sunbase.movietask.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.sunbase.movietask.data.local.model.RecentSearch
import com.sunbase.movietask.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class RecentSearchDAOTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: RecentSearchDatabase
    private lateinit var dao: RecentSearchDAO

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.getRecentSearchDAO()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertRecentSearch() = runBlocking {
        val recentSearch = RecentSearch(id = 1, search = "test")
        dao.updateRecentSearch(recentSearch)

        val recentSearches = dao.getTestRecentSearches().getOrAwaitValue()

        assertThat(recentSearches).contains(recentSearch)
    }

    @Test
    fun insertNoDuplicateRecentSearch() = runBlocking {
        val recentSearch = RecentSearch(search = "test")
        dao.updateRecentSearch(recentSearch)
        dao.updateRecentSearch(recentSearch.copy())

        val recentSearches = dao.getTestRecentSearches().getOrAwaitValue()

        assertThat(recentSearches).containsNoDuplicates()
    }

    @Test
    fun insertTenLimitedItemsOnly() = runBlocking {
        for (i in 0..11) {
            dao.updateRecentSearch(RecentSearch(search = i.toString()))
        }

        val recentSearches = dao.getTestRecentSearches().getOrAwaitValue()

        assertThat(recentSearches).hasSize(10)
    }
}