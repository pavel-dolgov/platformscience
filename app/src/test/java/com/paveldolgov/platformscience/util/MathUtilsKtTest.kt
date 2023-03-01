package com.paveldolgov.platformscience.util

import org.junit.Assert.*
import org.junit.Test

class MathUtilsKtTest {


    @Test
    fun `gcd returns correct value for 25 and 5`() {
        assertEquals(5, gcd(25, 5))
    }

    @Test
    fun `gcd returns correct value for 5 and 25`() {
        assertEquals(5, gcd(5, 25))
    }

    @Test
    fun `gcd returns correct value for 7 and 31`() {
        assertEquals(1, gcd(7, 31))
    }

    @Test
    fun `gcd returns correct value for 8 and 8`() {
        assertEquals(8, gcd(8, 8))
    }

    @Test
    fun `gcd returns correct value for 0 and 0`() {
        assertEquals(0, gcd(0, 0))
    }

    @Test
    fun `gcd returns correct value for 8 and 0`() {
        assertEquals(8, gcd(8, 0))
    }

    @Test
    fun `hasCommonFactorBeside1 returns true for 25 and 5`() {
        assertTrue(25.hasCommonFactorGreaterThan1(5))
    }

    @Test
    fun `hasCommonFactorBeside1 returns false for 31 and 7`() {
        assertFalse(31.hasCommonFactorGreaterThan1(7))
    }


}