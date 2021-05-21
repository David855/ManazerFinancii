package com.example.manazerfinancii.databaza

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.*

/**
 * Interface Dao pre prácu s tabuľkou polozka
 */
@Dao
interface PolozkaDao {

    /**
     * Vrátenie všetkých položiek na základe typu
     * @param typ typ položky
     */
    @Query("SELECT * FROM polozka where polozka.typ = :typ")
    fun getAllPolozky(typ : Int) : List<Polozka>

    /**
     * Vrátenie všetkých položiek na kategórie a typu položky
     * @param typ typ položky
     * @param kategoria číslo kategórie
     */
    @Query("SELECT * FROM polozka where polozka.typ = :typ and kategoria = :kategoria")
    fun getAllPolozkyByCategory(typ : Int, kategoria : Int) : List<Polozka>

    /**
     * Vrátenie všetkých položiek na základe prostriedku a typu položky
     * @param typ typ položky
     * @param prostriedok názov prostriedku
     */
    @Query("SELECT * FROM polozka where polozka.typ = :typ and prostriedok = :prostriedok")
    fun getAllPolozkyByProstriedok(typ : Int, prostriedok : String) : List<Polozka>

    /**
     * Vrátenie všetkých položiek na základe typu a že dátum patrí medzi interval dátumov z parametra
     * @param typ typ položky
     * @param prvyDatum začiatočný dátum
     * @param druhyDatum koncový dátum
     */
    @Query("SELECT * FROM polozka where polozka.typ = :typ and datum between :prvyDatum and :druhyDatum")
    fun getAllPolozkyByBetweenDate(typ : Int, prvyDatum : Date, druhyDatum : Date) : List<Polozka>

    /**
     *  Vrátenie všetkých položiek na základe typu a ktoré boli pridané posledný rok
     *  @param typ typ položky
     */

    @Query("SELECT * FROM polozka where polozka.typ = :typ and datetime(datum/1000, 'unixepoch', '+1 years') >= datetime('now')")
    fun getAllPolozkyRok(typ : Int) : List<Polozka>

    /**
     *  Vrátenie všetkých položiek na základe typu a ktoré boli pridané posledný mesiac
     *  @param typ typ položky
     */
    @Query("SELECT * FROM polozka where polozka.typ = :typ and datetime(datum/1000, 'unixepoch', '+1 months') >= datetime('now')")
    fun getAllPolozkyMesiac(typ : Int) : List<Polozka>

    /**
     *  Vrátenie všetkých položiek na základe typu a ktoré boli pridané posledný týždeň
     *  @param typ typ položky
     */
    @Query("SELECT * FROM polozka where polozka.typ = :typ and datetime(datum/1000, 'unixepoch', '+7 days') >= datetime('now')")
    fun getAllPolozkyTyzden(typ : Int) : List<Polozka>


    /**
     *  Vrátenie posledného manipulovania s peňaženkou
     */
    @Query("SELECT datum FROM polozka where prostriedok like 'Peňaženka' order by datum desc limit 1")
    fun getLastUsePenazenka() : Date

    /**
     *  Vrátenie posledného manipulovania s bankovým účtom
     */
    @Query("SELECT datum FROM polozka where prostriedok like 'Bankový účet' order by datum desc limit 1")
    fun getLastUseBankUcet() : Date

    /**
     * Vrátenie celkovej sumy mesačných príjmov aktuálneho mesiaca
     */
    @Query("SELECT sum(suma) FROM polozka where polozka.typ = 1 and strftime('%Y-%m', datetime(datum/1000, 'unixepoch')) = strftime('%Y-%m', 'now')")
    fun getSumPrijmyMonth() : Float

    /**
     * Vrátenie celkovej sumy mesačných výdavkov aktuálneho mesiaca
     */
    @Query("SELECT sum(suma) FROM polozka where polozka.typ = 2 and strftime('%Y-%m', datetime(datum/1000, 'unixepoch')) = strftime('%Y-%m', 'now')")
    fun getSumVydavkyMonth() : Float

    /**
     *  Vrátenie celkovej sumy prijmov na základe prostriedku peňaženka
     */
    @Query("SELECT sum(suma) FROM polozka where polozka.typ = 1 and prostriedok like 'Peňaženka'")
    fun getSumPenazenkaPrijmy() : Float

    /**
     *  Vrátenie celkovej sumy výdavkov na základe prostriedku peňaženka
     */
    @Query("SELECT sum(suma) FROM polozka where polozka.typ = 2 and prostriedok like 'Peňaženka'")
    fun getSumPenazenkaVydavky() : Float

    /**
     *  Vrátenie celkovej sumy prijmov na základe prostriedku bankový účet
     */
    @Query("SELECT sum(suma) FROM polozka where polozka.typ = 1 and prostriedok like 'Bankový účet'")
    fun getSumBankUcetPrijmy() : Float

    /**
     *  Vrátenie celkovej sumy výdavkov na základe prostriedku bankový účet
     */
    @Query("SELECT sum(suma) FROM polozka where polozka.typ = 2 and prostriedok like 'Bankový účet'")
    fun getSumBankUcetVydavky() : Float

    /**
     * Vrátenie všetkých šetrení, ktoré patria k aktuálnemu šetreniu
     * @param datum dátum vytvorenia šetrenia
     */
    @Query("SELECT * FROM polozka where polozka.typ = 2 and kategoria = 15 and datum > :datum")
    fun getAllSetrenia(datum : Date) : List<Polozka>

    /**
     * Vrátenie celkovej sumy našetrených peňazí
     * @param datum dátum vytvorenia šetrenia
     */
    @Query("SELECT sum(suma) FROM polozka where polozka.typ = 2 and kategoria = 15 and datum > :datum")
    fun getSumSetrenia(datum : Date) : Float

    /**
     * Vloženie položky do tabuľky
     * @param polozka položka
     */
    @Insert
    fun writePolozka(polozka: Polozka);

    /**
     * Vymazanie položky na základe id
     * @param id id položky
     */
    @Query("DELETE FROM polozka where id = :id")
    fun deletePolozka(id : Int)

}