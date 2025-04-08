package com.example.projekatv2.screens

import androidx.compose.material3.*
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projekatv2.R
import com.example.projekatv2.data.Resource
import com.example.projekatv2.screens.components.*
import com.example.projekatv2.ui.theme.mainColor
import com.example.projekatv2.viewmodels.SportViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddNewSportEventBottomSheet(
    sportViewModel: SportViewModel?,
    location: MutableState<LatLng?>,
    sheetState: ModalBottomSheetState,
    onEventAdded: () -> Unit // Callback for event added
) {
    val showedAlert = remember { mutableStateOf(false) }
    val sportFlow = sportViewModel?.sportFlow?.collectAsState()
    val inputDescription = remember { mutableStateOf("") }
    val isDescriptionError = remember { mutableStateOf(false) }
    val descriptionError = remember { mutableStateOf("Ovo polje je obavezno") }
    val selectedIntensity = remember { mutableStateOf(0) }
    val selectedSport = remember { mutableStateOf("Football") }
    val buttonIsEnabled = remember { mutableStateOf(true) }
    val buttonIsLoading = remember { mutableStateOf(false) }

    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val selectedTime = remember { mutableStateOf(LocalTime.now()) }
    val selectedPeople = remember { mutableStateOf(1) }

    // Mapa koja povezuje sportove sa ikonama
    val sportIcons = mapOf(
        "Football" to R.drawable.ic_football_marker,
        "Basketball" to R.drawable.ic_basketball_marker,
        "Tennis" to R.drawable.ic_tennis_marker,
        "Other" to R.drawable.add_event_icon
    )

    // MutableState za držanje trenutnog markera
    val markerPosition = remember { mutableStateOf<LatLng?>(null) }

    val mapUiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }
    val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(44.7866, 20.4489), 12f) // Default to Belgrade
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp, horizontal = 20.dp)
    ) {
        item { headingText(textValue = stringResource(id = R.string.add_new_sport_event_heading)) }
        item { Spacer(modifier = Modifier.height(20.dp)) }

        // Dugmići za izbor sporta
        item {
            Text(text = "Izaberite sport")
            SportButtonSelector(selectedSport, sportIcons)
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        item { inputTextIndicator(textValue = "Opis") }
        item { Spacer(modifier = Modifier.height(5.dp)) }
        item { customRichTextInput(inputValue = inputDescription, inputText = "Unesite opis", isError = isDescriptionError, errorText = descriptionError) }
        item { Spacer(modifier = Modifier.height(20.dp)) }

        // Custom intensity selector
        item {
            Text(text = "Izaberite intenzitet")
            IntensitySelector(selectedIntensity)
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        // Dodavanje mape gde korisnik može da klikne i odabere lokaciju
        item {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings,
                onMapClick = { latLng ->
                    location.value = latLng // Postavljamo lokaciju
                    markerPosition.value = latLng // Ažuriramo poziciju markera
                    Log.d("MapClick", "Selected location: $latLng")
                }
            ) {
                markerPosition.value?.let { position ->
                    Marker(
                        state = MarkerState(position = position),
                        icon = bitmapDescriptorFromVector(
                            LocalContext.current,
                            sportIcons[selectedSport.value] ?: R.drawable.add_event_icon
                        ), // Postavlja ikonicu za sport kao marker
                        title = "Izabrana lokacija"
                    )
                }
            }
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }

        // DatePicker za izbor datuma
        item {
            DatePicker(selectedDate)
        }

        // TimePicker za izbor vremena
        item {
            TimePicker(selectedTime)
        }

        // Polje za unos broja potrebnih ljudi
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Unesite broj potrebnih ljudi")
                CustomNumberInputField(value = selectedPeople.value) { newValue ->
                    selectedPeople.value = newValue
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        item {
            loginRegisterCustomButton(
                buttonText = "Dodaj događaj",
                isEnabled = buttonIsEnabled,
                isLoading = buttonIsLoading,
                mainColor = mainColor
            ) {
                showedAlert.value = false
                buttonIsLoading.value = true
                location.value?.let { nonNullLocation ->
                    sportViewModel?.saveSportData(
                        description = inputDescription.value,
                        intensity = selectedIntensity.value,
                        mainImage = Uri.EMPTY,
                        galleryImages = listOf(),
                        location = nonNullLocation,
                        date = Date(),
                        numberOfPeople = selectedPeople.value,
                        sportType = selectedSport.value // Sport tip
                    )
                } ?: run {
                    Log.d("Add Event", "Location is null, cannot save the event")
                }
            }
        }

        item { Spacer(modifier = Modifier.height(5.dp)) }
    }

    sportFlow?.value.let {
        when (it) {
            is Resource.Failure -> {
                Log.d("Stanje flowa", it.toString())
                buttonIsLoading.value = false
            }
            is Resource.Success -> {
                Log.d("AddEvent", "Successfully saved: ${it.result}")
                buttonIsLoading.value = false
                onEventAdded() // Call callback when the event is added
            }
            is Resource.Loading -> {
            }
            null -> {}
        }
    }
}

@Composable
fun SportButtonSelector(selectedSport: MutableState<String>, sportIcons: Map<String, Int>) {
    val sports = listOf("Football", "Basketball", "Tennis", "Other")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        sports.forEach { sport ->
            val isSelected = selectedSport.value == sport
            val icon = sportIcons[sport] ?: R.drawable.add_event_icon
            val iconSize = if (sport == "Other") 32.dp else 48.dp // Smanjena veličina za "Other"

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .background(if (isSelected) Color.Green else Color.LightGray)
                    .clickable {
                        selectedSport.value = sport
                    }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = sport,
                        modifier = Modifier.size(iconSize) // Ikona prilagođene veličine
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = sport,
                        color = if (isSelected) Color.White else Color.Black,
                        maxLines = 1 // Ograničenje teksta na jedan red
                    )
                }
            }
        }
    }
}

@Composable
fun IntensitySelector(selectedIntensity: MutableState<Int>) {
    val intensityLevels = listOf("Low", "Medium", "High")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        intensityLevels.forEachIndexed { index, label ->
            val isSelected = selectedIntensity.value == index + 1
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .background(if (isSelected) Color.Green else Color.LightGray)
                    .clickable {
                        selectedIntensity.value = index + 1
                    }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color.White else Color.Black,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

