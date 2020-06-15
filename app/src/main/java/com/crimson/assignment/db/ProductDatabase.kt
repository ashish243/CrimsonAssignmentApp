
package com.crimson.assignment.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.crimson.assignment.model.Converter
import com.crimson.assignment.model.DetailRoot
import com.crimson.assignment.model.Search

//import com.codingwithset.minie_commerce.model.Products

/**
 * Database schema that holds the list of products.
 */
@Database(
    entities = [Search::class, DetailRoot::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {

        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getInstance(context: Context): ProductDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ProductDatabase::class.java, "search.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}
