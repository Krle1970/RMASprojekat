/*package com.example.projekatv2.screens

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projekatv2.data.Resource
import com.example.projekatv2.screens.components.DatePicker
import com.example.projekatv2.screens.components.TimePicker
import com.example.projekatv2.viewmodels.SportViewModel
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddNewSportEventScreen(
    sportViewModel: SportViewModel?,
    location: MutableState<LatLng?>,
    navController: NavController
) {
    val context = LocalContext.current
    val eventDescription = remember { mutableStateOf("") }
    val eventDate = remember { mutableStateOf(LocalDate.now()) }
    val eventTime = remember { mutableStateOf(LocalTime.now()) }
    val selectedImage = remember { mutableStateOf<Uri?>(Uri.EMPTY) }
    val buttonIsEnabled = remember { mutableStateOf(true) }
    val buttonIsLoading = remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Unos opisa događaja
        TextField(
            value = eventDescription.value,
            onValueChange = { eventDescription.value = it },
            label = { Text("Opis događaja") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Biranje datuma
        Text("Datum događaja: ${eventDate.value}")
        DatePicker(selectedDate = eventDate)

        Spacer(modifier = Modifier.height(16.dp))

        // Biranje vremena
        Text("Vreme događaja: ${eventTime.value}")
        TimePicker(selectedTime = eventTime)

        Spacer(modifier = Modifier.height(16.dp))

        // Biranje slike za događaj
        Text("Dodaj sliku za događaj")
        // Implementiraj logiku za biranje slike

        Spacer(modifier = Modifier.height(16.dp))

        // Dugme za čuvanje događaja
        Button(
            onClick = {
                buttonIsLoading.value = true
                sportViewModel?.saveSportData(
                    description = eventDescription.value,
                    intensity = 1, // Ovde možeš dodati intenzitet ako je potreban
                    mainImage = selectedImage.value!!,
                    galleryImages = listOf(), // Ako imaš galeriju slika, promeni ovo
                    location = location,
                    date = Date.from(LocalDateTime.of(eventDate.value, eventTime.value).atZone(ZoneId.systemDefault()).toInstant())
                )
            },
            enabled = buttonIsEnabled.value
        ) {
            if (buttonIsLoading.value) {
                CircularProgressIndicator()
            } else {
                Text("Dodaj događaj")
            }
        }
    }

    // Upravljanje stanjem nakon čuvanja događaja
    val saveEventState = sportViewModel?.sportFlow?.collectAsState()
    saveEventState?.value.let {
        when (it) {
            is Resource.Success -> {
                Toast.makeText(context, "Događaj uspešno sačuvan", Toast.LENGTH_LONG).show()
                navController.popBackStack()
            }
            is Resource.Failure -> {
                Toast.makeText(context, "Došlo je do greške", Toast.LENGTH_LONG).show()
                buttonIsLoading.value = false
            }
            is Resource.Loading -> { /* Prikazivanje stanja učitavanja ako je potrebno */ }
            null -> { /* Nema akcije */ }
        }
    }
}*/
