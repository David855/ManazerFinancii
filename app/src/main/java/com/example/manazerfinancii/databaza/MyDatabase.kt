package com.example.manazerfinancii.databaza

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Databáza
 * Skladá sa z tabuľky polozka, kategoria a setrenie
 */
@Database(entities = arrayOf(Polozka::class, Kategoria::class, Setrenie::class), version = 2, exportSchema  = false)
@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {
    /**
     * Polozka dao
     *
     * @return polozkaDao
     */
    abstract fun polozkaDao() : PolozkaDao

    /**
     * Kategoria dao
     *
     * @return kategoriaDao
     */
    abstract fun kategoriaDao() : KategoriaDao

    /**
     * Setrenie dao
     *
     * @return setrenieDao
     */
    abstract fun setrenieDao() : SetrenieDao

    companion object {
        // singleton zakáže tvorbu viacerých inštancií databázy
        @Volatile
        private var INSTANCE: MyDatabase? = null

        /**
         * Get database
         *
         * @param context context
         * @return objekt databázy
         */
        fun getDatabase(context: Context): MyDatabase {
            // ak INSTANCE nie je null, tak vráti databázu
            // ak nie, vytvorí inštanciu
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "financny_manazer"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}