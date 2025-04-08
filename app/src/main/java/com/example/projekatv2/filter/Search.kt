package com.example.projekatv2.filter
import com.example.projekatv2.model.Sport

fun searchEventsByDescription(
    events: MutableList<Sport>,
    query: String
): List<Sport> {
    val regex = query.split(" ").joinToString(".*") {
        Regex.escape(it)
    }.toRegex(RegexOption.IGNORE_CASE)
    return events.filter { event ->
        regex.containsMatchIn(event.description)
    }
}
