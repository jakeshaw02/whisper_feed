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
package com.saulhdev.feeder

import com.saulhdev.feeder.utils.TEMP_UNIT_AUTO
import com.saulhdev.feeder.utils.TEMP_UNIT_CELSIUS
import com.saulhdev.feeder.utils.TEMP_UNIT_FAHRENHEIT
import com.saulhdev.feeder.utils.TempUnit
import com.saulhdev.feeder.utils.formatTemperature
import com.saulhdev.feeder.utils.resolveTempUnit
import org.junit.Assert.assertEquals
import org.junit.Test

class TemperatureUnitTest {

    private fun f(c: Double) = formatTemperature(c, TempUnit.FAHRENHEIT)

    @Test fun `freezing`() = assertEquals("32°", f(0.0))
    @Test fun `boiling`() = assertEquals("212°", f(100.0))
    @Test fun `minus forty is the same on both scales`() = assertEquals("-40°", f(-40.0))

    @Test fun `rounds after converting, not before`() {
        // 3.6 °C = 38.48 °F. Rounding the Celsius first would give 39°.
        assertEquals("38°", f(3.6))
    }

    @Test fun `celsius unchanged from the old chip`() {
        assertEquals("4°", formatTemperature(3.6, TempUnit.CELSIUS))
        assertEquals("-12°", formatTemperature(-12.4, TempUnit.CELSIUS))
    }

    @Test fun `hot day is three digits`() = assertEquals("104°", f(40.0))

    @Test fun `auto follows the region`() {
        assertEquals(TempUnit.FAHRENHEIT, resolveTempUnit(TEMP_UNIT_AUTO, "US"))
        assertEquals(TempUnit.CELSIUS, resolveTempUnit(TEMP_UNIT_AUTO, "GB"))
        assertEquals(TempUnit.CELSIUS, resolveTempUnit(TEMP_UNIT_AUTO, ""))
    }

    @Test fun `explicit choice beats the region`() {
        assertEquals(TempUnit.CELSIUS, resolveTempUnit(TEMP_UNIT_CELSIUS, "US"))
        assertEquals(TempUnit.FAHRENHEIT, resolveTempUnit(TEMP_UNIT_FAHRENHEIT, "GB"))
    }
}
