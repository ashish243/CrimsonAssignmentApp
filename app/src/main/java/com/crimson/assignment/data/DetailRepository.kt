package com.crimson.assignment.data

import androidx.paging.LivePagedListBuilder
import com.crimson.assignment.api.ProductService
import com.crimson.assignment.db.ProductLocalCache
import com.crimson.assignment.model.DetailResult

class DetailRepository (
    private val service: ProductService,
    private val cache: ProductLocalCache
) {

    /*
    this field help with network state,
    the field will help for refresh purpose
     */
    val networkState = ProductBoundaryCallback(service, cache)

    fun getDetail(): DetailResult {
        cache.deleteAllDetail()
        val dataSourceFactory = cache.getDetail()
        val boundaryCallback = DetailBoundaryCallback(service, cache)
        val networkError = boundaryCallback.networkErrors


        val data = LivePagedListBuilder(dataSourceFactory,1)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return DetailResult(data, networkError)
    }


}