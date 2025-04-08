package com.example.projekatv2.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import com.example.projekatv2.R
import com.example.projekatv2.data.Resource
import com.example.projekatv2.location.LocationService
import com.example.projekatv2.model.Sport
import com.example.projekatv2.model.SportUser
import com.example.projekatv2.screens.components.FilterOptions
import com.example.projekatv2.screens.components.SportFilterDialog
import com.example.projekatv2.screens.components.SportMapFooter
import com.example.projekatv2.screens.components.SportMapNavigationBar
import com.example.projekatv2.screens.components.bitmapDescriptorFromUrlWithRoundedCorners
import com.example.projekatv2.screens.components.bitmapDescriptorFromVector
import com.example.projekatv2.screens.dialogs.ShowSportEventInfoDialog
import com.example.projekatv2.screens.sports.SportFiltersBottomSheet
import com.example.projekatv2.ui.theme.mainColor
import com.example.projekatv2.viewmodels.AuthViewModel
import com.example.projekatv2.viewmodels.SportViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapIndexScreen(
    viewModel: AuthViewModel?,
    navController: NavController?,
    sportViewModel: SportViewModel?,
    isCameraSet: MutableState<Boolean> = remember { mutableStateOf(false) },
    cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(43.321445, 21.896104), 17f)
    },
    sportMarkers: MutableList<Sport>, // Bez potrebe za kopijom
    isFilteredParam: Boolean = false
) {
    val sportIcons = mapOf(
        "Football" to R.drawable.ic_football_marker,
        "Basketball" to R.drawable.ic_basketball_marker,
        "Tennis" to R.drawable.ic_tennis_marker,
        "Other" to R.drawable.add_event_icon
    )

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("filters", Context.MODE_PRIVATE)
    val options = sharedPreferences.getString("options", null)
    val intensity = sharedPreferences.getString("intensity", null)
    val range = sharedPreferences.getFloat("range", 1000f)

    val isFiltered = remember { mutableStateOf(false) }
    val isFilteredIndicator = remember { mutableStateOf(false) }

    val selectedEvent = remember { mutableStateOf<Sport?>(null) }
    val showInfoDialog = remember { mutableStateOf(false) }

    if (isFilteredParam && (options != null || intensity != null || range != 1000f)) {
        isFilteredIndicator.value = true
    }

    val sportsResource = sportViewModel?.sports?.collectAsState()
    val allSports = remember { mutableListOf<Sport>() }
    sportsResource?.value.let {
        when (it) {
            is Resource.Success -> {
                allSports.clear()
                allSports.addAll(it.result)
                // Ažuriranje markera na mapi
                sportMarkers.clear()
                sportMarkers.addAll(it.result) // Postavljanje novododatih sportskih događaja
            }
            is Resource.Loading -> {}
            is Resource.Failure -> {
                Log.e("Podaci", it.toString())
            }
            null -> {}
        }
    }

    viewModel?.getUserData()

    val userDataResource = viewModel?.currentUserFlow?.collectAsState()

    val filteredSports = remember { mutableListOf<Sport>() } // Lista za filtrirane sportove

    val searchValue = remember { mutableStateOf("") }
    val userData = remember { mutableStateOf<SportUser?>(null) }
    val profileImage = remember { mutableStateOf("") }
    val myLocation = remember { mutableStateOf<LatLng?>(null) }

    val showFilterDialog = remember { mutableStateOf(false) }
    val isAddNewBottomSheet = remember { mutableStateOf(true) }

    val receiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == LocationService.ACTION_LOCATION_UPDATE) {
                    val latitude = intent.getDoubleExtra(LocationService.EXTRA_LOCATION_LATITUDE, 0.0)
                    val longitude = intent.getDoubleExtra(LocationService.EXTRA_LOCATION_LONGITUDE, 0.0)
                    myLocation.value = LatLng(latitude, longitude)
                    Log.d("Nova lokacija", myLocation.toString())
                }
            }
        }
    }

    DisposableEffect(context) {
        LocalBroadcastManager.getInstance(context)
            .registerReceiver(receiver, IntentFilter(LocationService.ACTION_LOCATION_UPDATE))
        onDispose {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
        }
    }

    val uiSettings = remember { mutableStateOf(MapUiSettings()) }
    val properties = remember { mutableStateOf(MapProperties(mapType = MapType.TERRAIN)) }
    val markers = remember { mutableStateListOf<LatLng>() }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    LaunchedEffect(myLocation.value) {
        myLocation.value?.let {
            if (!isCameraSet.value) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 17f)
                isCameraSet.value = true
            }
            markers.clear()
            markers.add(it)
        }
    }

    val scope = rememberCoroutineScope()

    // Prikazivanje dijaloga kada je marker kliknut
    if (showInfoDialog.value && selectedEvent.value != null) {
        ShowSportEventInfoDialog(showInfoDialog = showInfoDialog, sportEvent = selectedEvent.value!!)
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            if (isAddNewBottomSheet.value) {
                // Bottom sheet for adding a new sport event
                AddNewSportEventBottomSheet(
                    sportViewModel = sportViewModel!!, // Ensure sportViewModel is passed correctly
                    location = myLocation, // Pass the location for the new event
                    sheetState = sheetState, // Use the modal sheet state
                    onEventAdded = {
                        // Once the event is added, refresh the sports list
                        sportViewModel.getAllSports() // This will reload all sports events and update the markers
                    }
                )
            } else {
                // Bottom sheet for filtering sports events
                SportFiltersBottomSheet(
                    sportViewModel = sportViewModel!!, // Ensure sportViewModel is passed
                    sports = allSports, // Pass all the sports events
                    sheetState = sheetState, // Modal sheet state for filtering options
                    isFiltered = isFiltered, // Pass filter state
                    isFilteredIndicator = isFilteredIndicator, // Pass filtered indicator state
                    filteredSports = filteredSports, // Pass the filtered sports list
                    sportMarkers = sportMarkers, // Pass the markers to be shown on the map
                    userLocation = myLocation.value // Pass the user's location
                )
            }
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp), // Styling for the modal sheet
        modifier = Modifier.fillMaxSize() // Modifier to make the modal full-screen
    )

    {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties.value,
                uiSettings = uiSettings.value
            ) {
                markers.forEach { marker ->
                    Marker(
                        state = rememberMarkerState(position = marker),
                        title = "Moja Lokacija",
                        icon = bitmapDescriptorFromVector(context, R.drawable.balls_sports),
                        snippet = "",
                    )
                }

                // Prikaz filtriranih sportskih događaja ako su filteri primenjeni
                val eventsToDisplay = if (isFiltered.value) filteredSports else sportMarkers

                eventsToDisplay.forEach { sportEvent ->
                    val iconRes = sportIcons[sportEvent.sportType] ?: R.drawable.add_event_icon
                    Marker(
                        state = rememberMarkerState(
                            position = LatLng(sportEvent.location.latitude, sportEvent.location.longitude)
                        ),
                        title = sportEvent.sportType,
                        icon = bitmapDescriptorFromVector(context, iconRes, 64), // Smanjena veličina ikone na 64x64 piksela
                        snippet = sportEvent.description,
                        onClick = {
                            selectedEvent.value = sportEvent
                            showInfoDialog.value = true // Prikaz dijaloga sa informacijama o događaju
                            true
                        }
                    )
                }
            }

            // Gornji bar sa opcijama
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                if (navController != null) {
                    SportMapNavigationBar(
                        searchValue = searchValue,
                        profileImage = profileImage.value.ifEmpty { "" },
                        onImageClick = {
                            val userJson = Gson().toJson(userData.value)
                            val encodedUserJson = URLEncoder.encode(userJson, StandardCharsets.UTF_8.toString())
                            navController?.navigate("userProfileScreen/$encodedUserJson")
                        },
                        sportEvents = sportMarkers, // Korišćenje originalne liste markera
                        navController = navController,
                        cameraPositionState = cameraPositionState
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .clickable {
                            isAddNewBottomSheet.value = false
                            scope.launch {
                                sheetState.show()
                            }
                        }
                        .background(
                            if (isFiltered.value || isFilteredIndicator.value)
                                mainColor
                            else
                                Color.White,
                            RoundedCornerShape(30.dp)
                        )
                        .padding(horizontal = 15.dp, vertical = 7.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FilterAlt,
                            contentDescription = "",
                            tint = if (isFiltered.value || isFilteredIndicator.value)
                                Color.White
                            else
                                mainColor
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Filteri",
                            style = TextStyle(
                                color = if (isFiltered.value || isFilteredIndicator.value)
                                    Color.White
                                else
                                    mainColor
                            )
                        )
                    }
                }
            }

            // Donji footer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                SportMapFooter(
                    openAddNewEvent = {
                        isAddNewBottomSheet.value = true
                        scope.launch {
                            sheetState.show()
                        }
                    },
                    active = 0,
                    onHomeClick = {},
                    onTableClick = {
                        val eventsJson = Gson().toJson(
                            if (!isFiltered.value)
                                sportMarkers
                            else
                                filteredSports
                        )
                        val encodedEventsJson = URLEncoder.encode(eventsJson, StandardCharsets.UTF_8.toString())
                        navController?.navigate("tableScreen/$encodedEventsJson")
                    },
                    onRankingClick = {
                        navController?.navigate("rankingScreen")
                    },
                    onSettingsClick = {
                        navController?.navigate("settingsScreen")
                    }
                )
            }
        }
    }

    userDataResource?.value.let {
        when (it) {
            is Resource.Success -> {
                userData.value = it.result
                profileImage.value = it.result.profileImage
            }
            null -> {
                userData.value = null
                profileImage.value = ""
            }
            is Resource.Failure -> {}
            Resource.Loading -> {}
        }
    }
}

