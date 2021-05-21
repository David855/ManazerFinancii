package com.example.manazerfinancii.databaza

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Interface Dao pre prácu s tabuľkou kategoria
 */
@Dao
interface KategoriaDao {
    /**
     * Získanie všetkych kategórií podľa typu položky
     *
     * @param typ položky
     * @return polozky
     */
    @Query("SELECT * from kategoria where typ=:typ")
    fun getAllCategories(typ : Int) : List<Kategoria>

    /**
     * Získanie mien všetkých kategórií podľa typu položky
     *
     * @param typ typ položky
     * @return pole názvov
     */
    @Query("SELECT nazov from kategoria where typ=:typ")
    fun getAllCategoriesName(typ : Int) : Array<String>

    /**
     * Získanie kategórie na základe id
     *
     * @param id id kategórie
     * @param typ typ položky
     * @return kategória
     */
    @Query("SELECT * from kategoria where id=:id and typ=:typ")
    fun getCategoryById(id: Int, typ : Int) : Kategoria

    /**
     * Vrátenie počtu kategórií na základe typu položky
     *
     * @param typ typ položky
     * @return počet kategórií
     */
    @Query("SELECT count(*) from kategoria where typ=:typ")
    fun getNumOfCategories(typ : Int) : Int

    /**
     * Vloženie kategórie do databázy
     *
     * @param kategoria konkrétna kategória
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun writeKategoria(kategoria: Kategoria);

}