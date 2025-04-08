package com.example.projekatv2.exceptions

object AuthExceptionsMessages {
    const val invalidCredential = "Uneta autentifikacija je netačna, neispravna ili je istekla."
    const val emptyFields = "Polje je prazno ili nije popunjeno."
    const val badlyEmailFormat = "Email adresa je u neispravnom formatu."
    const val emailUsed = "Ova email adresa je već u upotrebi za drugi nalog."
    const val shortPassword = "Lozinka je prekratka. [ Lozinka treba da ima najmanje 6 karaktera ]"
}
