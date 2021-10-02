package com.example.onik.view

import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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


class GoogleMapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private var myLocation: Location? = null
    private var map: GoogleMap? = null

    private val callback = OnMapReadyCallback { googleMap ->
        activateMyLocation(googleMap)
        map = googleMap
        getLocation()

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

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchByAddress()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_maps_fragment, menu)
        menu.findItem(R.id.action_sort)?.isVisible = false
        menu.findItem(R.id.action_maps)?.isVisible = false
        menu.findItem(R.id.action_search)?.isVisible = false
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


    @SuppressLint("MissingPermission")
    private fun activateMyLocation(googleMap: GoogleMap) {
        context?.let {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {
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


    @SuppressLint("SetTextI18n")
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
                            addresses[0].getAddressLine(0).also {
                                binding.textAddress.text = it
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
            val geoCoder = Geocoder(it.context)
            val searchText = binding.searchAddress.text.toString()
            Thread {
                try {
                    val addresses = geoCoder.getFromLocationName(searchText, 1)
                    if (addresses.size > 0) {
                        goToAddress(addresses, it, searchText)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
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
            setMarker(location, searchText, R.drawable.pin)
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }


    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int,
    ): Marker {
        return map?.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
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

