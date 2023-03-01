package com.paveldolgov.platformscience.util

import java.util.*

data class CharsInfo(
    val sourceString: String,
    val vowelsCount: Int,
    val consonantsCount: Int,
) {
    class Builder(
        val sourceString: String,
        var vowelsCount: Int = 0,
        var consonantsCount: Int = 0
    ) {
        fun build(): CharsInfo {
            return CharsInfo(sourceString, vowelsCount, consonantsCount)
        }
    }

}

/**
 * Calculates vowels and consonants count in a given string.
 */
fun String.getCharsInfo(): CharsInfo =
    lowercase(Locale.US).fold(CharsInfo.Builder(this)) { acc, char ->
        // check if char is a vowel ('a', 'e', 'i', 'o', 'y', 'u') or a consonant
        if (char == 'a' || char == 'e' || char == 'i' || char == 'o' || char == 'y' || char == 'u') {
            acc.vowelsCount++
        } else if (char in 'a'..'z') {
            acc.consonantsCount++
        }
        acc
    }.build()