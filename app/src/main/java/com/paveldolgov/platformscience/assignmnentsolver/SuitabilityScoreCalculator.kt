package com.paveldolgov.platformscience.assignmnentsolver

import com.paveldolgov.platformscience.model.Address
import com.paveldolgov.platformscience.util.CharsInfo
import com.paveldolgov.platformscience.util.hasCommonFactorGreaterThan1
import javax.inject.Inject

class SuitabilityScoreCalculator @Inject constructor() {
    companion object {
        const val VOWEL_WEIGHT = 1.5
        const val CONSONANTS_WEIGHT = 1.0
        const val FACTOR_WEIGHT = 0.5
    }


    fun calculateScore(address: Address, charsInfo: CharsInfo): Double {
        val baseScore = if (address.streetName.length % 2 == 0) {
            charsInfo.vowelsCount * VOWEL_WEIGHT
        } else {
            charsInfo.consonantsCount * CONSONANTS_WEIGHT
        }
        val additionalScore =
            if (address.streetName.length.hasCommonFactorGreaterThan1(charsInfo.sourceString.length)) {
                baseScore * FACTOR_WEIGHT
            } else {
                0.0
            }
        val totalScore = baseScore + additionalScore
        assert(totalScore != 0.0) { "Calculated score must not be 0. Address $address, driverInfo $charsInfo" }
        return totalScore
    }

}