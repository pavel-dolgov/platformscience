package com.paveldolgov.platformscience.assignmnentsolver

import com.paveldolgov.platformscience.assignmnentsolver.SuitabilityScoreCalculator.Companion.VOWEL_WEIGHT
import com.paveldolgov.platformscience.model.Address
import com.paveldolgov.platformscience.util.CharsInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class SuitabilityScoreCalculatorTest {

    private val testObject = SuitabilityScoreCalculator()

    @Test
    fun `vowel weight is correct`() {
        assertEquals(VOWEL_WEIGHT, 1.5, 0.0)
    }

    @Test
    fun `calculateScore calculates correct score for valid even street`() {
        val address = Address(123, "Even")
        val charsInfo = CharsInfo("aabb", 2, 2)
        val expectedBaseScore = charsInfo.vowelsCount * VOWEL_WEIGHT
        val expectedAdditionalScore = expectedBaseScore * SuitabilityScoreCalculator.FACTOR_WEIGHT
        val expectedScore = expectedBaseScore + expectedAdditionalScore

        val actual = testObject.calculateScore(address, charsInfo)

        assertEquals(expectedScore, actual, 0.0)
    }

    @Test
    fun `calculateScore calculates correct score for empty street`() {
        val address = Address(123, "")
        val charsInfo = CharsInfo("aaaa", 4, 0)
        val expectedBaseScore = charsInfo.vowelsCount * VOWEL_WEIGHT
        val expectedAdditionalScore = expectedBaseScore * SuitabilityScoreCalculator.FACTOR_WEIGHT
        val expectedScore = expectedBaseScore + expectedAdditionalScore

        val actual = testObject.calculateScore(address, charsInfo)

        assertEquals(expectedScore, actual, 0.0)
    }

    @Test(expected = AssertionError::class)
    fun `calculateScore throws assertion error if name is invalid`() {
        val address = Address(123, "Even")
        val charsInfo = CharsInfo("", 0, 0)

        testObject.calculateScore(address, charsInfo)
    }
}