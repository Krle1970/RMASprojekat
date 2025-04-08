package com.example.projekatv2.screens.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.net.Uri
import android.util.Log
import android.widget.ImageButton
import android.widget.Space
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TableRows
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.projekatv2.Navigation.Routes
import com.example.projekatv2.R
import com.example.projekatv2.model.Sport
import com.example.projekatv2.filter.searchEventsByDescription
import com.example.projekatv2.ui.theme.buttonDisabledColor
import com.example.projekatv2.ui.theme.greyTextColor
import com.example.projekatv2.ui.theme.lightMailColor
import com.example.projekatv2.ui.theme.lightRedColor
import com.example.projekatv2.ui.theme.mainColor
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.protobuf.Empty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.example.projekatv2.ui.theme.lightGreyColor
import com.example.projekatv2.ui.theme.lightMainColor2
import com.example.projekatv2.ui.theme.redTextColor

@Composable
fun loginImage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.sport_login_image), // Prilagođeno sportu
            contentDescription = "Login Image",
            modifier = Modifier
                .width(210.dp)
                .height(210.dp)
        )
    }
}

@Composable
fun registerImage(
    selectedImageUri: MutableState<Uri?>,
    isError: MutableState<Boolean>
) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri.value == Uri.EMPTY) {
            Image(
                painter = painterResource(id = R.drawable.sport_profile_image), // Prilagođeno sportu
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(140.dp)
                    .border(
                        if (isError.value) BorderStroke(2.dp, Color.Red) else BorderStroke(0.dp, Color.Transparent)
                    )
                    .clip(RoundedCornerShape(70.dp)) // 50% border radius
                    .clickable {
                        singlePhotoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
            )
        } else {
            selectedImageUri.value?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(70.dp)) // 50% border radius
                        .background(Color.LightGray)
                        .clickable {
                            singlePhotoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
@Composable
fun customRichTextInput(
    inputValue: MutableState<String>,
    inputText: String,
    isError: MutableState<Boolean>,
    errorText: MutableState<String>,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .shadow(
                6.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                if (isError.value) Color.Red else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                Color.White,
                shape = RoundedCornerShape(20.dp)
            )
    ){
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
                isError.value = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = greyTextColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrect = true,
                keyboardType = KeyboardType.Text
            )
        )
    }
    if(isError.value && errorText.value.isNotEmpty()) {
        Text(
            text = errorText.value,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        )
    }
}

@Composable
fun headingText(textValue: String) {
    Text(
        style = TextStyle(
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        ),
        text = textValue
    )
}

@Composable
fun greyTextBigger(textValue: String){
    Text(style = TextStyle(
        color = greyTextColor,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    ),
        text = textValue
    )
}
@Composable
fun greyText(textValue: String) {
    Text(
        style = TextStyle(
            color = greyTextColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        ),
        text = textValue
    )
}

@Composable
fun inputTextIndicator(textValue: String) {
    Text(
        style = TextStyle(
            color = Color.Black,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        ),
        text = textValue
    )
}

@Composable
fun customTextInput(
    isEmail: Boolean,
    isNumber: Boolean = false,
    inputValue: MutableState<String>,
    inputText: String,
    leadingIcon: ImageVector,
    isError: MutableState<Boolean>,
    errorText: MutableState<String>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .shadow(6.dp, shape = RoundedCornerShape(20.dp))
            .border(1.dp, if (isError.value) Color.Red else Color.Transparent, shape = RoundedCornerShape(20.dp))
            .background(Color.White, shape = RoundedCornerShape(20.dp))
    ) {
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
                isError.value = false
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = greyTextColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = "",
                    tint = Color.Black
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            keyboardOptions = if (isEmail && !isNumber) KeyboardOptions(keyboardType = KeyboardType.Email) else if (!isEmail && isNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
        )
    }
    if (isError.value && errorText.value.isNotEmpty()) {
        Text(
            text = errorText.value,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        )
    }
}

@Composable
fun customPasswordInput(
    inputValue: MutableState<String>,
    inputText: String,
    leadingIcon: ImageVector,
    isError: MutableState<Boolean>,
    errorText: MutableState<String>
) {
    var showPassword by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .shadow(6.dp, shape = RoundedCornerShape(20.dp))
            .border(1.dp, if (isError.value) Color.Red else Color.Transparent, shape = RoundedCornerShape(20.dp))
            .background(Color.White, shape = RoundedCornerShape(20.dp))
    ) {
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
                isError.value = false
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = greyTextColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            leadingIcon = {
                Icon(imageVector = leadingIcon, contentDescription = "", tint = Color.Black)
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (!showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "",
                        tint = Color.Black
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
    if (isError.value && errorText.value.isNotEmpty()) {
        Text(
            text = errorText.value,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        )
    } else {
        Text(text = " ")
    }
}
@Composable
fun CustomNumberInputField(value: Int, onValueChange: (Int) -> Unit) {
    var textState by remember { mutableStateOf(value.toString()) }

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = textState,
            onValueChange = { newText ->
                val newValue = newText.toIntOrNull() ?: 0
                textState = newText
                onValueChange(newValue)
            },
            label = { Text(text = "Number of People") },
            singleLine = true
        )
    }
}

@Composable
fun loginRegisterCustomButton(
    buttonText: String,
    isEnabled: MutableState<Boolean>,
    isLoading: MutableState<Boolean>,
    mainColor: Color, // Dodaj ovaj parametar da bi mogao da ga koristiš u funkciji
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .height(50.dp)
            .shadow(6.dp, shape = RoundedCornerShape(20.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = mainColor,
            contentColor = Color.Black,
            disabledContainerColor = buttonDisabledColor,
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        enabled = isEnabled.value
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading.value) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = buttonText,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}

@Composable
fun mapNavigationBar(
    searchValue: MutableState<String>,
    profileImage: String,
    onImageClick: () -> Unit,
    events: MutableList<Sport>,
    navController: NavController?,
    cameraPositionState: CameraPositionState
){
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchList = remember {
        mutableListOf<Sport>()
    }

    searchList.clear()
    searchList.addAll(searchEventsByDescription(events, searchValue.value).toMutableList())

    val focusRequester = remember {
        FocusRequester()
    }

    val isFocused = remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .shadow(
                    6.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    1.dp,
                    Color.Transparent,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(
                    Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .height(50.dp)
                    .focusRequester(focusRequester = focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused.value = focusState.isFocused
                    },
                value = searchValue.value,
                onValueChange = { newValue ->
                    searchValue.value = newValue
                    isFocused.value = true
                },
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_text),
                        style = TextStyle(
                            color = greyTextColor
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "",
                        tint = mainColor
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions.Default
            )
            if (isFocused.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 60.dp)
                        .background(Color.White),
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                    ) {
                        for (event in searchList) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val eventJson = Gson().toJson(event)
                                            val encodedEventJson = URLEncoder.encode(eventJson, StandardCharsets.UTF_8.toString())
                                            navController?.navigate(Routes.eventDetailsScreen + "/$encodedEventJson")
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                            .weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AsyncImage(
                                            model = event.mainImage,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .width(40.dp)
                                                .height(40.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = if (event.description.length > 26) {
                                                event.description.substring(0, 26) + "..."
                                            } else {
                                                event.description
                                            }
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            isFocused.value = false
                                            keyboardController?.hide()
                                            cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(event.location.latitude, event.location.longitude), 17f)
                                        },
                                        modifier = Modifier.wrapContentWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.MyLocation,
                                            contentDescription = "",
                                            tint = mainColor
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            }

        }
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .shadow(
                    6.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    1.dp,
                    Color.Transparent,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(
                    Color.White,
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center

        ) {
            AsyncImage(
                model = profileImage,
                contentDescription = null,
                modifier = Modifier
                    .padding(2.dp)
                    .size(50.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        onImageClick()
                    },
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun mapFooter(
    openAddNewEvent: () -> Unit,
    active: Int,
    onHomeClick: () -> Unit,
    onTableClick: () -> Unit,
    onRankingClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    spotColor = Color.Transparent
                )
                .border(
                    1.dp,
                    Color.Transparent,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = onHomeClick) {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = "",
                        tint = if(active == 0) mainColor else greyTextColor,
                        modifier = Modifier.size(35.dp)
                    )
                }
                IconButton(onClick = onTableClick) {
                    Icon(
                        imageVector = Icons.Outlined.TableRows,
                        contentDescription = "",
                        tint = if(active == 1) mainColor else greyTextColor,
                        modifier = Modifier.size(35.dp)
                    )
                }
                Spacer(modifier = Modifier.size(70.dp))
                IconButton(onClick = onRankingClick) {
                    Icon(
                        imageVector = Icons.Outlined.FormatListNumbered,
                        contentDescription = "",
                        tint = if(active == 2) mainColor else greyTextColor,
                        modifier = Modifier.size(35.dp)
                    )
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "",
                        tint = if(active == 3) mainColor else greyTextColor,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-30).dp)
                .size(90.dp)
        ) {
            IconButton(onClick = openAddNewEvent,
                modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.add_event_icon),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun CustomIntensity(
    selectedOption: MutableState<Int>
){
    val customModifier = Modifier
        .fillMaxSize()
        .shadow(
            elevation = 20.dp,
            shape = RoundedCornerShape(10.dp),
            spotColor = Color.Transparent
        )
        .border(
            1.dp,
            Color.Transparent,
            shape = RoundedCornerShape(10.dp),
        )
        .background(
            Color.White,
            shape = RoundedCornerShape(10.dp),
        )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier
            .width(100.dp)
            .height(40.dp)
        ) {
            Box(
                modifier =
                if(selectedOption.value == 0)
                    customModifier.background(Color.Green)
                else
                    customModifier.clickable {
                        selectedOption.value = 0
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Low")
            }
        }
        Row(modifier = Modifier
            .width(100.dp)
            .height(40.dp)
        ) {
            Box(
                modifier =
                if(selectedOption.value == 1)
                    customModifier.background(Color.Yellow)
                else
                    customModifier.clickable {
                        selectedOption.value = 1
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Medium")
            }
        }
        Row(modifier = Modifier
            .width(100.dp)
            .height(40.dp)
        ) {
            Box(
                modifier =
                if(selectedOption.value == 2)
                    customModifier.background(Color.Red)
                else
                    customModifier.clickable {
                        selectedOption.value = 2
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "High")
            }
        }
    }
}

@Composable
fun CustomImageForNewEvent(
    selectedImageUri: MutableState<Uri?>
){
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )

    val interactionSource = remember { MutableInteractionSource() }
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(vertical = 2.dp)
        .shadow(
            6.dp,
            shape = RoundedCornerShape(20.dp)
        )
        .border(
            1.dp,
            Color.Transparent,
            shape = RoundedCornerShape(20.dp)
        )
        .background(
            greyTextColor,
            shape = RoundedCornerShape(20.dp)
        ),
        contentAlignment = Alignment.Center
    ){
        if (selectedImageUri.value == Uri.EMPTY) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.uploadimage),
                    contentDescription = ""
                )
                Text(text = "Add main image")
            }
        }else{
            selectedImageUri.value?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun CustomGalleryForAddNewEvent(
    selectedImages: MutableState<List<Uri>>
) {
    val pickImagesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris != null) {
            selectedImages.value += uris
        }
    }

    LazyRow {
        if (selectedImages.value.size < 5) {
            item {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .width(100.dp)
                        .height(100.dp)
                        .border(
                            1.dp,
                            greyTextColor,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .background(
                            greyTextColor,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .clickable { pickImagesLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.AddAPhoto, contentDescription = "")
                }
            }
        }
        items(selectedImages.value.size) { index ->
            val uri = selectedImages.value[index]
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .width(100.dp)
                    .height(100.dp)
                    .border(
                        1.dp,
                        Color.Transparent,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .clickable { selectedImages.value -= uri },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
    }
}

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int,
    size: Int = 48// Opcioni parametar za prilagođavanje veličine
): BitmapDescriptor? {
    // Pribavljanje drawable ikone
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null

    // Postavi veličinu drawable-a
    val width = size ?: drawable.intrinsicWidth // Ako nije prosleđena veličina, koristi originalnu širinu
    val height = size ?: drawable.intrinsicHeight // Ako nije prosleđena veličina, koristi originalnu visinu

    drawable.setBounds(0, 0, width, height)

    // Kreiranje bitmapa odgovarajuće veličine
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    // Nacrtaj drawable na bitmap
    val canvas = Canvas(bitmap)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

@Composable
fun customClickableText(
    firstText: String,
    secondText: String,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = firstText,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
        Text(
            text = secondText,
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .padding(start = 4.dp),
            style = TextStyle(
                fontSize = 12.sp,
                color = mainColor
            )
        )
    }
}
@Composable
fun customAuthError(
    errorText: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red.copy(alpha = 0.1f)) // Svetlo crvena pozadina za grešku
            .padding(16.dp)
            .heightIn(min = 50.dp), // Visina greške može biti veća ako je tekst duži
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorText,
            color = Color.Red,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )
    }
}

@Composable
fun bitmapDescriptorFromUrlWithRoundedCorners(
    context: Context,
    imageUrl: String,
    cornerRadius: Float
): State<BitmapDescriptor?> {
    val bitmapDescriptorState = remember { mutableStateOf<BitmapDescriptor?>(null) }

    LaunchedEffect(imageUrl) {
        try {
            withContext(Dispatchers.IO) {
                val inputStream = URL(imageUrl).openStream()
                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                // Calculate the aspect ratio
                val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()

                // Resize the image while maintaining aspect ratio
                val targetWidth = 150
                val targetHeight = (targetWidth / aspectRatio).toInt()
                val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, false)

                // Create a rounded bitmap
                val roundedBitmap = Bitmap.createBitmap(
                    resizedBitmap.width,
                    resizedBitmap.height,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(roundedBitmap)
                val paint = Paint().apply {
                    isAntiAlias = true
                    shader = BitmapShader(resizedBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                }
                val rect = RectF(0f, 0f, resizedBitmap.width.toFloat(), resizedBitmap.height.toFloat())
                canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)

                val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(roundedBitmap)
                bitmapDescriptorState.value = bitmapDescriptor
            }
            Log.d("Loaded", "Image loaded with rounded corners")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Loaded", e.toString())
        }
    }

    return rememberUpdatedState(bitmapDescriptorState.value)
}
@Composable
fun DropdownWithCheckboxes(
    options: MutableList<String>,
    initiallyChecked: List<Boolean>,
    onSelectionChanged: (List<Boolean>) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    var checkedStates = remember {
        mutableListOf<Boolean>()
    }
    checkedStates.clear()
    checkedStates.addAll(initiallyChecked)

    Column{
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded.value = !expanded.value })
                .background(lightGreyColor, RoundedCornerShape(4.dp))
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Text("Izaberi organizatore", style = MaterialTheme.typography.body1)
            Icon(
                if (expanded.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown icon"
            )
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            if(options.isNotEmpty() && checkedStates.isNotEmpty())
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(onClick = {
                        checkedStates = checkedStates.toMutableList().apply {
                            this[index] = !this[index]
                        }
                        onSelectionChanged(checkedStates)
                    },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = checkedStates[index],
                                onCheckedChange = {
                                    checkedStates = checkedStates.toMutableList().apply {
                                        this[index] = it
                                    }
                                    onSelectionChanged(checkedStates)
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(option)
                        }
                    }
                }
        }
    }
}

@Composable
fun CustomIntensitySelector(
    selected: MutableState<Int>
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IntensityOption("Low", 0, selected)
        IntensityOption("Medium", 1, selected)
        IntensityOption("High", 2, selected)
    }
}

@Composable
fun IntensityOption(
    text: String,
    index: Int,
    selected: MutableState<Int>
) {
    val isSelected = remember { mutableStateOf(selected.value == index) }

    Box(
        modifier = Modifier
            .background(
                if (isSelected.value) mainColor else Color.White,
                RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected.value) mainColor else lightGreyColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                selected.value = index
                isSelected.value = (selected.value == index)
            }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Filled.Sports, contentDescription = "")
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = text)
        }
    }
}

@Composable
fun RangeSliderExample(
    rangeValues: MutableState<Float>
) {
    Slider(
        value = rangeValues.value,
        onValueChange = { rangeValues.value = it },
        valueRange = 0f..1000f,
        steps = 50,
        colors = SliderDefaults.colors(
            thumbColor = mainColor,
            activeTrackColor = lightMainColor2,
            inactiveTrackColor = lightGreyColor
        )
    )
}

@Composable
fun CustomFilterButton(
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(mainColor, RoundedCornerShape(30.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = mainColor,
            contentColor = Color.Black,
            disabledContainerColor = buttonDisabledColor,
            disabledContentColor = Color.White
        ),

        ) {
        Text(
            "Filtriraj",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun CustomResetFilters(
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(mainColor, RoundedCornerShape(30.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = redTextColor,
            contentColor = Color.White,
            disabledContainerColor = buttonDisabledColor,
            disabledContentColor = Color.White
        ),

        ) {
        Text(
            "Resetuj Filtere",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}
