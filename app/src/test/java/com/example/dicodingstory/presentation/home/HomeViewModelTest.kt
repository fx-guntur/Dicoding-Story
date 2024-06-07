package com.example.dicodingstory.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.dicodingstory.DataDummy
import com.example.dicodingstory.MainDispatcherRule
import com.example.dicodingstory.PagedDataSourceTest
import com.example.dicodingstory.data.model.Story
import com.example.dicodingstory.data.repository.StoryRepository
import com.example.dicodingstory.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        storyRepository = Mockito.mock(StoryRepository::class.java)
        homeViewModel = HomeViewModel(storyRepository)
    }

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyQuoteResponse()
        val data: PagingData<Story> = PagedDataSourceTest.snapshot(dummyStory)

        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data

        `when`(storyRepository.getPagingStory()).thenReturn(expectedStory)

        val mainViewModel = HomeViewModel(storyRepository)
        val actualStory: PagingData<Story> = mainViewModel.getStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = HomeAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainDispatcherRules.testDispatcher,
            workerDispatcher = mainDispatcherRules.testDispatcher
        )

        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val story: PagingData<Story> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = story
        `when`(storyRepository.getPagingStory()).thenReturn(expectedStory)

        val homeViewModel = HomeViewModel(storyRepository)
        val actualData: PagingData<Story> = homeViewModel.getStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = HomeAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainDispatcherRules.testDispatcher,
            workerDispatcher = mainDispatcherRules.testDispatcher
        )
        differ.submitData(actualData)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}