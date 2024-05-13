package com.example.landmarkremark.fragment.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.location.Location
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.landmarkremark.BR
import com.example.landmarkremark.R
import com.example.landmarkremark.activity.NotLoginActivity
import com.example.landmarkremark.databinding.FragmentMapBinding
import com.example.landmarkremark.fragment.BaseFragment
import com.example.landmarkremark.util.Constant
import com.example.landmarkremark.util.DelegatedPreferences
import com.example.landmarkremark.util.SingleClickListener
import com.example.landmarkremark.util.authenticate.UserManager
import com.example.landmarkremark.viewmodel.main.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker

class MapFragment : BaseFragment<MainViewModel, FragmentMapBinding>(R.layout.fragment_map),
    OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap

    override fun variableId(): Int = BR.mapViewModel

    override fun createViewModel(): Lazy<MainViewModel> = activityViewModels()

    override fun bindView(view: View): FragmentMapBinding = FragmentMapBinding.bind(view)

    override fun init() {
        super.init()
        binding.apply {
            locationBtLogout.setOnClickListener(object : SingleClickListener() {
                override fun onSingleClick(v: View) {
                    showAlertDialog("Do you want to logout account")
                }
            })
            locationTvUserName.text = getString(
                R.string.txt_user_name,
                DelegatedPreferences(requireContext(), Constant.KEY_USER_NAME, "").getValue()
            )
        }
    }


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

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            logoutAccount()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun logoutAccount() {
        UserManager.getInstance(requireContext()).removeAccount()
        val intent = Intent(requireActivity(), NotLoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }


}