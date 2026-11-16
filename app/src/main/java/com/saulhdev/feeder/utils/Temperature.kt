/*
 * This file is part of Whisper
 * Copyright (c) 2026   Whisper contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.saulhdev.feeder.utils

import kotlin.math.roundToInt

/** Follow the phone's region: °F where that is the everyday unit, °C elsewhere. */
const val TEMP_UNIT_AUTO = "auto"
const val TEMP_UNIT_CELSIUS = "celsius"
const val TEMP_UNIT_FAHRENHEIT = "fahrenheit"

enum class TempUnit { CELSIUS, FAHRENHEIT }

/** Regions whose everyday temperature unit is Fahrenheit. */
private val FAHRENHEIT_REGIONS = setOf("US", "LR", "MM", "BS", "BZ", "KY", "PW", "FM", "MH")

/** The unit to show for a stored [pref] value, with `auto` decided by [country]. */
fun resolveTempUnit(pref: String, country: String): TempUnit = when (pref) {
    TEMP_UNIT_CELSIUS -> TempUnit.CELSIUS
    TEMP_UNIT_FAHRENHEIT -> TempUnit.FAHRENHEIT
    else -> if (country.uppercase() in FAHRENHEIT_REGIONS) TempUnit.FAHRENHEIT else TempUnit.CELSIUS
}

/**
 * "39°". Converted at display time so a cached Celsius reading stays valid
 * when the unit changes. Rounded after converting, not before: 3.6 °C is
 * 38.48 °F, which is 38°, where rounding first would give 39°.
 *
 * The bare degree sign is kept so the chip width doesn't change with the unit.
 */
fun formatTemperature(celsius: Double, unit: TempUnit): String {
    val value = when (unit) {
        TempUnit.CELSIUS -> celsius
        TempUnit.FAHRENHEIT -> celsius * 9 / 5 + 32
    }
    return "${value.roundToInt()}°"
}
