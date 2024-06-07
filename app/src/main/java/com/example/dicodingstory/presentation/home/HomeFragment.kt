package com.example.dicodingstory.presentation.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingstory.R
import com.example.dicodingstory.base.BaseFragment
import com.example.dicodingstory.databinding.FragmentHomeBinding
import com.example.dicodingstory.presentation.add.AddFragment.Companion.IS_UPLOAD_SUCCESS_BUNDLE
import com.example.dicodingstory.presentation.add.AddFragment.Companion.IS_UPLOAD_SUCCESS_KEY
import com.example.dicodingstory.utils.helper.observe
import com.example.dicodingstory.utils.helper.showSnackBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val homeAdapter by lazy { HomeAdapter { idStory -> moveToDetail(idStory) } }

    private val homeViewModel: HomeViewModel by viewModel()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initStory()
        initObserve()
        initAction()
        resultStatus()
    }

    override fun initUI() {
        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.visibility = View.VISIBLE
    }

    override fun initProcess() {}

    private fun initStory() {
        binding.apply {
            rvStory.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = homeAdapter.withLoadStateFooter(
                    footer = HomeLoadStateAdapter {
                        homeAdapter.retry()
                    }
                )
                isNestedScrollingEnabled = false
            }

            homeAdapter.addLoadStateListener { loadState ->
                if (loadState.append.endOfPaginationReached) {
                    if (homeAdapter.itemCount < 1) {
                        showEmpty(true)
                    }
                }

                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        showProgressBar(true)
                    }

                    is LoadState.NotLoading -> {
                        showProgressBar(false)
                        binding.rvStory.scheduleLayoutAnimation()
                    }

                    is LoadState.Error -> {
                        showProgressBar(false)
                        binding.root.showSnackBar(getString(R.string.fail_to_load))
                    }
                }
            }
        }
    }

    private fun initAction() {
        binding.apply {
            refreshLayout.setOnRefreshListener {
                homeAdapter.refresh()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.rvStory.scrollToPosition(0)
                }, 50)
                refreshLayout.isRefreshing = false
            }
        }
    }

    private fun initObserve() {
        observe(homeViewModel.getStory()) { data ->
            homeAdapter.submitData(lifecycle, data)
        }
    }

    private fun resultStatus() {
        setFragmentResultListener(IS_UPLOAD_SUCCESS_KEY) { _, bundle ->
            val isUploadSuccess = bundle.getBoolean(IS_UPLOAD_SUCCESS_BUNDLE)

            if (isUploadSuccess) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.rvStory.scrollToPosition(0)
                }, 300)
            }
        }
    }

    private fun showProgressBar(status: Boolean) {
        binding.progressBar.visibility = if (status) View.VISIBLE else View.GONE
    }

    private fun showEmpty(isEmpty: Boolean) {

    }

    private fun moveToDetail(idStory: String) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailFragment(idStory)
        )
    }

}