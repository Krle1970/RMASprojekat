package com.example.projekatv2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.projekatv2.Navigation.Router
import com.example.projekatv2.viewmodels.AuthViewModel
import com.example.projekatv2.viewmodels.AuthViewModelFactory
import com.example.projekatv2.viewmodels.SportViewModel
import com.example.projekatv2.viewmodels.SportViewModelFactory
import com.example.projekatv2.ui.theme.ProjekatV2Theme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory()
    }
    private val sportViewModel: SportViewModel by viewModels {
        SportViewModelFactory()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicijalizuj Firebase ovde
        FirebaseApp.initializeApp(this)

        // Proveri da li dozvola za lokaciju već postoji
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Ako dozvola nije odobrena, zatraži dozvolu
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

        setContent {
            ProjekatV2Theme {
                Router(authViewModel, sportViewModel)
            }
        }
    }

    // Funkcija koja se poziva kada korisnik odgovori na zahtev za dozvolu
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            // Proveri da li je dozvola odobrena
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Dozvola je odobrena, možeš koristiti lokacione funkcije
            } else {
                // Dozvola je odbijena, obavesti korisnika ili onemogući funkcionalnost
            }
        }
    }
}
