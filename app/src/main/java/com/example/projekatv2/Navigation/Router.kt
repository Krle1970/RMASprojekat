package com.example.projekatv2.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projekatv2.data.Resource
import com.example.projekatv2.model.Sport
import com.example.projekatv2.screens.*
import com.example.projekatv2.viewmodels.AuthViewModel
import com.example.projekatv2.viewmodels.SportViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Router(
    authViewModel: AuthViewModel,
    sportViewModel: SportViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.loginScreen) {

        composable(Routes.loginScreen) {
            LoginScreen(
                viewModel = authViewModel,
                navController = navController
            )
        }
        composable(Routes.registerScreen) {
            RegisterScreen(
                viewModel = authViewModel,
                navController = navController
            )
        }
        composable(Routes.indexScreen) {
            val sportsResource = sportViewModel.sports.collectAsState()
            val sportMarkers = remember { mutableListOf<Sport>() }

            sportsResource.value.let {
                when (it) {
                    is Resource.Success -> {
                        sportMarkers.clear()
                        sportMarkers.addAll(it.result)
                    }
                    is Resource.Loading -> {
                        // Handle loading state
                    }
                    is Resource.Failure -> {
                        // Handle error
                    }
                }
            }

            MapIndexScreen(
                viewModel = authViewModel,
                navController = navController,
                sportViewModel = sportViewModel,
                sportMarkers = sportMarkers
            )
        }
        composable(
            route = Routes.indexScreenWithParams + "/{isCameraSet}/{latitude}/{longitude}",
            arguments = listOf(
                navArgument("isCameraSet") { type = NavType.BoolType },
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val isCameraSet = backStackEntry.arguments?.getBoolean("isCameraSet") ?: false
            val latitude = backStackEntry.arguments?.getFloat("latitude") ?: 0f
            val longitude = backStackEntry.arguments?.getFloat("longitude") ?: 0f

            // Extracting the sports data from the Resource wrapper
            val sportsList = when (val result = sportViewModel.sports.value) {
                is Resource.Success -> result.result // Extract the list of sports from the Resource
                else -> emptyList() // Handle other cases such as Loading or Failure by providing an empty list
            }

            MapIndexScreen(
                viewModel = authViewModel,
                navController = navController,
                sportViewModel = sportViewModel,
                isCameraSet = remember { mutableStateOf(isCameraSet) },
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(latitude.toDouble(), longitude.toDouble()), 17f)
                },
                sportMarkers = sportsList.toMutableList() // Convert to MutableList
            )
        }

        composable(
            route = Routes.sportScreen + "/{sportId}",
            arguments = listOf(
                navArgument("sportId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val sportId = backStackEntry.arguments?.getString("sportId") ?: ""

            // Kolektovanje sport podataka kao State sa početnim stanjem
            val sportResource = sportViewModel.loadSportById(sportId).collectAsState(initial = Resource.Loading)

            // Provera stanja resursa i odabir odgovarajućeg sport objekta
            val sport = when (val resource = sportResource.value) {
                is Resource.Success -> resource.result
                else -> Sport() // Ako nije uspešno, vrati prazan sport
            }

            // Kolektovanje liste sportova i proveravanje da li je uspešno učitana
            val sportsList = when (val sportsResource = sportViewModel.sports.value) {
                is Resource.Success -> sportsResource.result.toMutableList()
                else -> mutableListOf<Sport>() // Ako nema podataka, vrati praznu listu
            }

            SportScreen(
                navController = navController,
                sportViewModel = sportViewModel,
                sport = sport,
                viewModel = authViewModel,
                sports = sportsList
            )
        }

        composable(
            route = Routes.userProfileScreen + "/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""


            val allUsersResource = authViewModel.allUsers.collectAsState()

            // Pronađi korisnika prema userId
            val userData = allUsersResource.value?.let { resource ->
                when (resource) {
                    is Resource.Success -> resource.result.find { it.id == userId }
                    else -> null
                }
            }

            UserProfileScreen(
                navController = navController,
                viewModel = authViewModel,
                sportViewModel = sportViewModel,
                userData = userData,
                isMy = authViewModel.currentUser?.uid == userId
            )
        }

        composable(Routes.settingsScreen) {
            SettingScreen(navController = navController)
        }
        composable(Routes.rankingScreen) {
            RankingScreen(
                viewModel = authViewModel,
                navController = navController
            )
        }
        composable(Routes.scheduleScreen) {
            // Implement Schedule screen if required
        }
        composable(
            route = Routes.indexScreenWithParams + "/{sports}",
            arguments = listOf(
                navArgument("sports") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val sportsJson = backStackEntry.arguments?.getString("sports")
            val sports = Gson().fromJson(sportsJson, Array<Sport>::class.java).toList()
            MapIndexScreen(
                viewModel = authViewModel,
                navController = navController,
                sportViewModel = sportViewModel,
                sportMarkers = sports.toMutableList(),
                isFilteredParam = true
            )
        }
        composable(
            route = Routes.eventDetailsScreen + "/{eventId}",
            arguments = listOf(
                navArgument("eventId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            // EventDetailsScreen logic
        }
    }
}
