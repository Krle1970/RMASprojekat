package com.example.projekatv2.utils

import com.google.android.gms.tasks.Task
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.projekatv2.R
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

// Postojeća funkcija
suspend fun <T> Task<T>.await(): T {
    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            if (it.exception != null) {
                cont.resumeWithException(it.exception!!)
            } else {
                cont.resume(it.result, null)
            }
        }
    }
}

// Nova funkcija za dobijanje ikone sporta
fun getSportIcon(sportType: String): Int {
    return when (sportType) {
        "football" -> R.drawable.ic_football_marker
        "basketball" -> R.drawable.ic_basketball_marker
        "tennis" -> R.drawable.ic_tennis_marker
        // Dodajte ostale sportove ovde
        else -> R.drawable.add_event_icon
    }
}

// Nova funkcija za dodavanje markera na mapu
fun addSportMarkerToMap(
    map: GoogleMap?,
    location: LatLng,
    sportType: String,
    title: String
) {
    map?.let {
        val iconResId = getSportIcon(sportType)
        val markerOptions = MarkerOptions()
            .position(location)
            .title(title)
            .icon(BitmapDescriptorFactory.fromResource(iconResId))

        it.addMarker(markerOptions)
    }
}
