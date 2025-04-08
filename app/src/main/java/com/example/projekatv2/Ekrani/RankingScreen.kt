package com.example.projekatv2.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projekatv2.Navigation.Routes
import com.example.projekatv2.data.Resource
import com.example.projekatv2.model.Sport
import com.example.projekatv2.model.SportUser
import com.example.projekatv2.screens.components.firstThreeOrganizers
import com.example.projekatv2.screens.components.OtherOrganizers
import com.example.projekatv2.screens.components.mapFooter
import com.example.projekatv2.screens.dialogs.RateSportDialog
import com.example.projekatv2.viewmodels.AuthViewModel
import com.example.projekatv2.viewmodels.SportViewModel
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun RankingScreen(
    viewModel: AuthViewModel,
    navController: NavController
) {
    viewModel.getAllUserData()
    val allUsersResource = viewModel.allUsers.collectAsState()

    val allUsers = remember { mutableListOf<SportUser>() }
    val sportEvents = remember { mutableStateListOf<Sport>() }

    // Stanje za otvaranje dijaloga i ocenjivanje
    val showRateDialog = remember { mutableStateOf(false) }
    val selectedUserForRating = remember { mutableStateOf<SportUser?>(null) }
    val userRating = remember { mutableStateOf(0) }
    val isLoading = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(vertical = 25.dp, horizontal = 16.dp)
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = "RANG LISTA ORGANIZATORA",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        LazyColumn(
            modifier = Modifier.padding(top = 70.dp)
        ) {
            item {
                firstThreeOrganizers(
                    users = allUsers.take(3),
                    onUserClick = { user ->
                        selectedUserForRating.value = user
                        showRateDialog.value = true
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            if (allUsers.count() > 3) {
                item {
                    OtherOrganizers(
                        users = allUsers.drop(3),
                        onUserClick = { user ->
                            selectedUserForRating.value = user
                            showRateDialog.value = true
                        }
                    )
                }
            }
        }

        // Footer sa navigacionim opcijama
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            mapFooter(
                openAddNewEvent = {},
                active = 2,
                onHomeClick = {
                    navController.navigate(Routes.indexScreen)
                },
                onTableClick = {
                    val eventsJson = Gson().toJson(sportEvents)
                    val encodedEventsJson = URLEncoder.encode(eventsJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("eventsTableScreen/$encodedEventsJson")
                },
                onRankingClick = {
                    navController.navigate(Routes.rankingScreen)
                },
                onSettingsClick = {
                    navController.navigate(Routes.settingsScreen)
                }
            )
        }
    }

    // Upravljanje resursima korisnika
    allUsersResource.value.let {
        when (it) {
            is Resource.Failure -> {}
            is Resource.Success -> {
                allUsers.clear()
                allUsers.addAll(it.result.sortedByDescending { user -> user.rating })
            }
            Resource.Loading -> {}
            null -> {}
        }
    }

    // Prikaz dijaloga za ocenjivanje
    if (showRateDialog.value && selectedUserForRating.value != null) {
        RateSportDialog(
            showRateDialog = showRateDialog,
            rate = userRating,
            rateSport = {
                isLoading.value = true
                // Ovde možete dodati logiku za slanje ocene korisnika
                viewModel.rateUser(selectedUserForRating.value!!.id, userRating.value)
                isLoading.value = false
                showRateDialog.value = false
            },
            isLoading = isLoading
        )
    }
}
