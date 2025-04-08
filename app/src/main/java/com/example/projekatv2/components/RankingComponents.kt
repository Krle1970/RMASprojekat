package com.example.projekatv2.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.projekatv2.Navigation.Routes
import com.example.projekatv2.R
import com.example.projekatv2.model.SportUser
import com.example.projekatv2.ui.theme.mainColor
import com.example.projekatv2.ui.theme.redTextColor
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun StarRating(
    currentRating: Float,
    onRatingChange: (Float) -> Unit
) {
    var rating by remember { mutableStateOf(currentRating) }

    Row(modifier = Modifier.padding(8.dp)) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = if (i <= rating) R.drawable.start_fill else R.drawable.star_empty),
                contentDescription = "Star $i",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        rating = i.toFloat()
                        onRatingChange(rating)
                    }
            )
        }
    }
}

@Composable
fun SecondAndThirdOrganizer(
    place: Int,
    user: SportUser,
    onClick: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        if(place == 2) {
            Icon(
                imageVector = Icons.Filled.ArrowDropUp,
                contentDescription = "",
                tint = mainColor,
                modifier = Modifier
                    .size(24.dp)
                    .padding(0.dp)
            )
            Text(
                text = place.toString(),
                modifier = Modifier.offset(y = (-7).dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        else{
            Text(
                text = place.toString(),
                modifier = Modifier.offset(y = (7).dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "",
                tint = redTextColor,
                modifier = Modifier
                    .size(24.dp)
                    .padding(0.dp)
            )
        }
        AsyncImage(
            model = user.profileImage,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(70.dp)
                .height(70.dp)
                .clip(RoundedCornerShape(35.dp))
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = user.fullName)
        Spacer(modifier = Modifier.height(3.dp))
        greyText(
            textValue =
            if(user.rating <= 2) "Početnik"
            else if(user.rating <= 4) "Prosečan"
            else "Izuzetan"
        )
        Spacer(modifier = Modifier.height(3.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "",
                tint = mainColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = user.rating.toString(), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FirstPlaceOrganizer(
    user: SportUser,
    onClick: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = user.profileImage,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(50.dp))
            )
            Image(
                painter = painterResource(id = R.drawable.crown),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .offset(y = (-60).dp)
                    .size(50.dp)
                    .zIndex(1f) // Higher zIndex to ensure it's above the other image
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = user.fullName)
        Spacer(modifier = Modifier.height(3.dp))
        greyText(
            textValue =
            if(user.rating <= 2) "Početnik"
            else if(user.rating <= 4) "Prosečan"
            else "Izuzetan"
        )
        Spacer(modifier = Modifier.height(3.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "",
                tint = mainColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = user.rating.toString(), fontWeight = FontWeight.Bold)
        }
    }
}
@Composable
fun firstThreeOrganizers(
    users: List<SportUser>,
    onUserClick: (SportUser) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        if (users.count() > 1) {
            SecondAndThirdOrganizer(
                place = 2,
                user = users[1],
                onClick = {
                    onUserClick(users[1]) // Pozivanje `onUserClick` za ocenjivanje
                }
            )
        }
        if (users.isNotEmpty()) {
            FirstPlaceOrganizer(
                user = users[0],
                onClick = {
                    onUserClick(users[0]) // Pozivanje `onUserClick` za ocenjivanje
                }
            )
        }
        if (users.count() > 2) {
            SecondAndThirdOrganizer(
                place = 3,
                user = users[2],
                onClick = {
                    onUserClick(users[2]) // Pozivanje `onUserClick` za ocenjivanje
                }
            )
        }
    }
}

@Composable
fun OtherOrganizers(
    users: List<SportUser>,
    onUserClick: (SportUser) -> Unit
) {
    Column { // Koristimo Column da bismo stavili sve korisnike vertikalno
        users.forEachIndexed { index, user ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (index + 4).toString(), // Prikazujemo broj korisnika od 4. pozicije pa nadalje
                        modifier = Modifier.offset(y = (7).dp),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Row(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(40.dp))
                        .width(330.dp)
                        .padding(10.dp)
                        .clickable { onUserClick(user) }, // Dodajemo onClick za ocenjivanje
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Prikaz slike korisnika
                        AsyncImage(
                            model = user.profileImage,
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(60.dp)
                                .height(60.dp)
                                .clip(RoundedCornerShape(30.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            // Prikaz imena korisnika
                            Text(
                                text = user.fullName,
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            // Prikaz nivoa korisnika ("Početnik", "Prosečan", "Izuzetan")
                            greyText(
                                textValue =
                                if (user.rating <= 2) "Početnik"
                                else if (user.rating <= 4) "Prosečan"
                                else "Izuzetan"
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        // Prikaz zvezdice i ocene
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "User Rating",
                            tint = Color.Yellow,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = user.rating.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}




