package com.example.dicodingstory.base

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.dicodingstory.R
import com.example.dicodingstory.utils.constant.AppConstants.LOCATION_PERMISSION
import com.example.dicodingstory.utils.helper.showSnackBar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import timber.log.Timber

@Suppress("DEPRECATION")
abstract class BaseLocationFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    private val priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    private val cancellationTokenSource = CancellationTokenSource()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container, savedInstanceState)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initServicesCheckAndCallApi()
        initProcess()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): VB

    abstract fun initUI()

    abstract fun initProcess()

    private fun initServicesCheckAndCallApi() {
        if (isLocationEnabled()) {
            initPermissionAndGetLocation()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    protected fun getLocation(onLocationReceived: (location: Location) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.label_location_permission_needed))
                .setMessage(getString(R.string.label_need_location))
                .setPositiveButton(
                    "OK"
                ) { p0, _ ->
                    permReqLauncher.launch(LOCATION_PERMISSION)
                    p0.dismiss()
                }
                .create()
                .show()
        }

        mFusedLocationClient?.getCurrentLocation(priority, cancellationTokenSource.token)
            ?.addOnSuccessListener { location ->
                if (location == null) return@addOnSuccessListener
                onLocationReceived(location)
            }
            ?.addOnFailureListener { exception ->
                exception.message?.let { binding.root.showSnackBar(it) }
            }
    }

    protected fun initPermissionAndGetLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        permReqLauncher.launch(LOCATION_PERMISSION)
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                getLocation {
                    Timber.d("lat ${it.latitude}, lng ${it.longitude}")
                }
            }
        }
}