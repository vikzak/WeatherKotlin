package ru.gb.weatherkotlin

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.location.Criteria
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class GeoZones : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(this)

        //
        val radius = 100
        val geofence: Geofence = Geofence.Builder()
            .setRequestId("office")
            .setCircularRegion(55.774472, 37.583050, radius.toFloat())
            .build()

        val geofenceList = listOf(geofence)
        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofenceList)
            .build()

//        val geoService = Intent(this, GeoZones::class.java)
//        val pendingIntent = PendingIntent
//            .getService(this, 0, geoService, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
//        var googleApiClient = GoogleApiClient.Builder(this)
//            .addApi(LocationServices.API)
//            .addConnectionCallbacks(connectionCallBack)
//            .build()
//
//        val locationManager =
//            this.getSystemService(this.LOCATION_SERVICE) as LocationManager
//        val criteria = Criteria()
//        criteria.accuracy = Criteria.ACCURACY_COARSE
//
//        val provider = locationManager.getBestProvider(criteria, true)
//        provider?.let {
//            var onLocationListener = LocationListener()
//            locationManager.requestLocationUpdates( provider,
//                REFRESH_PERIOD,
//                MINIMAL_DISTANCE,
//                onLocationListener
//            ) }
    }
}