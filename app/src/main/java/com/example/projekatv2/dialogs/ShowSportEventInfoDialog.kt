package com.example.projekatv2.screens.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projekatv2.model.Sport
import com.example.projekatv2.ui.theme.buttonDisabledColor
import com.example.projekatv2.ui.theme.greyTextColor
import com.example.projekatv2.ui.theme.lightMailColor
import com.example.projekatv2.ui.theme.mainColor

@Composable
fun ShowSportEventInfoDialog(
    showInfoDialog: MutableState<Boolean>,
    sportEvent: Sport,
) {
    AlertDialog(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp)),
        onDismissRequest = { showInfoDialog.value = false },
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Naslov događaja
                    Text(
                        text = "Detalji o događaju",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )

                    // Opis događaja
                    Text(
                        text = "Opis: ${sportEvent.description}",
                        style = TextStyle(fontSize = 18.sp)
                    )

                    // Intenzitet događaja
                    Text(
                        text = "Intenzitet: ${sportEvent.intensity}",
                        style = TextStyle(fontSize = 18.sp)
                    )

                    // Broj učesnika
                    Text(
                        text = "Maksimalni broj učesnika: ${sportEvent.maxParticipants}",
                        style = TextStyle(fontSize = 18.sp)
                    )

                    // Datum održavanja
                    Text(
                        text = "Datum: ${sportEvent.date}",
                        style = TextStyle(fontSize = 18.sp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Zatvaranje dijaloga
                    Button(
                        onClick = { showInfoDialog.value = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = mainColor,
                            contentColor = Color.Black,
                            disabledContainerColor = buttonDisabledColor,
                            disabledContentColor = Color.White,
                        ),
                    ) {
                        Text(
                            text = "Zatvori",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}
