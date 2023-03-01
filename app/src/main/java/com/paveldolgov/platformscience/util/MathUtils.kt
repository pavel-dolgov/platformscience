package com.paveldolgov.platformscience.util

/**
 * Returns greatest common divisor for the num "a" and "b".
 * For example gcd for a 25 and 5 is 5, for 7 and 31 is 1.
 */
fun gcd(a: Int, b: Int): Int {
    if (a == 0) {
        return b
    }

    return gcd(b % a, a)
}

/**
 * Returns true if num "a" and "b" have common divisor greater than 1.
 * 31.hasGcdGreaterThan1(7) == false
 * 25.hasGcdGreaterThan1(5) == true
 */
fun Int.hasCommonFactorGreaterThan1(num: Int) = gcd(this, num) > 1