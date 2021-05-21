package com.example.manazerfinancii.databaza

import androidx.room.*

/**
 * Interface Dao pre prácu s tabuľkou setrenie
 */
@Dao
interface SetrenieDao {
    /**
     * Získanie šetrenia
     *
     * @return šetrenie
     */
    @Query("SELECT * from setrenie order by datum desc LIMIT 1")
    fun getSetrenie() : Setrenie

    /**
     * Zápis šetrenia do tabuľky
     *
     * @param setrenie dané šetrenie
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun writeSetrenie(setrenie : Setrenie);

    /**
     * Vymazanie šetrenia
     *
     * @param setrenie dané šetrenie
     */
    @Delete
    fun deleteSetrenie(setrenie : Setrenie);
}