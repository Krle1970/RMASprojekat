package com.example.projekatv2.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projekatv2.Navigation.Routes
import com.example.projekatv2.R
import com.example.projekatv2.data.Resource
import com.example.projekatv2.exceptions.AuthExceptionsMessages
import com.example.projekatv2.screens.components.*
import com.example.projekatv2.viewmodels.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel?,
    navController: NavController?
) {
    val registerFlow = viewModel?.registerFlow?.collectAsState()

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val fullName = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val profileImage = remember { mutableStateOf(Uri.EMPTY) }

    val isEmailError = remember { mutableStateOf(false) }
    val emailErrorText = remember { mutableStateOf("") }

    val isPasswordError = remember { mutableStateOf(false) }
    val passwordErrorText = remember { mutableStateOf("") }

    val isImageError = remember { mutableStateOf(false) }
    val isFullNameError = remember { mutableStateOf(false) }
    val isPhoneNumberError = remember { mutableStateOf(false) }

    val isError = remember { mutableStateOf(false) }
    val errorText = remember { mutableStateOf("") }

    val buttonIsEnabled = remember { mutableStateOf(true) }
    val isLoading = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 120.dp)
        ) {
            registerImage(
                profileImage,
                isImageError
            )
            Spacer(modifier = Modifier.height(20.dp))
            headingText(textValue = "Registracija Sportiste")
            Spacer(modifier = Modifier.height(5.dp))
            greyText(textValue = "Kreirajte nalog kako biste mogli zakazivati ili učestvovati u sportskim događajima.")
            Spacer(modifier = Modifier.height(20.dp))
            if (isError.value) customAuthError(errorText = errorText.value)
            Spacer(modifier = Modifier.height(20.dp))
            inputTextIndicator(textValue = "Email")
            Spacer(modifier = Modifier.height(10.dp))
            customTextInput(
                isEmail = false,
                inputValue = email,
                inputText = "primer@domen.com",
                leadingIcon = Icons.Outlined.MailOutline,
                isError = isEmailError,
                errorText = emailErrorText
            )
            Spacer(modifier = Modifier.height(10.dp))
            inputTextIndicator(textValue = "Ime i Prezime")
            Spacer(modifier = Modifier.height(10.dp))
            customTextInput(
                isEmail = false,
                inputValue = fullName,
                inputText = "Vaše ime i prezime",
                leadingIcon = Icons.Outlined.Person,
                isError = isFullNameError,
                errorText = emailErrorText
            )
            Spacer(modifier = Modifier.height(10.dp))
            inputTextIndicator(textValue = "Broj Telefona")
            Spacer(modifier = Modifier.height(10.dp))
            customTextInput(
                isEmail = false,
                isNumber = true,
                inputValue = phoneNumber,
                inputText = "Vaš broj telefona",
                leadingIcon = Icons.Outlined.Phone,
                isError = isPhoneNumberError,
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
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        ) {
            loginRegisterCustomButton(
                buttonText = "Registracija",
                isEnabled = buttonIsEnabled,
                isLoading = isLoading,
                mainColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    isImageError.value = false
                    isEmailError.value = false
                    isPasswordError.value = false
                    isImageError.value = false
                    isFullNameError.value = false
                    isPhoneNumberError.value = false
                    isError.value = false
                    isLoading.value = true

                    if (profileImage.value == Uri.EMPTY && profileImage.value != null) {
                        isImageError.value = true
                        isLoading.value = false
                    } else if (email.value.isEmpty()) {
                        isEmailError.value = true
                        isLoading.value = false
                    } else if (fullName.value.isEmpty()) {
                        isFullNameError.value = true
                        isLoading.value = false
                    } else if (phoneNumber.value.isEmpty()) {
                        isPhoneNumberError.value = true
                        isLoading.value = false
                    } else if (password.value.isEmpty()) {
                        isPasswordError.value = true
                        isLoading.value = false
                    } else {
                        viewModel?.register(
                            fullName = fullName.value,
                            phoneNumber = phoneNumber.value,
                            profileImage = profileImage.value,
                            email = email.value,
                            password = password.value
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            customClickableText(
                firstText = "Već imate nalog?",
                secondText = "Prijavite se!",
                onClick = {
                    navController?.navigate(Routes.loginScreen)
                }
            )
        }
    }

    registerFlow?.value.let {
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
                    AuthExceptionsMessages.shortPassword -> {
                        isPasswordError.value = true
                        passwordErrorText.value = "Lozinka je prekratka."
                    }
                    AuthExceptionsMessages.emailUsed -> {
                        isError.value = true
                        errorText.value = "Email adresa je već korišćena."
                    }

                    else -> {}
                }
            }
            is Resource.Loading -> {
                // Nema potrebe za akcijom, jer je isLoading već setovan na onClick
            }
            is Resource.Success -> {
                isLoading.value = false
                LaunchedEffect(Unit) {
                    navController?.navigate(Routes.indexScreen) {
                        popUpTo(Routes.indexScreen) {
                            inclusive = true
                        }
                    }
                }
            }
            null -> Log.d("Test", "Test")
        }
    }
}

@Preview
@Composable
fun showRegisterScreen() {
    RegisterScreen(viewModel = null, navController = null)
}
