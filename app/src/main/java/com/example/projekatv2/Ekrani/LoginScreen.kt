package com.example.projekatv2.screens

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projekatv2.Navigation.Routes
import com.example.projekatv2.R
import com.example.projekatv2.data.Resource
import com.example.projekatv2.exceptions.AuthExceptionsMessages
import com.example.projekatv2.screens.components.customAuthError
import com.example.projekatv2.screens.components.customClickableText
import com.example.projekatv2.screens.components.customPasswordInput
import com.example.projekatv2.screens.components.customTextInput
import com.example.projekatv2.screens.components.greyText
import com.example.projekatv2.screens.components.headingText
import com.example.projekatv2.screens.components.inputTextIndicator
import com.example.projekatv2.screens.components.loginImage
import com.example.projekatv2.screens.components.loginRegisterCustomButton
import com.example.projekatv2.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel?,
    navController: NavController
) {
    val loginFlow = viewModel?.loginFlow?.collectAsState()
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val isEmailError = remember { mutableStateOf(false) }
    val emailErrorText = remember { mutableStateOf("") }

    val isPasswordError = remember { mutableStateOf(false) }
    val passwordErrorText = remember { mutableStateOf("") }

    val isError = remember { mutableStateOf(false) }
    val errorText = remember { mutableStateOf("") }

    val buttonIsEnabled = remember { mutableStateOf(true) }
    val isLoading = remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        loginImage()  // Prikazuje sportsku sliku za login ekran
        headingText(textValue = "Dobrodošli!")  // Dobrodošlica za sportski projekat
        Spacer(modifier = Modifier.height(5.dp))
        greyText(textValue = "Prijavite se da biste nastavili")  // Poruka za prijavu
        Spacer(modifier = Modifier.height(20.dp))
        if (isError.value) customAuthError(errorText = errorText.value)
        Spacer(modifier = Modifier.height(20.dp))
        inputTextIndicator(textValue = "Email")
        Spacer(modifier = Modifier.height(10.dp))
        customTextInput(
            isEmail = true,
            inputValue = email,
            inputText = "primer@domen.com",
            leadingIcon = Icons.Outlined.Email,
            isError = isEmailError,
            errorText = emailErrorText
        )
        Spacer(modifier = Modifier.height(10.dp))
        inputTextIndicator(textValue = "Lozinka")
        Spacer(modifier = Modifier.height(10.dp))
        customPasswordInput(
            inputValue = password,
            inputText = "Vaša lozinka",
            leadingIcon = Icons.Outlined.Lock,
            isError = isPasswordError,
            errorText = passwordErrorText
        )
        Spacer(modifier = Modifier.height(50.dp))
        loginRegisterCustomButton(
            buttonText = "Prijava",
            isEnabled = buttonIsEnabled,
            isLoading = isLoading,
            mainColor = MaterialTheme.colorScheme.primary,
            onClick = {
                Log.d("LoginScreen", "Kliknuto na dugme za prijavu")
                isEmailError.value = false
                isPasswordError.value = false
                isError.value = false
                isLoading.value = true

                Log.d("LoginScreen", "Email: ${email.value}, Password: ${password.value}")

                viewModel?.login(email.value, password.value)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        customClickableText(firstText = "Još uvek nemate nalog?", secondText = "Registruj se", onClick = {
            navController.navigate(Routes.registerScreen)
        })
    }

    loginFlow?.value.let {
        when (it) {
            is Resource.Failure -> {
                isLoading.value = false
                Log.d("Error", it.exception.message.toString())
                when (it.exception.message.toString()) {
                    AuthExceptionsMessages.emptyFields -> {
                        isEmailError.value = true
                        isPasswordError.value = true
                    }
                    AuthExceptionsMessages.badlyEmailFormat -> {
                        isEmailError.value = true
                        emailErrorText.value = "Neispravan format email-a."
                    }
                    AuthExceptionsMessages.invalidCredential -> {
                        isError.value = true
                        errorText.value = "Pogrešni podaci za prijavu."
                    }
                    else -> {}
                }
            }
            is Resource.Loading -> {
                // Do nothing, as isLoading is already set in onClick
            }
            is Resource.Success -> {
                isLoading.value = false
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.indexScreen) {
                        popUpTo(Routes.indexScreen) {
                            inclusive = true
                        }
                    }
                }
            }
            null -> {}
        }
    }
}
