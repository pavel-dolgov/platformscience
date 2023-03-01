package com.paveldolgov.platformscience.model.adapter

import com.paveldolgov.platformscience.model.Address

object Parser {
    val addressRegex =
        "^(?<streetNumber>\\d+) (?<streetName>[A-Za-z ]+)( (?<unit>Apt\\. \\d+|Suite \\d+)?)?\$".toRegex()

    /**
     * Naive address extractor from a structured address string like
     * "1797 Adolf Island Apt. 744"
     * "8725 Aufderhar River Suite 859"
     * "215 Osinski Manors"
     */
    fun stringToAddress(address: String): Address {
        val groups = addressRegex.matchEntire(address)!!.groups as MatchNamedGroupCollection
        val streetNumber = groups["streetNumber"]!!.value.toInt()
        val streetName = groups["streetName"]!!.value
        val unit = groups["unit"]?.value
        return Address(streetNumber, streetName, unit)
    }

    fun fullNameToFirstAndLast(fullName: String): FirstAndLastName {
        val split = fullName.split(" ")
        return FirstAndLastName(split[0], split[1])
    }
}

data class FirstAndLastName(val first: String, val last: String)