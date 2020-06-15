package com.crimson.assignment.data


import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.crimson.assignment.api.ProductService
import com.crimson.assignment.db.ProductLocalCache

import com.crimson.assignment.model.ProductResult
import com.crimson.assignment.model.Search


/**
 * Repository class that works with local and remote data sources.
 */
class ProductRepository(

    private val service: ProductService,
    private val cache: ProductLocalCache
) {

    /*
    this field help with network state,
    the field will help for refresh purpose
     */
    val networkState = ProductBoundaryCallback(service, cache)

    /*
    query web service to retrieve product list and network error.
     */
    fun getProduct(): ProductResult {
        cache.deleteAllData()
        val dataSourceFactory = cache.getAllProduct()
        val boundaryCallback = ProductBoundaryCallback(service, cache)
        val networkError = boundaryCallback.networkErrors


        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return ProductResult(data, networkError)
    }



/*
this function handle the filter purpose
@param name {accept query which is the name of the product}
it retrieve the [LiveData<PagedList<Products>>]
 */

    fun getChacheMovie(): LiveData<PagedList<Search>> {
        //get product list from local database[room]
        val dataSourceFactory = cache.getAllProduct()

        //construct [LivePagedListBuilder] & retrieve [LiveData<PagedList<Products>>], and set [setInitialLoadKey = 10]
        return LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE).build()

    }

    fun getAllProductForFilter(name: String): LiveData<PagedList<Search>> {
        //get product list from local database[room]
        val dataSourceFactory = cache.getAllProductForFilter(name)

        //construct [LivePagedListBuilder] & retrieve [LiveData<PagedList<Products>>], and set [setInitialLoadKey = 10]
        return LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE).build()
            //.setInitialLoadKey(10)
            //.build()
    }

    /*
Define the number of items per page, to be retrieved by the paging library.
In the companion object, add another const val called DATABASE_PAGE_SIZE,
and set it to 30. Our PagedList will then page data from the DataSource in chunks of 30 items.
*/
    companion object {

        private const val DATABASE_PAGE_SIZE = 10//20
    }
}
