package ru.gb.weatherkotlin.view.main.maps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.search_map_layout.*
import ru.gb.weatherkotlin.R
import ru.gb.weatherkotlin.databinding.FragmentHistoryBinding
import ru.gb.weatherkotlin.databinding.SearchMapLayoutBinding
import java.io.IOException

class MapsFragment : Fragment() {
    private var _binding: SearchMapLayoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private val markers: ArrayList<Marker> = arrayListOf()
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val initialPlace = LatLng(52.52000659999999, 13.404953999999975)
        googleMap.addMarker(
            MarkerOptions()
                .position(initialPlace)
                //.title(getString(R.string.marker_start))
                .title("Start")
        )
        map.moveCamera(CameraUpdateFactory.newLatLng(initialPlace))
        map.uiSettings.isZoomControlsEnabled = true // ЗумКонтроль
        map.uiSettings.isZoomGesturesEnabled = true // жесты
        // моё местоположение
        activateMyLocation(map)
        map.setOnMapLongClickListener { latLng ->
            getAddressAsync(latLng)
            addMarkerToArray (latLng)
            drawLine ()
        }
    }

//    private val callback = OnMapReadyCallback { googleMap ->
//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//    }

    private fun activateMyLocation(googleMap: GoogleMap) {
        context?.let {
        val isPermissionGranted = ContextCompat
            .checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        googleMap.isMyLocationEnabled = isPermissionGranted
        googleMap.uiSettings.isMyLocationButtonEnabled = isPermissionGranted }
        //Получить разрешение, если его нет
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchMapLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchByAddress()
    }

    private fun getAddressAsync(location: LatLng) {
        context?.let {
            val geoCoder = Geocoder(it)
            Thread {
                try {
                    val addresses =
                        geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                    textAddress.post {
                        textAddress.text = addresses[0].getAddressLine(0)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(
            location, markers.size.toString()
        )
        if (marker != null){
            markers.add(marker)
        }
    }



    private fun setMarker(
        location: LatLng,
        searchText: String
    ): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
        )
    }

    private fun drawLine() {
        val last: Int = markers.size - 1
        if (last >= 1) {
            val previous: LatLng = markers[last - 1].position
            val current: LatLng = markers[last].position
            map.addPolyline(
                    PolylineOptions()
                        .add(previous, current)
                        .color(Color.RED)
                        .width(5f)
                    )
        }
    }

    private fun initSearchByAddress() {
        binding.buttonSearch.setOnClickListener {
        val geoCoder = Geocoder(it.context)
        val searchText = searchAddress.text.toString()
            Thread {
            try {
                val addresses = geoCoder.getFromLocationName(searchText, 1)
                if (addresses.size > 0) {
                    goToAddress(addresses, it, searchText)
                }
            } catch (e: IOException) { e.printStackTrace()
            }
            }.start()
    }
    }

    private fun goToAddress(addresses: MutableList<Address>, view: View, searchText: String
    ){
        val location = LatLng(
            addresses[0].latitude,
            addresses[0].longitude )
        view.post {
            setMarker(location, searchText)
            map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom( location, 15f )
                    )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = MapsFragment()
    }
}