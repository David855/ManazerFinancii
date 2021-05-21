package com.example.manazerfinancii.databaza

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Dátová trieda, ktorá predstavuje položku
 * Taktiež je to entita databázy
 *
 * @property id id polozky
 * @property typ typ položky
 * @property nazov nazov položky
 * @property popis popis položky
 * @property kategoria kategoria položky
 * @property suma suma položky
 * @property prostriedok prostriedok
 * @property datum datum pridania položky
 * @constructor Vytvorenie položky
 */
@Entity(tableName = "polozka")
data class Polozka(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name = "typ") val typ: Int,
        @ColumnInfo(name = "nazov") val nazov: String,
        @ColumnInfo(name = "popis") val popis:String,
        @ColumnInfo(name = "kategoria") val kategoria : Int,
        @ColumnInfo(name = "suma") val suma: Float,
        @ColumnInfo(name = "prostriedok") val prostriedok: String,
        @ColumnInfo(name = "datum") val datum: Date
)
