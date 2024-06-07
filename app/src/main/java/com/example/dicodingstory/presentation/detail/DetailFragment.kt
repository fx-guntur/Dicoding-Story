package com.example.dicodingstory.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dicodingstory.R
import com.example.dicodingstory.base.BaseFragment
import com.example.dicodingstory.data.model.Story
import com.example.dicodingstory.databinding.FragmentDetailBinding
import com.example.dicodingstory.domain.common.ResultStatus
import com.example.dicodingstory.utils.helper.formatDate
import com.example.dicodingstory.utils.helper.observe
import com.example.dicodingstory.utils.helper.showSnackBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailFragment : BaseFragment<FragmentDetailBinding>() {
    private lateinit var idStory: String

    private val detailViewModel: DetailViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.visibility = View.GONE
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater, container, false)
    }

    override fun initUI() {
        idStory = DetailFragmentArgs.fromBundle(
            requireArguments()
        ).idStory

        binding.apply {
            returnButton.setOnClickListener {
                backToMain()
            }
        }
    }

    override fun initProcess() {
        observe(detailViewModel.getStory(idStory), ::detailResultStatus)
    }

    private fun detailResultStatus(result: ResultStatus<Story>) {
        when (result) {
            is ResultStatus.Loading -> showProgressBar(true)

            is ResultStatus.Success -> {
                showProgressBar(false)
                setStoryDetail(result.data)
            }

            is ResultStatus.Error -> {
                showProgressBar(false)
                binding.root.showSnackBar(result.message)
            }
        }
    }

    private fun setStoryDetail(story: Story) {
        binding.apply {
            Glide.with(root.context)
                .load(story.photoUrl)
                .into(storyIv)
            usernameTv.text = story.name
            descriptionTv.text = story.description
            dateCreatedTv.text = story.createdAt.formatDate()
            if (story.latitude != null && story.longitude != null) {
                latitudeTv.text = story.latitude.toString()
                longitudeTv.text = story.longitude.toString()
            }
        }
    }

    private fun backToMain() {
        findNavController().navigateUp()
    }

    private fun showProgressBar(status: Boolean) {
        binding.progressBar3.visibility = if (status) View.VISIBLE else View.GONE
    }

}