package com.example.projekatv2.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projekatv2.Navigation.Routes
import com.example.projekatv2.data.Resource
import com.example.projekatv2.model.Sport
import com.example.projekatv2.model.SportRate
import com.example.projekatv2.screens.components.*
import com.example.projekatv2.screens.dialogs.RateSportDialog
import com.example.projekatv2.viewmodels.AuthViewModel
import com.example.projekatv2.viewmodels.SportViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.math.RoundingMode
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SportScreen(
    navController: NavController,
    sportViewModel: SportViewModel,
    sport: Sport,
    viewModel: AuthViewModel,
    sports: MutableList<Sport>?
) {
    val ratesResources = sportViewModel.rates.collectAsState()
    val newRateResource = sportViewModel.newRate.collectAsState()

    val rates = remember { mutableListOf<SportRate>() }
    val averageRate = remember { mutableStateOf(0.0) }
    val showRateDialog = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    val myRating = remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        SportMainImage(sport.mainImage)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            item {
                CustomBackButton {
                    if (sports == null) {
                        navController.popBackStack()
                    } else {
                        val isCameraSet = true
                        val latitude = sport.location.latitude
                        val longitude = sport.location.longitude

                        val sportsJson = Gson().toJson(sports)
                        val encodedSportsJson = URLEncoder.encode(sportsJson, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.indexScreenWithParams + "/$isCameraSet/$latitude/$longitude/$encodedSportsJson")
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(220.dp)) }
            item { CustomIntensityIndicator(intensity = sport.intensity) }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item { headingText(textValue = "Sportski događaj") }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item { CustomSportLocation(location = LatLng(sport.location.latitude, sport.location.longitude)) }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item { CustomSportRate(average = averageRate.value) }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item { greyTextBigger(textValue = sport.description.replace('+', ' ')) }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item { Text(text = "Galerija događaja", style = TextStyle(fontSize = 20.sp)) }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item { CustomSportGallery(images = sport.galleryImages) }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 15.dp, vertical = 20.dp)
        ) {
            CustomRateButton(
                enabled = sport.organizerId != viewModel.currentUser?.uid,
                onClick = {
                    val rateExist = rates.firstOrNull {
                        it.sportId == sport.id && it.userId == viewModel.currentUser!!.uid
                    }
                    if (rateExist != null)
                        myRating.value = rateExist.rate
                    showRateDialog.value = true
                }
            )
        }

        if (showRateDialog.value) {
            RateSportDialog(
                showRateDialog = showRateDialog,
                rate = myRating,
                rateSport = {
                    val rateExist = rates.firstOrNull {
                        it.sportId == sport.id && it.userId == viewModel.currentUser!!.uid
                    }
                    if (rateExist != null) {
                        isLoading.value = true
                        sportViewModel.updateRate(
                            rateId = rateExist.id,
                            rate = myRating.value
                        )
                    } else {
                        isLoading.value = true
                        sportViewModel.addRate(
                            sportId = sport.id,
                            rate = myRating.value,
                            sport = sport
                        )
                    }
                },
                isLoading = isLoading
            )
        }
    }

    ratesResources.value.let {
        when (it) {
            is Resource.Success -> {
                rates.addAll(it.result)
                var sum = 0.0
                for (rate in it.result) {
                    sum += rate.rate.toDouble()
                }
                if (sum != 0.0) {
                    val rawPositive = sum / it.result.count()
                    val rounded = rawPositive.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                    averageRate.value = rounded
                }else{}
            }
            is Resource.Loading -> { /* Loading state */ }
            is Resource.Failure -> {
                Log.e("Podaci", it.toString())
            }
        }
    }
    newRateResource.value.let {
        when (it) {
            is Resource.Success -> {
                isLoading.value = false
                val rateExist = rates.firstOrNull { rate -> rate.id == it.result }
                if (rateExist != null) {
                    rateExist.rate = myRating.value
                }
            }
            is Resource.Loading -> { /* Handle loading if needed */ }
            is Resource.Failure -> {
                val context = LocalContext.current
                Toast.makeText(context, "Došlo je do greške prilikom ocenjivanja događaja", Toast.LENGTH_LONG).show()
                isLoading.value = false
            }
            null -> {
                isLoading.value = false
            }
        }
    }
}
