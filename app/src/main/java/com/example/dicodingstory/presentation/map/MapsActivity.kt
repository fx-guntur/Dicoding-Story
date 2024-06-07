package com.example.dicodingstory.presentation.map

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.dicodingstory.R
import com.example.dicodingstory.base.BaseActivity
import com.example.dicodingstory.data.model.Story
import com.example.dicodingstory.databinding.ActivityMapsBinding
import com.example.dicodingstory.domain.common.ResultStatus
import com.example.dicodingstory.presentation.MainActivity
import com.example.dicodingstory.utils.helper.addMultipleMarker
import com.example.dicodingstory.utils.helper.centerAndZoomOnJakarta
import com.example.dicodingstory.utils.helper.convertToLatLng
import com.example.dicodingstory.utils.helper.observe
import com.example.dicodingstory.utils.helper.setCustomStyle
import com.example.dicodingstory.utils.helper.showSnackBar
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : BaseActivity<ActivityMapsBinding>() {
    private val mapsViewModel: MapsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    // Navigate to MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    override fun onViewBindingCreated(savedInstanceState: Bundle?) {
        super.onViewBindingCreated(savedInstanceState)

        initMap()
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(binding.containerMap.id) as SupportMapFragment

        mapFragment.getMapAsync { googleMap ->
            googleMap.uiSettings.isZoomControlsEnabled = true
            googleMap.uiSettings.isZoomGesturesEnabled = true
            googleMap.uiSettings.isCompassEnabled = true
            googleMap.setCustomStyle(this)
            observe(mapsViewModel.recentStories) {
                onStoriesResult(googleMap, it)
            }
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivityMapsBinding =
        ActivityMapsBinding::inflate

    private fun setButtonViews(googleMap: GoogleMap, listLocation: List<LatLng>) {
        binding.btnBounds.setOnClickListener {
            googleMap.centerAndZoomOnJakarta()
        }
    }

    private fun onStoriesResult(googleMap: GoogleMap, result: ResultStatus<List<Story>>) {
        when (result) {
            is ResultStatus.Loading -> showProgressBar(true)
            is ResultStatus.Success -> {
                showProgressBar(false)
                googleMap.addMultipleMarker(result.data)
                val listLocations = result.data.convertToLatLng()
                googleMap.centerAndZoomOnJakarta()
                setButtonViews(googleMap, listLocations)
            }

            is ResultStatus.Error -> {
                showProgressBar(false)
                binding.root.showSnackBar(result.message)
            }
        }
    }

    private fun showProgressBar(status: Boolean) {
        binding.progressBar6.visibility = if (status) View.VISIBLE else View.GONE
    }
}