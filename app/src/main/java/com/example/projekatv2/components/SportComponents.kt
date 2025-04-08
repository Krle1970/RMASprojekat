package com.example.projekatv2.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.projekatv2.model.Sport
import com.example.projekatv2.ui.theme.buttonDisabledColor
import com.example.projekatv2.ui.theme.greyTextColor
import com.example.projekatv2.ui.theme.mainColor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun SportMainImage(
    imageUrl: String
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun CustomSportLocation(
    location: LatLng
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "",
            tint = mainColor
        )
        Spacer(modifier = Modifier.width(5.dp))
        greyText(textValue = "${location.latitude}, ${location.longitude}")
    }
}

@Composable
fun CustomSportRate(
    average: Number
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = "",
            tint = Color.Yellow
        )
        Spacer(modifier = Modifier.width(5.dp))
        inputTextIndicator(textValue = "$average / 5")
    }
}

@Composable
fun CustomIntensityIndicator(
    intensity: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (intensity == 0) Color.Green
                    else if (intensity == 1) Color.Yellow
                    else Color.Red, shape = RoundedCornerShape(5.dp)
                )
                .border(
                    1.dp,
                    Color.Transparent,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 10.dp, vertical = 5.dp)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.People,
                    contentDescription = "",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(5.dp))
                inputTextIndicator(textValue =
                if (intensity == 0) "Low Intensity"
                else if (intensity == 1) "Medium Intensity"
                else "High Intensity"
                )
            }
        }
    }
}

@Composable
fun CustomSportGallery(
    images: List<String>
) {
    LazyRow {
        for (image in images) {
            item {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    AsyncImage(
                        model = image,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportFilterDialog(
    filterOptions: MutableState<FilterOptions>,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    val (selectedIntensity, setSelectedIntensity) = remember { mutableStateOf(filterOptions.value.intensity) }
    val (selectedType, setSelectedType) = remember { mutableStateOf(filterOptions.value.type) }
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val intensityLevels = listOf("Low", "Medium", "High")
    val sportTypes = listOf("Football", "Basketball")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Options") },
        text = {
            Column {
                Text("Select Intensity:")
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { setExpanded(!expanded) }
                ) {
                    TextField(
                        value = intensityLevels[selectedIntensity],
                        onValueChange = { },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            setExpanded(!expanded)
                        }),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { setExpanded(false) }
                    ) {
                        intensityLevels.forEachIndexed { index, label ->
                            DropdownMenuItem(
                                onClick = {
                                    setSelectedIntensity(index)
                                    setExpanded(false)
                                },
                                text = { Text(label) }
                            )
                        }
                    }
                }

                Text("Select Sport Type:")
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { setExpanded(!expanded) }
                ) {
                    TextField(
                        value = selectedType.ifEmpty { "Select a sport" },
                        onValueChange = { },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            setExpanded(!expanded)
                        }),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { setExpanded(false) }
                    ) {
                        sportTypes.forEach { sport ->
                            DropdownMenuItem(
                                onClick = {
                                    setSelectedType(sport)
                                    setExpanded(false)
                                },
                                text = { Text(sport) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                filterOptions.value = FilterOptions(selectedIntensity, selectedType)
                onApply()
            }) {
                Text("Apply")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

data class FilterOptions(
    var intensity: Int = 0,
    var type: String = ""
)

fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadius = 6371000.0

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c
}
@Composable
fun SportMapNavigationBar(
    searchValue: MutableState<String>,
    profileImage: String,
    onImageClick: () -> Unit,
    sportEvents: List<Sport>,  // Make sure the type matches your data model
    navController: NavController,
    cameraPositionState: CameraPositionState
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (profileImage.isNotEmpty()) {
                Image(
                    painter = rememberImagePainter(profileImage),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { onImageClick() }
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = searchValue.value,
                onValueChange = { searchValue.value = it },
                placeholder = { Text("Search sports...") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        // Optionally, add a row of icons or shortcuts for filtering or creating events
    }
}


@Composable
fun SportMapFooter(
    openAddNewEvent: () -> Unit,
    active: Int,
    onHomeClick: () -> Unit,
    onTableClick: () -> Unit,
    onRankingClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(56.dp)
            .background(Color.Gray, RoundedCornerShape(30.dp)) // Adjust color/theme as necessary
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onHomeClick) {
                Icon(Icons.Default.Home, contentDescription = "Home")
            }
            IconButton(onClick = openAddNewEvent) {
                Icon(Icons.Default.Add, contentDescription = "Add New Event")
            }
            IconButton(onClick = onTableClick) {
                Icon(Icons.Default.List, contentDescription = "View List")
            }
            IconButton(onClick = onRankingClick) {
                Icon(Icons.Default.Star, contentDescription = "Rankings")
            }
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    }
}


@Composable
fun CustomRateButton(
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(mainColor, RoundedCornerShape(30.dp)),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = mainColor,
            contentColor = Color.Black,
            disabledContainerColor = buttonDisabledColor,
            disabledContentColor = Color.White
        ),

        ) {
        Text(
            "Rate Event",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun CustomBackButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .background(
                Color.White,
                RoundedCornerShape(5.dp)
            )
            .padding(0.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = ""
        )
    }
}
