package com.example.projekatv2.data
import com.example.projekatv2.model.Sport
import com.example.projekatv2.model.SportRate

interface SportRateRepository {
    suspend fun getSportRates(
        sportId: String
    ): Resource<List<SportRate>>

    suspend fun getUserRates(): Resource<List<SportRate>>

    suspend fun getUserAdForSport(): Resource<List<SportRate>>

    suspend fun addRate(
        sportId: String,
        rate: Int,
        sport: Sport
    ): Resource<String>

    suspend fun updateRate(
        rateId: String,
        rate: Int,
    ): Resource<String>
}