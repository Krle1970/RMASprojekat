package com.example.projekatv2.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class LocationClientImpl(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationClient {

    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            // Proveri dozvole za lokaciju
            if (!context.hasLocationPermission()) {
                close(LocationClient.LocationException("Nedostaje odobrenje za lokaciju")) // Zatvori tok ako nema dozvola
                return@callbackFlow
            }

            // Provera da li su GPS i mreža omogućeni
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGpsEnabled && !isNetworkEnabled) {
                close(LocationClient.LocationException("GPS je onemogućen"))
                return@callbackFlow
            }

            // Kreiranje zahteva za ažuriranje lokacije
            val request = LocationRequest.Builder(interval)
                .setIntervalMillis(interval)
                .setMinUpdateIntervalMillis(interval)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            // Proveri dozvole još jednom pre nego što pošalješ zahtev za ažuriranje lokacije
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                close(LocationClient.LocationException("Nedostaju dozvole za lokaciju"))
                return@callbackFlow
            }

            // Pokretanje zahteva za ažuriranje lokacije
            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            // Zatvori tok kada ažuriranje lokacije prestane
            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }
}
