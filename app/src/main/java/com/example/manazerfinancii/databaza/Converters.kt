package com.example.manazerfinancii.databaza

import androidx.room.TypeConverter
import java.util.*

/**
 * Konvertér pre databázu
 *
 * @constructor Vytvorenie converterov
 */
class Converters {
    /**
     * Funkcia ktorá prekonvertuje timestamp na dátum
     *
     * @param value hodnota
     * @return datum
     */
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    /**
     * Funkcia ktorá prekonvertuje dátum na timestamp
     *
     * @param date hodnota
     * @return timestamp
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}