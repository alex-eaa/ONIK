package com.example.onik.view

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.onik.R
import com.example.onik.databinding.FragmentMapBinding
import java.io.IOException

const val REFRESH_PERIOD = 60000L
const val MINIMAL_DISTANCE = 100F

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        activity?.title = resources.getString(R.string.title_maps)
        getLocation()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_favorites_fragment, menu)
        menu.findItem(R.id.action_sort)?.isVisible = false
        menu.findItem(R.id.action_maps)?.isVisible = false
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchText: SearchView? = menu.findItem(R.id.action_search)?.actionView as SearchView?
        searchText?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(activity, "Поиск адреса", Toast.LENGTH_LONG).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        super.onPrepareOptionsMenu(menu)
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
                            binding.myCoordinates.text =
                                "Мои координаты: \nlat ${location.latitude}  lon ${location.longitude}"
                            getAddressByLocation(location)
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
                    getAddressByLocation(location)
                } ?: run {
                Toast.makeText(activity, "Невозможно определить координаты. Включите GPS",
                    Toast.LENGTH_LONG).show()
            }
        }


    }

    @SuppressLint("SetTextI18n")
    private fun getAddressByLocation(location: Location) {
        val geocoder = Geocoder(activity)

        Thread {
            try {
                val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                binding.container.post {
                    binding.myAddress.text = "Мой адрес: ${address[0].getAddressLine(0)}"
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }


    private fun getAddressByLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(activity)

        Thread {
            try {
                val address = geocoder.getFromLocation(latitude, longitude, 1)
                binding.container.post {
                    binding.myAddress.text = "Мой адрес: ${address[0].getAddressLine(0)}"
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}