package com.crimson.assignment.db

import androidx.paging.DataSource
import androidx.room.*
import com.crimson.assignment.model.DetailRoot
import com.crimson.assignment.model.Search


/**
 * Room data access object for accessing the [Search] table.
 */
@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Search>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail(posts: DetailRoot)

    @Query("SELECT * FROM search")
    fun getAllProduct(): DataSource.Factory<Int, Search>

    @Query("DELETE FROM search")
    fun deleteAllList()

    @Query("DELETE FROM detail")
    fun deleteAllDetail()


    @Query("SELECT * FROM detail")
    fun getDetail(): DataSource.Factory<Int, DetailRoot>

    @Query("SELECT * FROM search WHERE title LIKE :name ORDER BY  title ASC")
    fun getAllProductForFilter(name: String): DataSource.Factory<Int, Search>
}
