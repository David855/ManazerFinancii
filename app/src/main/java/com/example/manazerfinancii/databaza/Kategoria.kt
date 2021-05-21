package com.example.manazerfinancii.databaza

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Dátová trieda, ktorá predstavuje kategóriu pre položku
 * Taktiež je to entita databázy
 *
 * @property id id kategorie
 * @property typ typ položky
 * @property icon ikona kategorie
 * @property nazov názov kategórie
 * @constructor Vytvorenie kategórie
 */
@Entity(tableName = "kategoria", primaryKeys = ["id", "typ"])
data class Kategoria(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "typ") val typ: Int,
    @ColumnInfo(name = "icon") val icon: Int,
    @ColumnInfo(name = "nazov") val nazov: String
)