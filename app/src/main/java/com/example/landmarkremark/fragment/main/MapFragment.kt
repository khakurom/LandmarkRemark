package com.example.landmarkremark.fragment.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.landmarkremark.BR
import com.example.landmarkremark.R
import com.example.landmarkremark.activity.NotLoginActivity
import com.example.landmarkremark.databinding.FragmentMapBinding
import com.example.landmarkremark.fragment.BaseFragment
import com.example.landmarkremark.model.LandmarkInformation
import com.example.landmarkremark.util.Constant
import com.example.landmarkremark.util.DelegatedPreferences
import com.example.landmarkremark.util.DialogAddLandmark
import com.example.landmarkremark.util.DialogHelper
import com.example.landmarkremark.util.SingleClickListener
import com.example.landmarkremark.util.Utils
import com.example.landmarkremark.util.authenticate.UserManager
import com.example.landmarkremark.viewmodel.main.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : BaseFragment<MainViewModel, FragmentMapBinding>(R.layout.fragment_map),
	OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

	private lateinit var mMap: GoogleMap
	private var isSaveLandmark: Boolean = false

	override fun variableId(): Int = BR.mapViewModel

	override fun createViewModel(): Lazy<MainViewModel> = activityViewModels()

	override fun bindView(view: View): FragmentMapBinding = FragmentMapBinding.bind(view)

	private lateinit var fusedLocationClient: FusedLocationProviderClient


	override fun init() {
		super.init()
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
		accessLocationPermission()
		binding.apply {
			mapBtLogout.setOnClickListener(object : SingleClickListener() {
				override fun onSingleClick(v: View) {
					showAlertDialogLogoutAccount()
				}
			})
			mapBtnSaveLandmark.setOnClickListener(object : SingleClickListener() {
				override fun onSingleClick(v: View) {
					isSaveLandmark = true
					requestEnableLocationDevice(requireContext())
				}
			})
			mapBtnGetCurrentLocation.setOnClickListener(object : SingleClickListener() {
				override fun onSingleClick(v: View) {
					isSaveLandmark = false
					accessLocationPermission()
				}
			})
			mapTvUserName.text = getString(
				R.string.txt_user_name,
				DelegatedPreferences(requireContext(), Constant.KEY_USER_NAME, "").getValue()
			)
		}
	}

	@SuppressLint("MissingPermission")
	override fun onMapReady(googleMap: GoogleMap) {
		mMap = googleMap
		mMap.apply {
			setOnMyLocationClickListener(this@MapFragment)
			setOnMarkerClickListener(this@MapFragment)
			mMap.isMyLocationEnabled = true
			uiSettings.apply {
				isMyLocationButtonEnabled = true
			}
		}
		getAllLandmark()
		requestEnableLocationDevice(requireContext())
	}


	// check location permission in the application
	private fun accessLocationPermission() {
		return if (ContextCompat.checkSelfPermission(requireContext(), Constant.ACCESS_FINE_PERMISSION) == PackageManager.PERMISSION_DENIED) {
			requestAccessLocationPermissionLauncher.launch(Constant.ACCESS_FINE_PERMISSION)
		} else {
			val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
			mapFragment.getMapAsync(this)
		}
	}

	// request location permission if user has not permitted yet
	private val requestAccessLocationPermissionLauncher = registerForActivityResult(
		ActivityResultContracts.RequestPermission()
	) { granted ->
		if (!granted) {
			Toast.makeText(requireContext(), "Location permission is denied", Toast.LENGTH_SHORT).show()
		}
	}


	// require user turn on the device location
	private fun requestEnableLocationDevice(context: Context) {
		val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			showDialogTurnOnLocationDevice(context)
		} else {
			getCurrentLocation()
		}
	}

	// get all landmarks that are created by user
	private fun getAllLandmark() {
		if (Utils.isNetworkAvailable(requireContext())) {
			viewModel?.retrieveUserLandmark()
			viewModel?.landmarkList?.observe(this) {
				it.forEach { landmarkInformation ->
					val markerOptions = MarkerOptions()
						.position(LatLng(landmarkInformation.lat!!, landmarkInformation.lng!!))
						.icon(
							bitmapDescriptorFromLayout(
								R.layout.landmark_label,
								landmarkInformation.name ?: "",
								landmarkInformation.userName ?: "",
							)
						)
					mMap.addMarker(markerOptions)
				}
			}
		} else {
			DialogHelper.showAlertDialog(requireContext(), "Network is not available")
		}
	}

	private fun getCurrentLocation() {
		if (ContextCompat.checkSelfPermission(requireContext(), Constant.ACCESS_FINE_PERMISSION) == PackageManager.PERMISSION_DENIED) {
			requestAccessLocationPermissionLauncher.launch(Constant.ACCESS_FINE_PERMISSION)
		} else {
			fusedLocationClient.lastLocation
				.addOnSuccessListener { location: Location? ->
					location?.let {
						// Move camera to the current location with animation
						mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 50f), 2000, null)
						if (isSaveLandmark) {
							createLandmarkAtCurrentLocation(LatLng(it.latitude, it.longitude)) // create landmark at current location
							isSaveLandmark = false
						}
					}
				}
		}
	}

	private fun createLandmarkAtCurrentLocation(currentLocation: LatLng) {
		val userName = DelegatedPreferences(requireContext(), Constant.KEY_USER_NAME, "").getValue()
		val dialogAddLandmark = DialogAddLandmark(
			requireContext(),
			object : DialogAddLandmark.OnDialogClickListener {
				override fun onPositiveButtonClick(landmarkName: String) {
					// create a custom marker to the map
					val markerOptions = MarkerOptions()
						.position(currentLocation)
						.icon(
							bitmapDescriptorFromLayout(
								R.layout.landmark_label,
								landmarkName,
								userName,
							)
						)
					saveLandmark(userName, markerOptions, landmarkName, currentLocation) // save and add landmark into database

				}
			}
		)
		dialogAddLandmark.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		dialogAddLandmark.show()
	}

	private fun saveLandmark(userName: String, markerOptions: MarkerOptions, landmarkName: String, currentLocation: LatLng) {
		if (Utils.isNetworkAvailable(requireContext())) {
			viewModel?.createLandmark(
				userName,
				LandmarkInformation(landmarkName, currentLocation.latitude, currentLocation.longitude)
			)
			mMap.addMarker(markerOptions)
		} else {
			DialogHelper.showAlertDialog(requireContext(), "Network is not available")
		}
	}

	private fun bitmapDescriptorFromLayout(layoutId: Int, landmarkName: String, userName: String): BitmapDescriptor {
		val customMarkerView = layoutInflater.inflate(layoutId, null)
		val landmarkNameTextView = customMarkerView.findViewById<TextView>(R.id.tv_landmark_name)
		val userNameTextView = customMarkerView.findViewById<TextView>(R.id.tv_user_name)

		// Set information of landmark to label marker
		landmarkNameTextView.text = landmarkName
		userNameTextView.text = getString(R.string.txt_user_name, userName)

		// Measure and layout the custom marker view
		customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
		customMarkerView.layout(0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight)

		// Create a bitmap from the custom marker view
		val bitmap = Bitmap.createBitmap(customMarkerView.measuredWidth, customMarkerView.measuredHeight, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bitmap)
		customMarkerView.draw(canvas)

		return BitmapDescriptorFactory.fromBitmap(bitmap)
	}

	override fun onMyLocationClick(p0: Location) {

	}

	override fun onMarkerClick(p0: Marker): Boolean {
		mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)
		return false
	}

	private fun showDialogTurnOnLocationDevice(context: Context) {
		androidx.appcompat.app.AlertDialog.Builder(context)
			.setTitle("Turn on location of the device")
			.setMessage("To use the application, you should turn on location of the device")
			.setPositiveButton("Agree") { _, _ ->
				context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
			}
			.setNegativeButton("Cancel") { dialog, _ ->
				dialog.dismiss()
			}
			.show()
	}


	private fun showAlertDialogLogoutAccount() {
		val builder = AlertDialog.Builder(requireContext())
		builder.setMessage("Do you want to logout account")

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