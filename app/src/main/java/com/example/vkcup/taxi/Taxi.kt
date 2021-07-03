package com.example.vkcup.taxi

import android.Manifest
import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.vkcup.R
import com.example.vkcup.databinding.ActivityTaxiBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*
import kotlin.math.roundToInt


class Taxi : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var me: LatLng
    private var isMe: Boolean = false
    private var price: Int = 0
    private lateinit var to: LatLng
    private var isTo: Boolean = false
    private lateinit var binding: ActivityTaxiBinding
    private lateinit var locationManager: LocationManager
    private val myRequestId = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaxiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                myRequestId
            )
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            moveCamera(location)
        }

        override fun onProviderDisabled(provider: String) {
        }

        override fun onProviderEnabled(provider: String) {
            if (ActivityCompat.checkSelfPermission(
                    this@Taxi,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this@Taxi,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@Taxi,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    myRequestId
                )
                onProviderEnabled(provider)
            } else {
                moveCamera(locationManager.getLastKnownLocation(provider))
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        }
    }

    private fun moveCamera(location: Location?) {
        if (location == null) return
        me = LatLng(location.latitude, location.longitude)
        if (isMe) {
            mMap.clear()
            show(me, "Вы", false)
            if (isTo) {
                show(to, "Место назначения", false)
                drawLine()
            }
        } else {
            isMe = true
            show(me, "Вы", true)
        }
    }

    private fun showAlert() {
        val aboutDialog: AlertDialog = AlertDialog.Builder(
            this@Taxi
        ).setMessage("Поехали за $price ?")
            .setNegativeButton("Нет, дорого") { _, _ ->
                mMap.clear()
                show(me, "Вы", true)
            }
            .setPositiveButton("Да, отличная цена") { _, _ ->
                Toast.makeText(applicationContext, "Поездка прошла успешно!", Toast.LENGTH_LONG)
                    .show()
                mMap.clear()
                show(me, "Вы", true)
            }.create()

        aboutDialog.show()
        aboutDialog.setCancelable(false)

        (aboutDialog.findViewById(android.R.id.message) as TextView).movementMethod =
            LinkMovementMethod.getInstance()
    }

    private fun show(l: LatLng, text: String, needZoom: Boolean) {
        val markerOptions = MarkerOptions()
        markerOptions.position(l)
        markerOptions.title(text)
        mMap.addMarker(markerOptions)
        if (needZoom) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 15f))
        }
    }

    private fun drawLine() {
        val line = PolylineOptions()
        line.width(4f).color(R.color.black)
        val latLngBuilder = LatLngBounds.Builder()
        line.add(me)
        latLngBuilder.include(me)
        line.add(to)
        latLngBuilder.include(to)
        mMap.addPolyline(line)
        val w = resources.displayMetrics.widthPixels
        val h = resources.displayMetrics.heightPixels - 200
        val latLngBounds = latLngBuilder.build()
        val track = CameraUpdateFactory.newLatLngBounds(latLngBounds, w, h, 200)
        mMap.moveCamera(track)
        price =
            (kotlin.math.abs(me.latitude - to.latitude) * 10000 + kotlin.math.abs(me.longitude - to.longitude) * 10000).roundToInt()
        showAlert()
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this@Taxi,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@Taxi,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@Taxi,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                myRequestId
            )
            onResume()
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, (
                        1000 * 10).toLong(), 10f, locationListener
            )
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, (1000 * 10).toLong(), 10f,
                locationListener
            )
        }
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationListener)
    }
    
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener { ladling ->
            isTo = true
            mMap.clear()
            to = ladling
            if (isMe) {
                show(to, "Место назначения", false)
                show(me, "Вы", false)
                drawLine()
            } else {
                show(to, "Место назначения", false)
            }
        }
    }

}