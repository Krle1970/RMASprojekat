package com.example.projekatv2.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShareLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.projekatv2.model.Sport
import com.example.projekatv2.ui.theme.greyTextColor
import com.example.projekatv2.ui.theme.lightGreyColor
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CustomTable(
    events: List<Sport>?,
    navController: NavController
) {
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
            .horizontalScroll(horizontalScrollState)
    ) {
        Column {
            CustomTableHeader()

            Box(
                modifier = Modifier
                    .width(500.dp)
                    .height(2.dp)
                    .background(greyTextColor)
            )

            events?.forEachIndexed { index, event ->
                CustomTableRow(type = index % 2, event, openEventScreen = {
                    val eventJson = Gson().toJson(event)
                    val encodedEventJson = URLEncoder.encode(eventJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("eventScreen/$encodedEventJson")
                },
                    openEventLocation = {
                        val isCameraSet = true
                        val latitude = event.location.latitude
                        val longitude = event.location.longitude

                        val eventsJson = Gson().toJson(events)
                        val encodedEventsJson = URLEncoder.encode(eventsJson, StandardCharsets.UTF_8.toString())
                        navController.navigate("indexScreenWithParams/$isCameraSet/$latitude/$longitude/$encodedEventsJson")
                    }
                )
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun CustomTableHeader() {
    val boxModifier = Modifier.padding(12.dp)
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.width(60.dp))

        Box(modifier = boxModifier.width(200.dp)) {
            Text(
                text = "Opis",
                style = TextStyle(
                    fontSize = 20.sp
                )
            )
        }

        Box(modifier = boxModifier.width(120.dp)) {
            Text(
                text = "Intenzitet",
                style = TextStyle(
                    fontSize = 20.sp
                )
            )
        }

        Box(modifier = boxModifier.width(50.dp)) {}
    }
}

@Composable
fun CustomTableRow(
    type: Int,
    event: Sport,
    openEventScreen: () -> Unit,
    openEventLocation: () -> Unit
) {
    val boxModifier = Modifier.padding(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (type == 0) Color.Transparent else lightGreyColor
            )
            .clickable { openEventScreen() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(60.dp)) {
            AsyncImage(
                model = event.mainImage,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(60.dp)
                    .clip(RoundedCornerShape(5.dp))
            )
        }

        Box(modifier = boxModifier.width(200.dp)) {
            Text(
                text = if (event.description.length > 20) event.description.substring(0, 20) + "..." else event.description,
                style = TextStyle(
                    fontSize = 16.sp
                )
            )
        }

        Box(modifier = boxModifier.width(120.dp)) {
            CustomIntensityIndicator(intensity = event.intensity)
        }

        Box(modifier = boxModifier.width(50.dp)) {
            IconButton(
                onClick = openEventLocation,
            ) {
                Icon(
                    imageVector = Icons.Outlined.ShareLocation,
                    contentDescription = ""
                )
            }
        }
    }
}
