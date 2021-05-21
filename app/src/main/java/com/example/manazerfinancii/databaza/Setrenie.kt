package com.example.manazerfinancii.databaza

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Trieda ktorá predstavuje šetrenie, taktiež je to entita v databáze
 *
 * @property id id šetrenia
 * @property nazov nazov šetrenia
 * @property popis popis šetrenia
 * @property suma suma šetrenia
 * @property datum datum vytvorenia šetrenia
 * @constructor Vytvorenie šetrenia
 */
@Entity(tableName = "setrenie")
data class Setrenie(
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "nazov") val nazov : String,
    @ColumnInfo(name = "popis") val popis : String,
    @ColumnInfo(name = "suma") val suma : Float,
    @ColumnInfo(name = "datum") val datum: Date
)
