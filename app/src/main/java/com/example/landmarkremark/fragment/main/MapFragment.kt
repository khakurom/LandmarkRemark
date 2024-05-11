package com.example.landmarkremark.fragment.main

import android.annotation.SuppressLint
import android.location.Location
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.landmarkremark.BR
import com.example.landmarkremark.R
import com.example.landmarkremark.databinding.FragmentMapBinding
import com.example.landmarkremark.fragment.BaseFragment
import com.example.landmarkremark.viewmodel.main.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker

class MapFragment : BaseFragment<MainViewModel, FragmentMapBinding>(R.layout.fragment_map),
    OnMapReadyCallback , GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap

    override fun variableId(): Int = BR.mapViewModel

    override fun createViewModel(): Lazy<MainViewModel> = activityViewModels()

    override fun bindView(view: View): FragmentMapBinding = FragmentMapBinding.bind(view)


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.apply {
            isMyLocationEnabled = true
            setOnMyLocationClickListener(this@MapFragment)
            setOnMarkerClickListener(this@MapFragment)
            uiSettings.apply {
                isMyLocationButtonEnabled = true
            }
        }
    }

    override fun onMyLocationClick(p0: Location) {

    }

    override fun onMarkerClick(p0: Marker): Boolean {
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)
        return false
    }

}