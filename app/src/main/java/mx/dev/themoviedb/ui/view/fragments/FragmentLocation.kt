package mx.dev.themoviedb.ui.view.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mx.dev.themoviedb.R
import mx.dev.themoviedb.data.model.LocationModel
import mx.dev.themoviedb.databinding.FragmentLocationBinding
import mx.dev.themoviedb.ui.view.components.CustomDialogFragment
import mx.dev.themoviedb.ui.viewmodel.LocationViewModel

class FragmentLocation : Fragment() {
    private lateinit var binding: FragmentLocationBinding
    private lateinit var mMap: GoogleMap
    private var mapReady = false
    private var listLocations = emptyList<LocationModel>()
    private lateinit var dialog: CustomDialogFragment

    //View model
    private val locationViewModel: LocationViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        initLocation()
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            mapReady = true
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(20.743192443847654, -103.44736267089844)))
        }
        locationViewModel.onCreate()
        initObservers()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initLocation() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    Log.i("LOCATION", "Precise location access granted.")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Log.i("LOCATION", "Only approximate location access granted.")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false) -> {
                    Log.i("LOCATION", "Background location access granted.")
                }
                else -> {
                    Log.i("LOCATION", "No location access granted.")
                }
            }
        }
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        )
    }

    private fun initObservers() {
        locationViewModel.locationListMutable.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                updateMap(it)
            }
        })
    }

    private fun updateMap(locations: List<LocationModel>) {
        if (mapReady) {
            locations.forEach {
                val marker = LatLng(it.latitude, it.longitude);
                mMap.addMarker(MarkerOptions().position(marker))
            }
            val lastLocation = LatLng(locations[locations.size-1].latitude, locations[locations.size-1].longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLocation))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11F))
        }
    }

    private fun cleanListLocation(){
        locationViewModel.removeListMutable()
    }

    private fun addListLocation(){
        locationViewModel.copyListToMutableList()
    }

    private fun showDialogResults(title:String, content:String){
        dialog = CustomDialogFragment(title = title, content = content,)
        if (!dialog.isAdded)
            dialog.show(childFragmentManager, "TAG")
    }

    override fun onResume() {
        super.onResume()
        addListLocation()
        Log.i("ONREs", "asasa")
    }

    override fun onPause() {
        super.onPause()
        cleanListLocation()
    }

}