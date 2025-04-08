package com.example.projekatv2.screens.sports

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projekatv2.data.Resource
import com.example.projekatv2.model.Sport
import com.example.projekatv2.model.SportUser
import com.example.projekatv2.screens.components.CustomFilterButton
import com.example.projekatv2.screens.components.CustomIntensitySelector
import com.example.projekatv2.screens.components.CustomResetFilters
import com.example.projekatv2.screens.components.DropdownWithCheckboxes
import com.example.projekatv2.screens.components.RangeSliderExample
import com.example.projekatv2.screens.components.calculateDistance
import com.example.projekatv2.ui.theme.buttonDisabledColor
import com.example.projekatv2.ui.theme.lightGreyColor
import com.example.projekatv2.ui.theme.lightMainColor2
import com.example.projekatv2.ui.theme.mainColor
import com.example.projekatv2.ui.theme.redTextColor
import com.example.projekatv2.viewmodels.AuthViewModel
import com.example.projekatv2.viewmodels.SportViewModel

import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.math.RoundingMode
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SportFiltersBottomSheet(
    sportViewModel: SportViewModel,
    sports: MutableList<Sport>,
    sheetState: ModalBottomSheetState,
    isFiltered: MutableState<Boolean>,
    isFilteredIndicator: MutableState<Boolean>,
    filteredSports: MutableList<Sport>, // Lista filtriranih sportova
    sportMarkers: MutableList<Sport>,   // Originalna lista markera
    userLocation: LatLng?               // Lokacija korisnika
) {
    val context = LocalContext.current

    // SharedPreferences za čuvanje filtera
    val sharedPreferences = context.getSharedPreferences("filters", Context.MODE_PRIVATE)
    val intensity = sharedPreferences.getString("intensity", null)
    val range = sharedPreferences.getFloat("range", 1000f)

    val selectedIntensity = remember { mutableStateOf(0) } // Intenzitet od 1 do 3
    val rangeValues = remember { mutableFloatStateOf(1000f) }

    // Dodaj stanje za selektovani sport
    val selectedSport = remember { mutableStateOf("") }

    val filtersSet = remember {
        mutableStateOf(false)
    }

    // Postavljanje vrednosti filtera
    if (!filtersSet.value) {
        rangeValues.floatValue = range
        filtersSet.value = true
    }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 16.dp)
    ) {
        // Dugmad za biranje sportova
        Text(
            text = "Izaberite sport",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Red dugmadi za sportove
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SportButton("Football", selectedSport)
            SportButton("Basketball", selectedSport)
            SportButton("Tennis", selectedSport)
            SportButton("Other", selectedSport)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dugmad za biranje intenziteta
        Text(
            text = "Izaberite intenzitet",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Red dugmadi za intenzitet
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IntensityButton("Low", selectedIntensity, 1)
            IntensityButton("Medium", selectedIntensity, 2)
            IntensityButton("High", selectedIntensity, 3)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dugme za primenu filtera
        CustomFilterButton {
            val filteredEvents = sportMarkers.toMutableList() // koristi originalne markere za filtriranje

            // Filtriraj događaje po sportu
            if (selectedSport.value.isNotEmpty()) {
                filteredEvents.retainAll { sport ->
                    sport.sportType == selectedSport.value
                }
            }

            // Filtriraj događaje po intenzitetu
            if (selectedIntensity.value != 0) {
                filteredEvents.retainAll { sport ->
                    sport.intensity == selectedIntensity.value
                }
            }

            // Filtriraj događaje po udaljenosti
            if (rangeValues.floatValue != 1000f && userLocation != null) {
                filteredEvents.retainAll { sport ->
                    calculateDistance(
                        userLocation.latitude,
                        userLocation.longitude,
                        sport.location.latitude,
                        sport.location.longitude
                    ) <= rangeValues.floatValue
                }
            }

            // Ažuriraj listu filtriranih sportova i mape
            filteredSports.clear()
            filteredSports.addAll(filteredEvents)

            isFiltered.value = true
            isFilteredIndicator.value = true

            // Sakrij filter bottom sheet
            coroutineScope.launch {
                sheetState.hide()
            }
        }

        // Dugme za resetovanje filtera
        CustomResetFilters {
            sportMarkers.clear()
            sportMarkers.addAll(sports)

            selectedSport.value = ""
            selectedIntensity.value = 0

            rangeValues.floatValue = 1000f

            isFiltered.value = false
            isFilteredIndicator.value = false

            coroutineScope.launch {
                sheetState.hide()
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// Funkcija za biranje sportova
@Composable
fun SportButton(sportName: String, selectedSport: MutableState<String>) {
    Button(
        onClick = {
            selectedSport.value = sportName
        },
        modifier = Modifier
            .width(120.dp)
            .height(50.dp)
            .background(
                if (selectedSport.value == sportName) Color.Green else Color.White,
                RoundedCornerShape(10.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedSport.value == sportName) Color.Green else Color.White,
            contentColor = if (selectedSport.value == sportName) Color.White else Color.Black
        )
    ) {
        Text(
            text = sportName,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            maxLines = 1,
            modifier = Modifier.padding(4.dp)
        )
    }
}

// Funkcija za biranje intenziteta
@Composable
fun IntensityButton(intensityName: String, selectedIntensity: MutableState<Int>, intensityValue: Int) {
    Button(
        onClick = {
            selectedIntensity.value = intensityValue
        },
        modifier = Modifier
            .width(120.dp)
            .height(50.dp)
            .background(
                if (selectedIntensity.value == intensityValue) Color.Green else Color.White,
                RoundedCornerShape(10.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedIntensity.value == intensityValue) Color.Green else Color.White,
            contentColor = if (selectedIntensity.value == intensityValue) Color.White else Color.Black
        )
    ) {
        Text(
            text = intensityName,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            maxLines = 1,
            modifier = Modifier.padding(4.dp)
        )
    }
}
