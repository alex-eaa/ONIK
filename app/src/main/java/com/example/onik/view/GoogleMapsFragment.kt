package com.example.onik.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.onik.R
import com.example.onik.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

const val REFRESH_PERIOD = 60000L
const val MINIMAL_DISTANCE = 100F

class GoogleMapsFragment : Fragment() {

    private val permissionGeoResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            when (result) {
                result -> if (result) {
                    map?.let { activateMyLocation(it) }
                }
                else -> {
                    Toast.makeText(requireContext(),
                        "ВЫ НЕ ДАЛИ ДОСТУП К ГЕОЛОКАЦИИ",
                        Toast.LENGTH_LONG).show()
                }
            }
        }

    companion object {
        const val BUNDLE_EXTRA_ADDRESS: String = "BUNDLE_EXTRA_ADDRESS"

        fun newInstance(bundle: Bundle): GoogleMapsFragment =
            GoogleMapsFragment().apply { arguments = bundle }
    }

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private var menu: Menu? = null

    private var myLocation: Location? = null
    private var map: GoogleMap? = null

    private val callback = OnMapReadyCallback { googleMap ->
        activateMyLocation(googleMap)
        map = googleMap

        googleMap.setOnMapLongClickListener { latLng ->
            getAddressAsync(latLng)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        activity?.title = resources.getString(R.string.title_maps)

        arguments?.getString(BUNDLE_EXTRA_ADDRESS)?.let {
            searchByAddress(it)
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchByAddress()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_maps_fragment, menu)
        menu.findItem(R.id.action_sort)?.isVisible = false
        menu.findItem(R.id.action_maps)?.isVisible = false
        menu.findItem(R.id.action_search)?.isVisible = false
        this.menu = menu
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_get_location -> {
                activity?.let {
                    showInfo("Мои координаты:",
                        "lat: ${myLocation?.latitude}  \nlon: ${myLocation?.longitude}")
                }
                return true
            }
            R.id.action_get_address -> {
                myLocation?.let { showMyAddressAsync(it) }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun activateMyLocation(googleMap: GoogleMap) {
        context?.let {
            val isPermissionGranted =
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED

            googleMap.isMyLocationEnabled = isPermissionGranted
            googleMap.uiSettings.isMyLocationButtonEnabled = isPermissionGranted

            menu?.findItem(R.id.action_get_address)?.isVisible = isPermissionGranted
            menu?.findItem(R.id.action_get_location)?.isVisible = isPermissionGranted


            if (!isPermissionGranted) {
                permissionGeoResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                getMyLocation()
            }
        }

    }


    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.getProvider(LocationManager.GPS_PROVIDER)?.let {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    REFRESH_PERIOD,
                    MINIMAL_DISTANCE,
                    object : LocationListener {
                        @SuppressLint("SetTextI18n")
                        override fun onLocationChanged(location: Location) {
                            myLocation = location
                            val latLng = LatLng(location.latitude, location.longitude)
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?,
                        ) {
                        }

                        //                        Если пользователь включил GPS.
                        override fun onProviderEnabled(provider: String) {}

                        //                        Если пользователь выключил GPS или сразу, если GPS был отключён изначально.
                        override fun onProviderDisabled(provider: String) {}
                    }
                )
            }
        } else {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?.let { location ->
                    myLocation = location
                    val latLng = LatLng(location.latitude, location.longitude)
                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                } ?: run {
                Toast.makeText(activity, "Невозможно определить координаты. Включите GPS",
                    Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun showMyAddressAsync(location: Location) {
        context?.let { context ->
            val geoCoder = Geocoder(context)

            Thread {
                try {
                    val address =
                        geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                    address[0].getAddressLine(0)?.let {
                        view?.post { showInfo("Мой адрес:", it) }
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }


    private fun getAddressAsync(location: LatLng) {
        context?.let {
            val geoCoder = Geocoder(it)

            Thread {
                try {
                    val addresses =
                        geoCoder.getFromLocation(location.latitude, location.longitude, 1)

                    view?.let { view ->
                        view.post {
                            addresses[0].getAddressLine(0).also { str ->
                                binding.textAddress.text = str
                            }
                        }

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }


    private fun initSearchByAddress() {
        binding.buttonSearch.setOnClickListener {
            val searchText = binding.searchAddress.text.toString()
            searchByAddress(searchText)
        }
    }


    private fun searchByAddress(address: String) {
        val geoCoder = Geocoder(view?.context)
        Thread {
            try {
                val addresses = geoCoder.getFromLocationName(address, 1)
                if (addresses.size > 0) {
                    view?.let { goToAddress(addresses, it, address) }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }


    private fun goToAddress(
        addresses: MutableList<Address>,
        view: View,
        searchText: String,
    ) {
        val location = LatLng(
            addresses[0].latitude,
            addresses[0].longitude
        )
        view.post {
            setMarker(location, searchText)
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        }
    }


    private fun setMarker(
        location: LatLng,
        searchText: String,
    ): Marker {
        return map?.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
        )!!
    }


    private fun showInfo(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it).setTitle(title)
                .setMessage(message).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

