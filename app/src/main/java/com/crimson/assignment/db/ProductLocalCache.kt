package com.crimson.assignment.db

import android.util.Log
import androidx.paging.DataSource
import com.crimson.assignment.model.DetailRoot
import com.crimson.assignment.model.Search

import java.util.concurrent.Executor


/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.

 */
class ProductLocalCache(
    private val productDao: ProductDao,
    private val ioExecutor: Executor
) {

    /**
     * Insert a list of products in the database, on a background thread.
     */
    fun insert(products: List<Search>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("ProductLocalCache", "inserting ${products.size} products")
            productDao.insert(products)
            insertFinished()
        }
    }

    fun insertDetail(products: DetailRoot, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("ProductLocalCache", "inserting ${products} products")
            productDao.insertDetail(products)
            insertFinished()
        }
    }
    fun deleteAllData(){
        ioExecutor.execute {
            productDao.deleteAllList()
        }
    }

    fun deleteAllDetail(){
        ioExecutor.execute {
            productDao.deleteAllDetail()
        }
    }
    /*
    get all product from cache database<room> via paging
     */
    fun getAllProduct(): DataSource.Factory<Int, Search> {
        return productDao.getAllProduct()
    }

    fun getDetail(): DataSource.Factory<Int, DetailRoot> {
        return productDao.getDetail()
    }

    //get all product base on the search key which is the name of product
    //if search word contain in the product name.
    fun getAllProductForFilter(searchQuery: String):DataSource.Factory<Int, Search>{
        return productDao.getAllProductForFilter(searchQuery)
    }
}
