package com.paveldolgov.platformscience.util

import org.junit.Assert.assertEquals
import org.junit.Test

class StringUtilsKtTest {
    @Test
    fun `getCharsInfo returns correct result for aeioyubcd`() {
        val actual = "aeioyubcd".getCharsInfo()

        assertEquals(6, actual.vowelsCount)
        assertEquals(3, actual.consonantsCount)
    }
}